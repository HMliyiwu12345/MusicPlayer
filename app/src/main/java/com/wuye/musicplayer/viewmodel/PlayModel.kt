package com.wuye.musicplayer.viewmodel

import android.app.Activity
import com.wuye.musicplayer.service.MusicService

class PlayModel(context: Activity?,binder: MusicService.MusicBinder?) {
    var mContext: Activity?= null
    var mBinder: MusicService.MusicBinder? = null

    init {
        mContext=context
        mBinder=binder
    }

//    private fun play() {
//        //播放本地音乐
//        mBinder?.onPlay(iv_play)
//
//    }

}