package com.nagycsongor.moviedatabase.Fragments

import Json4Kotlin_Base_Movies
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagycsongor.moviedatabase.Adapters.FilmAdapter
import com.nagycsongor.moviedatabase.HelpClass.Movies
import com.nagycsongor.moviedatabase.Interfaces.GetMovieList
import com.nagycsongor.moviedatabase.R
import com.nagycsongor.moviedatabase.Retrofit.RetrofitMoviesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {
    private lateinit var movies : ArrayList<Movies>
    private lateinit var mainRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mainRecyclerView = view.findViewById(R.id.mainRecyclerView) as RecyclerView
        mainRecyclerView.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        mainRecyclerView.layoutManager = LinearLayoutManager(context)
        getPopularMovies()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.menuSearch) as MenuItem
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                movies = ArrayList()

                val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
                val dataFlight = service?.getAllData(query.toString())
                dataFlight?.enqueue(object: Callback<Json4Kotlin_Base_Movies> {

                    override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
                        Toast.makeText(context, "Error parsing json", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<Json4Kotlin_Base_Movies>,
                        response: Response<Json4Kotlin_Base_Movies>
                    ) {
                        val body = response.body()

                        for (element in body!!.results) {
                            movies.add(
                                Movies(
                                    element.title,
                                    element.original_title,
                                    element.overview,
                                    element.poster_path
                                )
                            )
                        }

                        if (movies.size == 0) {
                            Toast.makeText(context, "No data to be shown", Toast.LENGTH_SHORT).show()
                        }
                        mainRecyclerView.adapter =
                            FilmAdapter(
                                movies,
                                requireContext()
                            )


                    }
                })

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()) {
                    movies = ArrayList()

                    val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
                    val dataFlight = service?.getAllData(newText.toString())
                    dataFlight?.enqueue(object : Callback<Json4Kotlin_Base_Movies> {

                        override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
                            Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<Json4Kotlin_Base_Movies>,
                            response: Response<Json4Kotlin_Base_Movies>
                        ) {
                            val body = response.body()

                            for (element in body!!.results) {
                                movies.add(
                                    Movies(
                                        element.title,
                                        element.original_title,
                                        element.overview,
                                        element.poster_path
                                    )
                                )
                            }

                            if (movies.size == 0) {
                                Toast.makeText(context, "No data to be shown", Toast.LENGTH_SHORT).show()
                            }
                            mainRecyclerView.adapter =
                                FilmAdapter(
                                    movies,
                                    requireContext()
                                )


                        }
                    })

                }else{
                    getPopularMovies()
//                    movies = ArrayList()
//                    mainRecyclerView.adapter =
//                        FilmAdapter(movies, requireContext())
                }
                return true
            }

        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
//                movies = ArrayList()
//                mainRecyclerView.adapter =
//                    FilmAdapter(movies, requireContext())
                getPopularMovies()
                return true
            }

        })
    }

    private fun getPopularMovies(){
        movies = ArrayList()

        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getAllPopular()
        dataFlight?.enqueue(object : Callback<Json4Kotlin_Base_Movies> {
            override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
                Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Json4Kotlin_Base_Movies>, response: Response<Json4Kotlin_Base_Movies>) {
                val body = response.body()

                for (element in body!!.results) {
                    movies.add(
                        Movies(
                            element.title,
                            element.original_title,
                            element.overview,
                            element.poster_path
                        )
                    )
                }

                if (movies.size == 0) {
                    Toast.makeText(context, "No data to be shown", Toast.LENGTH_SHORT).show()
                }
                mainRecyclerView.adapter =
                    FilmAdapter(
                        movies,
                        requireContext()
                    )
            }

        })
    }


}
