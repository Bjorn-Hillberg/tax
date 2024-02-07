package com.example.congestiontaxcalculator.toll;

import com.example.congestiontaxcalculator.Vehicle;

import java.util.Date;

public class TollFeeService {
    public int getTollFee(Date date) {
        int hour = date.getHours();
        int minute = date.getMinutes();

        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if ((hour == 8 && minute >= 30) || hour > 8 && hour <= 14) return 8;
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour == 15 || hour == 16) return 18;
        else if (hour == 17) return 13;
        else if (hour == 18 && minute <= 29) return 8;
        else return 0;
    }
}
