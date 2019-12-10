package com.nagycsongor.moviedatabase.Fragments

import Json4Kotlin_Base_Movies
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagycsongor.moviedatabase.Adapters.PaginationAdapter
import com.nagycsongor.moviedatabase.Adapters.PaginationScrollListener
import com.nagycsongor.moviedatabase.HelpClass.Movies
import com.nagycsongor.moviedatabase.Interfaces.GetMovieList
import com.nagycsongor.moviedatabase.R
import com.nagycsongor.moviedatabase.Retrofit.RetrofitMoviesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NowPlayingFragment : Fragment() {

    var adapter: PaginationAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    private lateinit var movies : ArrayList<Movies>
    var rv: RecyclerView? = null

    private val PAGE_START = 1
    private var mIsLoading = false
    private var mIsLastPage = false
    private var TOTAL_PAGES = 20
    private var mCurrentPage = PAGE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)

        rv = view.findViewById(R.id.nowPlayingRecyclerView) as RecyclerView

        adapter = PaginationAdapter(requireContext())

        linearLayoutManager = LinearLayoutManager(context)
        rv!!.layoutManager = linearLayoutManager

        rv!!.itemAnimator = DefaultItemAnimator()

        rv!!.adapter = adapter


        rv!!.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager!!){
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

        return view
    }

    private fun loadFirstPage() {
        movies = ArrayList()
        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getNowPlaying(mCurrentPage)
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

                adapter!!.addAll(movies)
                if (mCurrentPage <= TOTAL_PAGES) adapter!!.addLoadingFooter() else mIsLastPage = true
            }

        })

    }

    fun loadNextPage(){
        movies = ArrayList()
        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getNowPlaying(mCurrentPage)
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

                adapter!!.removeLoadingFooter()
                mIsLoading = false
                adapter!!.addAll(movies)

                if (mCurrentPage != TOTAL_PAGES) adapter!!.addLoadingFooter() else mIsLastPage = true
            }

        })
    }
}
