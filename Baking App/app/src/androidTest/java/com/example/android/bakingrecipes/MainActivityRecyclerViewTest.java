package com.example.android.bakingrecipes;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.android.bakingrecipes.data.Recipe;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;

/**
 * Created by martonnagy on 2018. 04. 21..
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityRecyclerViewTest {

    /**
     *  Testing the items added to recycler view for name of the item and than if it is possible to
     *  click them. Intents are stubbed so we can test them all in one breath.
     */

    // I had to look up this code for more help on intending:
    // https://github.com/googlesamples/android-testing/blob/master/ui/espresso/IntentsAdvancedSample/app/src/androidTest/java/com/example/android/testing/espresso/intents/AdvancedSample/ImageViewerActivityTest.java

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void stubExternalIntents() {
        intending(IntentMatchers.anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }
    @Test
    public void recylerView_Test() {

        String[] recipes = new String[] {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};

        for (int i = 0; i < recipes.length; i++) {

            String name = recipes[i];

            onView(ViewMatchers.withId(R.id.main_recycler_view))
                    .check(matches(hasDescendant(withText(name))));

            onView(ViewMatchers.withId(R.id.main_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i,
                            click()));
        }
    }
}
