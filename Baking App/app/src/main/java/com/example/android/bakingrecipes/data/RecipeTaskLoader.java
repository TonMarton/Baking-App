package com.example.android.bakingrecipes.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.example.android.bakingrecipes.utils.Networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martonnagy on 2018. 03. 08..
 */

public final class RecipeTaskLoader extends AsyncTaskLoader<List<Recipe>> {

    public RecipeTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Recipe> loadInBackground() {

        List<Recipe> recipeList = new ArrayList<>();

        try {
            String data = Networking.getResponseFromHttpUrl(Networking.buildUrl());
            recipeList = parseJsonData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    private List<Recipe> parseJsonData(String data) {
        List<Recipe> recipeList = new ArrayList<>();

        try {
            JSONArray recipes = new JSONArray(data);

            for (int i = 0; i < recipes.length(); i++) {

                JSONObject recipeData = recipes.getJSONObject(i);
                int recipeId = recipeData.getInt("id");
                String recipeName = recipeData.getString("name");
                int servings = recipeData.getInt("servings");
                String image = recipeData.getString("image");

                Recipe recipe = new Recipe(recipeId, recipeName, servings, image);

                JSONArray ingredients = recipeData.getJSONArray("ingredients");
                for (int k = 0; k < ingredients.length(); k++) {
                    JSONObject ingredient = ingredients.getJSONObject(k);
                    int quantity = ingredient.getInt("quantity");
                    String measure = ingredient.getString("measure");
                    String ingredientText = ingredient.getString("ingredient");

                    recipe.addIngredient(quantity, measure, ingredientText);
                }

                JSONArray steps = recipeData.getJSONArray("steps");
                for (int k = 0; k < steps.length(); k++) {
                    JSONObject step = steps.getJSONObject(k);
                    int id = step.getInt("id");
                    String shortDescription = step.getString("shortDescription");
                    String description = step.getString("description");
                    String videoUrl = step.getString("videoURL");
                    String thumbnailUrl = step.getString("thumbnailURL");

                    recipe.addStep(id, shortDescription, description, videoUrl, thumbnailUrl);
                }
                recipeList.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }
}
