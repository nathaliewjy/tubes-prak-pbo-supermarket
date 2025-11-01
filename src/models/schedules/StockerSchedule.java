package models.schedules;

import java.sql.Date;
import java.sql.Time;

public class StockerSchedule extends Schedule {

    public StockerSchedule(Date date, Time clockIn, Time clockOut) {
        super(date, clockIn, clockOut);
    }
}
