package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cl.dreamit.elevateit.DataModel.DAO.ParametrosControladores;
import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;

import java.util.List;

@Entity
@Table(name = "controlador")
public class Controlador {
    @Id
    //@GeneratedValue
    public int id;
    public String nombre;
    public String ip;
    public String mac;
    public String gateway;
    public int id_propietario;

    /* Estructura que viene desde la API */
    @Transient
    public ParametroControlador[] parametros_controlador;

    @Transient
    public PuntoAcceso[] punto_acceso;

    /**
     * Recupera un PuntoAcceso de este controlador desde la base de datos.
     *
     * @param canal Numero del canal buscado, empezando desde 0
     * @return
     */
    public PuntoAcceso obtenerPuntoAcceso(int canal){
        return PuntosAccesos.getPuntoAccesoControlador(this.id, canal);
    }

    /**
     * Recupera un valor de parametro de este Controlador.
     *
     * @param parametro Nombre del parametro buscado
     * @return
     */
    public String obtenerValorParametro(String parametro) {
        ParametroControlador p = ParametrosControladores.getParametroControlador(this.id, parametro);
        if (p == null) {
            return null;
        }
        return p.valor_override != null ? p.valor_override : p.valor;
    }

    public ParametroControlador obtenerParametro(String nombreParametro) {
        return ParametrosControladores.getParametroControlador(this.id, nombreParametro);
    }

    /**
     * Actualiza el valor 'valor' del parametro de central indicado.
     */
    public void actualizarParametro(String nombreParametro, String valor) {
        ParametroControlador p = this.obtenerParametro(nombreParametro);
        if (p != null) {
            p.valor = valor;
        }
        ParametrosControladores.save(p);
    }

    /**
     * Retorna True si el controlado está destinado a controlar un ascensor, mediante el correspondiente parametro.
     *
     * @return
     */
    public boolean controladorAscensor() {
        ParametroControlador p = this.obtenerParametro("controladorAscensor");
        return p == null ? false : p.valor.equals("1");
    }

    public List<PuntoAcceso> obtenerPuntosAcceso() {
        return PuntosAccesos.getPuntosAccesoControlador(this.id);
    }
}