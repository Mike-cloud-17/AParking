package com.example.aparking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val code = findViewById<EditText>(R.id.code)
        val confirmButton = findViewById<Button>(R.id.confirm_button)

        confirmButton.setOnClickListener {
            val enteredCode = code.text.toString()
            // Проверяем код подтверждения (здесь мы просто сравниваем с конкретным кодом для демонстрации)
            if (enteredCode == "153517" || enteredCode == "382856" || enteredCode == "117105" || enteredCode == "1") {
                // Получаем номер телефона
                val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
                // Проверяем наличие номера в базе данных (здесь мы просто сравниваем с конкретным номером для демонстрации)
                if (phoneNumber == "89268255158" || phoneNumber == "1") {
                    // Переход к экрану успешного входа
                    val intent = Intent(this, SuccessfulLoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // Переход к экрану регистрации
                    val intent = Intent(this, RegistrationActivity::class.java)
                    startActivity(intent)
                }
            } else {
                // Отображение сообщения об ошибке
                Toast.makeText(this, "Неправильный код верификации", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
