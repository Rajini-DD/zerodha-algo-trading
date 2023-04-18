package com.dtech.kitecon.formulae;

import java.util.ArrayList;

public class StochasticRSI {

        private ArrayList<Double> rsiValues = new ArrayList<>();

        public void calculateStochasticRSI(ArrayList<Double> closePrices, int rsiPeriod, int stochPeriod, int stochSmoothing) {
            // Calculate the RSI values
            RSI rsi = new RSI();
            rsi.calculateRSI(closePrices, rsiPeriod);
            rsiValues = rsi.getRSI();

            ArrayList<Double> stochRSI = new ArrayList<>();
            double stochRSIValue = 0;

            // Calculate the Stochastic RSI values
            for (int i = rsiPeriod + stochPeriod - 1; i < rsiValues.size(); i++) {
                double highestRSI = rsiValues.get(i);
                double lowestRSI = rsiValues.get(i);

                for (int j = i - stochPeriod + 1; j <= i; j++) {
                    if (rsiValues.get(j) > highestRSI) {
                        highestRSI = rsiValues.get(j);
                    }
                    if (rsiValues.get(j) < lowestRSI) {
                        lowestRSI = rsiValues.get(j);
                    }
                }

                double stochRSIUnsmoothed = (rsiValues.get(i) - lowestRSI) / (highestRSI - lowestRSI);

                if (i == rsiPeriod + stochPeriod - 1) {
                    stochRSIValue = stochRSIUnsmoothed;
                } else {
                    stochRSIValue = ((stochSmoothing - 1) * stochRSIValue + stochRSIUnsmoothed) / stochSmoothing;
                }

                stochRSI.add(stochRSIValue);
            }
        }

        public ArrayList<Double> getStochRSI() {
            return stochRSI;
        }
    }

