package com.example.aparking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val name = findViewById<EditText>(R.id.name)
        val surname = findViewById<EditText>(R.id.surname)
        val birthdate = findViewById<EditText>(R.id.birthdate)
        val carNumber = findViewById<EditText>(R.id.carNumber)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val enteredName = name.text.toString()
            val enteredSurname = surname.text.toString()
            val enteredBirthdate = birthdate.text.toString()
            val enteredCarNumber = carNumber.text.toString()

            // Регистрируем нового пользователя (в этом примере мы просто сравниваем введенные данные с определенными значениями для демонстрации)
            if (enteredName == "Михаил" && enteredSurname == "Шкляр" && enteredBirthdate == "17.07.2001" && enteredCarNumber == "777KNS02") {
                // Переход к экрану успешного входа
                val intent = Intent(this, SuccessfulLoginActivity::class.java)
                startActivity(intent)
            } else {
                // Отображение сообщения об ошибке
                Toast.makeText(this, "Вы ввели некорректные данные", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
