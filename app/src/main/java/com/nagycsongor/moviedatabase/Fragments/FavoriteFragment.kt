package com.nagycsongor.moviedatabase.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nagycsongor.moviedatabase.Adapters.FilmAdapter
import com.nagycsongor.moviedatabase.HelpClass.Movies
import com.nagycsongor.moviedatabase.R
import kotlinx.android.synthetic.main.details_dialog.*

class FavoriteFragment(private val sharedPreferences: SharedPreferences?) : Fragment() {
    private var database: FirebaseDatabase? = null
    private lateinit var movies: ArrayList<Movies>
    private lateinit var mainRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        mainRecyclerView = view.findViewById(R.id.favoriteRecyclerView) as RecyclerView
        mainRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        mainRecyclerView.layoutManager = LinearLayoutManager(context)
        movies = ArrayList()
        ////
        return view
    }

    override fun onStart() {
        super.onStart()
        val userId: String? = sharedPreferences?.getString("userId", "")
        database!!.getReference().child("users").child(userId.toString()).child("favorite")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    movies = ArrayList()
                    for (movieSnapshot in dataSnapshot.children) {
                        val movie: Movies? = movieSnapshot.getValue<Movies>(Movies::class.java)
                        if (movie != null) {
                            movies.add(movie)
                        }
                    }

                    try {
                        mainRecyclerView.adapter =
                            FilmAdapter(
                                movies,
                                requireContext(), sharedPreferences
                            )
                    } catch (e: Exception) {
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
}
