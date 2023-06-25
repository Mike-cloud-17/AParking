package com.example.aparking.parkingTimer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.aparking.MapViewModel
import com.example.aparking.sessions.SessionsActivity
import com.example.aparking.databinding.FragmentParkingTimerBinding

const val FIRST_SCREEN = "first_screen"
const val CURRENT_SESSION = "current_session"

interface ParkingActionListener {
    fun onStartParking()
}

class ParkingTimerFragment : Fragment() {
    private lateinit var binding: FragmentParkingTimerBinding
    private val viewModel: ParkingTimerViewModel by viewModels()
    private val parentViewModel: MapViewModel by activityViewModels()
    private var time = 0L
    private var spotNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingTimerBinding.inflate(inflater, container, false)

        viewModel.timeString.observe(viewLifecycleOwner) {
            binding.textViewTimer.text = it
        }
        viewModel.time.observe(viewLifecycleOwner) {
            time = it
        }
        parentViewModel.getSelectedSpotLiveData().observe(viewLifecycleOwner) {
            spotNumber = it
        }

        viewModel.startTimer()

        // Делаем фон диалогового окна прозрачным
        binding.root.setBackgroundColor(Color.TRANSPARENT)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTimer.setOnClickListener {
            startActivity(
                Intent(activity, SessionsActivity::class.java)
                    .putExtra("current_time", time)
                    .putExtra("spot_number", spotNumber)
                    .putExtra(FIRST_SCREEN, CURRENT_SESSION)
            )
        }
    }
}

