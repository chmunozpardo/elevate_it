package cl.dreamit.elevateit.DataModel.Const;

public abstract class Operation {
    public static int VALID_OPEN = 1; //Apertura valida
    public static int INVALID_OPEN = 2; //Apertura invalida
    public static int PASSBACK_REFUSE = 3; //Passback evito la apertura.
    public static int TIME_REFUSE = 4; //Fuera de horario valido
    public static int PERMISSION_REFUSE = 5; //Sin permisos para acceder
    public static int BLOCKED_ACCESS = 6; //Puerta bloqueada
    public static int EMERGENCY_OPEN = 7; //Apertura por emergencia
    public static int EMERGENCY_DISARM = 8; //Condicion de emergencia levantada
    public static int RS485_NEW_READER = 9; //Condicion de emergencia levantada
    public static int RS485_READER_RESET = 10; //Condicion de emergencia levantada
    public static int RS485_READER_DIED = 11;    //Condicion de emergencia levantada
}
