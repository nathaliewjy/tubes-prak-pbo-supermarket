package models.presensi;

import models.users.Employee;

import java.sql.Date;
import java.sql.Time;

public class Presensi {
    private Date date;
    private Time clockOut;
    private Presensi pres;
    private Employee emp;

    public Presensi(Date date, Time clockOut, Presensi pres, Employee emp) {
        this.date = date;
        this.clockOut = clockOut;
        this.pres = pres;
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

    public Presensi getPres() {
        return this.pres;
    }

    public void setPres(Presensi pres) {
        this.pres = pres;
    }

    @Override
    public String toString() {
        return this.date + " " + this.clockOut + " " + this.pres;
    }

}
