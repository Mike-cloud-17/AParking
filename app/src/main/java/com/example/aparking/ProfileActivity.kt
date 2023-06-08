package com.example.aparking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    // создайте экземпляр User здесь
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

        // Для открытия меню
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

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

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    // Открыть профиль
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_map -> {
                    // Открыть карту парковок
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_cars -> {
                    // Открыть мои автомобили
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    // Открыть информацию о проекте
                    val intent = Intent(this, WebViewActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // открывает боковое меню
                drawerLayout.openDrawer(GravityCompat.START)
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
