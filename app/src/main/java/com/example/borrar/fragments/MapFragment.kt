package com.example.borrar.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.borrar.DialogPersonalized
import com.example.borrar.R
import com.example.borrar.databinding.FragmentMapBinding
import com.example.borrar.proxi.RetrofitController
import com.example.borrar.viewModel.ViewModelClass
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_map.*
import org.json.JSONObject
import org.koin.android.ext.android.inject


class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: ViewModelClass by activityViewModels()
    private val retrofitController: RetrofitController by inject()
    private val dialog: DialogPersonalized by inject()


    private lateinit var mMap: GoogleMap
    private var longitudSelf:Double? = null
    private var latitudSelf:Double? = null
    private var longitudSelected = 0.0
    private var latitudSelected = 0.0
    private var yourPosition: Marker? = null
    private var selectedPosition: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMapBinding=
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.context = requireContext()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ubicacion_propia.setOnClickListener{
            if(latitudSelf!=null && longitudSelf!=null){
                yourPosition?.remove()
                val latlongPosition = LatLng(latitudSelf!!, longitudSelf!!)
                yourPosition =
                    mMap.addMarker(MarkerOptions().position(latlongPosition).title("Estas aquí"))
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latlongPosition, 16f),
                    4000,
                    null
                )
                viewModel.ownCorSelected.value = true
            }else{
                dialog.contenido = "Location not found. Please check in your device configuration the location permissions"
                dialog.showDialog()
            }
        }
        distime.setOnClickListener{
            distanceRequest()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            latitudSelected = it.latitude
            longitudSelected = it.longitude
            selectedPosition?.let { it.remove() }
            val latlongPosition = LatLng(latitudSelected,longitudSelected)
            selectedPosition = mMap.addMarker(MarkerOptions().position(latlongPosition).title("Destino"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlongPosition,16f),4000,null)
            viewModel.destinyCorSelected.value = true
        }

        val locationManager =  requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                latitudSelf = location.latitude
                longitudSelf = location.longitude
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }
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
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0.toLong(),
                0.toFloat(),
                locationListener
            )
        } catch (e: Exception) {
            Log.e("Exception1", "Entro al catch $e")
        }
        try {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0.toLong(),
                0.toFloat(),
                locationListener
            )
        } catch (e: Exception) {
            Log.e("Exception2", "Entro al catch $e")
        }
    }

    private fun distanceRequest(){
        val query = HashMap<String,String>()
        query["origins"] = "$latitudSelf,$longitudSelf"
        query["destinations"] = "$latitudSelected,$longitudSelected"
        query["departure_time"] = "now"
        query["key"] =  viewModel.keyGPS

        charging_response.setAnimation(R.raw.listen)
        charging_response.playAnimation()
        charging_response.visibility=View.VISIBLE

        retrofitController.executeAPI ("maps/api/distancematrix/json",query,{

            if (it.code().toString() == "200") {

                val jsonObj= (JSONObject(it.body().toString())
                    .getJSONArray("rows")[0] as JSONObject).getJSONArray("elements")[0] as JSONObject

                val distancia = jsonObj
                    .getJSONObject("distance")
                    .getString("text")

                val tiempo = jsonObj
                    .getJSONObject("duration")
                    .getString("text")


                viewModel.valCalulado.value!!.distancia = distancia
                viewModel.valCalulado.value!!.tiempo = tiempo

                //try{Navigation.findNavController(distime).navigate(R.id.black_fragment)}
                //catch (e:Exception){}

            } else {
                dialog.contenido = "Unexpected response code of the server, please check the url of the server"
                dialog.showDialog()
            }

            charging_response.visibility=View.GONE
            charging_response.clearAnimation()
        },{
            charging_response.visibility=View.GONE
            charging_response.clearAnimation()
            dialog.contenido = "Missing information response, please check the internet connection"
            dialog.showDialog()

        })
    }

}