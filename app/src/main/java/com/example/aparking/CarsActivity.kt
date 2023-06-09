package com.example.aparking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aparking.databinding.ActivityCarsBinding

class CarsActivity : AppCompatActivity() {
    val cars = mutableListOf(
        Car("Nissan 1", "776 NNS 02"),
        Car("Nissan 2", "777 KNS 02"),
        Car("Nissan 3", "778 KSS 02"),
        Car("Nissan 4", "779 KSS 02"),
    )
    private lateinit var adapter: CarsAdapter
    private lateinit var binding: ActivityCarsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Прячем верхнюю панель (ActionBar)
        supportActionBar?.hide()

        binding = ActivityCarsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.imageButton.setOnClickListener { finish() }

        adapter = CarsAdapter(cars)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this, R.drawable.divider_space)!!)
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(dividerItemDecoration)
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeCar(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }
}