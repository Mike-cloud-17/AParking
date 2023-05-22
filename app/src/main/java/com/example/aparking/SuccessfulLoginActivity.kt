package com.example.aparking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SuccessfulLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_login)
        val handler = Handler().also {
            it.postDelayed({
                // Запуск активности с картой
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish() // Закрыть SuccessfulLoginActivity
            }, 3000)
        } // 3000 миллисекунд = 3 секунды
    }
}
