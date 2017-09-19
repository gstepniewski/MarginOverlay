package com.rollercoaster.marginoverlay

import android.app.Application
import io.reactivex.subjects.BehaviorSubject

class MOApp: Application() {
    companion object {
        val serviceRunning = BehaviorSubject.createDefault(false)
        var showAt16 = BehaviorSubject.createDefault(true)
        var showAt10 = BehaviorSubject.createDefault(false)
        var showHorizontal = BehaviorSubject.createDefault(false)
    }
}