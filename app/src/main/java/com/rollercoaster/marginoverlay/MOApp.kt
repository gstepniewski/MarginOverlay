package com.rollercoaster.marginoverlay

import android.app.Application
import io.reactivex.subjects.BehaviorSubject

class MOApp: Application() {
    companion object {
        val serviceRunning = BehaviorSubject.createDefault(false)
    }
}