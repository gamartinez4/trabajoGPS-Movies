package com.example.borrar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.borrar.Animations
import com.example.borrar.R

import com.example.borrar.databinding.FragmentFilmDetailsBinding
import com.example.borrar.viewModel.ViewModelClass
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_film_details.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val realm = Realm.getInstance(
            RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build())
        text_favorites.setOnClickListener {
            viewModel.selectedFilm.value!!.let{
                realm?.executeTransaction { r ->
                    it.isFavourite = !it.isFavourite
                    r.insertOrUpdate(it)
                }
            }
            Navigation.findNavController(it).navigate(R.id.movies_fragment,null, Animations.options_slide_in)
        }
    }
}