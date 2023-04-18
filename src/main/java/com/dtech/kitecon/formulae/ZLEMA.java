package com.dtech.kitecon.formulae;

import java.util.ArrayList;

public class ZLEMA {
    private ArrayList<Double> zlemaValues = new ArrayList<>();

    public void calculateZLEMA(ArrayList<Double> closePrices, int period) {
        EMA ema = new EMA();
        ArrayList<Double> ema1 = new ArrayList<>();
        ArrayList<Double> ema2 = new ArrayList<>();

        ema.calculateEMA(closePrices, period);
        ema1 = ema.getEMA();
        ema.calculateEMA(ema1, period);
        ema2 = ema.getEMA();

        for (int i = 0; i < ema1.size(); i++) {
            double zlema = 2 * ema1.get(i) - ema2.get(i);
            zlemaValues.add(zlema);
        }
    }

    public ArrayList<Double> getZLEMA() {
        return zlemaValues;
    }
}

