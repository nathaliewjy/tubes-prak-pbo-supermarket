package models.schedules;

import java.sql.Time;
import java.sql.Date;

public class SupplierSchedule extends Schedule {
    // kurang productResupplied

    public SupplierSchedule(Date date, Time clockIn, Time clockOut) {
        super(date, clockIn, clockOut);
    }

}
