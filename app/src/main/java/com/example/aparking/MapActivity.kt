package com.example.aparking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yandex.mapkit.MapKitFactory

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("68729688-2101-4bda-8ee8-58be30117867")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_map)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MapFragment())
            .commit()
    }
}