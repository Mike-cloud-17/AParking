package com.example.aparking

import android.content.Context
import android.graphics.Color
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

    fun setColor(color: Int) {
        when (color) {
            Color.GREEN -> binding.root.setBackgroundResource(R.drawable.cluster_circle_green)
            Color.YELLOW -> binding.root.setBackgroundResource(R.drawable.cluster_circle_yellow)
            Color.RED -> binding.root.setBackgroundResource(R.drawable.cluster_circle_red)
        }
    }
}