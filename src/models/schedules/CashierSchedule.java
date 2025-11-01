package models.schedules;

import java.sql.Date;
import java.sql.Time;

public class CashierSchedule extends Schedule {

    public CashierSchedule(int noMachine, Date date, Time clockIn, Time clockOut) {
        super(date, clockIn, clockOut);
    }
}
