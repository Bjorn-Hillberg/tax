package com.example.congestiontaxcalculator;

import com.example.congestiontaxcalculator.toll.TollDateService;
import com.example.congestiontaxcalculator.toll.TollFeeService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CongestionTaxCalculator {
    private final TollDateService tollDateService;
    private final TollFeeService tollFeeService;

    public CongestionTaxCalculator(TollDateService tollDateService, TollFeeService tollFeeService) {
        this.tollDateService = tollDateService;
        this.tollFeeService = tollFeeService;
    }

    public int getTax(Vehicle vehicle, List<Date> dates) {
        int totalFee = 0;
        Map<LocalDate, List<Date>> dateListMap = groupByDay(dates);
        for (List<Date> dates1 : dateListMap.values()) {
            totalFee += calculateFeePerDay(vehicle, dates1);
        }
        return totalFee;
    }

    private Map<LocalDate, List<Date>> groupByDay(List<Date> dates) {
        return dates.stream().collect(Collectors.groupingBy(date -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
    }

    private int calculateFeePerDay(Vehicle vehicle, List<Date> dates) {
        dates.sort(Date::compareTo);
        int dayFee = 0;
        Date intervalStart = dates.get(0);
        for (Date date : dates) {
            int nextFee = getTollFee(date, vehicle);
            int tempFee = getTollFee(intervalStart, vehicle);

            long diffInMillis = date.getTime() - intervalStart.getTime();
            long minutes = diffInMillis / 1000 / 60;
            if (minutes <= 60) {
                if (dayFee > 0) dayFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                dayFee += tempFee;
            } else {
                dayFee += nextFee;
            }
        }
        if (dayFee > 60) dayFee = 60;
        return dayFee;
    }

    private int getTollFee(Date date, Vehicle vehicle) {
        if (tollDateService.isTollFreeDate(date) || vehicle.isTollFree()) return 0;
        return tollFeeService.getTollFee(date);
    }
}
