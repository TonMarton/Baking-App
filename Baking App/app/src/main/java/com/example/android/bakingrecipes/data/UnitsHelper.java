package com.example.android.bakingrecipes.data;

import android.util.Log;

/**
 * Created by martonnagy on 2018. 03. 06..
 */

/*This class translates the measurement units into human friendly text*/
public final class UnitsHelper {

    private static String[][] mMeasurements = {
            {"cup", "cups"},
            {"tablespoon", "tablespoons"},
            {"teaspoon","teaspoons"},
            {"kilo", "kilos"},
            {"gram", "grams"},
            {"ounce","ounces"},
            {"", ""},
    };

    private static String[] mMeasurementKeys = {
            "CUP", "TBLSP", "TSP", "K", "G", "OZ", "UNIT"
    };

    public static String getMeasurementName(String name, int quantity) {
        int isPlural = 0;
        if (quantity > 1) {isPlural = 1;}
        for (int i = 0; i < mMeasurementKeys.length; i++) {
            if (name.equals(mMeasurementKeys[i])) {
                return mMeasurements[i][isPlural];
            }
        }
        return "(?)";
    }
}
