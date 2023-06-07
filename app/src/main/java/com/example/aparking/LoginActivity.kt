package com.example.aparking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("68729688-2101-4bda-8ee8-58be30117867")
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("AParking", Context.MODE_PRIVATE)
        val isLogged = sharedPreferences.getBoolean("isLogged", false)

        if (isLogged) {
            // Переход на экран с картой, так как пользователь уже вошел
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            // Если пользователь еще не вошел, то отображаем экран входа
            val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
            val loginButton = findViewById<Button>(R.id.loginButton)

            loginButton.setOnClickListener {
                val intent = Intent(this, ConfirmationActivity::class.java)
                intent.putExtra("PHONE_NUMBER", phoneNumber.text.toString())
                startActivity(intent)
            }
        }
    }
}
