package com.a2345.mimeplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;


/**
 * Created by fanzf on 2016/1/25.
 */
public class ProtoPlayerActivity extends Activity implements View.OnClickListener {
    Button mBPlay, mBPause, mBStop;//播放，停止
    SurfaceView mSurface;
    SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_proto);

        initView();
        initListener();
        initSurface();
    }

    /**
     * surfaceView初始化
     */
    private void initSurface() {
        mSurface.getHolder().addCallback(mSurfaceCallBack);
    }

    int currentPosition;
    SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i("info", "surfaceCreated");
            if (currentPosition > 0) {
                playFromLastPosition(currentPosition);
                currentPosition = 0;
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i("info", "surfaceChanged");

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i("info", "surfaceDestroyed");
            currentPosition = mPlayer.getCurrentPosition();
            mPlayer.stop();
        }
    };

    MediaPlayer mPlayer;
    Uri mLocalUri;
    boolean mIsPlaying;
    private void playFromLastPosition(int position) {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setDisplay(mSurface.getHolder());
        mLocalUri = Uri.parse("file:///sdcard/kgmusic/download/girls.mp4");
        try {
            mPlayer.setDataSource(getApplicationContext(), mLocalUri);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
                mPlayer.seekTo(currentPosition);
                mSeekBar.setMax(mPlayer.getDuration());

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            mIsPlaying = true;
                            while (mIsPlaying) {
                                mSeekBar.setProgress(mPlayer.getCurrentPosition());
                            }
                            sleep(500l);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                mBPlay.setEnabled(false);
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBPlay.setEnabled(true);
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                playFromLastPosition(0);
                return false;
            }
        });

    }

    /**
     * click事件注册
     */
    private void initListener() {
        mBPlay.setOnClickListener(this);
        mBPause.setOnClickListener(this);
        mBStop.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarListener);
    }
    SeekBar.OnSeekBarChangeListener mSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsPlaying = false;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(mPlayer != null && mPlayer.isPlaying()){
                mPlayer.seekTo(seekBar.getProgress());
                mIsPlaying = true;
            }
        }
    };

    /**
     * view初始化
     */
    private void initView() {
        mBPlay = (Button) findViewById(R.id.play);
        mBPause = (Button) findViewById(R.id.pause);
        mBStop = (Button) findViewById(R.id.stop);
        mSurface = (SurfaceView) findViewById(R.id.surfaceview);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**activity销毁前，player释放*/
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                playFromLastPosition(0);
                break;
            case R.id.pause:
                pause();
                break;
            case R.id.stop:
                onProtoStop();
                break;
            default:
                break;
        }
    }

    private void pause(){
        if (mPlayer != null && mBPause.getText().toString().trim().equals("继续")){
            mBPause.setText("暂停");
            mPlayer.start();
            return;
        }
        if (mPlayer != null && mPlayer.isPlaying()){
            mBPause.setText("继续");
            mPlayer.pause();
        }
    }
    /**
     * player停止
     */
    private void onProtoStop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mBPlay.setEnabled(true);
            mIsPlaying = false;
        }
    }

}
