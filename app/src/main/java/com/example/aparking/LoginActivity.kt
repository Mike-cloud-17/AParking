package com.example.aparking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val intent = Intent(this, ConfirmationActivity::class.java)
            intent.putExtra("PHONE_NUMBER", phoneNumber.text.toString())
            startActivity(intent)
        }
    }
}
















//class LoginActivity : AppCompatActivity() {
//    // Замените этими значениями для имитации отправки кода
//    val mockEmail = "mishklyar@edu.hse.ru"
//    val mockCode = "8765"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        val emailField = findViewById<EditText>(R.id.emailField)
//        val loginButton = findViewById<Button>(R.id.loginButton)
//
//        loginButton.setOnClickListener {
//            val email = emailField.text.toString()
//
//            // Имитация отправки кода на email
//            if (email == mockEmail) {
//                val sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
//                with(sharedPref.edit()) {
//                    putString("email", mockEmail)
//                    putString("code", mockCode)
//                    apply()
//                }
//                // Переход к экрану подтверждения
//                val intent = Intent(this, ConfirmationActivity::class.java)
//                startActivity(intent)
//            } else {
//                // Отображение сообщения об ошибке
//                Toast.makeText(this, "Invalid email.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
