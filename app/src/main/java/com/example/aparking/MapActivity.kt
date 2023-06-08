package com.example.aparking

import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.aparking.parkingTimer.ParkingActionListener
import com.example.aparking.parkingTimer.ParkingTimerFragment
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.transport.TransportFactory

class MapActivity : AppCompatActivity(), ParkingActionListener {
    private val viewModel: MapViewModel by viewModels()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Прячем верхнюю панель (ActionBar)
        supportActionBar?.hide()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        // Запустить MapFragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.map_fragment_container, MapFragment())
            .commit()

        // Запустить ParkingFragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.parking_fragment_container, ParkingFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun getDrawerLayout(): DrawerLayout {
        return drawerLayout
    }

    override fun onStartParking() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.parking_fragment_container, ParkingTimerFragment())
            .commit()
    }
}