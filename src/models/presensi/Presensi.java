package models.presensi;

import models.users.Employee;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public class Presensi {
    private UUID presensiID;
    private Date date;
    private StatusKehadiran status;
    private Employee emp;

    public Presensi(UUID presensiID, Date date, Time clockOut, StatusKehadiran status, Employee emp) {
        this.presensiID = presensiID;
        this.date = date;
        this.status = status;
        this.emp = emp;
    }

    public UUID getPresensiID() {
        return this.presensiID;
    }

    public void setPresensiID(UUID presensiID) {
        this.presensiID = presensiID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StatusKehadiran getStatus() {
        return this.status;
    }

    public void setStatus(StatusKehadiran status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.date + " " + this.status + " " + this.emp.getName();
    }

}
