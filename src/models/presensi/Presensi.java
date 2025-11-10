package models.presensi;

import models.users.Employee;

import java.sql.Date;
import java.sql.Time;

public class Presensi {
    private Date date;
    private Time clockOut;
    private StatusPresensi status;
    private Employee emp;

    public Presensi(Date date, Time clockOut, StatusPresensi status, Employee emp) {
        this.date = date;
        this.clockOut = clockOut;
        this.status = status;
        this.emp = emp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getClockOut() {
        return this.clockOut;
    }

    public void setClockOut(Time clockOut) {
        this.clockOut = clockOut;
    }

    public StatusPresensi getStatus() {
        return this.status;
    }

    public void setStatus(StatusPresensi status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.date + " " + this.clockOut + " " + this.status;
    }

}
