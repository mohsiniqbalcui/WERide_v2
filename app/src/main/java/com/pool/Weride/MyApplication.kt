package com.backendme.ui.uiexample

import android.app.Application
import com.pool.Weride.FontsOverride


class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        FontsOverride.setDefaultFont(this, "MONOSPACE", "kol.ttf")

    }



}