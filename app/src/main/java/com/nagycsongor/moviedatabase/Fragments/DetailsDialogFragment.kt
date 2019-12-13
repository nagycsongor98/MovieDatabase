package com.nagycsongor.moviedatabase.Fragments

import MoviesRespons
import TrailerRespons
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.nagycsongor.moviedatabase.Adapters.PaginationAdapter
import com.nagycsongor.moviedatabase.Adapters.PaginationScrollListener
import com.nagycsongor.moviedatabase.HelpClass.Movies
import com.nagycsongor.moviedatabase.HelpClass.User
import com.nagycsongor.moviedatabase.Interfaces.GetMovieList
import com.nagycsongor.moviedatabase.Main.MainActivity
import com.nagycsongor.moviedatabase.R
import com.nagycsongor.moviedatabase.Retrofit.RetrofitMoviesClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailsDialogFragment(private val movie: Movies, private val sharedPreferences: SharedPreferences?) :
    DialogFragment() {

    private var database: FirebaseDatabase? = null
    private var reference: DatabaseReference? = null

    private var isUploaded: Boolean = false

    var adapter: PaginationAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    private lateinit var movies: ArrayList<Movies>
    var recyclerView: RecyclerView? = null

    private val PAGE_START = 1
    private var mIsLoading = false
    private var mIsLastPage = false
    private var TOTAL_PAGES = 5
    private var mCurrentPage = PAGE_START
    private var key: String = ""
    private lateinit var favorite: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)

        val userId: String? = sharedPreferences?.getString("userId", "")

        database = FirebaseDatabase.getInstance()
        reference = database!!.getReference("users").child(userId.toString()).child("favorite")


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.details_dialog, container, false)
        val close = view.findViewById<ImageButton>(R.id.fullscreen_dialog_close)
        close.setOnClickListener { dismiss() }
        val title = view.findViewById<TextView>(R.id.titleTextView)
        val description = view.findViewById<TextView>(R.id.descriptionTextView)
        description.movementMethod = ScrollingMovementMethod()
        title.text = movie.movieName
        description.text = movie.movieDetail
        favorite = view.findViewById(R.id.favoriteImageButton)
        val ref = reference!!.child(movie.moveId.toString())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val u: Movies? = dataSnapshot.getValue(Movies::class.java)
                if (u != null) {
                    favorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp)
                    isUploaded = true
                } else {
                    favorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
                    isUploaded = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        favorite.setOnClickListener { upload() }

        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getTrailer(movie.moveId)
        dataFlight?.enqueue(object : Callback<TrailerRespons> {
            override fun onFailure(call: Call<TrailerRespons>, t: Throwable) {
                Toast.makeText(context, "This film don't have trailer!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<TrailerRespons>, response: Response<TrailerRespons>) {
                val body = response.body()
                for (element in body!!.results) {
                    key = element.key
                    if (key.isNotEmpty()) {
                        break
                    }
                }
                if (key.isNotEmpty()) {
                    val webView = view.findViewById<View>(R.id.webView) as WebView
                    webView.webViewClient = object : WebViewClient() {}
                    webView.settings.javaScriptEnabled = true
                    webView.loadUrl("https://www.youtube.com/watch?v=" + key)
                } else {
                    Toast.makeText(context, "This film don't have trailer.", Toast.LENGTH_LONG).show()
                }

            }

        })

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )

        adapter = PaginationAdapter(requireContext(), sharedPreferences)

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = linearLayoutManager

        recyclerView!!.itemAnimator = DefaultItemAnimator()

        recyclerView!!.adapter = adapter


        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager!!) {
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

    private fun upload() {
        if (!isUploaded) {
            reference!!.child(movie.moveId.toString()).setValue(movie)
            favorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp)
            isUploaded = true
        } else {
            reference!!.child(movie.moveId.toString()).removeValue()
            favorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
            isUploaded = false
        }


    }

    private fun loadFirstPage() {
        movies = ArrayList()
        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getSimilar(movie.moveId, mCurrentPage)
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
                            element.poster_path,
                            element.release_date
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
        val dataFlight = service?.getSimilar(movie.moveId, mCurrentPage)
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
                            element.poster_path,
                            element.release_date
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