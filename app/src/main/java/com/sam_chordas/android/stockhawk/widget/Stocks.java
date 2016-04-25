package com.sam_chordas.android.stockhawk.widget;

/**
 * Created by unnikrishnanpatel on 25/04/16.
 */
public class Stocks {
    private String mStockSymbol;
    private String mBidPrice;
    private String mPercentChange;
    private int mIsUp;

    public Stocks(String stockSymbol, String bidPrice, String percentChange, int isUp) {
        mStockSymbol = stockSymbol;
        mBidPrice = bidPrice;
        mPercentChange = percentChange;
        mIsUp = isUp;
    }

    public String getStockSymbol() {
        return mStockSymbol;
    }

    public String getBidPrice() {
        return mBidPrice;
    }

    public String getPercentChange() {
        return mPercentChange;
    }

}