package com.example.aparking.parkingChoice

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aparking.MapViewModel
import com.example.aparking.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.aparking.databinding.FragmentChooseParkingSpotBinding
import com.example.aparking.parkingTimer.ParkingActionListener

class ChooseParkingSpotFragment : BottomSheetDialogFragment() {

    private val viewModel: ChooseParkingSpotViewModel by viewModels()
    private val parentViewModel: MapViewModel by activityViewModels()
    private lateinit var binding: FragmentChooseParkingSpotBinding
    // Для создания фрагмента с таймером
    private lateinit var parkingActionListener: ParkingActionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseParkingSpotBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parkingSpotAdapter = ParkingSpotAdapter { spot ->
            viewModel.selectSpot(spot)
            parentViewModel.selectSpot(spot.spotNumber!!)
        }

        binding.apply {
            recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = parkingSpotAdapter

                // set divider
                val dividerItemDecoration =
                    DividerItemDecoration(recyclerView.context, DividerItemDecoration.HORIZONTAL)
                val drawable = ContextCompat.getDrawable(context, R.drawable.divider)
                if (drawable != null) {
                    dividerItemDecoration.setDrawable(drawable)
                    recyclerView.addItemDecoration(dividerItemDecoration)
                }
            }

            viewModel.parkingSpots.observe(viewLifecycleOwner) { spots ->
                parkingSpotAdapter.submitList(spots)
            }

            viewModel.carNumber.observe(viewLifecycleOwner) { number ->
                carNumber.text = number
            }

            viewModel.isConfirmed.observe(viewLifecycleOwner) { isConfirmed ->
                if (isConfirmed) {
                    confirmButton.setImageResource(R.drawable.ic_choosed_item)
                } else {
                    confirmButton.setImageResource(R.drawable.ic_unchoosed_item)
                }
            }

            confirmButton.setOnClickListener {
                viewModel.confirmCar()
                startParkingButton.visibility = View.VISIBLE
            }

            // создание PopupWindow
            val popupView: View = layoutInflater.inflate(R.layout.popup_cars_list, null)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popupWindow = PopupWindow(popupView, width, height, true)

            // настройка ListView
            val listViewCars = popupView.findViewById<ListView>(R.id.listViewCars)
            val carTitles = viewModel.getCarTitles()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, carTitles)
            listViewCars.adapter = adapter

            // обработка выбора элемента в списке
            listViewCars.setOnItemClickListener { parent, _, position, _ ->
                val selectedCarTitle = parent.getItemAtPosition(position).toString()
                val selectedCarNumber = viewModel.getCarNumber(selectedCarTitle)
                binding.carNumber.text = selectedCarNumber
                binding.carTitle.text = selectedCarTitle
                popupWindow.dismiss()
            }

            // обработка нажатия на ImageButton
            binding.dropdownCarsButton.setOnClickListener {
                popupWindow.showAsDropDown(it)
            }

            // обработка нажатия на кнопку начала парковки
            startParkingButton.setOnClickListener {
                viewModel.startParking()
                parkingActionListener.onStartParking()
            }

            viewModel.isParkingStarted.observe(viewLifecycleOwner) { isStarted ->
                if (isStarted) {
                    parkingActionListener.onStartParking()
                    dismiss()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            parkingActionListener = context as ParkingActionListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ParkingActionListener")
        }
    }
}

