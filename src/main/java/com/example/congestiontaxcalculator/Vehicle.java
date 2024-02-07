package com.example.congestiontaxcalculator;

public enum Vehicle {
    MOTORCYCLE(true),
    CAR(false),
    TRACTOR(true),
    EMERGENCY(true),
    BUS(true),
    DIPLOMAT(true),
    FOREIGN(true),
    MILITARY(true);

    private final boolean taxExempt;

    Vehicle(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public boolean isTollFree() {
        return taxExempt;
    }
}
