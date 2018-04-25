package com.example.android.bakingrecipes.data;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.bakingrecipes.R;

/**
 * Created by martonnagy on 2018. 04. 16..
 */

public class IngredientWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String[] strings = intent.getStringArrayExtra(this.getString(R.string.intent_extra_tag_string));
        return (new ListProvider(this.getApplicationContext(), strings));
    }

}