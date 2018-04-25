package com.example.android.bakingrecipes.data;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingrecipes.IngredientsWidgetProvider;
import com.example.android.bakingrecipes.R;
import com.example.android.bakingrecipes.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martonnagy on 2018. 03. 10..
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    List<Recipe> mRecipeList = new ArrayList<>();
    Context mContext;

    public RecipeAdapter(List<Recipe> recipes, Context context) {
        mRecipeList = recipes;
        mContext = context;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, final int position) {
        final Recipe recipe = mRecipeList.get(position);
        RelativeLayout imgFrame = (RelativeLayout) holder.mItemLayout.getChildAt(0);
        final ImageView img = (ImageView) imgFrame.getChildAt(0);

        // even though there are no pictures supplied in the api, this would work in case...
        String imageUrl = recipe.getImageUrl();
        if (!imageUrl.isEmpty()) {
            /* For faulty or inaccurate urls
            * In case the loader fails, even after null check, the default image will get loaded*/
            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    Log.e("Picasso load failed", ", default image used instead");
                    assignDrawableToImg(position, img);
                }
            });
            builder.build().load(imageUrl).into(img);
        } else {
            assignDrawableToImg(position, img);
        }

        LinearLayout ll = (LinearLayout) holder.mItemLayout.getChildAt(1);
        ll = (LinearLayout) ll.getChildAt(0);
        TextView nameTextView = (TextView) ll.getChildAt(0);
        nameTextView.setText(recipe.getName());

        ll = (LinearLayout) ll.getChildAt(1);
        TextView servingsTextView = (TextView) ll.getChildAt(1);
        servingsTextView.setText(Integer.toString(recipe.getServings()));

        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // the following chunk of code I got and than modified from: https://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver
                Intent updateIntent = new Intent(mContext, IngredientsWidgetProvider.class);
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(mContext)
                        .getAppWidgetIds((new ComponentName(mContext, IngredientsWidgetProvider.class)));
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                updateIntent.putExtra(mContext.getString(R.string.intent_extra_tag),recipe);
                mContext.sendBroadcast(updateIntent);

                Intent intent = new Intent(mContext, RecipeActivity.class);
                intent.putExtra(
                        mContext.getResources().getString(R.string.intent_extra_tag), recipe );
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mItemLayout;
        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            mItemLayout = itemView;
        }
    }

    private void assignDrawableToImg(int position, ImageView img) {
        int remainder = position % 4;
        switch (remainder) {
            case 0:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default0));
                break;
            case 1:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default1));
                break;
            case 2:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default2));
                break;
            case 3:
                img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default3));
                break;
        }
    }
}