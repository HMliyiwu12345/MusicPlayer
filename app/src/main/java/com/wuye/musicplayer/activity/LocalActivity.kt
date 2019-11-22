package com.wuye.musicplayer.activity

import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import android.os.IBinder

import androidx.appcompat.app.AppCompatActivity
import com.wuye.musicplayer.R
import android.content.ServiceConnection as ServiceConnection1

class LocalActivity : Activity() {
    var conn: ServiceConnection1? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local)
        conn= object:ServiceConnection1{
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }


}
