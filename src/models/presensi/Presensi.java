package models.presensi;

import models.users.Employee;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public class Presensi {
    private UUID presensiID;
    private Date date;
    private StatusKehadiran status;
    private UUID empID;

    public Presensi( Date date, Time clockOut, StatusKehadiran status, UUID empID) {
        this.presensiID = UUID.randomUUID();
        this.date = date;
        this.status = status;
        this.empID = empID;
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
        return this.date + " " + this.status + " " + this.empID.getName();
    }

}
