package com.rollercoaster.marginoverlay

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startService
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.app.Activity
import android.net.Uri
import android.provider.Settings.canDrawOverlays
import android.os.Build
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    private val binding = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName)), 1)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.add(
            MOApp.serviceRunning.observeOn(AndroidSchedulers.mainThread()).subscribe(this::refresh)
        )
    }


    private fun refresh(running: Boolean) {
        if (running) {
            main_button.text = "Stop Service"
            main_button.onClick { stopService(Intent(this@MainActivity, MarginService::class.java)) }
        } else {
            main_button.text = "Start Service"
            main_button.onClick { startService(Intent(this@MainActivity, MarginService::class.java)) }
        }
    }
}
