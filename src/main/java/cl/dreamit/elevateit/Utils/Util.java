package cl.dreamit.elevateit.Utils;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Util {
    public static SimpleDateFormat dateFormat =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getDateTime(Date date) {
       return dateFormat.format(date);
    }

    public static String bytesToHexString(byte[] src, boolean addSpace) {
        return src != null ? bytesToHexString(src, addSpace, src.length) : "";
    }

    public static String bytesToHexString(byte[] src, boolean addSpace, int n) {
        if (src == null) {
            return "";
        }
        if (src.length < 0) {
            return "INVALID ARRAY LENGTH";
        }
        byte[] output;
        if (addSpace)
            output = new byte[n * 3];
        else
            output = new byte[n * 2];

        for (int i = 0; (i < n) && (i < src.length); i++) {
            byte upper = (byte) (src[i] >>> 4 & 0x0F);
            byte lower = (byte) (src[i] & 0x0F);
            if (addSpace) {
                output[i * 3] = upper > 9 ? (byte) (upper + 55) : (byte) (upper + 48);
                output[(i * 3) + 1] = lower > 9 ? (byte) (lower + 55) : (byte) (lower + 48);
                output[(i * 3) + 2] = ' ';
            } else {
                output[i * 2] = upper > 9 ? (byte) (upper + 55) : (byte) (upper + 48);
                output[(i * 2) + 1] = lower > 9 ? (byte) (lower + 55) : (byte) (lower + 48);
            }
        }
        return new String(output);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 |
                           charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static int getDiaSemana(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int diaSemanaGringo = calendar.get(Calendar.DAY_OF_WEEK);
        switch (diaSemanaGringo) {
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 4;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            case Calendar.SUNDAY:
                return 7;
            default:
                return 1;
        }
    }
}
