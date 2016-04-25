package com.sam_chordas.android.stockhawk.widget;

/**
 * Created by unnikrishnanpatel on 25/04/16.
 */
import android.content.Intent;
import android.widget.RemoteViewsService;
public class WidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }
}