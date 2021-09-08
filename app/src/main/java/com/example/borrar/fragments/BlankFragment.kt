package com.example.borrar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.borrar.DialogPersonalized
import com.example.borrar.R
import com.example.borrar.databinding.FragmentBlankBinding
import com.example.borrar.databinding.FragmentMapBinding
import com.example.borrar.proxi.RetrofitController
import com.example.borrar.viewModel.ViewModelClass
import org.koin.android.ext.android.inject

class BlankFragment : Fragment() {

    private val viewModel: ViewModelClass by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentBlankBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_blank, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


}