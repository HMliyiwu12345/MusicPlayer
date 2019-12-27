package com.wuye.musicplayer.service

import android.app.Service
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.parseUri
import android.media.MediaPlayer
import android.os.*
import android.widget.ImageView
import com.wuye.musicplayer.R
import com.wuye.musicplayer.bean.eventbus.PostDuration
import com.wuye.musicplayer.consf.Constant
import com.wuye.musicplayer.utils.MCLog
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.*

class MusicService : Service(), MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener,
    Runnable{



    val instance by lazy { this }
    var TAG: String = "MusicService"
    var mediaPlayer: MediaPlayer? = null
    var mThread:Thread?=null
    var temp:Boolean=false
    var mProcess:Int?=0



    override fun onBind(intent: Intent?): IBinder? {
        mediaPlayer ?: let {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setOnPreparedListener(instance)
            mediaPlayer?.setOnCompletionListener(instance)
            mediaPlayer?.setOnErrorListener(instance)

        }
        return MusicBinder()
    }


    inner class MusicBinder : Binder() {

        /**
         * 重新点播音频
         */
        fun onReset(path:String?){
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(path)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.isLooping = false
            mThread= Thread(instance)

        }

        fun onResetSeek(path: String?,process: Int?){
            mProcess=process
            mediaPlayer?.setDataSource(path)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.isLooping = false
            mThread= Thread(instance)

        }

        /**
         * 在音频的资源加载下播放或者暂停音乐
         */
        fun onPlay(ivPlay: ImageView?) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer?.pause()
                ivPlay?.setBackgroundResource(R.drawable.ic_play)
            } else {
                mediaPlayer?.start()
                ivPlay?.setBackgroundResource(R.drawable.ic_pause)

            }
        }

        /**
         * 拖动进度条调节音频进度
         */
        fun onSeek(process: Int) {
            val mesc:Int= ((process.toFloat()/100)*mediaPlayer?.duration!!).toInt()
            MCLog.d("lyw",((mesc).toString()))
            mediaPlayer?.seekTo(mesc)
        }

        fun isPlaying(): Boolean {
            return temp
        }

    }


    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mThread?.interrupt()
        mediaPlayer?.stop()
        temp=false
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp!!.start()
        mThread?.start()
        if(mProcess!=0){
            mProcess?.let { mp.seekTo(it).let { mProcess=0 } }
        }
        temp=true
    }

    override fun run() {
        try {
        val duration: Int = mediaPlayer?.duration!!
        while (true) {
            if (Thread.interrupted()) {
                break
            }
            Thread.sleep(1000)
            val currentPosition: Int = mediaPlayer?.currentPosition!!
            val bundle = Bundle()
            bundle.putInt(Constant.DURATION, duration)
            bundle.putInt(Constant.CURRENT_POSITION, currentPosition)
            EventBus.getDefault().post(PostDuration(bundle))
        }
        }catch (e:InterruptedException){
            e.printStackTrace()
            Thread.currentThread().interrupt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mThread?.interrupt()
        mediaPlayer?.stop()
        temp=false
    }
}


