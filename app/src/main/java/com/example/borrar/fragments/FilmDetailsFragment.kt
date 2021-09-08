package com.example.borrar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.borrar.R
import com.example.borrar.databinding.FragmentBlankBinding
import com.example.borrar.databinding.FragmentFilmDetailsBinding
import com.example.borrar.viewModel.ViewModelClass
import kotlinx.android.synthetic.main.fragment_movies.*


class FilmDetailsFragment : Fragment() {

    private val viewModel: ViewModelClass by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFilmDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_film_details, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}