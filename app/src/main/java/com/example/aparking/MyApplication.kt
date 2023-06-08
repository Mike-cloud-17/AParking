package com.example.aparking

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("68729688-2101-4bda-8ee8-58be30117867")
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
    }
}