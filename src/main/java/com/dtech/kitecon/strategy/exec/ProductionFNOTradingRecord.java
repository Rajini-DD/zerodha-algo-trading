package com.dtech.kitecon.strategy.exec;

import com.dtech.kitecon.data.Instrument;
import com.dtech.kitecon.market.orders.OrderManager;
import com.dtech.kitecon.options.OptionInstrumentType;
import com.dtech.kitecon.service.DataFetchService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.ta4j.core.Trade;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

public class ProductionFNOTradingRecord extends ProductionTradingRecord{
    @Autowired
    private DataFetchService dataFetchService;

    public ProductionFNOTradingRecord(Trade.TradeType orderType, OrderManager ordermanager, Instrument instrument) {
        super(orderType, ordermanager, instrument);
    }
    @Override
    public Instrument getInstrument(Double price) throws IOException, KiteException {
        List<Instrument> instrumentList = null ;
        Instrument inst = super.getInstrument(price);
        if(inst.equals(OptionInstrumentType.NIFTY.toString())){
            instrumentList = dataFetchService.downloadOptionInstruments(OptionInstrumentType.NIFTY.getInstrumentConstructed(dateTime, price));
        } else if (inst.equals(OptionInstrumentType.BANK_NIFTY.toString())){
            instrumentList = dataFetchService.downloadOptionInstruments(OptionInstrumentType.BANK_NIFTY.getInstrumentConstructed(dateTime, price));
        } else if (inst.equals(OptionInstrumentType.FIN_NIFTY.toString())){
            instrumentList = dataFetchService.downloadOptionInstruments(OptionInstrumentType.FIN_NIFTY.getInstrumentConstructed(dateTime, price));
        }
        return instrumentList.stream().findFirst().get();
    }

    @Override
    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }
}