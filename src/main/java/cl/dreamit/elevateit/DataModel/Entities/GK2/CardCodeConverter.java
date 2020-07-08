package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.DataModel.Const.CardTypes;

import com.google.gson.Gson;

public class CardCodeConverter{
    //Solo para no tener que hacer magia y determinar el tipo de un arreglo.
    private static long[] returnType = {0L};

    public static long[] fromString(int tipoMedio, String value) {
        try {
            switch (tipoMedio) {
                case CardTypes.MIFARE_ID:
                    return new Gson().fromJson(value, returnType.getClass());
                case CardTypes.PERSON_ID:
                    //Es Cedula, contiener el separador?
                    if (value.contains("-") == false) {
                        //Nop. retorna un codigo muy especifico para indicar que hay un error.
                        return new long[]{-1L, -1L};
                    } else {
                        String[] elementos = value.split("-");
                        return new long[]{Long.parseLong(elementos[0]), (long) elementos[1].charAt(0)};
                    }
                }
            }
        catch (Exception ex){
            //LOG.error("Error intentando obtener codigos de MedioAcceso: " + value);
        }
        return new long[]{0L, 0L};
    }
}
