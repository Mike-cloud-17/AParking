package com.example.aparking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aparking.databinding.FragmentSessionUnpaidBinding

class UnpaidSessionFragment : Fragment() {
    private lateinit var binding: FragmentSessionUnpaidBinding

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
    }
}