package com.example.aparking

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ParkingFragment : Fragment() {
    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parking, container, false)
    }

    // Обработка взаимодействий пользователя здесь...
    // например, при нажатии на кнопку "построить маршрут" вызываем interaction?.onRouteBuildRequested()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.routeButton).setOnClickListener {
            viewModel.showRoute()
        }
    }
}