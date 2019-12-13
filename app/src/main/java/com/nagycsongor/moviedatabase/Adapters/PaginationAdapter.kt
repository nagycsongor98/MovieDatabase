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
import com.squareup.picasso.Picasso


class PaginationAdapter (val context: Context,val sharedPreferences: SharedPreferences?) : RecyclerView.Adapter<PaginationAdapter.ViewHolder>() {

    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false
    private var movieResults: ArrayList<Movies> = ArrayList()

    fun getMovies(): ArrayList<Movies>? {
        return movieResults
    }

    fun setMovies(movieResults: ArrayList<Movies>) {
        this.movieResults = movieResults

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(com.nagycsongor.moviedatabase.R.layout.film_list_item, parent, false)
        )
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1: View = inflater.inflate(com.nagycsongor.moviedatabase.R.layout.film_list_item, parent, false)
        viewHolder = ViewHolder(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return movieResults?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movieResults[position].movieName
        holder.item.setOnClickListener {
            val detailsDialogFragment =
                DetailsDialogFragment(movieResults[position],sharedPreferences)
            detailsDialogFragment.show((context as MainActivity).supportFragmentManager, "PaginationAdapter")
        }
        try{
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movieResults[position].moviePoster}")
                .into(holder.poster)
        } catch (e: Exception){
            holder.poster.setImageDrawable(null)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movieResults!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(r: Movies) {
        this.movieResults.add(r)
        notifyItemInserted(movieResults!!.size - 1)
    }

    fun addAll(moveResults: ArrayList<Movies>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: Movies) {
        val position = movieResults!!.indexOf(r)
        if (position > -1) {
            movieResults.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Movies())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieResults!!.size - 1
        val result = getItem(position)
        if (result != null) {
            movieResults.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Movies {
        return movieResults!![position]
    }


    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val title: TextView = itemView.findViewById(com.nagycsongor.moviedatabase.R.id.titleTextView)
        val poster: ImageView = itemView.findViewById(com.nagycsongor.moviedatabase.R.id.imageView)
        val item: ConstraintLayout = itemView.findViewById(com.nagycsongor.moviedatabase.R.id.listItem)
    }


    class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!){}

}