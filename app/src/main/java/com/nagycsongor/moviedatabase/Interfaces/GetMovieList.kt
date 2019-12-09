package com.nagycsongor.moviedatabase.Interfaces

import Json4Kotlin_Base_Movies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetMovieList {

    @GET("search/movie?api_key=ca90ad738881eade27d3b39cd98b9d9a")

    fun getAllData(@Query("query") user: String): Call<Json4Kotlin_Base_Movies>

}