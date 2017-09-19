package com.rollercoaster.marginoverlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.layoutInflater
import kotlinx.android.synthetic.main.overlay.*
import kotlinx.android.synthetic.main.overlay.view.*

class MarginService: Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlay: View

    private val bindings = CompositeDisposable()

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
        bindings.addAll(
            MOApp.showAt16.subscribe {
                overlay.start_margin_16.visibleIf(it)
                overlay.end_margin_16.visibleIf(it)
            },
            MOApp.showAt10.subscribe {
                overlay.start_margin_10.visibleIf(it)
                overlay.end_margin_10.visibleIf(it)
            },
            MOApp.showHorizontal.subscribe {
                overlay.horizontal_margin.visibleIf(it)
            }
        )
    }

    override fun onDestroy() {
        bindings.dispose()
        overlay?.let { windowManager.removeView(it) }
        MOApp.serviceRunning.onNext(false)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

}

fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}
