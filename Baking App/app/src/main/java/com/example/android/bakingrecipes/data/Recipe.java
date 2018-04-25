package com.example.android.bakingrecipes.data;

/**
 * Created by martonnagy on 2018. 03. 06..
 */

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/*
*  Data structure for the recipes parsed from the request via the RecipeTaskLoader
*  Both Recipe and its inner private classes, Ingredient and Step, implement the Parcelable interface.
*  This helps in keeping the activities' code clean and eliminates the need of writing the into intent bundles
*  every variable one by one. Unluckily the this approach to reconstructing custom objects requires loads
*  of boiler-plate code.
*/

public class Recipe implements Parcelable {

    int mId;
    String mName;
    int mServings;
    String mImageUrl;
    List<Ingredient> mIngredients = new ArrayList<>();
    List<Step> mSteps = new ArrayList<>();

    // default constructor
    public Recipe(int id, String name, int servings, String imageUrl) {

        mId = id;
        mName = name;
        mServings = servings;
        mImageUrl = imageUrl;
    }

    // primitive type getters
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getServings() {
        return mServings;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    // methods for constructing subclass instances
    public void addIngredient(int quantity, String measure, String ingredient) {
        mIngredients.add( new Ingredient(quantity, measure,ingredient));
    }

    public void addStep(int id, String shortDes, String des, String videoUrl, String imageUrl) {
        mSteps.add(new Step(id, shortDes, des, videoUrl, imageUrl));
    }

    // methods providing access to the underlying subclasses
    public int getIngredientCount() {
        return mIngredients.size();
    }

    public int getStepsCount() {
        return mSteps.size();
    }

    public String[][] getIngredientsData() {
        String[][] ingredientsData = new String[mIngredients.size()][3];
        for (int i = 0; i < mIngredients.size(); i++) {
            Ingredient obj = mIngredients.get(i);
            String[] array = {
                    Integer.toString(obj.mQuantity),
                    obj.mMeasure,
                    obj.mIngredient
            };
            ingredientsData[i] = array;
        }
        return ingredientsData;
    }

    public String[] getStepData(int index) {
        if (mSteps.size() <= index ) {return null;}
        Step obj = mSteps.get(index);
        String[] array = {
                obj.mShortDescription,
                obj.mDescription,
                obj.mVideoUrl,
                obj.mThumbNailUrl
        };
        return array;
    }

    // parcel constructor
    public Recipe(Parcel in){
        mId = in.readInt();
        mName = in.readString();
        mServings = in.readInt();
        mImageUrl = in.readString();
        mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        mSteps = in.createTypedArrayList(Step.CREATOR);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    // acquired some know how from: https://stackoverflow.com/questions/10071502/read-writing-arrays-of-parcelable-objects
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mServings);
        dest.writeString(mImageUrl);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mSteps);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // inner private classes
    private static class Ingredient implements Parcelable {
        int mQuantity;
        String mMeasure;
        String mIngredient;

        private Ingredient(int quantity, String measure, String ingredient ) {
            mQuantity = quantity;
            mMeasure = UnitsHelper.getMeasurementName(measure,quantity);
            mIngredient = ingredient;
        }

        protected Ingredient(Parcel in) {
            mQuantity = in.readInt();
            mMeasure = in.readString();
            mIngredient = in.readString();
        }

        public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            @Override
            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mQuantity);
            parcel.writeString(mMeasure);
            parcel.writeString(mIngredient);
        }
    }

    private static class Step implements Parcelable{
        int mId;
        String mShortDescription;
        String mDescription;
        String mVideoUrl;
        String mThumbNailUrl;

        private Step(int id, String shortDes, String des, String videoUrl, String imageUrl) {
            mId = id;
            mShortDescription = shortDes;
            mDescription = des;
            mVideoUrl = videoUrl;
            mThumbNailUrl = imageUrl;
        }

        protected Step(Parcel in) {
            mId = in.readInt();
            mShortDescription = in.readString();
            mDescription = in.readString();
            mVideoUrl = in.readString();
            mThumbNailUrl = in.readString();
        }

        public static final Creator<Step> CREATOR = new Creator<Step>() {
            @Override
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            @Override
            public Step[] newArray(int size) {
                return new Step[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mId);
            parcel.writeString(mShortDescription);
            parcel.writeString(mDescription);
            parcel.writeString(mVideoUrl);
            parcel.writeString(mThumbNailUrl);
        }
    }
}