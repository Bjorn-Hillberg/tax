package com.example.congestiontaxcalculator;

import com.example.congestiontaxcalculator.toll.TollDateService;
import com.example.congestiontaxcalculator.toll.TollFeeService;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.congestiontaxcalculator.Vehicle.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CongestionTaxCalculatorTest {
    private CongestionTaxCalculator taxCalculator = new CongestionTaxCalculator(new TollDateService(), new TollFeeService());
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
    private List<DateTaxTestUtil> taxDateAmounts = populateDatesAndTax();

    @Test
    void testSmallGapAboveAnHour() throws ParseException {
        List<DateTaxTestUtil> dateTaxTestUtils = new ArrayList<>();
        dateTaxTestUtils.add(new DateTaxTestUtil(formatter.parse("2013-02-08 14:35:00"), 8));
        dateTaxTestUtils.add(new DateTaxTestUtil(formatter.parse("2013-02-08 15:29:00"), 13));
        dateTaxTestUtils.add(new DateTaxTestUtil(formatter.parse("2013-02-08 15:47:00"), 18));
        dateTaxTestUtils.add(new DateTaxTestUtil(formatter.parse("2013-02-08 16:01:00"), 18));

        int tax = taxCalculator.getTax(Vehicle.CAR, dateTaxTestUtils.stream().map(DateTaxTestUtil::taxDate).toList());
        assertEquals(49, tax);
        }

    @Test
    void simpleTaxCalculatorTest() {
        for (DateTaxTestUtil taxTestUtil : taxDateAmounts) {
            int tax = taxCalculator.getTax(Vehicle.CAR, List.of(taxTestUtil.taxDate()));
            assertEquals(taxTestUtil.taxAmount(), tax, "The value for " + taxTestUtil.taxDate().toString() + " is wrong!");
        }
    }

    @Test
    void listTaxCalculatorTest() {
        int tax = taxCalculator.getTax(Vehicle.CAR, taxDateAmounts.stream().map(DateTaxTestUtil::taxDate).toList());
        assertEquals(97, tax);
    }

    @Test
    void simpleNoTaxCalculatorTest() {
        List<Vehicle> noTaxVehicles = List.of(BUS, EMERGENCY, DIPLOMAT, MOTORCYCLE, MILITARY, FOREIGN);
        for (Vehicle vehicle : noTaxVehicles) {
            for (DateTaxTestUtil taxTestUtil : taxDateAmounts) {
                int tax = taxCalculator.getTax(vehicle, List.of(taxTestUtil.taxDate()));
                assertEquals(0, tax, "The value for " + taxTestUtil.taxDate().toString() + " is wrong!");
            }
        }
    }

    private List<DateTaxTestUtil> populateDatesAndTax() {
        List<DateTaxTestUtil> tmp = new ArrayList<>();
        try {
            //Don't add values to list.
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-01-14 21:00:00"), 0));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-01-15 21:00:00"), 0));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-07 06:23:27"), 8));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-07 15:27:00"), 13));

            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 06:20:27"), 8));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 06:27:00"), 8));

            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 14:35:00"), 8));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 15:29:00"), 13));

            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 15:47:00"), 18));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 16:01:00"), 18));

            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 16:48:00"), 18));

            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 17:49:00"), 13));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 18:29:00"), 8));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-02-08 18:35:00"), 0));

            tmp.add(new DateTaxTestUtil(formatter.parse("2013-03-26 14:25:00"), 8));
            tmp.add(new DateTaxTestUtil(formatter.parse("2013-03-28 14:07:27"), 8));
        } catch (ParseException e) {
            throw new RuntimeException("Couldn't parse Date", e);
        }
        return tmp;
    }
}