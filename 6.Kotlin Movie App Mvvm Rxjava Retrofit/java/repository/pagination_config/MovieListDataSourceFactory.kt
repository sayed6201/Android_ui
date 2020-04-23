package com.sayed.ahmed.story_app.repository.pagination_config

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sayed.ahmed.story_app.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieListDataSourceFactory (private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<MovieListDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieListDataSource =
            MovieListDataSource(compositeDisposable)
        moviesLiveDataSource.postValue(movieListDataSource)
        return movieListDataSource
    }
}