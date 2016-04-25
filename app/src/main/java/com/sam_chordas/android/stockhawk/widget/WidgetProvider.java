package com.sam_chordas.android.stockhawk.widget;
/**
 * Created by unnikrishnanpatel on 25/04/16.
 */
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int widgetId : appWidgetIds) {
            Intent remoteViewServiceIntent = new Intent(context, WidgetRemoteViewsService.class);
            remoteViewServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            remoteViewServiceIntent.setData(Uri.parse(remoteViewServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main_layout);
            remoteViews.setRemoteAdapter(widgetId, R.id.recycler_view_widget_layout, remoteViewServiceIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}