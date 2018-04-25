package com.example.android.bakingrecipes;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.android.bakingrecipes.data.IngredientListAdapter;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by martonnagy on 2018. 04. 03..
 */
public class StepDetailFragment extends Fragment {

    OnDataChangeListener mDataChangeListener;

    private RelativeLayout mTopLayout;
    private LinearLayout mIngredientLayout;
    private ScrollView mStepLayout;
    private PlayerView mPlayerView;
    public SimpleExoPlayer mExoPlayer;

    public int currentPositionDisplayed = 0;

    public interface OnDataChangeListener {
        String[][] getIngredients();
        String[] getStepData(int position);
        void returnSelf(StepDetailFragment fragment);
        long getVideoPosition();
        boolean setPLay();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mDataChangeListener = (OnDataChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataChangeListener");
        }
        mDataChangeListener.returnSelf(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTopLayout = (RelativeLayout) inflater.inflate(R.layout.step_detail_layout, container, false);
        mIngredientLayout = mTopLayout.findViewById(R.id.ingredients_detail_container_layout);
        mStepLayout = mTopLayout.findViewById(R.id.step_detail_container_layout);
        mPlayerView = mTopLayout.findViewById(R.id.media_player_view);
        return mTopLayout;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePLayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void processData(int position) {
        currentPositionDisplayed = position;
        mTopLayout.setVisibility(View.VISIBLE);
        if (position == 0) {
            mStepLayout.setVisibility(View.INVISIBLE);
            mIngredientLayout.setVisibility(View.VISIBLE);

            String[][] ingredientsData = mDataChangeListener.getIngredients();
            String[] array = new String[ingredientsData.length];
            for (int i = 0; ingredientsData.length > i; i++) {
                String quantity = ingredientsData[i][0];
                String measure = ingredientsData[i][1];
                String ingredient = ingredientsData[i][2];

                String ingredientText = quantity + " " + measure + " " + ingredient;
                array[i] = ingredientText;
            }

            // set up ui for displaying the ingredients
            ListView listView = mIngredientLayout.findViewById(R.id.ingredient_detail_list_view);

            // assign adapter
            listView.setAdapter(new IngredientListAdapter(getContext(), array));
        } else {
            mStepLayout.setVisibility(View.VISIBLE);
            mIngredientLayout.setVisibility(View.INVISIBLE);

            String[] stepData = mDataChangeListener.getStepData(position);

            TextView numberTextView = mTopLayout.findViewById(R.id.number_tv);
            TextView shortDescTextView = mTopLayout.findViewById(R.id.short_description_detail_tv);
            TextView descriptionTextView = mTopLayout.findViewById(R.id.description_tv);

            numberTextView.setText(Integer.toString(position));
            shortDescTextView.setText(stepData[0]);
            descriptionTextView.setText(stepData[1]);

            if (mExoPlayer == null) {
                initializePLayer();
            }
            mPlayerView.setVisibility(View.VISIBLE);
            String videoUrl = stepData[2];
            if (videoUrl.isEmpty()) {
                if (!stepData[3].isEmpty()) {
                    videoUrl = stepData[3];
                } else {
                    // disable video part of the ui, stop the player etc and hide it
                    mPlayerView.setVisibility(View.INVISIBLE);
                    mExoPlayer.stop();

                    return;
                }
            }
            String thumbnailUr = stepData[3];
            if (!thumbnailUr.isEmpty()) {
                ImageView imageView = mTopLayout.findViewById(R.id.the_useless_image_view);
                Picasso.get().load(thumbnailUr).into(imageView);
            }
            String userAgent = Util.getUserAgent(getContext(), "Baking Recipes" );
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mDataChangeListener.setPLay());
            mExoPlayer.seekTo(mDataChangeListener.getVideoPosition());
            mPlayerView.hideController();
        }
    }

    public void initializePLayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        mPlayerView.setPlayer(mExoPlayer);
    }

    private void releasePLayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void stopPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mPlayerView.showController();
        }
    }
}