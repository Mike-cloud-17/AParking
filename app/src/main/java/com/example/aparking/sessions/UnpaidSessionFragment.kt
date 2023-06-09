package com.example.aparking.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aparking.R
import com.example.aparking.databinding.FragmentSessionUnpaidBinding

class UnpaidSessionFragment : Fragment() {
    private lateinit var binding: FragmentSessionUnpaidBinding
    private lateinit var adapter: UnpaidSessionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionUnpaidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.header.text = getString(R.string.number_unpaid_header, 2)

        adapter = UnpaidSessionAdapter(SessionsRepository().getSessions().take(4).toMutableList())
        binding.recycler.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.divider_space)!!)
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(dividerItemDecoration)
    }
}