package cl.dreamit.elevateit.DataModel.Const;

public enum ReportSource {
    WIEGAND_READER(1),
    DREAMIT_READER(2),
    BUTTON(3),
    SOFTWARE(4),
    EMERGENCY(5);

    private final int code;

    ReportSource(int n) {
        code = n;
    }

    public int getCode() {
        return code;
    }

    public static ReportSource resolve(int n) {
        for (ReportSource rs : ReportSource.values()) {
            if (rs.getCode() == n) {
                return rs;
            }
        }
        return null;
    }
}
