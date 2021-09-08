package com.example.borrar.proxi
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiRetrofit {

    @GET("{path}")
    fun getAllPost(
        @Path("path") stringOfPath:String,
        @QueryMap queries: Map<String,String>
    ): Observable<Response<String>>

    @GET("{path}")
    fun getAllMovies(
        @Path("path") stringOfPath:String,
        @QueryMap queries: Map<String,String>
    ): Observable<Response<String>>


}