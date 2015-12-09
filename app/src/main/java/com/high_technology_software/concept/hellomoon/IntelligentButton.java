package com.high_technology_software.concept.hellomoon;

import android.content.Context;
import android.widget.Button;

public class IntelligentButton {

    private Button mButton;
    private Context mContext;
    private AudioPlayer mAudioPlayer;
    private int mStatus;

    public IntelligentButton (Button button, Context context, AudioPlayer audioPlayer) {
        mButton = button;
        mContext = context;
        mAudioPlayer = audioPlayer;
        mStatus = -1;
    }

    public void click() {

        switch (mStatus) {
            case -1:
                mAudioPlayer.play(mContext);
                mButton.setText(R.string.hellomoon_pause);
                break;
            case 0:
                mAudioPlayer.pause();
                mButton.setText(R.string.hellomoon_resume);
                break;
            case 1:
                mAudioPlayer.resume();
                mButton.setText(R.string.hellomoon_pause);
                break;
        }

        mStatus = (mStatus + 1) % 2;
    }

    public void reset() {
        mStatus = -1;
        mButton.setText(R.string.hellomoon_play);
    }

}
