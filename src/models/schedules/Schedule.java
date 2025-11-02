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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getClockIn() {
        return this.clockIn;
    }

    public void setClockIn(Time clockIn) {
        this.clockIn = clockIn;
    }

    public Time getClockOut() {
        return this.clockOut;
    }

    public void setClockOut(Time clockOut) {
        this.clockOut = clockOut;
    }

    @Override
    public String toString() {
        return this.date + " " + this.clockIn + " " + this.clockOut;
    }
}
