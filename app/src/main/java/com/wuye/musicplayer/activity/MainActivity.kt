package com.wuye.musicplayer.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.wuye.musicplayer.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var local: Button = findViewById(R.id.local)
        local.setOnClickListener(this)
        var network: Button = findViewById(R.id.network)
        network.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        when (p0?.id) {
            R.id.local -> local()
            R.id.network -> network()
        }

    }

    fun local() {
        var intent = Intent()
        intent.setClass(this, LocalActivity::class.java)
        startActivity(intent)
    }

    fun network() {
        var intent = Intent()
        intent.setClass(this, NetworkActivity::class.java)
        startActivity(intent)
    }
}
