package com.example.aparking

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class SettingsActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var spinner: Spinner
    private lateinit var switchAvailableParking: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        // Для открытия меню
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // изменяем заголовок ActionBar
        supportActionBar?.title = "Настройки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinner = findViewById(R.id.language_spinner)
        switchAvailableParking = findViewById(R.id.switch_available_spots)

        val items = listOf(
            Item(R.drawable.ic_flag_rus, "Русский"),
            Item(R.drawable.ic_flag_kz, "Казахский"),
            Item(R.drawable.ic_flag_gb, "Английский")
        )
        val adapter = SpinnerAdapter(this, R.layout.spinner_item, items)
        spinner.adapter = adapter

        switchAvailableParking.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filterParkingSpots()
            }
        }

        // открываем левое меню
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
                    // Открыть настройки
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher
        return true
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

    private fun filterParkingSpots() {
        val allParkingSpots: List<ParkingSpot> = ParkingSpotsRepository().getParkingSpots()
        val freeParkingSpots = allParkingSpots.filter { !it.isOccupied }
        // Обновляем список парковок на карте с использованием freeParkingSpots
    }
}

