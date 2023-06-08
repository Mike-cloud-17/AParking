package com.example.aparking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SessionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sessions)

        // Прячем верхнюю панель (ActionBar)
        supportActionBar?.hide()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CurrentSessionFragment(), "CurrentSessionFragment")
            .commit()
    }
}