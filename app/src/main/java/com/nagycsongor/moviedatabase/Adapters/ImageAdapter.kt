package com.nagycsongor.moviedatabase.Adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.nagycsongor.moviedatabase.R
import com.squareup.picasso.Picasso

class ImageAdapter(val images: ArrayList<String>, val context: Context) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.image_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${images[position]}")
                .into(holder.image)
        } catch (e: Exception) {
            holder.image.setImageDrawable(null)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = itemView.findViewById(R.id.posterImageView)
    }

}