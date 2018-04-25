package com.example.android.bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.example.android.bakingrecipes.data.IngredientWidgetService;
import com.example.android.bakingrecipes.data.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static Recipe mRecipe;
    AppWidgetManager mAppWidgetManager;
    int[] mAppWidgetIds;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        CharSequence widgetText = context.getString(R.string.widget_title_extra_text) +
                " " +  mRecipe.getName();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);


        //reformatting the ingredients data
        String[][] ingredientsData = recipe.getIngredientsData();
        String[] array = new String[ingredientsData.length];
        for (int i = 0; ingredientsData.length > i; i++) {
            String quantity = ingredientsData[i][0];
            String measure = ingredientsData[i][1];
            String ingredient = ingredientsData[i][2];

            String ingredientText = quantity + " " + measure + " " + ingredient;
            array[i] = ingredientText;
        }

        /**
         * I found out how to define such a service, how to start it and create the classes for it to work at:
         * https://laaptu.wordpress.com/2013/07/19/android-app-widget-with-listview/ */
        //setting an adapter for the ListView through a service
        Intent svcIntent = new Intent(context, IngredientWidgetService.class);
        svcIntent.putExtra(context.getString(R.string.intent_extra_tag_string), array);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(appWidgetId, R.id.container_list_view,
                svcIntent);

        //onClick listener on the whole widget screen
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(context.getResources().getString(R.string.intent_extra_tag), recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_container_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            if (mRecipe == null) {
                displayHelpMessage(context, appWidgetManager, appWidgetId);
            } else {
                return;
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (mAppWidgetManager == null || mAppWidgetIds == null) {return;}
        Recipe recipe = intent.getParcelableExtra(context.getString(R.string.intent_extra_tag));
        if (recipe != null) {
            mRecipe = recipe;
            for (int appWigetId : mAppWidgetIds) {
                updateAppWidget(context, mAppWidgetManager, appWigetId, mRecipe);
            }
        }
    }

    private void displayHelpMessage(Context context, AppWidgetManager am, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_help_layout);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.layout_top_wrapper, pendingIntent);

        am.updateAppWidget(appWidgetId, views);
    }
}

