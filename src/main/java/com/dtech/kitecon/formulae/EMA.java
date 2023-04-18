package com.dtech.kitecon.formulae;

import java.util.ArrayList;

public class EMA {
    private ArrayList<Double> emaValues = new ArrayList<>();

    public void calculateEMA(ArrayList<Double> closePrices, int period) {
        double multiplier = 2.0 / (period + 1);
        double ema = closePrices.get(0);

        emaValues.add(ema);

        for (int i = 1; i < closePrices.size(); i++) {
            double closePrice = closePrices.get(i);
            ema = (closePrice - ema) * multiplier + ema;
            emaValues.add(ema);
        }
    }

    public ArrayList<Double> getEMA() {
        return emaValues;
    }
}

