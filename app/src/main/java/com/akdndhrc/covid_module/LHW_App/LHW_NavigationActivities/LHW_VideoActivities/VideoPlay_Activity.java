package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_VideoActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

public class VideoPlay_Activity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback

{
    Context ctx = VideoPlay_Activity.this;

    private static final String TAG1 = "PreprationStep1guest";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    public String fileName, filenameSDCard;


    //Shared Pref

    public String starting_time;
    public String ending_time;
    public String video_duration;


    public String time;
    public String ending;
    public String videoLink ,videoLink2;
    public String category;
    public String seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_video_play);


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this,
                VideoPlay_Activity.class));


        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                videoLink = extras.getString("KeyVideoLink");

               // File root = new File(Environment.getExternalStorageDirectory() + "/TeekoPlus+/Videos/");

                fileName = Environment.getExternalStorageDirectory() + "/HayatPK" + "/" + "Videos/" + videoLink;


                mVideoLayout = findViewById(R.id.video_layout);
                mBottomLayout = findViewById(R.id.bottom_layout);
                mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
                mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);


                mVideoView.setMediaController(mMediaController);
                mVideoView.setVideoURI(Uri.parse(fileName));


                mVideoView.setVideoViewCallback(this);
                setVideoAreaSize();


                mVideoView.seekTo(mSeekPosition);
                if (mSeekPosition > 0) {

                }
                mVideoView.start();

                try {
                    int duration = mVideoView.getDuration() / 1000;
                    int hours = duration / 3600;
                    int minutes = (duration / 60) - (hours * 60);
                    int seconds = duration - (hours * 3600) - (minutes * 60);

                    starting_time = String.format("%02d:%02d", minutes, seconds);

                    //   Toast.makeText(ctx, "Starting TIme: " + starting_time.toString(), Toast.LENGTH_SHORT).show();


                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int duration = mp.getDuration() / 1000;
                            int hours = duration / 3600;
                            int minutes = (duration / 60) - (hours * 60);
                            int seconds = duration - (hours * 3600) - (minutes * 60);

                            video_duration = String.format("%02d:%02d", minutes, seconds);

                            time = String.valueOf(video_duration);
                            //  Toast.makeText(ctx, "Video Duration: " + time.toString(), Toast.LENGTH_SHORT).show();


                            mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                                @Override
                                public void onSeekComplete(MediaPlayer mp) {


                                    int duration = mVideoView.getCurrentPosition() / 1000;
                                    int hours = duration / 3600;
                                    int minutes = (duration / 60) - (hours * 60);
                                    int seconds = duration - (hours * 3600) - (minutes * 60);
                                    seek = String.format("%02d:%02d", minutes, seconds);
                                    //  ending = String.valueOf(ending_time);


                                    //    Toast.makeText(ActVideoPlay.this, "Seek Position is: " + seek, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });


                } catch (Exception e) {
                    //  Toast.makeText(ctx, "Failed", Toast.LENGTH_SHORT).show();
                }
                mMediaController.setTitle(videoLink);

                mVideoView.seekTo(mSeekPosition);

                if (mSeekPosition > 0) {

                }
                mVideoView.start();
                mMediaController.setTitle(videoLink);
            }

        } catch (Exception e) {

            Log.d("000999", "Exception " + e);

        }


        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //meausreBPbuton.setText("Next");
                Log.d(TAG1, "onCompletion ");
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(ctx);

            dialogBuilder.setMessage("Are you sure you want to exit?");
            dialogBuilder.setPositiveButton(String.valueOf(R.string.yes), null);
            dialogBuilder.setNegativeButton(R.string.no, null);

            final android.app.AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            // override the text color of positive button
            positiveButton.setTextColor(getResources().getColor(R.color.dark_blue_color));

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  int duration = mVideoView.getCurrentPosition() / 1000;
                    int hours = duration / 3600;
                    int minutes = (duration / 60) - (hours * 60);
                    int seconds = duration - (hours * 3600) - (minutes * 60);
                    ending_time = String.format("%02d:%02d", minutes, seconds);
                    ending = String.valueOf(ending_time);
                  //  Toast.makeText(ctx, "Stop Duration : " + ending, Toast.LENGTH_LONG).show();


                    Intent newIntent = new Intent(ctx, VideoList_Activity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);

                }
            });

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            // override the text color of negative button
            negativeButton.setTextColor(getResources().getColor(R.color.dark_blue_color));
            // provides custom implementation to negative button click
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getApplicationContext(),"Work UploadAPIs Cancel",Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
        return false;
    }


    @Override
    protected void onPause() {

        Log.d(TAG1, "onPause ");
        super.onPause();
        mSeekPosition = mVideoView.getCurrentPosition(); //stopPosition is an int

        mVideoView.pause();


    }

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                //cachedHeight = (int) (width * 405f / 720f);
                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                // videoLayoutParams.height = cachedHeight;
                videoLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoLayout.setLayoutParams(videoLayoutParams);

                mVideoView.setVideoPath(fileName);

                mVideoView.requestFocus();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG1, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);

    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG1, "onRestoreInstanceState Position=" + mSeekPosition);

    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);


        }

        //switchTitleBar(!isFullscreen);
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }


}