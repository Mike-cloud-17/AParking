package com.example.aparking

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.example.aparking.databinding.FragmentPointBinding
import com.example.aparking.parkingChoice.ChooseParkingSpotFragment
import com.example.aparking.parkingTimer.ParkingTimerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yandex.mapkit.geometry.Point

class PointBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPointBinding
    private val parentViewModel: MapViewModel by activityViewModels()
    lateinit var behavior: BottomSheetBehavior<FrameLayout>
    lateinit var location: Point
    var spotName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPointBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        behavior = (dialog as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.getSpotLiveData().observe(viewLifecycleOwner, this::setPointData)
        binding.routeButton.setOnClickListener {
            parentViewModel.showRoute(location)
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.parkingButton.setOnClickListener {
            parentViewModel.selectSpot(spotName)
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.parking_fragment_container, ParkingTimerFragment())
                .commit()
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setPointData(point: ParkingSpot) {
        location = Point(point.latitude!!, point.longitude!!)
        spotName = point.spotNumber!!
        binding.address.text = point.address
        binding.condition.text = if (point.isOccupied) "Занято" else "Свободно"
        binding.distance.text = getString(R.string.distance, point.distanceToSpot)

    }
}