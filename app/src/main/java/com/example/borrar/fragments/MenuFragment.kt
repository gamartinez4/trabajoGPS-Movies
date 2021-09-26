package com.example.borrar.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.borrar.Animations
import com.example.borrar.R
import com.example.borrar.viewModel.ViewModelClass
import com.google.firebase.firestore.FirebaseFirestore
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : Fragment() {

    private val viewModel: ViewModelClass by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            var db = FirebaseFirestore.getInstance()
            db.collection("ApiKeys").document("1").get().addOnSuccessListener {
                if (it != null) {
                    viewModel.keyGPS = it.get("gps").toString()
                    viewModel.keyMovies = it.get("movies").toString()
                }
            }
        }catch(e:Exception){}
        val permissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) ; else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
        Realm.init(context)
        peliculas.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.movies_fragment,null, Animations.options_slide_in)
        }
        gps.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.map_fragment,null, Animations.options_slide_in)
        }
    }

}