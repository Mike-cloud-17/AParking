package com.example.aparking.parkingTimer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aparking.databinding.FragmentParkingTimerBinding

interface ParkingActionListener {
    fun onStartParking()
}

class ParkingTimerFragment : Fragment() {
    private lateinit var binding: FragmentParkingTimerBinding
    private val viewModel: ParkingTimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingTimerBinding.inflate(inflater, container, false)

        viewModel.timeString.observe(viewLifecycleOwner) {
            binding.textViewTimer.text = it
        }

        viewModel.startTimer()

        // Делаем фон диалогового окна прозрачным
        binding.root.setBackgroundColor(Color.TRANSPARENT)

        return binding.root
    }
}

