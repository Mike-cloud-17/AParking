package com.example.aparking.sessions

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aparking.R
import com.example.aparking.UNPAID_SESSIONS
import com.example.aparking.databinding.ActivitySessionsBinding
import com.example.aparking.parkingTimer.CURRENT_SESSION
import com.example.aparking.parkingTimer.FIRST_SCREEN

class SessionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionsBinding
    private var defaultSize: Int = 42
    private var selectedSize: Int = 92
    private val viewModel: SessionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val scale = resources.displayMetrics.density
        defaultSize = (defaultSize * scale).toInt()
        selectedSize = (selectedSize * scale).toInt()

        super.onCreate(savedInstanceState)
        binding = ActivitySessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Прячем верхнюю панель (ActionBar)
        supportActionBar?.hide()

        viewModel.startTimer(intent.getLongExtra("current_time", 0L))
        viewModel.setSpotNumber(intent.getStringExtra("spot_number") ?: "A21")
        val firstScreen = intent.getStringExtra(FIRST_SCREEN) ?: CURRENT_SESSION
        if (firstScreen == UNPAID_SESSIONS) {
            selectUnpaidSessions()
        } else {
            selectCurrentSession()
        }

        with(binding) {
            currentButton.setOnClickListener {
                selectCurrentSession()
            }

            unpaidButton.setOnClickListener {
                selectUnpaidSessions()
            }

            historyButton.setOnClickListener {
                selectSessionsHistory()
            }

            imageButton.setOnClickListener { finish() }
        }
    }

    private fun selectCurrentSession() = with(binding) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                CurrentSessionFragment(),
                "CurrentSessionFragment"
            ).commit()
        currentSessionTitle.hide()
        unpaidSessionsTitle.show()
        sessionsHistoryTitle.show()
        currentButton.changeSize(selectedSize)
        unpaidButton.changeSize(defaultSize)
        historyButton.changeSize(defaultSize)
    }

    private fun selectUnpaidSessions() = with(binding) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                UnpaidSessionFragment(),
                "UnpaidSessionFragment"
            )
            .commit()
        currentSessionTitle.show()
        unpaidSessionsTitle.hide()
        sessionsHistoryTitle.show()
        unpaidButton.changeSize(selectedSize)
        currentButton.changeSize(defaultSize)
        historyButton.changeSize(defaultSize)
    }

    private fun selectSessionsHistory() = with(binding) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                HistorySessionFragment(),
                "HistorySessionFragment"
            )
            .commit()
        currentSessionTitle.show()
        unpaidSessionsTitle.show()
        sessionsHistoryTitle.hide()
        historyButton.changeSize(selectedSize)
        currentButton.changeSize(defaultSize)
        unpaidButton.changeSize(defaultSize)
    }

    private fun LinearLayout.changeSize(size: Int) {
        layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = size
        layoutParams.width = size
    }

    // Вынести в какой-нибудь core модуль для всех вьюх
    fun View.hide() {
        visibility = View.GONE
    }

    fun View.show() {
        visibility = View.VISIBLE
    }
}