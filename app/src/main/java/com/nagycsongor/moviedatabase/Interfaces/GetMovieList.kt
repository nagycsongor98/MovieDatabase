package com.nagycsongor.moviedatabase.Interfaces

import MoviesRespons
import TrailerRespons
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetMovieList {

    @GET("search/movie?api_key=ca90ad738881eade27d3b39cd98b9d9a")
    fun getAllData(@Query("query") user: String): Call<MoviesRespons>

    @GET("movie/popular?api_key=ca90ad738881eade27d3b39cd98b9d9a")
    fun getAllPopular(@Query("page") page: Int):Call<MoviesRespons>

    @GET("movie/now_playing?api_key=ca90ad738881eade27d3b39cd98b9d9a")
    fun getNowPlaying(@Query("page") page: Int): Call<MoviesRespons>

    @GET("movie/{movie_id}/videos?api_key=ca90ad738881eade27d3b39cd98b9d9a")
    fun getTrailer(@Path("movie_id") id: Int): Call<TrailerRespons>





}