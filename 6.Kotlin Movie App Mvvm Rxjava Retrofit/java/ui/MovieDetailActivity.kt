package com.sayed.ahmed.story_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sayed.ahmed.story_app.R
import com.sayed.ahmed.story_app.model.MovieDetailData
import com.sayed.ahmed.story_app.repository.NetworkState
import com.sayed.ahmed.story_app.retrofit.POSTER_BASE_URL
import com.sayed.ahmed.story_app.viewmodel.ViewModelMovieDetail
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.text.NumberFormat
import java.util.*
import android.os.StrictMode



class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModelMovieDetail
//    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId: Int = intent.getIntExtra("id",1)

        viewModel = ViewModelProvider(this).get(ViewModelMovieDetail::class.java)

//        viewModel = getViewModel()

        viewModel.getMovieData(movieId)

        viewModel.downloadedMovieResponse.observe(this, androidx.lifecycle.Observer {
            bindUI(it)
        })

        viewModel.networkStateLiveData.observe(this, androidx.lifecycle.Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    private fun getViewModel(): ViewModelMovieDetail {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ViewModelMovieDetail() as T
            }
        })[ViewModelMovieDetail::class.java]
    }

    fun bindUI( it: MovieDetailData){

        Log.d("DATA_UI",it.posterPath);

        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster);
    }
}
