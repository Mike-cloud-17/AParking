package com.example.aparking.parkingTimer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ParkingTimerViewModel : ViewModel() {

    private val _timeString = MutableLiveData("00:00:00")
    private val _time = MutableLiveData<Long>()
    val timeString: LiveData<String> = _timeString
    val time: LiveData<Long> = _time

    ////////////// Close in 30 sec
    private val _parkingFinished = MutableLiveData<Boolean>()
    val parkingFinished: LiveData<Boolean> = _parkingFinished

    private var timer: CountDownTimer? = null

    fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            var timeInMilliseconds = 0L
            override fun onTick(millisUntilFinished: Long) {
                timeInMilliseconds += 1000
                _time.value = timeInMilliseconds
                _timeString.value = convertTime(timeInMilliseconds)

                /////////////////////
                if (timeInMilliseconds >= 30000) {
                    _parkingFinished.value = true
                }
            }

            override fun onFinish() {
            }
        }
        timer?.start()
    }

    private fun convertTime(time: Long): String {
        val totalSeconds = time / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
