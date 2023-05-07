package com.dtech.kitecon.options;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;

public enum OptionInstrumentType {
    NIFTY("NIFTY",50),
    BANK_NIFTY("BANKNIFTY",100),
    FIN_NIFTY("FINNIFTY",50);

    private final String name;
    private final int strikePrice;
    OptionInstrumentType(String name, int strikePoint){
        this.name = name;
        this.strikePrice = strikePoint;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     *
     * 1 Mon
     * 2 Tue
     * 3 Wed
     * 4 Thru
     * 5 Fri
     * 6 Sat
     * 7 Sun
     */
    public String getInstrumentConstructed(ZonedDateTime date, Double amount) {
        //Getting the current date value

        //Getting the current day
        int currentDay = date.getDayOfMonth();

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        int week = dayOfWeek.getValue();

        int thru = 7 - week;
        System.out.println("Current day: "+currentDay);
        //Getting the current month
        Month currentMonth = date.getMonth();
        System.out.println("Current month: "+currentMonth);
        //getting the current year
        int currentYear = date.getYear();

        String symbol = name
                + currentYear%100
                + currentMonth
                + (currentDay + thru)
                + getCallPut(amount);
        return symbol;
    }

    private String getCallPut(Double amount) {
        int amt = amount.intValue() % strikePrice;
        if( ((amt/strikePrice) * 100) < 50 )
            return "PE";
        else
            return "CE";
    }
}
