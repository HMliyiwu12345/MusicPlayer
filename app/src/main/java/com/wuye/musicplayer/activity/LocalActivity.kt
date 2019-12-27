package com.wuye.musicplayer.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.ComponentName
import android.content.Context

import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils

import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.wuye.musicplayer.R
import com.wuye.musicplayer.bean.Music
import com.wuye.musicplayer.bean.eventbus.PostDuration
import com.wuye.musicplayer.consf.Constant
import com.wuye.musicplayer.service.MusicService
import com.wuye.musicplayer.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LocalActivity : BaseActivity(), View.OnClickListener {
    var TAG: String = "LocalActivity"
    val instance by lazy { this }
    var serviceItent: Intent? = null
    var mBinder: MusicService.MusicBinder? = null
    var iv_play: ImageView? = null
    var tv_name: TextView? = null
    var seekBar: SeekBar? = null
    var bundle: Bundle? = null
    var audio: Music? = null
    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as MusicService.MusicBinder
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local)
        initView()
        initData()
    }

    override fun initData() {

        //实例化activity跟service的实例，连接service
        EventBus.getDefault().register(this)
        serviceItent = Intent(this, MusicService::class.java)
        onStartAudioConnect()
        //将上次退出时播放的信息重新恢复
        val bundle = Bundle()
        bundle.putInt(Constant.DURATION, SpUtil.getInt(instance, Constant.DURATION, 0))
        bundle.putInt(
            Constant.CURRENT_POSITION,
            SpUtil.getInt(instance, Constant.CURRENT_POSITION, 0)
        )

        bundle.let { PostDuration(it) }.let { updateSeekBar(it) }

        tv_name?.text = SpUtil.getString(instance, Constant.MUSIC_NAME, "当前无资源")


    }

    /**
     * 实例化控件
     */
    override fun initView() {
        //播放按钮
        iv_play = findViewById(R.id.iv_play)
        iv_play?.setOnClickListener(instance)
        //歌曲的名字
        tv_name = findViewById(R.id.tv_res)
        findViewById<ImageView>(R.id.iv_previous).setOnClickListener(instance)
        findViewById<ImageView>(R.id.iv_next).setOnClickListener(instance)
        findViewById<ImageView>(R.id.iv_resourse).setOnClickListener(instance)
        //进度条
        seekBar = findViewById(R.id.sb_process)
        seekBar?.progress = 0
        seekBar?.setOnSeekBarChangeListener(OnSeekBarChangeListener())
    }

    override fun onClick(p0: View?) {

        PermissionUtil.requestPower(this, READ_EXTERNAL_STORAGE)
        when (p0?.id) {
            R.id.iv_play -> play()
            R.id.iv_resourse -> chooseAudio()
        }
    }

    private fun play() {
        //播放本地音乐
//        mBinder?.let {
        if (mBinder?.isPlaying()!!) {
            mBinder?.onPlay(iv_play)
        } else {
            mBinder?.onResetSeek(
                SpUtil.getString(instance, Constant.MUSIC_PATH, ""),
                SpUtil.getInt(instance, Constant.CURRENT_POSITION, 0)
            )
            iv_play?.setBackgroundResource(R.drawable.ic_pause)

        }
//        }

//            audio!!.path=SpUtil.getString(instance, Constant.MUSIC_PATH, "")
//            onStartAudioConnect(audio!!.path) }

    }

    private fun chooseAudio() {
//      调用本地音乐资源
        MusicUtils.chooseMusic(instance)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_MUSIC && resultCode == RESULT_OK && data != null) {
            //得到选择音频的播放信息
            audio = MusicUtils.getMusicInfo(instance, data.data)[0]
//          如果服务正在运行，开始播放音乐
            if (ServiceUtil.isServiceRunning(instance, "com.wuye.musicplayer.service.MusicService")
            ) {
                mBinder?.onReset((audio as Music).path)
            }
            iv_play?.setBackgroundResource(R.drawable.ic_pause)
            tv_name?.text = (audio as Music).title
        }
    }


    fun onStartAudioConnect() {
        // 开始服务连接

        bindService(serviceItent, conn, Context.BIND_AUTO_CREATE)

    }

    fun onStopAudioConnect() {
        //停止服务连接
        if (ServiceUtil.isServiceRunning(instance, "com.wuye.musicplayer.service.MusicService")) {
            unbindService(conn)
        }
    }

    /**
     * 接受从service返回过来的歌曲进度
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateSeekBar(postDuration: PostDuration) {
        bundle = postDuration.mBundle
        val max = bundle?.getInt(Constant.DURATION)
        val currentPosition = bundle?.getInt(Constant.CURRENT_POSITION)
        seekBar?.max = 100
        seekBar?.progress =
            ((max?.let { currentPosition?.toFloat()?.div(it) })?.times(100))!!.toInt()
    }

    /**
     * 进度条监听器
     */
    inner class OnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            //进度条改变时
//            MCLog.d(TAG, "--process$progress")
            if (fromUser)
                mBinder?.onSeek(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //进度条开始改变
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //进度条改变以后
        }

    }

//    override fun onSaveInstanceState(outState: Bundle?) {
//        bundle?.getInt(Constant.DURATION)?.let { outState?.putInt(Constant.DURATION, it) }
//        bundle?.getInt(Constant.CURRENT_POSITION)
//            ?.let { outState?.putInt(Constant.CURRENT_POSITION, it) }
//        audio?.title.let { outState?.putString(Constant.MUSIC_NAME, it) }
//        audio?.path.let { outState?.putString(Constant.MUSIC_PATH, it) }
//        super.onSaveInstanceState(outState)
//
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//        savedInstanceState?.let {
//            PostDuration(it)
//        }?.let {
//            updateSeekBar(it)
//            MCLog.v(TAG, "lyw")
//        }
//        tv_name?.text = savedInstanceState?.getString(Constant.MUSIC_NAME)
//    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        bundle?.getInt(Constant.DURATION)?.let { SpUtil.putInt(instance, Constant.DURATION, it) }
        bundle?.getInt(Constant.CURRENT_POSITION)
            ?.let { SpUtil.putInt(instance, Constant.CURRENT_POSITION, it) }
        audio?.title?.let { SpUtil.putString(instance, Constant.MUSIC_NAME, it) }
        audio?.path?.let { SpUtil.putString(instance, Constant.MUSIC_PATH, it) }
        onStopAudioConnect()
        super.onDestroy()

    }


}
