package com.wuye.musicplayer.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    var playBinder: IBinder? = null
    private var mMediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder {

//        TODO("Return the communication channel to the service.")
        if (playBinder == null) {
            playBinder = PlayBinder()
        }
        return playBinder as IBinder
    }

    override fun onCreate() {
        super.onCreate()
        if(mMediaPlayer==null){
            mMediaPlayer = MediaPlayer()
        }
    }

   inner class PlayBinder : Binder() {

        fun setDataSource(path: String) {
            mMediaPlayer?.setDataSource(path)
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        fun isPrepare():Boolean{
            var temp:Boolean=false
            mMediaPlayer?.prepareAsync()
            mMediaPlayer?.setOnPreparedListener(object: MediaPlayer.OnPreparedListener{
                override fun onPrepared(p0: MediaPlayer?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    temp=true
                }
            })
            return temp

       }

        fun onStart() {
            
        }

        fun onResume() {

        }

        fun onPause() {

        }

        fun onStop() {

        }

        fun onDestroy() {

        }
    }
}
