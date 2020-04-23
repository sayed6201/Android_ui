package com.sayed.ahmed.story_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.sayed.ahmed.story_app.model.Movie
import com.sayed.ahmed.story_app.repository.MoviePagedListRepository
import com.sayed.ahmed.story_app.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val movieRepository : MoviePagedListRepository = MoviePagedListRepository();

    val  moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}