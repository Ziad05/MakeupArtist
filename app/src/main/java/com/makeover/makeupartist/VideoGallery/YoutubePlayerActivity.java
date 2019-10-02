package com.makeover.makeupartist.VideoGallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.makeover.makeupartist.Common;
import com.makeover.makeupartist.R;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_REQUEST = 111;
    private YouTubePlayerView youTubePlayerView;
    private FrameLayout player_Holder;
    private RelativeLayout player_Layout;
    private YouTubePlayer mPlayer;
    private AppCompatImageButton playButton, pauseButton;
    private AppCompatTextView mPlayTime_tv;
    private Handler mHandler = new Handler();
    private Handler player_Handler = new Handler();
    private Runnable player_Runnable;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(Common.Youtube_API_Key, this);

        playButton = findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mPlayer && !mPlayer.isPlaying()) mPlayer.play();
            }
        });

        pauseButton = findViewById(R.id.pause_btn);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mPlayer && mPlayer.isPlaying()) mPlayer.pause();
            }
        });

        mPlayTime_tv = findViewById(R.id.play_time);
        SeekBar mSeekBar = findViewById(R.id.video_seekBar);
        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);

        player_Holder = findViewById(R.id.player_Holder);
        player_Layout = findViewById(R.id.player);

        player_Holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_Layout.setVisibility(View.VISIBLE);
                player_Runnable = new Runnable() {
                    @Override
                    public void run() {
                        player_Layout.setVisibility(View.GONE);
                    }
                };
                player_Handler.postDelayed(player_Runnable, 4000);
            }
        });

        player_Layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        try {
                            player_Runnable = new Runnable() {
                                @Override
                                public void run() {
                                    player_Layout.setVisibility(View.GONE);
                                }
                            };
                            player_Handler.postDelayed(player_Runnable, 4000);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;

                    case MotionEvent.ACTION_DOWN:
                        try {
                            player_Handler.removeCallbacks(player_Runnable);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private String Video_Id(){
        return getIntent().getStringExtra(Common.Video_Id_Intent_Key);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        if (null == youTubePlayer) return;
        mPlayer = youTubePlayer;

        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

        if (!wasRestored) {
            youTubePlayer.loadVideo(Video_Id());
        }

        // Add listeners to YouTubePlayer instance
        mPlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);
        mPlayer.setPlaybackEventListener(mPlaybackEventListener);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        }
        else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Common.Custom_Toast(this, error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            youTubePlayerView.initialize(Common.Youtube_API_Key, this);
        }
    }


    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPaused() {
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            mHandler.removeCallbacks(runnable);
        }
    };


    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            // Called when an error occurs.
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoaded(String arg0) {
            // Called when a video is done loading.
        }

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
            pauseButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
            displayCurrentTime();
        }
    };

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        try {
            String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
            mPlayTime_tv.setText(formattedTime);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }
    }
}