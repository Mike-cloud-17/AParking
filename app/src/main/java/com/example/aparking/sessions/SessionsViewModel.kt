package com.example.aparking.sessions

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class SessionsViewModel : ViewModel() {
    private val _timeString = MutableLiveData("00:00:00")
    private val _spotNumber = MutableLiveData("")
    val timeString: LiveData<String> = _timeString
    val spotNumber: LiveData<String> = _spotNumber
    private var timer: CountDownTimer? = null

    fun setSpotNumber(number: String) {
        _spotNumber.postValue(number)
    }

    fun startTimer(time: Long) {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            var timeInMilliseconds = time
            override fun onTick(millisUntilFinished: Long) {
                timeInMilliseconds += 1000
                _timeString.postValue(convertTime(timeInMilliseconds))
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