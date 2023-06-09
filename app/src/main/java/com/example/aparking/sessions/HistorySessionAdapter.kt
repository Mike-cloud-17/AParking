package com.example.aparking.sessions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.aparking.R

class HistorySessionAdapter(
    private val sessions: MutableList<Session>
) : Adapter<HistorySessionViewHolder>() {
    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorySessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HistorySessionViewHolder(inflater.inflate(R.layout.holder_session_parking, parent, false))
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(holder: HistorySessionViewHolder, position: Int) {
        holder.bind(sessions[position], position == expandedPosition)

        holder.extraInfo.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
        holder.arrow.setOnClickListener {
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
    }

    fun clearData() {
        val number = sessions.size
        sessions.clear()
        notifyItemRangeRemoved(0, number)
    }
}

class HistorySessionViewHolder(itemView: View) : ViewHolder(itemView) {
    val date: TextView = itemView.findViewById(R.id.session_date)
    val timePassed: TextView = itemView.findViewById(R.id.timePassed)
    val timePeriod: TextView = itemView.findViewById(R.id.timePeriod)
    val address: TextView = itemView.findViewById(R.id.address)
    val placeCode: TextView = itemView.findViewById(R.id.place_code)
    val brand: TextView = itemView.findViewById(R.id.car_brand)
    val carCode: TextView = itemView.findViewById(R.id.car_code)
    val price: TextView = itemView.findViewById(R.id.price)
    val arrow: ImageView = itemView.findViewById(R.id.arrow)
    val extraInfo: LinearLayout = itemView.findViewById(R.id.extra_info)

    fun bind(session: Session, isExpanded: Boolean) {
        date.text = session.date
        timePassed.text = session.duration
        timePeriod.text = session.interval
        address.text = session.address
        placeCode.text = session.spotNumber
        brand.text = session.carTitle
        carCode.text = session.carNumber
        price.text = session.debt.toString()

        arrow.setImageResource(if (isExpanded) R.drawable.ic_arrow_up_24 else R.drawable.ic_arrow_down_24)
    }
}