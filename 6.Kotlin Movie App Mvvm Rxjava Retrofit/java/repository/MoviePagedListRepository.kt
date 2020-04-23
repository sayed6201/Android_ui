package com.sayed.ahmed.story_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sayed.ahmed.story_app.model.Movie
import com.sayed.ahmed.story_app.repository.pagination_config.MovieListDataSource
import com.sayed.ahmed.story_app.repository.pagination_config.MovieListDataSourceFactory
import com.sayed.ahmed.story_app.retrofit.POST_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository () {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieListDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieListDataSourceFactory(compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieListDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieListDataSource::networkState)
    }
}