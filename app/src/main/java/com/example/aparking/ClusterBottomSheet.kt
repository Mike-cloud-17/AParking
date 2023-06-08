package com.example.aparking

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.example.aparking.databinding.FragmentClusterBinding
import com.example.aparking.parkingChoice.ChooseParkingSpotFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yandex.mapkit.geometry.Point

class ClusterBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentClusterBinding
    private val parentViewModel: MapViewModel by activityViewModels()
    lateinit var behavior: BottomSheetBehavior<FrameLayout>
    lateinit var location: Point

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClusterBinding.inflate(inflater, container, false)
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
        parentViewModel.getLoadLiveData().observe(viewLifecycleOwner, this::setLoad)
        binding.routeButton.setOnClickListener {
            parentViewModel.showRoute(location)
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.parkingButton.setOnClickListener {
            val chooseParkingSpotFragment = ChooseParkingSpotFragment()
            chooseParkingSpotFragment.show(parentFragmentManager, "ChooseParkingSpot")
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setPointData(point: ParkingSpot) {
        location = Point(point.latitude!!, point.longitude!!)
        binding.address.text = point.address
        binding.distance.text = getString(R.string.distance, point.distanceToSpot)
    }

    private fun setLoad(load: Pair<Int, Int>) {
        binding.occupied.text = load.first.toString()
        binding.free.text = load.second.toString()
        binding.loadProgress.progress = (load.first / (load.first.toDouble() + load.second) * 100).toInt()
    }
}