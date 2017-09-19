package com.rollercoaster.marginoverlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import org.jetbrains.anko.layoutInflater

class MarginService: Service() {

    private lateinit var windowManager: WindowManager

    private var overlay: View? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlay = layoutInflater.inflate(R.layout.overlay, null)

        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        windowManager.addView(overlay, params)

        MOApp.serviceRunning.onNext(true)
    }

    override fun onDestroy() {
        overlay?.let { windowManager.removeView(it) }

        MOApp.serviceRunning.onNext(false)

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

}
