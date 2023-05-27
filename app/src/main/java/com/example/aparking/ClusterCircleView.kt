package com.example.aparking

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aparking.databinding.ClusterCircleBinding

class ClusterCircleView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private val binding: ClusterCircleBinding =
        ClusterCircleBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
    }

    fun setText(text: String) {
        binding.number.text = text
    }

    fun setColor(value: Int) {
        when (value) {
            0 -> binding.root.setBackgroundResource(R.drawable.cluster_circle_green)
            1 -> binding.root.setBackgroundResource(R.drawable.cluster_circle_yellow)
            2 -> binding.root.setBackgroundResource(R.drawable.cluster_circle_red)
        }
    }
}