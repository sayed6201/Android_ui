package com.sayed.ahmed.story_app.repository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayed.ahmed.story_app.model.MovieDetailData
import com.sayed.ahmed.story_app.retrofit.ApiClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsRepository (private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()

    private val _downloadedMovieDetailDataResponse =  MutableLiveData<MovieDetailData>()

    //with this get, no need to implement get function to get networkSate
    val networkState: LiveData<NetworkState>
        get() = _networkState

    //with this get, no need to implement get function to get networkSate
    val downloadedMovieResponse: LiveData<MovieDetailData>
        get() = _downloadedMovieDetailDataResponse

    fun fetchMovieDetailData(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)

        Log.d("MovieDetailsRepository", "fetching movies");

        try {
            compositeDisposable.add(
                ApiClient.getClient().getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailDataResponse.postValue(it);
                            _networkState.postValue(NetworkState.LOADED);
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR);
                            Log.e("MovieDataDataSource", it.message.toString());
                        }
                    )
            )

        }

        catch (e: Exception){
            Log.e("MovieDataDataSource",e.message.toString());
        }

    }
}