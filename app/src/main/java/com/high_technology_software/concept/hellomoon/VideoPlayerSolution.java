package com.high_technology_software.concept.hellomoon;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;

public class VideoPlayerSolution {

    private static final String TAG = "VideoPlayerSolution";

    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsVideoReadyToBePlayed;
    private boolean mIsVideoSizeKnown;

    public VideoPlayerSolution(Context context, SurfaceHolder surfaceHolder) {
        mContext = context;
        mSurfaceHolder = surfaceHolder;
    }

    public void playVideo(int resId) {
        doCleanUp();
        try {
            mMediaPlayer = MediaPlayer.create(mContext, resId);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.d(TAG, "onBufferingUpdate percent:" + percent);
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "onCompletion called");
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared called");
                    mIsVideoReadyToBePlayed = true;
                    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
                        startVideoPlayback();
                    }
                }
            });
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.v(TAG, "onVideoSizeChanged called");
                    if (width == 0 || height == 0) {
                        Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
                        return;
                    }
                    mIsVideoSizeKnown = true;
                    mVideoWidth = width;
                    mVideoHeight = height;
                    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
                        startVideoPlayback();
                    }
                }
            });
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    public void stopVideo() {
        mMediaPlayer.stop();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }
    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }
}
