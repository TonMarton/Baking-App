package com.example.android.bakingrecipes;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingrecipes.data.Recipe;
import com.example.android.bakingrecipes.data.RecipeAdapter;
import com.example.android.bakingrecipes.data.RecipeTaskLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    int LOADER_ID = 0;
    private final String RECIPE_ARRAY_TAG = "recipe_array";

    Recipe[] mRecipeList;

    @BindView(R.id.main_recycler_view) RecyclerView mRecyclerView;
    RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        int orientation = getResources().getConfiguration().orientation;
        if (getResources().getBoolean(R.bool.isTablet)) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                RecyclerView.LayoutManager lm = new GridLayoutManager(this, 2);
                mRecyclerView.setLayoutManager(lm);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                RecyclerView.LayoutManager lm = new GridLayoutManager(this, 3);
                mRecyclerView.setLayoutManager(lm);
            }
        } else {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                RecyclerView.LayoutManager lm = new GridLayoutManager(this, 1);
                mRecyclerView.setLayoutManager(lm);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                RecyclerView.LayoutManager lm = new GridLayoutManager(this, 2);
                mRecyclerView.setLayoutManager(lm);
            }
        }

        if (savedInstanceState == null) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            mRecipeList = (Recipe[]) savedInstanceState.getParcelableArray(RECIPE_ARRAY_TAG);
            List<Recipe> data = Arrays.asList(mRecipeList);
            initAdapter(data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(RECIPE_ARRAY_TAG ,mRecipeList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //start the loading animation here, since it can't be in the onCreate - doesn't stop for some reason
        /*
        if (mAdapter == null) {
            mLoadingAnimation = (AnimationDrawable) mLoadingImage.getDrawable();
            mLoadingAnimation.start();
        }
        */
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        RecipeTaskLoader loader = new RecipeTaskLoader(this);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        initAdapter(data);
        mRecipeList = data.toArray(new Recipe[data.size()]);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    private void initAdapter(List<Recipe> data) {
        mAdapter = new RecipeAdapter(data, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
