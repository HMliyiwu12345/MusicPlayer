package com.wuye.musicplayer.activity

import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import com.wuye.musicplayer.R
import com.wuye.musicplayer.service.MusicService
import kotlinx.android.synthetic.main.activity_local.*
import android.content.ServiceConnection as ServiceConnection1

class LocalActivity : BaseActivity(), View.OnClickListener {

    var conn: ServiceConnection1? = null
    var mIBinder: MusicService.PlayBinder? = null
    var playSwitch: Boolean=false

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
        when(p0?.id){
            R.id.iv_play->play()
            R.id.iv_previous->previous()
            R.id.iv_next->next()
        }
    }

    /**
     * 播放音乐
     */
    private fun play() {
        if(playSwitch){
            //声音暂停播放的逻辑
            iv_play.setBackgroundResource(R.drawable.ic_play)
            mIBinder?.onStart()
        }else{
            //声音开始播放的逻辑
            iv_play.setBackgroundResource(R.drawable.ic_pause)

        }
    playSwitch=!playSwitch;
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
