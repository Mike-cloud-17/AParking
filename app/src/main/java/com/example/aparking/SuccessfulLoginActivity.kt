package com.example.aparking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessfulLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_login)

        // Сохранить состояние авторизации пользователя
        val sharedPreferences = getSharedPreferences("AParking", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLogged", true).apply()

//        val handler = Handler().also {
//            it.postDelayed({
//                // Запуск активности с картой
//                val intent = Intent(this, MapActivity::class.java).apply{
//                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                }
//                startActivity(intent)
//                finish() // Закрыть SuccessfulLoginActivity
//            }, 3000)
//        } // 3000 миллисекунд = 3 секунды

        // Запустить корутину
        lifecycleScope.launch {
            delay(3000) // 3000 миллисекунд = 3 секунды

            // Запуск активности с картой
            val intent = Intent(this@SuccessfulLoginActivity, MapActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)

            finish() // Закрыть SuccessfulLoginActivity
        }
    }
}
