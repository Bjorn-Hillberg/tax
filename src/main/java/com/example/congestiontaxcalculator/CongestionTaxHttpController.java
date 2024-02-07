package com.example.congestiontaxcalculator;

import com.example.congestiontaxcalculator.toll.TollDateService;
import com.example.congestiontaxcalculator.toll.TollFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@ResponseBody
public class CongestionTaxHttpController {
    Logger logger = LoggerFactory.getLogger(CongestionTaxHttpController.class);
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
    private final CongestionTaxCalculator congestionTaxCalculator = new CongestionTaxCalculator(new TollDateService(), new TollFeeService());

    @PostMapping("/calculateTax")
    int calculateTax(@RequestBody VehicleDates vehicleDates) throws ParseException {
        if (Objects.isNull(vehicleDates)) {
            logger.warn("object is missing");
            return -1;
        }
        List<Date> datesToCheck = new ArrayList<>();
        for (String sDate : vehicleDates.dates()) {
            datesToCheck.add(formatter.parse(sDate));
        }
        return congestionTaxCalculator.getTax(vehicleDates.vehicle(), datesToCheck);
    }
}