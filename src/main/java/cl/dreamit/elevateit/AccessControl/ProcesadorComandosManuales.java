package cl.dreamit.elevateit.AccessControl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.DataModel.Const.CardTypes;
import cl.dreamit.elevateit.DataModel.Const.Operation;
import cl.dreamit.elevateit.DataModel.Const.ReadSource;
import cl.dreamit.elevateit.DataModel.Const.TipoCredencial;
import cl.dreamit.elevateit.DataModel.DAO.CanalesHorarios;
import cl.dreamit.elevateit.DataModel.DAO.Configuraciones;
import cl.dreamit.elevateit.DataModel.DAO.ConjuntosReservas;
import cl.dreamit.elevateit.DataModel.DAO.Controladores;
import cl.dreamit.elevateit.DataModel.DAO.LogsAcceso;
import cl.dreamit.elevateit.DataModel.DAO.Personas;
import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;
import cl.dreamit.elevateit.DataModel.DAO.ReservasValidadas;
import cl.dreamit.elevateit.DataModel.DAO.RespuestasComandos;
import cl.dreamit.elevateit.DataModel.DAO.TarjetasAcceso;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.Configuracion;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.ReservaValidada;
import cl.dreamit.elevateit.DataModel.Entities.GK2.CanalHorario;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ConjuntoReserva;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Controlador;
import cl.dreamit.elevateit.DataModel.Entities.GK2.LogAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.MedioAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Persona;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Reserva;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ResumenTarjetaControlador;
import cl.dreamit.elevateit.Hardware.Relay;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ComandoManual;
import cl.dreamit.elevateit.DataModel.Entities.GK2.RespuestaComandoManual;
import cl.dreamit.elevateit.Utils.Log;
import cl.dreamit.elevateit.Utils.Util;

public enum ProcesadorComandosManuales {
    INSTANCE;

    public final static String COMANDO_OPEN = "OUTPUT:OPEN:";

    public void procesarComandos(ComandoManual[] comandos) {
        for (ComandoManual comandoManual : comandos) {
            Log.error("Procesando Comando Manual: " + comandoManual);
            if (comandoManual.comando.startsWith(COMANDO_OPEN)) {
                procesar_comando_open(comandoManual);
            }
        }
    }

