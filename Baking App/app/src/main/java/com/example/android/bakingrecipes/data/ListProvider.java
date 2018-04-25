package com.example.android.bakingrecipes.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingrecipes.R;
import com.example.android.bakingrecipes.RecipeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martonnagy on 2018. 04. 16..
 */

/**
I have created this class by the guide: https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/,
 I have copied over than modified this class accordingly.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory{

    private String[] mIngredients;
    private Context mContext = null;


    public ListProvider(Context context, String[] strings) {
        mContext = context;
        mIngredients = strings;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_ingredient_item);
        String text = mIngredients[i];
        remoteView.setTextViewText(R.id.ingredient_text_view, text);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
