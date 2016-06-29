package com.a2345.mimeplayer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by fanzf on 2015/12/9.
 */
public class PlayActivity extends Activity implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
    private ProgressBar pb;
    private TextView downloadRateView, loadRateView;
    private VideoView video;
    private FrameLayout mVolumeBrightnessLayout;
    private ImageView mOperationBg, mOperationPercent;
    private String url;
    private GestureDetector mGestureDetector;

    private AudioManager mAudioManager;

    //    private int mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
    private int mDispWidth;
    private int mDispHeight;
    private int mMaxVolume;
    private int mVolume = -1;
    private float mLight = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_play);

        pb = (ProgressBar) findViewById(R.id.probar);
        downloadRateView = (TextView) findViewById(R.id.download_rate);
        loadRateView = (TextView) findViewById(R.id.load_rate);
        video = (VideoView) findViewById(R.id.video);
        mVolumeBrightnessLayout = (FrameLayout) findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        url = getIntent().getStringExtra("url");
        play();

        mGestureDetector = new GestureDetector(this, new MiMeGestureListener());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDispHeight = metrics.heightPixels;
        mDispWidth = metrics.widthPixels;

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        if (event.getAction() == MotionEvent.ACTION_UP)
            endGesture();

        return super.onTouchEvent(event);
    }

    private void endGesture() {
        mVolume = -1;
//        mBrightness = -1f;

        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    private void play() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        video.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0f);
//        video.getHolder().setFixedSize(metrics.widthPixels, metrics.heightPixels);
        video.setVideoURI(Uri.parse(url));
        video.setBufferSize(512 * 1024);
        video.setMediaController(new MediaController(this));
        video.setOnInfoListener(this);
        video.setOnBufferingUpdateListener(this);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (video != null) {
            video.stopPlayback();
            video = null;
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (video.isPlaying()) {
                    video.pause();
                    pb.setVisibility(View.VISIBLE);
                    downloadRateView.setText("");
                    loadRateView.setText("");
                    downloadRateView.setVisibility(View.VISIBLE);
                    loadRateView.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                video.start();
                pb.setVisibility(View.GONE);
                downloadRateView.setVisibility(View.GONE);
                loadRateView.setVisibility(View.GONE);
                video.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
                video.getHolder().setFixedSize(mDispWidth, mDispHeight);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadRateView.setText("" + extra + "kb/s" + "  ");
                break;


        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent + "%");
    }


    public class MiMeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldx = e1.getX();
            float mOldy = e1.getY();
            float y = e2.getRawY();
            if (mOldx > mDispWidth * 4.0 / 5.0) {
                onVolumeSlide((mOldy - y) / mDispHeight);
            } else if (mOldx < mDispWidth / 5.0) {
//                onLightSlide((mOldy - y) / mDispHeight);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private void onVolumeSlide(float delt) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
            mOperationBg.setImageResource(R.drawable.voice_dot1);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (delt * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);

    }
}
