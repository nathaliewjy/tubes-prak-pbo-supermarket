package view;

import controller.ManagerController;
import models.users.Role;
import util.CLIUtil;

import java.util.UUID;

public class ManagerView {
    private ManagerController managerController;

    public ManagerView() {
        this.managerController = new ManagerController();
    }

    public void menu() {
        System.out.println("Menu manager : ");
        System.out.println("1. Hire employee");
        System.out.println("2. Fire employee");
        System.out.println("3. Change employee's role");
        System.out.println("4. Calculate employee's salary");
        System.out.println("5. Assign restock to stocker");
        System.out.println("6. Monitor total uang");
        System.out.println("7. Monitor total barang");

        int pilihMenu = CLIUtil.getInt("Pilih menu : ");

        switch (pilihMenu) {

        }
    }

    //menu 1
    public void menuHire(String name, Role role, int salary, int workingHours, String nik) {
        System.out.println("Role employee : ");
        System.out.println("1. Cashier");
        System.out.println("2. Stocker");

        int pilihRole = CLIUtil.getInt("Pilih role employee : ");

        switch (pilihRole) {

        }
    }

    //menu 2
    public void menuFire(String nik) {
        nik = CLIUtil.getString("NIK emplouee yang mau di fire : ")
    }

    //menu 3
    public void menuChangeRole(UUID employeeID, Role newRole) {

    }



}