    public void searchCard(int code_1, int code_2, int cardType){

        Configuracion id_controlador = Configuraciones.INSTANCE.getParametro("idDevice");
        int id = Integer.parseInt(id_controlador.valor);
        Controlador controlador = Controladores.INSTANCE.getByID(id);
        int canal = 0;

        PuntoAcceso puntoAcceso = PuntosAccesos.INSTANCE.getPuntoAccesoControlador(controlador.id, canal);
        String codigoAcceso =  code_1 + "-" + code_2;

        int validacionMedioAcceso = Operation.INVALID_OPEN;
        int validacionReserva = Operation.INVALID_OPEN;
        long[] cardCodes;

        try {
            cardCodes = MedioAcceso.obtenerCodigos(cardType, codigoAcceso);
        } catch (Exception ex) {
            return;
        }

        //Primero buscamos si existe el codigo del medio de acceso como un ResumenTarjetaControlador.
        ResumenTarjetaControlador rtc = (cardCodes != null) ? TarjetasAcceso.INSTANCE.getTarjeta(cardType, cardCodes[0], cardCodes[1]) : null;

        //Permisos completos de la validacion por MedioAcceso o Reserva. Se sumaran los que correspondan segun la Reserva y su destino. Valido solo para control tipo ascensor.
        int permisosTotales = 0;

        if (rtc != null) {
            permisosTotales = rtc.permisos;
            Log.error("Codigo corresponde a una TarjetaAcceso. Validando...");
            if (controlador.controladorAscensor()) {
                //Logica de ascensor. cualquier permiso != 0 es valido para 'generar' apertura.
                validacionMedioAcceso = rtc.permisos != 0 ? Operation.VALID_OPEN : Operation.INVALID_OPEN;
            } else {
                //Controladore estandar requiere que el punto de acceso en cuestion permita el acceso.
                validacionMedioAcceso = puntoAcceso.validar(rtc);//rtc.validar(puntoAcceso);
            }

            //Validacion de canal horario. Solo lo aplicamos si cualquiera validacion anterior estaba 'permitiendo' el acceso. De lo contrario fue un rechazo por otro motivo. ergo. ya no es importante esta validacion.
            if (validacionMedioAcceso == Operation.VALID_OPEN) {
                CanalHorario canalHorario = rtc.id_canal_horario != 0 ? CanalesHorarios.INSTANCE.getById(rtc.id_canal_horario) : null;

                if (canalHorario != null && !canalHorario.esValido()){
                    Log.error("El canal Horario no está en un horario válido");
                    validacionMedioAcceso = Operation.TIME_REFUSE;
                }
            }
        }

        //Buscamos si existe una Persona con credencial igual al codigoAcceso.
        String fechaHoy = Util.dateFormat.format(new Date());
        Persona persona = null;
        List<Reserva> reservas = new ArrayList<Reserva>();

        if (cardType == CardTypes.PERSON_ID){
            persona = Personas.INSTANCE.getByCredencial(TipoCredencial.CEDULA_IDENTIDAD, codigoAcceso);

            if (persona != null)
                {
                //La persona efectivamente existe. debemos buscar Reservas para esta persona el dia de hoy.
                reservas = persona.obtenerReservasValidas(fechaHoy);
                }
            } else if (cardType == CardTypes.PASSPORT_ID) {
            persona = Personas.INSTANCE.getByCredencial(TipoCredencial.PASAPORTE, codigoAcceso);
            if (persona != null) {
                //La persona efectivamente existe. debemos buscar Reservas para esta persona el dia de hoy.
                reservas = persona.obtenerReservasValidas(fechaHoy);
            }
        } else if (cardType == CardTypes.RESERVATION_CODE) {
            ConjuntoReserva conjuntoReserva = ConjuntosReservas.INSTANCE.getConjuntoReservaCodigoReserva(codigoAcceso);
            if (conjuntoReserva != null) {
                reservas = conjuntoReserva.reservasAsociadasFecha(fechaHoy);
                //Igualmente necesitaremos los datos de la persona vinculada a dicha reserva.
                if (reservas.size() >= 1) {
                    persona = reservas.get(0).getPersonaReservada();
                }
            }
        } else if(cardType == CardTypes.RESERVATION_ID) {
            // String codigoQr[] = codigoAcceso.split("_");
            // int idConjuntoReserva = Integer.parseInt(codigoQr[1]);
            // ConjuntoReserva conjuntoReserva = ConjuntosReservas.INSTANCE.getByID(idConjuntoReserva);
            // if (conjuntoReserva != null) {
            //     reservas = conjuntoReserva.reservasAsociadasFecha(fechaHoy);
            //     //Igualmente necesitaremos los datos de la persona vinculada a dicha reserva.
            //     if (reservas.size() >= 1) {
            //         persona = reservas.get(0).getPersonaReservada();
            //     }
            // }
        }

        //Se logró recuperar alguna reserva?
        if (reservas.isEmpty()) {
            //No hay reservas para esta persona. nada que hacer.
            Log.error("No se han encontrado reservas para la persona");
        } else {
            //Validamos todas las reservas que se encuentren. Ojo que en modo de control de ascensores, el Controlador tendra puntos como pisos/botones.
            List<PuntoAcceso> puntosAcceso = new ArrayList<PuntoAcceso>();
            if (controlador.controladorAscensor()) {
                //En modo Ascensor tendremos que validar en cada punto.
                puntosAcceso = controlador.obtenerPuntosAcceso();
            } else {
                    //Modo estandar, solo validamos el canal en cuestion.
            puntosAcceso.add(puntoAcceso);
            }
            for (Reserva r : reservas) {
                Log.error(String.format("Revisando reserva: " + new Gson().toJson(r)));
                int validacion = Operation.INVALID_OPEN;
                //Intentamos validar esta Reserva en todos los puntos de acceso de este controlador.
                for (PuntoAcceso p : puntosAcceso) {
                    Log.error("Validando reserva en punto de acceso: " + p.toString());
                    validacion = r.validar(p);
                    if (validacionReserva != Operation.VALID_OPEN && validacion != Operation.INVALID_OPEN) {
                        //La validacion de todas las Reservas aun no genera apertura; Y además el codigo de este ultimo intento no es sencillamente 'No apertura'.
                        //Por tanto, traspasamos ese codigo de operacion al resultado 'final' de la validacion de las posibles Reservas, para ser lo mas especificos en caso de no apertura.
                        validacionReserva = validacion;
                    }
                    if (validacion == Operation.VALID_OPEN) {
                        //Si el punto permite acceder a la Reserva, necesitamos sumar el bit correspondiente a los permisos para los botones en caso de Controlador de ascensores.
                        Log.error("Reserva valida en este punto");
                        permisosTotales |= (0b1 << p.numeroCanal);
                    }
                }
                //La Reserva, en cualquiera de los puntos de acceso logró ser validada.
                if (validacion == Operation.VALID_OPEN) {
                    //Si esta reserva genera una validacion exitosa, debemos notificar a GK2 que la Reserva ha validado, y cambie el estado al que corresponda.
                    //Para ello es almacenado en una tabla interna que es sincronizada de manera automatica
                    //Esto no se realiza en el IF anterior dado que duplicaria este registro para la Reserva.
                    ReservasValidadas.INSTANCE.save(new ReservaValidada(r, controlador.id, canal));
                }
            }
        }

        Log.info(true,"Permisos totales para la validacion: " + permisosTotales);

        //Para almacenar el registro en el log.
        LogAcceso log = new LogAcceso();

        //Data que no depende de la tarjeta ni la persona asociada.
        log.id_controlador = controlador.id;
        log.id_punto_acceso = puntoAcceso.id;
        log.id_cliente = controlador.id_propietario;
        log.nombre_controlador = controlador.nombre;
        log.nombre_punto_acceso = puntoAcceso.nombre;
        log.id_origen_lectura = ReadSource.SOURCE_HFA5_READER;
        log.tipoMedioAcceso = cardType;
        log.codigo_medio_acceso = codigoAcceso;
        log.fecha_registro = Util.getDateTime(new Date());

        if (rtc != null) {
            //Habia un ResumenTarjeta, recuperamos esos datos.
            log.id_cliente_real = rtc.id_cliente;
            log.id_medio_acceso = rtc.id_medio_acceso;
            log.nombre_portador_medio_acceso = rtc.nombre + " " + rtc.apellido;
            log.credencial = rtc.credencial;
        } else if (persona != null) {
            //No habia ResumenTarjeta, podria ser un antipassback, usamos el codigo de retorno de la validacion de Reservas.
            log.nombre_portador_medio_acceso = persona.nombre + " " + persona.apellido;
            log.credencial = persona.credencial;
            log.id_operacion_lectura = validacionReserva;
        } else {
            //Ni idea quien fue.
        }
        if (validacionMedioAcceso == Operation.VALID_OPEN || validacionReserva == Operation.VALID_OPEN) {
            log.id_operacion_lectura = Operation.VALID_OPEN;
            if (controlador.controladorAscensor()) {
                String ipControladoraBotonera = controlador.obtenerValorParametro("ipControladoraBotonera");
                notificarControladorBotonera(ipControladoraBotonera, permisosTotales);
            }
        } else {
            if (rtc != null) {
                log.id_operacion_lectura = validacionMedioAcceso;
            } else {
                log.id_operacion_lectura = validacionReserva;
            }
        }
        LogsAcceso.INSTANCE.save(log);
    }

