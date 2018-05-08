package com.afaya.mp3playerdemo;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class MediaDemo {
    private static final String TAG = "MediaDemo";
    private Context context;
    private MediaPlayer mediaPlayer = null;
    public MediaDemo(Context context) {
        this.context = context;
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.drawable.ic_launcher_background);
        }
    }


}
