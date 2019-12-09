package com.nagycsongor.moviedatabase

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    val strings: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        strings.add("0")
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
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val mainRecyclerView = view.findViewById(R.id.mainRecyclerView) as RecyclerView
        mainRecyclerView.layoutManager = LinearLayoutManager(context)
        mainRecyclerView.adapter = FilmAdapter(strings,requireContext())
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.menuSearch) as MenuItem
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "submit", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(context, "change", Toast.LENGTH_SHORT).show()
                return true
            }

        })
    }


}
