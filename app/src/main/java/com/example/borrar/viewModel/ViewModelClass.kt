package com.example.borrar.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.borrar.models.FilmsModel
import com.example.borrar.models.ResponseModel
//import java.util.ArrayList


class ViewModelClass : ViewModel(){
    val valCalulado = MutableLiveData(ResponseModel())
    val ownCorSelected = MutableLiveData(false)
    val destinyCorSelected = MutableLiveData(false)
    val listFilms = MutableLiveData(ArrayList<FilmsModel>())
    val selectedFilm = MutableLiveData(FilmsModel())

    var keyGPS = "XznjxOlb9DatxnbYUemGJWQsFl3R6"
    var keyMovies =  "d113156ac4758822fee6aeab5867ca30"

}