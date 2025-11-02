package models.schedules;

import models.products.Product;
import models.users.Supplier;

import java.sql.Time;
import java.sql.Date;

public class SupplierSchedule extends Schedule {
    private Supplier sup;
    private Product prodResupplied;

    public SupplierSchedule(Supplier sup, Product prodResupplied, Date date, Time clockIn, Time clockOut) {
        super(date, clockIn, clockOut);
        this.sup = sup;
        this.prodResupplied = prodResupplied;
    }

    public Supplier getSupplier() {
        return this.sup;
    }

    public void setSupplier(Supplier sup) {
        this.sup = sup;
    }

    public Product getProductResupplied() {
        return this.prodResupplied;
    }

    public void setProductResupplied(Product prodResupplied) {
        this.prodResupplied = prodResupplied;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.sup + " " + this.prodResupplied;
    }
}
