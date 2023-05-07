package com.dtech.kitecon.strategy.exec;

import com.dtech.kitecon.data.Instrument;
import com.dtech.kitecon.data.StrategyParameters;
import com.dtech.kitecon.market.orders.OrderManager;
import com.dtech.kitecon.misc.StrategyEnvironment;
import com.dtech.kitecon.repository.InstrumentRepository;
import com.dtech.kitecon.repository.StrategyParametersRepository;
import com.dtech.kitecon.strategy.TradeDirection;
import com.dtech.kitecon.strategy.TradingStrategy;
import com.dtech.kitecon.strategy.builder.StrategyBuilder;
import com.dtech.kitecon.strategy.builder.StrategyConfig;
import com.dtech.kitecon.strategy.dataloader.InstrumentDataLoader;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Trade.TradeType;

@RequiredArgsConstructor
public class ProductionHandler {

  private final InstrumentRepository instrumentRepository;
  private final InstrumentDataLoader instrumentDataLoader;
  private final OrderManager ordermanager;
  private final ProductionSeriesManager productionSeriesManager;

  String[] exchanges = new String[]{"NSE", "NFO"};

  private AlgoTradingRecord record;

  private final Timer executionTimer = new Timer();
  private final StrategyParametersRepository strategyParametersRepository;

  public void initialise(String instrumentName, String direction, String type) {
    Instrument tradingIdentity = instrumentRepository
        .findByTradingsymbolAndExchangeIn(instrumentName, exchanges);
    TradeType orderType = null;
    if (direction.equalsIgnoreCase("Buy")) {
      orderType = TradeType.BUY;
    }
    if (direction.equalsIgnoreCase("Sell")) {
      orderType = TradeType.SELL;
    }
    if(type.equalsIgnoreCase("FNO")) {
      record = new ProductionTradingRecord(orderType, ordermanager,
              tradingIdentity);
    } else {
      record = new ProductionFNOTradingRecord(orderType, ordermanager,
              tradingIdentity);
    }
  }

  public void runStrategy(String instrumentName, StrategyBuilder strategyBuilder,
      String direction, String quantity, String candle) {
    Instrument tradingIdentity = instrumentRepository
        .findByTradingsymbolAndExchangeIn(instrumentName, exchanges);
    Map<Instrument, BarSeries> barSeriesMap = instrumentDataLoader.loadHybridData(tradingIdentity,
            candle);

    StrategyConfig config = getStrategyConfig(instrumentName,
        strategyBuilder, StrategyEnvironment.PROD);

    TradingStrategy strategy = strategyBuilder.build(tradingIdentity, barSeriesMap, config);
    BarSeries barSeries = barSeriesMap.get(tradingIdentity);
    if (barSeries.isEmpty())
      return;
    if (direction.equalsIgnoreCase("Buy")) {
      TradeDirection buy = TradeDirection.Buy;
      TradeType orderType = TradeType.BUY;
      startExecution(tradingIdentity, strategy, barSeries, buy, orderType,quantity);
    }
    if (direction.equalsIgnoreCase("Sell")) {
      TradeDirection buy = TradeDirection.Sell;
      TradeType orderType = TradeType.SELL;
      startExecution(tradingIdentity, strategy, barSeries, buy, orderType, quantity);
    }
  }

  private void startExecution(Instrument tradingIdentity, TradingStrategy strategy,
      BarSeries barSeries, TradeDirection buy, TradeType orderType,  String quantity) {
    Bar bar =  barSeries.getLastBar();
    ZonedDateTime dateTime = bar.getEndTime();
    record.setDateTime(dateTime);
    ProductionStrategyRunner runner = new ProductionStrategyRunner(barSeries, strategy, record,
        productionSeriesManager, buy, dateTime);
    runner.exec(barSeries, strategy, quantity);
  }


  public void startStrategy(String instrumentName, StrategyBuilder strategyBuilder,
      String direction, String quantity, String candle) {
    executionTimer.schedule(getTimerTask(instrumentName, strategyBuilder, direction, quantity, candle), 0, 60 * 1000);
  }

  private TimerTask getTimerTask(String instrumentName, StrategyBuilder strategyBuilder,
      String direction, String quantity, String candle) {
    return new TimerTask() {
      @Override
      public void run() {
        runStrategy(instrumentName, strategyBuilder, direction, quantity, candle);
      }
    };
  }

  public void stopStrategy() {
    executionTimer.cancel();
  }

  public StrategyConfig getStrategyConfig(String instrumentName, StrategyBuilder strategyBuilder,
      StrategyEnvironment strategyEnvironment) {
    List<StrategyParameters> strategyParameters = strategyParametersRepository
        .findByStrategyNameAndInstrumentNameAndEnvironment(strategyBuilder.getName(),
            instrumentName,
            strategyEnvironment);
    StrategyConfig config = StrategyConfig.builder().params(strategyParameters).build();
    return config;
  }

}
