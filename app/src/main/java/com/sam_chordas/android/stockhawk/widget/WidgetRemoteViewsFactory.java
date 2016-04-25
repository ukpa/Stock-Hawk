package com.sam_chordas.android.stockhawk.widget;

/**
 * Created by unnikrishnanpatel on 25/04/16.
 */
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<Stocks> mStocks;

    public WidgetRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
        this.mStocks = new ArrayList<>();
    }

    private void getStockData() {
        mStocks.clear();

        String[] requiredColumns = new String[]
                {QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.CHANGE, QuoteColumns.ISUP};
        Cursor stockCursor = mContext.getContentResolver()
                .query(QuoteProvider.Quotes.CONTENT_URI, requiredColumns,
                        QuoteColumns.ISCURRENT + " = ?", new String[]{"1"}, null);

        if (stockCursor != null) {
            stockCursor.moveToFirst();
            do {
                Stocks stock = new Stocks(stockCursor.getString(0), stockCursor.getString(1), stockCursor.getString(2), stockCursor.getInt(3));
                mStocks.add(stock);
            } while (stockCursor.moveToNext());

            stockCursor.close();
        }
    }

    @Override
    public void onCreate() {
        getStockData();
    }

    @Override
    public void onDataSetChanged() {
        getStockData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mStocks.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Stocks stock = mStocks.get(position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_layout);
        remoteViews.setTextViewText(R.id.stock_symbol, stock.getStockSymbol());
        remoteViews.setTextViewText(R.id.bid_price, stock.getBidPrice());
        remoteViews.setTextViewText(R.id.change, stock.getPercentChange());



        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}