package com.dtech.kitecon.formulae;

import java.util.ArrayList;

public class RSI {
    private ArrayList<Double> rsiValues = new ArrayList<>();

    public void calculateRSI(ArrayList<Double> closePrices, int period) {
        ArrayList<Double> gain = new ArrayList<>();
        ArrayList<Double> loss = new ArrayList<>();

        double prevPrice = closePrices.get(0);

        // Calculate the gains and losses
        for (int i = 1; i < closePrices.size(); i++) {
            double currPrice = closePrices.get(i);
            double priceChange = currPrice - prevPrice;
            prevPrice = currPrice;

            if (priceChange >= 0) {
                gain.add(priceChange);
                loss.add(0.0);
            } else {
                gain.add(0.0);
                loss.add(-priceChange);
            }
        }

        // Calculate the average gain and average loss
        ArrayList<Double> avgGain = new ArrayList<>();
        ArrayList<Double> avgLoss = new ArrayList<>();

        double sumGain = 0;
        double sumLoss = 0;

        for (int i = 0; i < period; i++) {
            sumGain += gain.get(i);
            sumLoss += loss.get(i);
        }

        avgGain.add(sumGain / period);
        avgLoss.add(sumLoss / period);

        for (int i = period; i < closePrices.size(); i++) {
            sumGain = sumGain - gain.get(i - period) + gain.get(i);
            sumLoss = sumLoss - loss.get(i - period) + loss.get(i);
            avgGain.add(sumGain / period);
            avgLoss.add(sumLoss / period);
        }

        // Calculate the RSI values
        for (int i = 0; i < avgGain.size(); i++) {
            double rs = avgGain.get(i) / avgLoss.get(i);
            double rsiValue = 100.0 - (100.0 / (1.0 + rs));
            rsiValues.add(rsiValue);
        }
    }

    public ArrayList<Double> getRSI() {
        return rsiValues;
    }
}

