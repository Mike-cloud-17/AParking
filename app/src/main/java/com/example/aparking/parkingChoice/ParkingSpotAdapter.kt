package com.example.aparking.parkingChoice

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aparking.ParkingSpot
import com.example.aparking.R
import com.example.aparking.databinding.ItemParkingSpotBinding

class ParkingSpotAdapter(private val onClick: (ParkingSpot) -> Unit) :
    ListAdapter<ParkingSpot, ParkingSpotAdapter.ParkingSpotViewHolder>(ParkingSpotComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingSpotViewHolder {
        val binding =
            ItemParkingSpotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkingSpotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkingSpotViewHolder, position: Int) {
        val currentSpot = getItem(position)
        holder.bind(currentSpot)
    }

    inner class ParkingSpotViewHolder(private val binding: ItemParkingSpotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun dpToPx(dp: Int): Int {
            val density = Resources.getSystem().displayMetrics.density
            return (dp * density).toInt()
        }

        fun bind(spot: ParkingSpot) {
            binding.apply {
                spotLabel.text = spot.spotNumber
                // Устанавливаем параметры в зависимости от выбора места
                val params = binding.background.layoutParams
                if (spot.isSelected) {
                    selectSpot.setImageResource(R.drawable.ic_choosed_item)
                    // Увеличить высоту фона
                    params.height = dpToPx(109)
                    // Устанавливаем текст
                    spotLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                } else {
                    selectSpot.setImageResource(R.drawable.ic_unchoosed_item)
                    // Вернуть высоту фона к первоначальным параметрам
                    params.height = dpToPx(93)
                    // Устанавливаем текст в желтый цвет
                    spotLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.yellow))
                }
                binding.background.layoutParams = params
                selectSpot.setOnClickListener {
                    onClick(spot)
                }
            }
        }
    }

    class ParkingSpotComparator : DiffUtil.ItemCallback<ParkingSpot>() {
        override fun areItemsTheSame(oldItem: ParkingSpot, newItem: ParkingSpot) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ParkingSpot, newItem: ParkingSpot) =
            oldItem == newItem
    }
}