    private void procesar_comando_open(ComandoManual comandoManual) {
        Log.error("Solicitud de apertura desde sistema GK");
        try {
            //Almacenamos el Log. Para ello necesitamos primero el Controlador.
            Configuracion idControlador = Configuraciones.INSTANCE.getParametro("idDevice");
            Controlador controlador = Controladores.INSTANCE.getByID(Integer.parseInt(idControlador.valor));
            if (controlador == null) {
                return;
            }
            int numeroCanalSolicitado = Integer.parseInt(comandoManual.comando.substring(COMANDO_OPEN.length()));
            PuntoAcceso puntoAcceso = controlador.obtenerPuntoAcceso(numeroCanalSolicitado);
            if (puntoAcceso == null) {
                return;
            }
            LogAcceso log = new LogAcceso();
            log.id_controlador = controlador.id;
            log.id_punto_acceso = puntoAcceso.id;
            log.id_cliente = controlador.id_propietario;
            log.nombre_controlador = controlador.nombre;
            log.nombre_punto_acceso = puntoAcceso.nombre;
            log.tipoMedioAcceso = 0;
            log.id_operacion_lectura = Operation.VALID_OPEN;
            log.fecha_registro = Util.getDateTime(new Date());
            log.id_origen_lectura = ReadSource.SOURCE_SOFTWARE;
            log.codigo_medio_acceso = String.format("%d", comandoManual.id_usuario);
            LogsAcceso.INSTANCE.save(log);
            RespuestasComandos.INSTANCE.save(
                new RespuestaComandoManual(comandoManual.id, "OK")
            );
            Relay.INSTANCE.openRelay(numeroCanalSolicitado);
        } catch (Exception ex) {}
    }

    private static void notificarControladorBotonera(String ipControladoraBotonera, int permisosTotales) {
        if (permisosTotales == 0 || permisosTotales > ((1 << CONF.CANTIDAD_CANALES) - 1)) {
            Log.error("Permisos no validos: " + permisosTotales);
            return;
        }
        List<String> canalesPermitidos = new ArrayList<String>();
        List<Integer> canalesApertura = new ArrayList<Integer>();
        try {
            for (int i = 0; i < 16; i++) {
                if ((permisosTotales & (0b1 << i)) != 0) {
                    canalesPermitidos.add(String.format("%d", i));
                    canalesApertura.add(i);
                }
            }
            String comando = String.format("OUTPUT:OPEN:%s\r\n", String.join(",", canalesPermitidos));
            System.out.println(comando);
            Relay.INSTANCE.openRelay(canalesApertura);
        } catch(Exception ex){}
    }
}
