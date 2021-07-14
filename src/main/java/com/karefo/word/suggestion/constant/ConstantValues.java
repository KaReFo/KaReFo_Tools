package com.karefo.word.suggestion.constant;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class ConstantValues {

    public static String TODAYS_DATE = "";
    public static int TOTAL_REQUEST_COUNT = 500;
    public static int REQUEST_COUNT = 0;

    static {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        //System.out.println("Initialized:"+dtf.format(now));
        TODAYS_DATE  = dtf.format(now);
    }

    public static String USER_DETAILS[][] = new String[][]{{"brundanivas@gmail.com","cfYnAN"}, {"chezhian@gmail.com","karefo"}};
}
