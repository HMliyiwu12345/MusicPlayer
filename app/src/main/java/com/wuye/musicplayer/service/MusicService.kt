package com.wuye.musicplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    var playBinder:IBinder?=null

    override fun onBind(intent: Intent): IBinder {

//        TODO("Return the communication channel to the service.")
        if(playBinder==null){
            playBinder=PlayBinder()
        }
        return playBinder as IBinder
    }

    class PlayBinder : Binder(){
        fun onStart(){

        }

        fun onResume(){

        }

        fun onPause(){

        }

        fun onStop(){

        }

        fun onDestroy(){

        }
    }
}
