package com.example.aparking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ParkingFragment : Fragment() {

    private var interaction: MapActivityInteraction? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MapActivityInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement MapActivityInteraction")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parking, container, false)
    }

    // Обработка взаимодействий пользователя здесь...
    // например, при нажатии на кнопку "построить маршрут" вызываем interaction?.onRouteBuildRequested()
}