package com.example.misha.musicapp.Classes;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.net.URI;

/**
 * Created by Misha on 11.04.2017.
 */

public class Music {
    private String name;
    private MediaPlayer audio;
    private int resid;
    public Music(String name, int resid, Context context){
        this.name = name;
        this.resid = resid;
        audio = MediaPlayer.create(context,resid);
    }

    public String getName() {
        return name;
    }
    public MediaPlayer getAudio(){
        return audio;
    }
    public int getResid(){return resid;}
}
