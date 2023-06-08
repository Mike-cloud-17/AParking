package com.example.aparking.sessions

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aparking.R
import com.example.aparking.databinding.ActivitySessionsBinding

class SessionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionsBinding
    private val viewModel: SessionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Прячем верхнюю панель (ActionBar)
        supportActionBar?.hide()

        viewModel.startTimer(intent.getLongExtra("current_time", 0L))

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CurrentSessionFragment(), "CurrentSessionFragment")
            .commit()

        binding.currentButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, CurrentSessionFragment(), "CurrentSessionFragment")
                .commit()
        }

        binding.unpaidButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, UnpaidSessionFragment(), "UnpaidSessionFragment")
                .commit()
        }

        binding.historyButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, HistorySessionFragment(), "HistorySessionFragment")
                .commit()
        }

        binding.imageButton.setOnClickListener { finish() }
    }
}