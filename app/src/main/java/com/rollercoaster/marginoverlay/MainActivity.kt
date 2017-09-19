package com.rollercoaster.marginoverlay

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.net.Uri
import android.provider.Settings
import com.jakewharton.rxbinding2.widget.RxCompoundButton


class MainActivity : AppCompatActivity() {

    private val binding = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Settings.canDrawOverlays(this)) {
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName)), 1)
        }

        RxCompoundButton.checkedChanges(checkbox_16).skipInitialValue().subscribe { MOApp.showAt16.onNext(it) }
        RxCompoundButton.checkedChanges(checkbox_10).skipInitialValue().subscribe { MOApp.showAt10.onNext(it) }
    }

    override fun onStart() {
        super.onStart()
        binding.addAll(
            MOApp.serviceRunning.observeOn(AndroidSchedulers.mainThread()).subscribe(this::refresh),
            MOApp.showAt16.observeOn(AndroidSchedulers.mainThread()).subscribe { checkbox_16.isChecked = it },
            MOApp.showAt10.observeOn(AndroidSchedulers.mainThread()).subscribe { checkbox_10.isChecked = it }
        )
    }

    override fun onStop() {
        binding.clear()
        super.onStop()
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
