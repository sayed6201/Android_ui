=============================================================================================
ListAdapter Classs
=============================================================================================
package com.sayed.kotlintesterapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sayed.kotlintesterapp.Movie
import com.sayed.kotlintesterapp.R

class ListAdapter(private val list: List<Movie>): RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = list[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

}

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null


    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
    }

    fun bind(movie: Movie) {
        mTitleView?.text = movie.title
        mYearView?.text = movie.year.toString()
    }

}




=============================================================================================
MainActivity.kt 
=============================================================================================
package com.sayed.kotlintesterapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main);

        val fm = supportFragmentManager


        var fragment = MainFragment.newInstance("Welcome to RV");

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

    }
}




=============================================================================================
MainFragment.kt
=============================================================================================
package com.sayed.kotlintesterapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayed.kotlintesterapp.adapters.ListAdapter
import kotlinx.android.synthetic.main.fragment_main.*

data class Movie(val title: String, val year: Int, val image: String);

class MainFragment(val textArg: String) : Fragment() {

    private val nicCageMovies = listOf(
        Movie("Raising Arizona", 1987, "raising_arizona.jpg"),
        Movie("Vampire's Kiss", 1988, "vampires_kiss.png"),
        Movie("Con Air", 1997, "con_air.jpg"),
        Movie("Face/Off", 1997, "face_off.jpg"),
        Movie("National Treasure", 2004, "national_treasure.jpg"),
        Movie("The Wicker Man", 2006, "wicker_man.jpg"),
        Movie("Bad Lieutenant", 2009, "bad_lieutenant.jpg"),
        Movie("Kick-Ass", 2010, "kickass.jpg"),
        Movie("Raising Arizona", 1987, "raising_arizona.jpg"),
        Movie("Vampire's Kiss", 1988, "vampires_kiss.png"),
        Movie("Con Air", 1997, "con_air.jpg"),
        Movie("Face/Off", 1997, "face_off.jpg"),
        Movie("National Treasure", 2004, "national_treasure.jpg"),
        Movie("The Wicker Man", 2006, "wicker_man.jpg"),
        Movie("Bad Lieutenant", 2009, "bad_lieutenant.jpg")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_main, container, false);

        return v;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv.apply {
            text = textArg
        }
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            adapter = ListAdapter(nicCageMovies)
        }
    }

    companion object {
        fun newInstance( textArg: String): MainFragment = MainFragment(textArg);
    }
}