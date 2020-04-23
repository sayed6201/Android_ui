package com.sayed.ahmed.story_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sayed.ahmed.story_app.model.MovieDetailData
import com.sayed.ahmed.story_app.repository.MovieDetailsRepository
import com.sayed.ahmed.story_app.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class ViewModelMovieDetail ()  : ViewModel() {

    private val compositeDisposable = CompositeDisposable();
    private val movieRepository : MovieDetailsRepository = MovieDetailsRepository(compositeDisposable);

    fun getMovieData(movieID: Int){
        Log.d("ViewModelMovieDetail","fetching movie")
        movieRepository.fetchMovieDetailData(movieID);
    }

    val downloadedMovieResponse : LiveData<MovieDetailData> by lazy {
        movieRepository.downloadedMovieResponse
    }

    val networkStateLiveData : LiveData<NetworkState> by lazy {
        movieRepository.networkState
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}