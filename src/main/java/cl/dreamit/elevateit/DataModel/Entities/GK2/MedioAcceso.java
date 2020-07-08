package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.DataModel.Const.CardTypes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "medioAcceso")
public class MedioAcceso {
    @Id
    //@GeneratedValue
    public int id;
    public int id_subtipo_medio;
    public int id_colaborador;
    public String nombre;
    public String fecha_inicio;
    public String fecha_termino;
    public boolean estado;
    public String codigo;
    public String tipo_medio;

    public static long[] obtenerCodigos(int tipoMedioAcceso, String codigoAcceso) {
        long[] retorno = new long[]{0L, 0L};
        String elements[];
        switch (tipoMedioAcceso) {
            case CardTypes.PERSON_ID:
                elements = codigoAcceso.split("-");
                retorno[0] = Long.parseLong(elements[0]);
                retorno[1] = (long) elements[1].charAt(0);
                break;
            case CardTypes.RFID_ID:
                if (codigoAcceso.contains(".")) {
                    elements = codigoAcceso.split(".");
                    retorno[0] = Long.parseLong(elements[0]);
                    retorno[1] = Long.parseLong(elements[1]);
                } else if (codigoAcceso.contains("-")) {
                    elements = codigoAcceso.split("-");
                    retorno[0] = Long.parseLong(elements[0]);
                    retorno[1] = Long.parseLong(elements[1]);
                } else {
                    retorno[0] = 0;
                    retorno[1] = Long.parseLong(codigoAcceso);
                }
                break;
            case CardTypes.MIFARE_ID:
                retorno[0] = Long.parseLong(codigoAcceso);
                break;
            default:
                return null;
        }
        return retorno;
    }
}
