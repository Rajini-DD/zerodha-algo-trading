package com.dtech.kitecon.formulae;

import java.util.ArrayList;

public class MACD {
    private ArrayList<Double> macdValues = new ArrayList<>();

    public void calculateMACD(ArrayList<Double> closePrices, int fastPeriod, int slowPeriod, int signalPeriod) {
        ArrayList<Double> emaFast = calculateEMA(closePrices, fastPeriod);
        ArrayList<Double> emaSlow = calculateEMA(closePrices, slowPeriod);

        ArrayList<Double> macdLine = new ArrayList<>();
        for (int i = 0; i < emaFast.size(); i++) {
            double macdValue = emaFast.get(i) - emaSlow.get(i);
            macdLine.add(macdValue);
        }

        ArrayList<Double> signalLine = calculateEMA(macdLine, signalPeriod);

        // Calculate the MACD histogram
        for (int i = 0; i < macdLine.size(); i++) {
            double macdHistogram = macdLine.get(i) - signalLine.get(i);
            macdValues.add(macdHistogram);
        }
    }

    private ArrayList<Double> calculateEMA(ArrayList<Double> closePrices, int period) {
        ArrayList<Double> emaValues = new ArrayList<>();

        double multiplier = 2.0 / (period + 1);
        double ema = closePrices.get(0);

        emaValues.add(ema);

        for (int i = 1; i < closePrices.size(); i++) {
            double closePrice = closePrices.get(i);
            ema = (closePrice - ema) * multiplier + ema;
            emaValues.add(ema);
        }

        return emaValues;
    }

    public ArrayList<Double> getMACDHistogram() {
        return macdValues;
    }
}

