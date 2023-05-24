package com.example.aparking

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import com.yandex.mapkit.MapKitFactory

class MapActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("68729688-2101-4bda-8ee8-58be30117867")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_map)

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
                }
                R.id.nav_map -> {
                    // Открыть карту парковок
                }
                R.id.nav_cars -> {
                    // Открыть мои автомобили
                }
                R.id.nav_settings -> {
                    // Открыть настройки
                }
                R.id.nav_about -> {
                    // Открыть о нас
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Запустить MapFragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MapFragment())
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
}