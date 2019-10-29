package com.example.letsplay

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.example.letsplay.di.letsPlayApp
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

class App: MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, letsPlayApp)
        Fabric.with(this, Crashlytics())
    }

    companion object {
        @JvmStatic var instance: App? = null
            private set
    }

}