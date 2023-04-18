package com.dtech.kitecon.formulae;

import java.util.ArrayList;

public class HeikinAshi {
    private ArrayList<Double> haClose = new ArrayList<>();
    private ArrayList<Double> haOpen = new ArrayList<>();
    private ArrayList<Double> haHigh = new ArrayList<>();
    private ArrayList<Double> haLow = new ArrayList<>();

    public void calculateHeikinAshi(ArrayList<Double> close, ArrayList<Double> open,
                                    ArrayList<Double> high, ArrayList<Double> low) {
        double haOpenPrev = (open.get(0) + close.get(0)) / 2.0;
        double haClosePrev = (open.get(0) + close.get(0) + high.get(0) + low.get(0)) / 4.0;

        haOpen.add(haOpenPrev);
        haClose.add(haClosePrev);
        haHigh.add(high.get(0));
        haLow.add(low.get(0));

        for (int i = 1; i < close.size(); i++) {
            double haCloseCurr = (close.get(i) + open.get(i) + high.get(i) + low.get(i)) / 4.0;
            double haOpenCurr = (haOpenPrev + haClosePrev) / 2.0;
            double haHighCurr = Math.max(high.get(i), Math.max(haOpenCurr, haCloseCurr));
            double haLowCurr = Math.min(low.get(i), Math.min(haOpenCurr, haCloseCurr));

            haClose.add(haCloseCurr);
            haOpen.add(haOpenCurr);
            haHigh.add(haHighCurr);
            haLow.add(haLowCurr);

            haClosePrev = haCloseCurr;
            haOpenPrev = haOpenCurr;
        }
    }

    public ArrayList<Double> getHaClose() {
        return haClose;
    }

    public ArrayList<Double> getHaOpen() {
        return haOpen;
    }

    public ArrayList<Double> getHaHigh() {
        return haHigh;
    }

    public ArrayList<Double> getHaLow() {
        return haLow;
    }
}

