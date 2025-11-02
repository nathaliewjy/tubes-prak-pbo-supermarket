package models.schedules;

import models.users.employees.Stocker;

import java.sql.Date;
import java.sql.Time;

public class StockerSchedule extends Schedule {
    private Stocker stock;

    public StockerSchedule(Stocker stock, Date date, Time clockIn, Time clockOut) {
        super(date, clockIn, clockOut);
        this.stock = stock;
    }

    public Stocker getStocker() {
        return this.stock;
    }

    public void setStocker(Stocker stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.stock;
    }
}
