package com.example.aparking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    // создаем экземпляр User здесь
    private val user = User.getInstance()

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var actionButton: Button
    private lateinit var logoutButton: Button

    private var isInEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // изменяем заголовок ActionBar
        supportActionBar?.title = "Профиль"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nameEditText = findViewById(R.id.name_edit_text)
        surnameEditText = findViewById(R.id.surname_edit_text)
        dobEditText = findViewById(R.id.birth_date_edit_text)
        actionButton = findViewById(R.id.edit_save_button)
        logoutButton = findViewById(R.id.logout_button)

        // заполняем поля данными из экземпляра User
        nameEditText.setText(user.name)
        surnameEditText.setText(user.surname)
        dobEditText.setText(user.birthday)

        nameEditText.isEnabled = isInEditMode
        surnameEditText.isEnabled = isInEditMode
        dobEditText.isEnabled = isInEditMode

        actionButton.text = if (isInEditMode) "Сохранить" else "Редактировать"

        actionButton.setOnClickListener {
            if (isInEditMode) {
                // обновляем User
                if (validateInput()) {
                    user.name = nameEditText.text.toString()
                    user.surname = surnameEditText.text.toString()
                    user.birthday = dobEditText.text.toString()

                    isInEditMode = false
                    nameEditText.isEnabled = false
                    surnameEditText.isEnabled = false
                    dobEditText.isEnabled = false
                    actionButton.text = "Редактировать"
                } else {
                    Toast.makeText(this, "Некорректный ввод", Toast.LENGTH_SHORT).show()
                }
            } else {
                isInEditMode = true
                nameEditText.isEnabled = true
                surnameEditText.isEnabled = true
                dobEditText.isEnabled = true
                actionButton.text = "Сохранить"
            }
        }

        logoutButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("AParking", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLogged", false)
            editor.apply()

            // Logout user and go back to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // возвращает назад на предыдущий экран
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateInput(): Boolean {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val dob = dobEditText.text.toString()

        if (name.any { it.isDigit() } || surname.any { it.isDigit() }) {
            return false
        }

        if (dob.length != 10) {
            return false
        }

        return true
    }
}
