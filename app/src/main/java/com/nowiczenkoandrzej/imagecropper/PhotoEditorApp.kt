package com.nowiczenkoandrzej.imagecropper

import android.app.Application
import com.gu.toolargetool.TooLargeTool
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PhotoEditorApp: Application() {
    override fun onCreate() {
        super.onCreate()

        TooLargeTool.startLogging(this)
    }
}