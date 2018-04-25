package com.example.android.bakingrecipes.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.android.bakingrecipes.R;

/**
 * Created by martonnagy on 2018. 03. 13..
 */
// included some of this code about how to implement more than one types of viewholders into one adapter:
// https://stackoverflow.com/questions/28110033/recyclerview-with-different-cardlayouts

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private static final int INGREDIENT_CARD = 0;
    private static final int STEP_CARD = 1;

    Context mContext;
    Recipe mRecipe;

    OnRecipeClickAdapterItemClicked mItemClickListener;

    public interface OnRecipeClickAdapterItemClicked {
        void onCardClicked(int position);
    }

    public RecipeDetailAdapter(Context context, Recipe recipe, OnRecipeClickAdapterItemClicked listener) {
        mContext = context;
        mRecipe = recipe;
        mItemClickListener = listener;
    }

    public class IngredientViewHolder extends ViewHolder {
        public IngredientViewHolder(LinearLayout itemView) {
            super(itemView);
        }
    }

    public class StepViewHolder extends ViewHolder {

        public StepViewHolder(LinearLayout itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mItemLayout;

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            mItemLayout = itemView;
        }
    }

    @Override
    public RecipeDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == INGREDIENT_CARD) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_item, parent, false);
            IngredientViewHolder vh = new IngredientViewHolder(v);
            return vh;
        } else if(viewType == STEP_CARD) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_item, parent, false);
            StepViewHolder vh = new StepViewHolder(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        switch (position) {
            case 0:
                break;
            default:
                final int stepIndex = position - 1;

                TextView descTextView = (TextView) holder.mItemLayout.getChildAt(0);
                String[] stepData = mRecipe.getStepData(stepIndex);
                descTextView.setText(stepData[0]);

                RelativeLayout rl = (RelativeLayout) holder.mItemLayout.getChildAt(1);
                TextView indexTextView = (TextView) rl.getChildAt(0);
                indexTextView.setText(Integer.toString(stepIndex + 1));
                break;
        }
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onCardClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // number of ingredient cards + number of step cards
        int ingredient;
        if (mRecipe.getIngredientCount() > 0) {
            ingredient = 1;
        } else {
            ingredient = 0;
        }
        return ingredient + mRecipe.getStepsCount();
    }

    @Override
    public int getItemViewType (int position) {
        switch (position) {
            case 0:
                return INGREDIENT_CARD;
            default:
                return STEP_CARD;
        }
    }
}
