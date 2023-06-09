package com.example.aparking

import android.os.Bundle
import android.view.MenuItem
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var switchAvailableParking: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
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

    private fun filterParkingSpots() {
        val allParkingSpots: List<ParkingSpot> = ParkingSpotsRepository().getParkingSpots()
        val freeParkingSpots = allParkingSpots.filter { !it.isOccupied }
        // Обновляем список парковок на карте с использованием freeParkingSpots
    }
}

