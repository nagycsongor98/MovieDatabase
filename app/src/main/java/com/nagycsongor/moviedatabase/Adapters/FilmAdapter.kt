package com.nagycsongor.moviedatabase.Adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.nagycsongor.moviedatabase.Fragments.DetailsDialogFragment
import com.nagycsongor.moviedatabase.HelpClass.Movies
import com.nagycsongor.moviedatabase.Main.MainActivity
import com.nagycsongor.moviedatabase.R
import com.squareup.picasso.Picasso

class FilmAdapter(val movies: ArrayList<Movies>, val context: Context, val sharedPreferences: SharedPreferences?) :
    RecyclerView.Adapter<FilmAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.film_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movies[position].movieName
        holder.releaseDate.text = movies[position].movieReleaseDate
        holder.item.setOnClickListener {
            val detailsDialogFragment =
                DetailsDialogFragment(movies[position], sharedPreferences)
            detailsDialogFragment.show((context as MainActivity).supportFragmentManager, "PaginationAdapter")
        }
        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movies[position].moviePoster}")
                .into(holder.poster)
        } catch (e: Exception) {
            holder.poster.setImageDrawable(null)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val releaseDate: TextView = itemView.findViewById(R.id.releaseDateTextView)
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val poster: ImageView = itemView.findViewById(R.id.imageView)
        val item: ConstraintLayout = itemView.findViewById(R.id.listItem)
    }

}