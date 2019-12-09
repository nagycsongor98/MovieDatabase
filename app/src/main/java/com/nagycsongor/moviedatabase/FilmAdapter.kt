package com.nagycsongor.moviedatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.film_list_item.view.*

class FilmAdapter (val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.film_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FilmAdapter.ViewHolder, position: Int) {
        holder.title.setText(items.get(position))
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val image = view.imageView
        val title = view.titleTextView as TextView
    }

}