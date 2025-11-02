package models.schedules;

public class CashierRegister {
    private int noMachine;

    public CashierRegister(int noMachine) {
        this.noMachine = noMachine;
    }

    public int getNoMachine() {
        return this.noMachine;
    }

    public void setNoMachine(int noMachine) {
        this.noMachine = noMachine;
    }

    @Override
    public String toString() {
        return String.valueOf(this.noMachine); // ni diconvert dr int ke strinh
    }
}
