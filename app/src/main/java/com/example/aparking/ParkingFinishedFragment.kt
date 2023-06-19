package com.example.aparking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aparking.databinding.FragmentFinishedSessionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FinishedSessionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFinishedSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
}
