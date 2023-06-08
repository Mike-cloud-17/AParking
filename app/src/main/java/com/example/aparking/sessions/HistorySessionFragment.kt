package com.example.aparking.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.aparking.R
import com.example.aparking.databinding.FragmentSessionHistoryBinding

class HistorySessionFragment : Fragment() {
    private lateinit var binding: FragmentSessionHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menu.setOnClickListener {
            showMenu()
        }
    }

    private fun showMenu() {
        val popup = PopupMenu(context, binding.menu)
        popup.menuInflater.inflate(R.menu.history_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            true
        }
        popup.show()
    }
}