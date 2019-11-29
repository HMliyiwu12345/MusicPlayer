package com.wuye.musicplayer.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar

import androidx.appcompat.app.AppCompatActivity
import com.wuye.musicplayer.R
import com.wuye.musicplayer.bean.Music
import com.wuye.musicplayer.consf.Constant
import com.wuye.musicplayer.service.MusicService
import com.wuye.musicplayer.utils.MCLog
import com.wuye.musicplayer.utils.MusicUtils
import com.wuye.musicplayer.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_local.*
import android.content.ServiceConnection as ServiceConnection1

class LocalActivity : BaseActivity(), View.OnClickListener {
    var TAG: String = "LocalActivity"
    val instance by lazy { this }
    var sb_process: SeekBar? = null

    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                Constant.UPDATE_SEEKBAR ->
                    updateSeekBar(msg.obj as Bundle)
            }
        }
    }

    var conn: ServiceConnection1 = object : ServiceConnection1 {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            val musicList = MusicUtils.getMusicInfo(instance)
            MCLog.d(TAG, musicList[0].path)
            resetPlay(musicList[0].path, p1)

//            sb_process?.max= mIBinder?.onGetMax()!!
//            sb_process?.progress= mIBinder?.onGetProcess()!!

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
    var mIBinder: MusicService.PlayBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local)
        initView()

    }

    /**
     * 实例化控件
     */
    override fun initView() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates
        var iv_play: ImageView = findViewById(R.id.iv_play)
        iv_play.setOnClickListener(instance)
        var iv_previous: ImageView = findViewById(R.id.iv_previous)
        iv_previous.setOnClickListener(instance)
        var iv_next: ImageView = findViewById(R.id.iv_next)
        iv_next.setOnClickListener(instance)
        sb_process = findViewById(R.id.sb_process)
        sb_process?.progress=0
        sb_process?.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                mIBinder?.onSeekto(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        PermissionUtil.requestPower(this, READ_EXTERNAL_STORAGE)
        when (p0?.id) {
            R.id.iv_play ->
                if (mIBinder == null) {
                    var intent = Intent(this, MusicService::class.java)

                    bindService(intent, conn, BIND_AUTO_CREATE)
                } else {
                    play(iv_play)
                }
            R.id.iv_previous -> previous()
            R.id.iv_next -> next()
        }
    }


    /**
     * 开始播放
     */
    private fun resetPlay(path: String, p1: IBinder?) {
        mIBinder = p1 as MusicService.PlayBinder
        mIBinder!!.mHandler=mHandler
        mIBinder?.onStartPlay(path, iv_play)
    }

    /**
     * 播放音乐
     */
    private fun play(ivPlayer: ImageView) {
        mIBinder?.onPlay(ivPlayer)


    }

    /**
     * 上一首
     */
    private fun previous() {

    }

    /**
     * 下一首
     */
    private fun next() {
    }

    /**
     * 更新进度条
     */
    private fun updateSeekBar(bundle: Bundle) {
        var max = bundle.getInt("duration")
        var currentPosition = bundle.getInt("currentPosition")

        sb_process?.max = 100
        sb_process?.progress = ((currentPosition.toFloat()/max)*100).toInt()

    }




}
