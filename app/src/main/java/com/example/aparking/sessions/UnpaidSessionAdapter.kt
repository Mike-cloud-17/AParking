package com.example.aparking.sessions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.aparking.R

class UnpaidSessionAdapter(
    private val sessions: MutableList<Session>
) : Adapter<UnpaidSessionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnpaidSessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UnpaidSessionViewHolder(inflater.inflate(R.layout.holder_session_unpaid, parent, false))
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(holder: UnpaidSessionViewHolder, position: Int) {
        holder.bind(sessions[position])
    }
}

class UnpaidSessionViewHolder(itemView: View) : ViewHolder(itemView) {
    val date: TextView = itemView.findViewById(R.id.session_date)
    val timePassed: TextView = itemView.findViewById(R.id.timePassed)
    val timePeriod: TextView = itemView.findViewById(R.id.timePeriod)
    val address: TextView = itemView.findViewById(R.id.address)
    val placeCode: TextView = itemView.findViewById(R.id.place_code)
    val brand: TextView = itemView.findViewById(R.id.car_brand)
    val carCode: TextView = itemView.findViewById(R.id.car_code)
    val price: TextView = itemView.findViewById(R.id.price)
    val button: Button = itemView.findViewById(R.id.pay_button)

    fun bind(session: Session) {
        date.text = session.date
        timePassed.text = session.duration
        timePeriod.text = session.interval
        address.text = session.address
        placeCode.text = session.spotNumber
        brand.text = session.carTitle
        carCode.text = session.carNumber
        price.text = session.debt.toString()
    }
}