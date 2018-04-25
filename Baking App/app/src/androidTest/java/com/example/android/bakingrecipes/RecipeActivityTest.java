package com.example.android.bakingrecipes;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
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
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by martonnagy on 2018. 04. 23..
 */


/**
 * This test needs the recipe item, so it is started from the MainActivity.*/

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void checkThroughSteps() throws InterruptedException {

        onView(ViewMatchers.withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));


        for (int i = 0; i < 8; i++) {
            /** I need to put the thread to sleep for a little while, because of the custom
             * animation I have implemented for fragment swapping.
             * I know that this would be considered bad practice, but in this case I don't think so,
             * since I just need to wait for the view to be clickable again until it is in focus.
             * So this is perfect for my purposes here.
             */
            Thread.sleep(100);

            onView(ViewMatchers.withId(R.id.recipe_recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            onView(ViewMatchers.withId(R.id.recipe_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i,
                            click()));

            if (i == 2) {
                Espresso.onView(ViewMatchers.withId(R.id.short_description_detail_tv)).check(matches(ViewMatchers.withText("Starting prep")));
            } else if (i == 6) {
                Espresso.onView(ViewMatchers.withId(R.id.short_description_detail_tv)).check(matches(ViewMatchers.withText("Finish filling prep")));
            }

            Espresso.pressBack();
        }

    }
}
