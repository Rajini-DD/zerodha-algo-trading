package com.dtech.kitecon.strategy.builder;

import com.dtech.kitecon.data.Instrument;
import com.dtech.kitecon.strategy.TradeDirection;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.*;

@Component
public class SimpleMovingAverageStrategyBuider extends BaseStrategyBuilder {

  private static Strategy create3DaySmaUnderStrategy(BarSeries series) {
    ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
    OpenPriceIndicator openPrice = new OpenPriceIndicator(series);
    SMAIndicator sma = new SMAIndicator(closePrice, 9);
    SMAIndicator sma1 = new SMAIndicator(closePrice, 5);
    EMAIndicator ema9 = new EMAIndicator(openPrice,9);
    EMAIndicator ema5 = new EMAIndicator(openPrice,5);
   /** return new BaseStrategy(
        new UnderIndicatorRule(sma, closePrice),
        new OverIndicatorRule(sma1, closePrice)
                //1993
                //.or(new StopGainRule(closePrice, 500))
                .or(new StopGainRule(closePrice, closePrice.numOf(5)))
               // .or(new StopLossRule(closePrice, 500))
                .or(new StopLossRule(closePrice, closePrice.numOf(5)))
                .or(new TrailingStopLossRule(closePrice, DecimalNum.valueOf(1)))
    );**/
//9480215566
    return new BaseStrategy(
            new UnderIndicatorRule(ema9, closePrice),
            new OverIndicatorRule(ema5, closePrice)

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
    return "SimpleMovingAverage";
  }

}


