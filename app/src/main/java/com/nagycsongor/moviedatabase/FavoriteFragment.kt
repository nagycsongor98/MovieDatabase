package com.nagycsongor.moviedatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteFragment : Fragment() {
    val strings: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        strings.add("1")
        strings.add("2")
        strings.add("3")
        strings.add("4")
        strings.add("5")
        strings.add("6")
        strings.add("7")
        strings.add("8")
        strings.add("9")
        strings.add("10")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        val mainRecyclerView = view.findViewById(R.id.favoriteRecyclerView) as RecyclerView
        mainRecyclerView.layoutManager = LinearLayoutManager(context)
        mainRecyclerView.adapter = FilmAdapter(strings,requireContext())
        return view
    }


}
