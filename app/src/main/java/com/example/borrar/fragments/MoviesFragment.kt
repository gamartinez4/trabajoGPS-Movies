package com.example.borrar.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.borrar.AdaptadorLista
import com.example.borrar.DialogPersonalized
import com.example.borrar.R
import com.example.borrar.databinding.FragmentMoviesBinding
import com.example.borrar.models.FilmsModel
import com.example.borrar.proxi.RetrofitController
import com.example.borrar.viewModel.ViewModelClass
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_movies.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class MoviesFragment : Fragment() {

    private val viewModel: ViewModelClass by activityViewModels()
    private val retrofitController: RetrofitController by inject()
    private val dialog: DialogPersonalized by inject()
    private var realm: Realm? = null
    private var favouriteFilterActive = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMoviesBinding=
        DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        binding.viewModel = viewModel
        binding.activeFavourite = favouriteFilterActive
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        dialog.context = requireContext()
        charging_movies.visibility = View.GONE
        realm = Realm.getInstance(
            RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build())
        if(realm!!.isEmpty)moviesRequest()
        else{
            viewModel.listFilms.value = realm!!.where<FilmsModel>().findAll().toList() as ArrayList<FilmsModel>
            try {
                viewModel.listFilms.value =
                    (viewModel.listFilms.value!!.sortedBy { it.popularity.toDouble() }) as ArrayList<FilmsModel>
            }catch(e:Exception){ }
            recycler.adapter = AdaptadorLista(viewModel,viewModel.listFilms.value!!)
        }
        refresh_card.setOnClickListener {
            realm?.executeTransaction{it.where<FilmsModel>().findAll().deleteAllFromRealm()}
            viewModel.listFilms.value = ArrayList()
            moviesRequest()
        }

        filter_favourites_card.setOnClickListener {
            favouriteFilterActive = !favouriteFilterActive
           recycler.adapter = AdaptadorLista(
               viewModel,
               if(favouriteFilterActive)
                   (viewModel.listFilms.value!!.filter { it.isFavourite }) as ArrayList<FilmsModel> else viewModel.listFilms.value!!)
        }
    }

    private fun moviesRequest(){
        val query = HashMap<String,String>()
        query["api_key"] = viewModel.keyMovies

        charging_movies.setAnimation(R.raw.listen)
        charging_movies.playAnimation()
        charging_movies.visibility = View.VISIBLE

        retrofitController.executeAPIMovies("popular",query,{
            if (it.code().toString() == "200") {
                val jsonArray= JSONObject(it.body().toString()).getJSONArray("results")
                for (i in 0 until jsonArray.length()-1){
                    val filmModel = FilmsModel()
                    val jsonObj = jsonArray[i] as JSONObject
                    Log.e("respuset",jsonObj.toString())

                    filmModel.id = jsonObj.getInt("id")
                    filmModel.description = jsonObj.getString("overview")
                    filmModel.realeseDate = jsonObj.getString("release_date")
                    filmModel.title = jsonObj.getString("title")
                    filmModel.imgUrl = "https://image.tmdb.org/t/p/original${jsonObj.getString("backdrop_path")}"
                    filmModel.popularity = jsonObj.getString("popularity")
                    Log.e("pop","filmModel.popula2rity")
                    filmModel.vote = jsonObj.getString("vote_average")
                    Log.e("pop","filmModel.popularityl")
                    viewModel.listFilms.value!!.add(filmModel)
                }
//
                try {
                    viewModel.listFilms.value =
                        (viewModel.listFilms.value!!.sortedBy { it.popularity.toDouble() }) as ArrayList<FilmsModel>
                }catch (e:Exception){
                    e.printStackTrace()
                }

                realm?.executeTransaction { it.insert(viewModel.listFilms.value)}
                recycler.adapter = AdaptadorLista(viewModel,viewModel.listFilms.value!!)
            } else {
                dialog.contenido = "Hay un problema con la url, se esta retornando un codigo de respuesta al servicio incorrecto de origen desconocido"
                dialog.showDialog()
            }
            charging_movies.visibility=View.GONE
            charging_movies.clearAnimation()
        },{
            charging_movies.visibility=View.GONE
            charging_movies.clearAnimation()
            dialog.contenido = "La información no pudo ser recibida satisfactoriamente, revise su conexion a internet"
            dialog.showDialog()
        })


    }
}