package util;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class DbUtils {
    public static String getUUID(){
        return java.util.UUID.randomUUID().toString();
    }
}
