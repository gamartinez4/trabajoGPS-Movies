package com.example.borrar


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.borrar.databinding.ItemLayoutBinding
import com.example.borrar.fragments.MoviesFragment
import com.example.borrar.viewModel.ViewModelClass


class AdaptadorLista(
    private var viewModel: ViewModelClass,
    private val fragmento:MoviesFragment
    ) : RecyclerView.Adapter<AdaptadorLista.ViewHolderList>() {

    inner class ViewHolderList(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.film_details_fragment)
                viewModel.selectedFilm.value = binding.film
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolderList{
        return ViewHolderList(ItemLayoutBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
        holder.binding.film = viewModel.listFilms.value!![position]
        }

    override fun getItemCount() = viewModel.listFilms.value!!.size
}


