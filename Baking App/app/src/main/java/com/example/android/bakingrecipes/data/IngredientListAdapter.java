package com.example.android.bakingrecipes.data;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.example.android.bakingrecipes.R;

import org.w3c.dom.Text;

/**
 * Created by martonnagy on 2018. 04. 17..
 */

public class IngredientListAdapter implements ListAdapter {

    private Context mContext;
    private String[] mIngredients;

    public IngredientListAdapter(Context context, String[] ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public int getCount() {
        return mIngredients.length;
    }

    @Override
    public Object getItem(int i) {
        return mIngredients[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.ingredient_detail_list_item, viewGroup, false);
        TextView textView = (TextView) linearLayout.getChildAt(0);
        textView.setText(mIngredients[i]);
        return linearLayout;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        if (mIngredients.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}
