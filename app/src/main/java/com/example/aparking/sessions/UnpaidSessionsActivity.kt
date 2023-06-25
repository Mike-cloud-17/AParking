package com.example.aparking.sessions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.aparking.R
import com.example.aparking.databinding.UnpaidSessoinsActivityBinding

class UnpaidSessionsActivity : AppCompatActivity() {
    private lateinit var binding: UnpaidSessoinsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UnpaidSessoinsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Прячем верхнюю панель (ActionBar)
        supportActionBar?.hide()

        binding.imageButton.setOnClickListener { finish() }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.unpaid_fragment_container, UnpaidSessionFragment().apply {
                arguments = bundleOf(
                    "showHeader" to false,
                )
            }, "UnpaidSessionFragment")
            .commit()
    }
}