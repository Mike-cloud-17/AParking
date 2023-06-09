package com.example.aparking.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aparking.R
import com.example.aparking.databinding.FragmentSessionHistoryBinding

class HistorySessionFragment : Fragment() {
    private lateinit var binding: FragmentSessionHistoryBinding
    private lateinit var adapter: HistorySessionAdapter

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

        adapter = HistorySessionAdapter(SessionsRepository().getSessions().takeLast(4).toMutableList())
        binding.recycler.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider_space)!!)
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(dividerItemDecoration)
    }

    private fun showMenu() {
        val popup = PopupMenu(context, binding.menu)
        popup.menuInflater.inflate(R.menu.history_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            adapter.clearData()
            true
        }
        popup.show()
    }
}