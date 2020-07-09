package com.example.signal;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnTicks;

public class MykiteTicker extends KiteTicker {
    private String[] InstrumentArray;
    private float[] TargetArray;
    private float[] StopLossArray;

    public MykiteTicker(String accessToken, String apiKey) {
        super(accessToken, apiKey);
    }

    @Override
    public void setOnTickerArrivalListener(OnTicks onTickerArrivalListener) {
        //TODO TICK PROCESSING
        super.setOnTickerArrivalListener(onTickerArrivalListener);
    }

    public void setInstrumentArray(String[] instrument){
        this.InstrumentArray = instrument;
    }

    public void setTargetArray(float[] targetArray){
        this.TargetArray = targetArray;
    }

    public void getStopLossArray(float[] stopLossArray) {
        this.StopLossArray = stopLossArray;
    }
}

