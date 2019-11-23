package com.wuye.musicplayer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.wuye.musicplayer.R


class MainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }


    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        when (p0?.id) {
            R.id.local -> local()
            R.id.network -> network()
        }

    }

    /**
     * \实例化变量
     */
    override fun initView() {
        var local: Button = findViewById(R.id.local)
        local.setOnClickListener(this)
        var network: Button = findViewById(R.id.network)
        network.setOnClickListener(this)
    }

    /**
     * 跳转到本地资源播放页面
     */
    fun local() {
        var intent = Intent()
        intent.setClass(this, LocalActivity::class.java)
        startActivity(intent)
    }

    /**
     * 跳转到网络资源播放页面
     */
    fun network() {
        var intent = Intent()
        intent.setClass(this, NetworkActivity::class.java)
        startActivity(intent)
    }
}
