package com.example.android.bakingrecipes;

import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.android.bakingrecipes.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements
        MasterListFragment.OnRecipeClickListener, StepDetailFragment.OnDataChangeListener {

    private static int FRAGMENT_DISPLAYED = 0;
    private final int LIST_FRAGMENT_DISPLAYED = 0;
    private final int STEPDETAIL_FRAGMENT_DISPLAYED = 1;

    private final int FLY_OUT_LIST = 0;
    private final int FLY_IN_LIST = 1;

    private final String CURRENT_POSITION_TAG = "current_position";
    private static String FRAGMENT_ID_TAG = "fragment_id";
    private final String RECIPE_TAG = "recipe";
    private final String PLAY_WHEN_READY_TAG = "play_when_ready";
    private final String EXOPLAYER_CURRENT_POSITION = "video_position";

    private double mListFragmentScreenProportion;
    private double mStepDetailFragmentProportion;
    private float mScreenWidth;

    private long mVideoPosition;
    private boolean mSetPlay;

    Recipe mRecipe;
    StepDetailFragment mStepDetailFragment;
    ViewTreeObserver mVto;

    Bundle mState;

    @BindView(R.id.list_fragment_container) FrameLayout mListFragmentContainer;
    @BindView(R.id.step_detail_fragment_container) FrameLayout mStepDetailFragmentContainer;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mRecipe = getIntent().getParcelableExtra(getResources().getString(R.string.intent_extra_tag));
        setTitle(mRecipe.getName());


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.list_fragment_container, new MasterListFragment())
                    .add(R.id.step_detail_fragment_container, new StepDetailFragment())
                    .commit();
        } else {
            mState = savedInstanceState;
                long postion = savedInstanceState.getLong(EXOPLAYER_CURRENT_POSITION);
                if (postion != 0) {
                    mVideoPosition = postion;
                }
                boolean setPLay = savedInstanceState.getBoolean(PLAY_WHEN_READY_TAG);
                if (setPLay) {
                    mSetPlay = true;
                }
        }

        int orientation = getResources().getConfiguration().orientation;
        if (getResources().getBoolean(R.bool.isTablet)) {
            // tablet
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                mListFragmentScreenProportion = 0.5;
                mStepDetailFragmentProportion = 1;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mListFragmentScreenProportion = 0.3;
                mStepDetailFragmentProportion = 0.7;
            }
        } else {
            // phone
            mListFragmentScreenProportion = 1;
            mStepDetailFragmentProportion = 1;
        }

        mVto = mStepDetailFragmentContainer.getViewTreeObserver();
        mVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScreenWidth = mListFragmentContainer.getWidth();
                float newListWidth =
                        mListFragmentContainer.getWidth() * (float) mListFragmentScreenProportion;
                float newStepWidth =
                        mStepDetailFragmentContainer.getWidth() * (float) mStepDetailFragmentProportion;

                // setting the new dimensions of the fragments - according to the orientation and device type
                mListFragmentContainer.setLayoutParams(new RelativeLayout.LayoutParams(
                        (int) newListWidth, mListFragmentContainer.getHeight()));
                mStepDetailFragmentContainer.setLayoutParams(new RelativeLayout.LayoutParams(
                        (int) newStepWidth, mStepDetailFragmentContainer.getHeight()));

                // in case of recreating is needed because of rotation - exclude landscape tablet
                if (mState != null && mStepDetailFragmentProportion + mListFragmentScreenProportion != 1) {
                    FRAGMENT_DISPLAYED = mState.getInt(FRAGMENT_ID_TAG);

                    switch (FRAGMENT_DISPLAYED) {
                        case LIST_FRAGMENT_DISPLAYED:
                            // listFragment is in place by default
                            break;
                        case STEPDETAIL_FRAGMENT_DISPLAYED:
                            // move the listFragment out of the way
                            mStepDetailFragmentContainer.setVisibility(View.VISIBLE);
                            mListFragmentContainer.setX(-newListWidth);
                            mStepDetailFragmentContainer.setX(0);
                            mStepDetailFragment.processData(mState.getInt(CURRENT_POSITION_TAG));
                            break;
                    }
                } else if (mState != null && mStepDetailFragmentProportion + mListFragmentScreenProportion == 1) {
                    mStepDetailFragmentContainer.setX(newListWidth);
                    mStepDetailFragment.processData(mState.getInt(CURRENT_POSITION_TAG));
                } else if (mState == null && mStepDetailFragmentProportion + mListFragmentScreenProportion == 1){
                    // if it is tablet - landscape and not rotation
                    mStepDetailFragmentContainer.setX(newListWidth);
                    mStepDetailFragment.processData(0);
                } else if (mState == null && mListFragmentScreenProportion + mStepDetailFragmentProportion <
                        2 && mListFragmentScreenProportion + mStepDetailFragmentProportion > 1) {
                    // tablet portrait and not after rotation
                    mStepDetailFragment.processData(0);
                }else {
                    // when it is not restored after rotation and not tablet
                    mStepDetailFragmentContainer.setX(newStepWidth);
                }


                // only needed this for the first time it gets called - so it gets removed
                mStepDetailFragmentContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                removeViewTreeObserver();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(FRAGMENT_ID_TAG, FRAGMENT_DISPLAYED);
        outState.putInt(CURRENT_POSITION_TAG, mStepDetailFragment.currentPositionDisplayed);
        outState.putParcelable(RECIPE_TAG, mRecipe);
        if (mStepDetailFragment.mExoPlayer != null) {
            outState.putBoolean(PLAY_WHEN_READY_TAG, mStepDetailFragment.mExoPlayer.getPlayWhenReady());
            outState.putLong(EXOPLAYER_CURRENT_POSITION, mStepDetailFragment.mExoPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private void removeViewTreeObserver() {
        mVto = null;
    }

    /* The following function tweak the behaviour of the up navigation,
        to make it seem that the fragments getting swapped is just like opening a new intent */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (FRAGMENT_DISPLAYED == STEPDETAIL_FRAGMENT_DISPLAYED) {
                    FRAGMENT_DISPLAYED = LIST_FRAGMENT_DISPLAYED;
                    mStepDetailFragment.stopPlayer();
                    animateFragmentSwap(FLY_IN_LIST);
                } else if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // how to deal with deep links and their effect on the backstack I learned from:
                    // https://developer.android.com/training/implementing-navigation/ancestral.html
                        TaskStackBuilder.create(this)
                                .addNextIntentWithParentStack(upIntent)
                                .startActivities();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* overriding the Back button functionality of the device in order to switch back to the
     MasterListFragment instead of jumping back to MainActivity or navigating outside of the app
     - since there is no way to access the detail fragment from outside the app,
      that case is ignored. */
    @Override
    public void onBackPressed()
    {
       switch (FRAGMENT_DISPLAYED) {
           case STEPDETAIL_FRAGMENT_DISPLAYED:
               FRAGMENT_DISPLAYED = LIST_FRAGMENT_DISPLAYED;
               animateFragmentSwap(FLY_IN_LIST);
               mStepDetailFragment.stopPlayer();
               break;
           case LIST_FRAGMENT_DISPLAYED:
               super.onBackPressed();
               break;
       }
    }

    @Override
    protected void onPause() {
        if (mStepDetailFragment.mExoPlayer != null) {
            mStepDetailFragment.mExoPlayer.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mStepDetailFragment.mExoPlayer != null) {
            mStepDetailFragment.initializePLayer();
            mStepDetailFragment.processData(mStepDetailFragment.currentPositionDisplayed);
        }
    }

    //Interface methods
    @Override
    public void onItemClicked(int position) {
        mStepDetailFragment.processData(position);
        animateFragmentSwap(FLY_OUT_LIST);
        FRAGMENT_DISPLAYED = STEPDETAIL_FRAGMENT_DISPLAYED;
    }

    @Override
    public Recipe getDataToDisplay() {
        return mRecipe;
    }

    @Override
    public String[][] getIngredients() {
        return mRecipe.getIngredientsData();
    }

    @Override
    public String[] getStepData(int position) {
        return mRecipe.getStepData(position-1);
    }

    @Override
    public void returnSelf(StepDetailFragment fragment) {
        mStepDetailFragment = fragment;
    }

    @Override
    public long getVideoPosition() {
        return mVideoPosition;
    }

    @Override
    public boolean setPLay() {
        return mSetPlay;
    }

    private void animateFragmentSwap(int direction) {

        if (mListFragmentScreenProportion + mStepDetailFragmentProportion == 1) {
            // tablet - landscape, they don't need animation, both present completely all the time
            return;
        }

        float listX = 0;
        float stepDetailX = 0;

        if (mListFragmentScreenProportion + mStepDetailFragmentProportion == 2) {
            /* Phones: both frags take full screen (1), so they should swap, if we open the step
            the list goes to minus the width of the screen and the step goes to 0... */
            switch (direction) {
                case FLY_OUT_LIST:
                    listX = -mScreenWidth;
                    stepDetailX = 0;
                    break;
                case FLY_IN_LIST:
                    listX = 0;
                    stepDetailX = mScreenWidth;
                    break;
            }
        } else if (mListFragmentScreenProportion + mStepDetailFragmentProportion <
                2 && mListFragmentScreenProportion + mStepDetailFragmentProportion > 1) {
            /* tablet - portrait - only need to move the list, in and out by its width... */
            switch (direction) {
                case FLY_OUT_LIST:
                    listX = -mScreenWidth * (float) mListFragmentScreenProportion;
                    stepDetailX = 0;
                    break;
                case FLY_IN_LIST:
                    listX = 0;
                    stepDetailX = 0;
                    break;
            }
        }

        ObjectAnimator animList = ObjectAnimator.ofFloat(mListFragmentContainer,
                "translationX", listX);
        ObjectAnimator animStep = ObjectAnimator.ofFloat(mStepDetailFragmentContainer,
                "translationX", stepDetailX);
        animList.setDuration(400);
        animStep.setDuration(400);
        animList.start();
        animStep.start();
    }
}
