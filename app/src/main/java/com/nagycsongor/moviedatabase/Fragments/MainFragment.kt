package com.nagycsongor.moviedatabase.Fragments

import MoviesRespons
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagycsongor.moviedatabase.Adapters.FilmAdapter
import com.nagycsongor.moviedatabase.Adapters.PaginationAdapter
import com.nagycsongor.moviedatabase.Adapters.PaginationScrollListener
import com.nagycsongor.moviedatabase.HelpClass.Movies
import com.nagycsongor.moviedatabase.Interfaces.GetMovieList
import com.nagycsongor.moviedatabase.R
import com.nagycsongor.moviedatabase.Retrofit.RetrofitMoviesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment(private val sharedPreferences: SharedPreferences?) : Fragment() {

    var adapter: PaginationAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private val PAGE_START = 1
    private var mIsLoading = false
    private var mIsLastPage = false
    private var TOTAL_PAGES = 20
    private var mCurrentPage = PAGE_START


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
                mainRecyclerView.layoutManager = LinearLayoutManager(context)
                movies = ArrayList()

                val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
                val dataFlight = service?.getAllData(query.toString())
                dataFlight?.enqueue(object: Callback<MoviesRespons> {

                    override fun onFailure(call: Call<MoviesRespons>, t: Throwable) {
                        Toast.makeText(context, "Error parsing json", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<MoviesRespons>,
                        response: Response<MoviesRespons>
                    ) {
                        val body = response.body()

                        for (element in body!!.results) {
                            movies.add(
                                Movies(
                                    element.id,
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
                                requireContext(),sharedPreferences
                            )


                    }
                })

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()) {
                    mainRecyclerView.layoutManager = LinearLayoutManager(context)
                    movies = ArrayList()

                    val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
                    val dataFlight = service?.getAllData(newText.toString())
                    dataFlight?.enqueue(object : Callback<MoviesRespons> {

                        override fun onFailure(call: Call<MoviesRespons>, t: Throwable) {
                            Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<MoviesRespons>,
                            response: Response<MoviesRespons>
                        ) {
                            val body = response.body()

                            for (element in body!!.results) {
                                movies.add(
                                    Movies(
                                        element.id,
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
                                    requireContext(),sharedPreferences
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
//        movies = ArrayList()
//
//        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
//        val dataFlight = service?.getAllPopular()
//        dataFlight?.enqueue(object : Callback<Json4Kotlin_Base_Movies> {
//            override fun onFailure(call: Call<Json4Kotlin_Base_Movies>, t: Throwable) {
//                Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onResponse(call: Call<Json4Kotlin_Base_Movies>, response: Response<Json4Kotlin_Base_Movies>) {
//                val body = response.body()
//
//                for (element in body!!.results) {
//                    movies.add(
//                        Movies(
//                            element.title,
//                            element.original_title,
//                            element.overview,
//                            element.poster_path
//                        )
//                    )
//                }
//
//                if (movies.size == 0) {
//                    Toast.makeText(context, "No data to be shown", Toast.LENGTH_SHORT).show()
//                }
//                mainRecyclerView.adapter =
//                    FilmAdapter(
//                        movies,
//                        requireContext()
//                    )
//            }
//
//        })

        mIsLoading = false
        mIsLastPage = false
        mCurrentPage = PAGE_START

        adapter = PaginationAdapter(requireContext(),sharedPreferences)

        linearLayoutManager = LinearLayoutManager(context)
        mainRecyclerView!!.layoutManager = linearLayoutManager

        mainRecyclerView!!.itemAnimator = DefaultItemAnimator()

        mainRecyclerView!!.adapter = adapter


        mainRecyclerView!!.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager!!){
            override fun loadMoreItems() {
                mIsLoading = true
                mCurrentPage += 1

                Handler().postDelayed(Runnable { loadNextPage() }, 1000)
            }

            override val totalPageCount: Int
                get() = TOTAL_PAGES
            override val isLastPage: Boolean
                get() = mIsLastPage
            override val isLoading: Boolean
                get() = mIsLoading

        })

        loadFirstPage()

    }

    private fun loadFirstPage() {
        movies = ArrayList()
        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getAllPopular(mCurrentPage)
        dataFlight?.enqueue(object : Callback<MoviesRespons> {
            override fun onFailure(call: Call<MoviesRespons>, t: Throwable) {
                Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MoviesRespons>, response: Response<MoviesRespons>) {
                val body = response.body()

                for (element in body!!.results) {
                    movies.add(
                        Movies(
                            element.id,
                            element.title,
                            element.original_title,
                            element.overview,
                            element.poster_path
                        )
                    )
                }

                adapter!!.addAll(movies)
                if (mCurrentPage <= TOTAL_PAGES) adapter!!.addLoadingFooter() else mIsLastPage = true
            }

        })

    }

    fun loadNextPage() {
        movies = ArrayList()
        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getAllPopular(mCurrentPage)
        dataFlight?.enqueue(object : Callback<MoviesRespons> {
            override fun onFailure(call: Call<MoviesRespons>, t: Throwable) {
                Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MoviesRespons>, response: Response<MoviesRespons>) {
                val body = response.body()

                for (element in body!!.results) {
                    movies.add(
                        Movies(
                            element.id,
                            element.title,
                            element.original_title,
                            element.overview,
                            element.poster_path
                        )
                    )
                }

                adapter!!.removeLoadingFooter()
                mIsLoading = false
                adapter!!.addAll(movies)

                if (mCurrentPage != TOTAL_PAGES) adapter!!.addLoadingFooter() else mIsLastPage = true
            }

        })
    }


}
