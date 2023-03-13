package com.dtech.kitecon.strategy.builder;

import com.dtech.kitecon.data.Instrument;
import com.dtech.kitecon.strategy.TradeDirection;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.indicators.volume.VWAPIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.*;

public class VwapRsiEma extends BaseStrategyBuilder{

    private static Strategy create3DaySmaUnderStrategy(BarSeries series) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        OpenPriceIndicator openPrice = new OpenPriceIndicator(series);
        EMAIndicator ema = new EMAIndicator(openPrice,9);
        EMAIndicator ema1 = new EMAIndicator(openPrice,5);
        VWAPIndicator vwap = new VWAPIndicator(series, 2);


//9480215566
        return new BaseStrategy(
                new UnderIndicatorRule(ema, closePrice),
                new OverIndicatorRule(ema1, closePrice)

                        //1993
                        //.or(new StopGainRule(closePrice, 500))
                        .or(new StopGainRule(closePrice, closePrice.numOf(5)))
                        // .or(new StopLossRule(closePrice, 500))
                        .or(new StopLossRule(closePrice, closePrice.numOf(5)))
                        .or(new TrailingStopLossRule(closePrice, DecimalNum.valueOf(1)))
        );
    }

    @Override
    protected Strategy getSellStrategy(Instrument tradingIdentity,
                                       Map<Instrument, BarSeries> barSeriesMap,
                                       Map<String, String> sell) {
        return null;
    }

    @Override
    public Strategy getBuyStrategy(Instrument tradingIdentity,
                                   Map<Instrument, BarSeries> BarSeriesMap,
                                   Map<String, String> config) {
        return create3DaySmaUnderStrategy(BarSeriesMap.get(tradingIdentity));
    }

    @Override
    public TradeDirection getTradeDirection() {
        return TradeDirection.Buy;
    }

    @Override
    public String getName() {
        return "VwapRsiEma";
    }

}
