package models.schedules;

import java.sql.Date;
import java.sql.Time;

public abstract class Schedule {
    private Date date;
    private Time clockIn;
    private Time clockOut;

    public Schedule(Date date, Time clockIn, Time clockOut) {
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
    }
}
