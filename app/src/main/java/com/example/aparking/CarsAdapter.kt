package com.example.aparking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class CarsAdapter(
    private var cars: MutableList<Car>
) : Adapter<CarViewHolder>() {
    private var expandedPosition = -1
    var pickedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CarViewHolder(inflater.inflate(R.layout.holder_car, parent, false))
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
        holder.collapsedName.visibility = if (position == expandedPosition) View.GONE else View.VISIBLE
        holder.collapsedNumber.visibility = if (position == expandedPosition) View.GONE else View.VISIBLE
        holder.expandedName.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.expandedNumber.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.isSelected.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.carImage.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.nameHolder.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.numberHolder.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.height.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.isSelected.text = if (position == pickedPosition) "Сейчас используется" else "Поеду на нем"
        holder.selectButton.setImageResource(
            if (position == pickedPosition)
                R.drawable.ic_choosed_item
            else
                R.drawable.ic_unchoosed_item
        )

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition == expandedPosition) {
                expandedPosition = -1
                notifyItemChanged(holder.adapterPosition)
            } else {
                val previousPosition = expandedPosition
                expandedPosition = holder.adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(holder.adapterPosition)
            }
        }
        holder.selectButton.setOnClickListener {
            if (holder.adapterPosition == pickedPosition) {
                pickedPosition = -1
                notifyItemChanged(holder.adapterPosition)
            } else {
                val previousPosition = pickedPosition
                pickedPosition = holder.adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(holder.adapterPosition)
            }
        }
    }
}

class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val collapsedName: TextView = itemView.findViewById(R.id.car_name_collapsed)
    val collapsedNumber: TextView = itemView.findViewById(R.id.car_number_collapsed)
    val expandedName: TextView = itemView.findViewById(R.id.car_name_expanded)
    val expandedNumber: TextView = itemView.findViewById(R.id.car_number_expanded)
    val selectButton: ImageButton = itemView.findViewById(R.id.selectSpot)
    val isSelected: TextView = itemView.findViewById(R.id.is_selected_text)
    val carImage: ImageView = itemView.findViewById(R.id.imageView)
    val nameHolder: LinearLayout = itemView.findViewById(R.id.name_holder)
    val numberHolder: LinearLayout = itemView.findViewById(R.id.number_holder)
    val height: LinearLayout = itemView.findViewById(R.id.add_height)

    fun bind(car: Car) {
        collapsedName.text = car.carTitle
        collapsedNumber.text = car.carNumber
        expandedName.text = car.carTitle
        expandedNumber.text = car.carNumber
    }
}