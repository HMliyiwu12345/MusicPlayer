package com.wuye.musicplayer.service

import android.annotation.TargetApi
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.text.TextUtils
import android.widget.ImageView
import com.wuye.musicplayer.R
import com.wuye.musicplayer.consf.Constant.UPDATE_SEEKBAR
import com.wuye.musicplayer.utils.MCLog
import com.wuye.musicplayer.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_local.*
import java.lang.Throwable
import java.util.*

class MusicService : Service() {
    val instance by lazy { this }
    var TAG: String = "MusicService"

    var playBinder: IBinder? = null

    var temp: Boolean = false

    private var mMediaPlayer: MediaPlayer? = null

    var mThread:Thread?=null


    override fun onBind(intent: Intent): IBinder {

//        TODO("Return the communication channel to the service.")

        return playBinder as IBinder
    }

    override fun onCreate() {
        super.onCreate()
        if (playBinder == null) {
            playBinder = PlayBinder()
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
    }

    inner class PlayBinder : Binder() {
        var mHandler:Handler?=null


        /**
         * 设置歌曲源，并开始播放
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun setDataSource(path: String?, ivPlayer: ImageView) {
            if(TextUtils.isEmpty(path)){
                return
            }
            mMediaPlayer?.reset()
            mMediaPlayer?.setDataSource(path)
            mMediaPlayer?.prepareAsync()
            //注册一个回调函数，以便在媒体源就绪时调用为回放
            mMediaPlayer?.setOnPreparedListener {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                onPlay(ivPlayer)
                updateSeekBar()
                temp=true
            }
            //判断音乐是否播放完成
            mMediaPlayer?.setOnCompletionListener {
                temp=false
                ivPlayer.setBackgroundResource(R.drawable.ic_play)
                onComplete()

            }
            //音乐出错的回调
            mMediaPlayer?.setOnErrorListener { mediaPlayer, i, i2 ->
                mMediaPlayer!!.reset()
                (mThread as Thread).isDaemon
                return@setOnErrorListener false
            }
            //设置是否循环播放
            mMediaPlayer?.isLooping = false
        }

        /**
         * 是否缓存完毕
         */
        fun isPrepare(): Boolean {
            //异步装载

            MCLog.d(TAG, temp.toString())

            return temp

        }

        /**
         * 在还没有加载音乐资源下进行音乐播放
         */
        fun onStartPlay(path: String?, ivPlayer: ImageView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setDataSource(path,ivPlayer)
                if(isPrepare()){
                    onPlay(ivPlayer)
                }
            } else {
                ToastUtil.showToast(instance, "android系统不得低于4.4")
            }
        }

        /**
         * 已经进行音乐资源加载下进行音乐播放
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


//        /**
//         * 停止按钮
//         */
//        fun onStop() {
//            mMediaPlayer?.stop()
//        }

        /**
         * 完成
         */
         fun onComplete() {
            mMediaPlayer?.release()
            mThread?.interrupt()
        }

        /**
         * 设置歌曲的播放进度
         */
        fun onSeekto(process: Int) {
            val mesc:Int= ((process.toFloat()/100)*mMediaPlayer?.duration!!).toInt()
            MCLog.d("lyw",((mesc).toString()))

            mMediaPlayer?.seekTo(mesc)
        }
        /**
         * 更新seekbar
         */
        fun updateSeekBar(){
        //获取总时长
            val duration:Int=mMediaPlayer?.duration!!
        //开启线程
            mThread=object:Thread(){
                override fun run() {
                    while(true){
                        sleep(1000)
                        val currentPosition:Int=mMediaPlayer?.currentPosition!!
                        val message:Message=Message.obtain()
                        val bundle=Bundle()
                        bundle.putInt("duration",duration)
                        bundle.putInt("currentPosition",currentPosition)
                        message.what=UPDATE_SEEKBAR
                        message.obj=bundle
                        mHandler!!.sendMessage(message)
                    }
                }
            }
            (mThread as Thread).start()
        }



    }


}
