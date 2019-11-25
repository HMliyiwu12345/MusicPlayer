package com.wuye.musicplayer.service

import android.annotation.TargetApi
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.widget.ImageView
import com.wuye.musicplayer.R
import com.wuye.musicplayer.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_local.*
import java.util.*

class MusicService : Service() {
    val instance by lazy { this }
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
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
    }

    inner class PlayBinder : Binder() {
        /**
         * 设置歌曲源
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun setDataSource(path: String) {
            mMediaPlayer?.setDataSource(path)
            mMediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder().setContentType(
                    CONTENT_TYPE_MUSIC
                ).build()
            )
            //设置是否循环播放
            mMediaPlayer?.isLooping = false
        }

        /**
         * 是否缓存完毕
         */
        fun isPrepare(): Boolean {
            var temp: Boolean = false
            //异步装载
            mMediaPlayer?.prepareAsync()
            mMediaPlayer?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(p0: MediaPlayer?) {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    temp = true
                }
            })

            return temp

        }

        /**
         * 开始播放
         */
        fun onStartPlay(vararg params: Any) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isPrepare()) {
                    onPlay(params[0] as ImageView)
                } else if(!TextUtils.isEmpty(params[1] as String)){
                    setDataSource(params[1] as String)
                    onStartPlay(params[1], params[0])
                }
            }else{
                ToastUtil.showToast(instance,"android系统不得低于4.4")
            }
        }

        /**
         * 播放按钮
         */
        fun onPlay(ivPlayer: ImageView) {
            if (mMediaPlayer?.isPlaying!!) {
                mMediaPlayer?.pause()
                ivPlayer.setBackgroundResource(R.drawable.ic_play)
            } else {
                mMediaPlayer?.start()
                ivPlayer.setBackgroundResource(R.drawable.ic_pause)

            }
        }

        /**
         * 停止按钮
         */
        fun onStop() {
            mMediaPlayer?.stop()
        }

        /**
         * 销毁按钮
         */
        fun onDestroy() {
            mMediaPlayer?.release()
        }

        /**
         * 设置歌曲的播放进度
         */
        fun onSeekto(mesc: Int) {
            mMediaPlayer?.seekTo(mesc)
        }
    }
}
