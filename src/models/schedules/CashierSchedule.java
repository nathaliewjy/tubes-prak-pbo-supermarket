package models.schedules;

import models.users.employees.Cashier;

import java.sql.Date;
import java.sql.Time;

public class CashierSchedule extends Schedule {
    private Cashier cash;
    private CashierRegister cashReg;

    public CashierSchedule(Cashier cash, CashierRegister cashReg, int noMachine, Date date, Time clockIn, Time clockOut) {
        super(date, clockIn, clockOut);
        this.cash = cash;
        this.cashReg = cashReg;
    }

    public Cashier getCashier() {
        return this.cash;
    }

    public void setCashier(Cashier cash) {
        this.cash = cash;
    }

    public CashierRegister getRegister() {
        return this.cashReg;
    }

    public void setRegister(CashierRegister cashReg) {
        this.cashReg = cashReg;
    }

    @Override
    public String toString() {
        return super.toString() +  " " + this.cash + " " + this.cashReg;
    }
}
