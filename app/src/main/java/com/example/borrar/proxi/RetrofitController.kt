package com.example.borrar.proxi


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.collections.HashMap

class RetrofitController(private val retrofitStrings: RetrofitStrings){

    fun executeAPI(
        stringOfPath: String,
        mapOfQuery: HashMap<String,String>,
        goodFunction: (response:Response<String>) -> Any,
        badFunction: () -> Any
    ): @NonNull Disposable? {
       return Retrofit.Builder()
            .baseUrl(retrofitStrings.webApiURL())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(ApiRetrofit::class.java).getAllPost(stringOfPath,mapOfQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        goodFunction(it)
                    },
                    {
                        badFunction()
                    }
                )
    }

    fun executeAPIMovies(
        stringOfPath: String,
        mapOfQuery: HashMap<String,String>,
        goodFunction: (response:Response<String>) -> Any,
        badFunction: () -> Any
    ): @NonNull Disposable? {
        return Retrofit.Builder()
            .baseUrl(retrofitStrings.webApiURLFilm())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(ApiRetrofit::class.java).getAllPost(stringOfPath,mapOfQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    goodFunction(it)
                },
                {
                    badFunction()
                }
            )
    }

}