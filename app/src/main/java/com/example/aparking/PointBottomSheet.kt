package com.example.aparking

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.example.aparking.databinding.FragmentPointBinding
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
    var buttonAvailable = true

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
            if (buttonAvailable) {
                parentViewModel.selectSpot(spotName)
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.parking_fragment_container, ParkingTimerFragment())
                    .commit()
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun setPointData(point: ParkingSpot) {
        location = Point(point.latitude!!, point.longitude!!)
        spotName = point.spotNumber!!
        binding.address.text = point.address
        buttonAvailable = point.isOccupied
        binding.condition.text = if (point.isOccupied) "Занято" else "Свободно"
        binding.distance.text = getString(R.string.distance, point.distanceToSpot)
        binding.buttonText.text = if (point.isOccupied) "Начинаю парковаться" else "Парковка недоступна"
        binding.parkingButton.setBackgroundResource(
            if (point.isOccupied) R.drawable.holder_rounded_orange else R.drawable.holder_rounded_gray)

        // Обновляем метку адреса, делая номер парковки жирным
        val labelText = getString(R.string.parking_address)
        val formattedLabelText = getFormattedLabelText(labelText, spotName)
        binding.addressLabel.text = formattedLabelText
    }

    private fun getFormattedLabelText(labelText: String, spotNumber: String): SpannableString {
        val fullText = "$labelText \"$spotNumber\""

        // Создаем SpannableString и устанавливаем жирный стиль для номера парковки
        val spannableString = SpannableString(fullText)
        val start = fullText.indexOf(spotNumber)
        val end = start + spotNumber.length
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }
}