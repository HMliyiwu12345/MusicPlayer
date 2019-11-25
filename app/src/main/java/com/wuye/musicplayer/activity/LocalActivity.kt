package com.wuye.musicplayer.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import com.wuye.musicplayer.R
import com.wuye.musicplayer.bean.Music
import com.wuye.musicplayer.service.MusicService
import com.wuye.musicplayer.utils.MCLog
import com.wuye.musicplayer.utils.MusicUtils
import com.wuye.musicplayer.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_local.*
import android.content.ServiceConnection as ServiceConnection1

class LocalActivity : BaseActivity(), View.OnClickListener {
    var TAG: String = "LocalActivity"
    var conn: ServiceConnection1? = null
    var mIBinder: MusicService.PlayBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local)
        conn = object : ServiceConnection1 {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                mIBinder = p1 as MusicService.PlayBinder
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        initView()
    }

    /**
     * 实例化控件
     */
    override fun initView() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates
        var iv_play: ImageView = findViewById(R.id.iv_play)
        iv_play.setOnClickListener(this)
        var iv_previous: ImageView = findViewById(R.id.iv_previous)
        iv_previous.setOnClickListener(this)
        var iv_next: ImageView = findViewById(R.id.iv_next)
        iv_next.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        PermissionUtil.requestPower(this, READ_EXTERNAL_STORAGE)
        when (p0?.id) {
            R.id.iv_play ->
                onPlay()
            R.id.iv_previous -> previous()
            R.id.iv_next -> next()
        }
    }

    private fun onPlay(){
        var intent = Intent(this, MusicService::class.java)
        bindService(intent, conn, BIND_AUTO_CREATE)
        if (mIBinder?.isPrepare()!!) {
            preparePlay()
        }
        else {
            val musicList = MusicUtils.getMusicInfo(this)
            MCLog.d(TAG, musicList[0].toString())
            startPlay(musicList[0].path)
        }
    }

    /**
     * 开始播放
     */
    private fun startPlay(path: String) {

        mIBinder?.onStartPlay(path, iv_play)
    }

    /**
     * 播放音乐
     */
    private fun preparePlay() {

        mIBinder?.onPlay(iv_play)

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
}
