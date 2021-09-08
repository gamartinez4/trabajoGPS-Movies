package com.example.borrar

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.borrar.models.FilmsModel
import com.example.borrar.models.ResponseModel

@BindingAdapter("android:loadImage")
fun imageURLLoad(imageView: ImageView,url:String){
    Glide.with(imageView).load(url).into(imageView)
}

