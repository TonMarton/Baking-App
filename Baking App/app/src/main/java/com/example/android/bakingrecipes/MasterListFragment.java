package com.example.android.bakingrecipes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.bakingrecipes.data.Recipe;
import com.example.android.bakingrecipes.data.RecipeDetailAdapter;

/**
 * Created by martonnagy on 2018. 03. 13..
 */

public class MasterListFragment extends Fragment implements RecipeDetailAdapter.OnRecipeClickAdapterItemClicked{

    OnRecipeClickListener mCallback;

    @Override
    public void onCardClicked(int position) {
        mCallback.onItemClicked(position);
    }

    public interface OnRecipeClickListener {
        void onItemClicked(int position);
        Recipe getDataToDisplay();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClikListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.list_fragment_layout, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecipeDetailAdapter(getContext(), mCallback.getDataToDisplay(), this));
        return recyclerView;
    }
}
