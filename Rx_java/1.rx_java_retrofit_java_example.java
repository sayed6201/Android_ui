
link:
-----
https://proandroiddev.com/exploring-rxjava-in-android-e52ed7ef32e2
https://www.toptal.com/android/functional-reactive-android-rxjava

kotlin:
https://medium.com/@gabrieldemattosleon/fundamentals-of-rxjava-with-kotlin-for-absolute-beginners-3d811350b701


What is reactive programming:
==============================
    # Reactive programming allows to propagates event changes to registered observers.
    # In reactive programming the consumer reacts to the data as it comes in. 
    This is the reason why asynchronous programming is also called reactive programming.


What is RxJava:
==============================
 # Reactivex is a library for composing asynchronous and event based programs
 # RxJava describes itself as an API for asynchronous programming with observable streams.
 # The two main classes are Observable and Subscriber. 
    In RxJava, an Observable is a class that emits a stream of data or events, 
    and a Subscriber is a class that acts upon the emitted items.


 The build blocks for RxJava code are the following ( 3 O )
 ===========================================================

        1. observables representing sources of data  -> ( Observable<LIst<MovieList>>, Single<Movie> )
        2. subscribers (or observers) listening to the observables  ->  ( sunscribe() )
        3. a set of methods for modifying and composing the data  ->  ( operators )


AsyncTAsk vs Rxjava:
==============================
 # memory/context leaks are easily created in AsyncTAsk
 since NetworkRequestTask is an inner class and thus holds an implicit reference to the outer class.

 # if we want to chain another long operation after the network call? 
 We’d have to nest two AsyncTasks which can significantly reduce readability.


4 types of Observable Rxjava:
==============================
	1. Flowable and Observable can represent finite or infinite streams. Flowable support back-pressure.
	2. Single are streams with a single element, Single represent Observable that emit single value or error.
	3. Maybe are streams with either 0 or one element.
	4. Finally a Completable represents a stream with no elements, i.e it can only complete without a value or fail, 
	Completable represent Observable that emits no value, but only terminal events, either onError or onCompleted


CompositeDisposable:
==============================
    # A CompositeDisposable is a class that helps us to manage all our disposables.

    # When we create observables, and they emit data, 
        a stream is created which later needs to be removed it takes up resources. 
        This stream is the called a disposable

    # Since you can create more than one disposable , it would be tiresome to clear them one by one, 
         hence we use a CompositeDisposable which will contain all the disposables created 
         and they can be cleared by just clearing the CompositeDisposable. 
         We use the add() method to add one disposable

    //EXample , Every Obseravle returns disposable
         Disposable disposable = Observable.timer(5000, TimeUnit.MILLISECONDS)
         compositeDisposable.add(disposable);


Specifying Thread:
==============================
 # subscribeOn() and observeOn() method specifies the threads on which the call should be executed on. 
 If we specify the subscribeOn() only, we tell the app to run the call on the current thread. 


Subscribe:
==============================
 # A observable can have any number of subscribers
 # Inside the subscribe() method we have two lambda expressions for kotlin. 
        1. The first one is always executed if our call was successful and it returns our value 
            i.e PopularMovies object,then we call the onResponse function that we create to handle our response. 

        2. If there is an error in our application then the second expression is executed to 
            return a throwable then we call a function in our case, onFailure
# For Java:
    1. onNext() : If a new item is emitted from the observable, the onNext() method is called on each subscriber

    2. onComplete(): if the observable finishes its data flow successful, the onComplete() method is called on each subscriber

    3. onError(): if the observable finishes its data flow with an error, the onError() method is called on each subscriber




Dependency for Rxjava and retrofit:
-------------------------------------

github: 
https://github.com/ReactiveX/RxAndroid

// for retrofie
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
compile 'com.squareup.retrofit2:retrofit:2.1.0'

	//compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
    //compile 'io.reactivex:rxandroid:1.2.0'
    //compile 'io.reactivex:rxjava:1.1.8'

    //RxJava2 adapter
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.6.0'
    //RxJava2
    implementation 'io.reactivex.rxjava2:rxjava:2.2.12'
    //RxAndroid
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'








=======================================KOTLIN-Rxjava2-retrofit2-MVVM=============================================
Example: 2
KOtlin Movie App movie data fetch
=======================================KOTLIN-Rxjava2-retrofit2-MVVM=============================================


//--------------------------------------------------------------------------------------
    Apiinterface 
//--------------------------------------------------------------------------------------
interface ApiInterface {

/*
-------------------------------------------------------
    returns Single<MovieResponse> because Api
    gives an object not iterable List
-------------------------------------------------------
*/
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetailData>
}


//--------------------------------------------------------------------------------------
    ApiClient 
//--------------------------------------------------------------------------------------
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "df44e70079dac4a881b5537f37e99fed"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

// https://api.themoviedb.org/3/movie/popular?api_key=6e63c2317fbe963d76c3bdc2b785f6d1&page=1
// https://api.themoviedb.org/3/movie/299534?api_key=6e63c2317fbe963d76c3bdc2b785f6d1
// https://image.tmdb.org/t/p/w342/or06FN3Dka5tukK1e9sl16pB3iy.jpg

object ApiClient {

    fun getClient(): ApiInterface {

        val requestInterceptor = Interceptor { chain ->
            // Interceptor take only one argument which is a lambda function so parenthesis can be omitted

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)   //explicitly return a value from whit @ annotation. lambda always returns the value of the last expression implicitly
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}



//--------------------------------------------------------------------------------------
    MovieDetailsRepository

    Api call from repository using Rxjava
//--------------------------------------------------------------------------------------

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


//--------------------------------------------------------------------------------------
    Viewmodel.
    compositeDisposable.dispose() is called on onCleared method
//--------------------------------------------------------------------------------------
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





=======================================KOTLIN-Rxjava2-retrofit2=============================================
Example: 3
Kotlin Rxjava2 Retrofit2 Example 
link:
https://dev.to/paulodhiambo/kotlin-rxjava-retrofit-tutorial-18hn
=======================================KOTLIN-Rxjava2-retrofit2=============================================


//--------------------------------------------------------------------------------------
    Model
//--------------------------------------------------------------------------------------
data class PopularMovies(
    val results: List<Result>
)

data class Result(
    val id: Int,    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int
)



//--------------------------------------------------------------------------------------
    ApiInterface
//--------------------------------------------------------------------------------------
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbEndpoints {

    @GET("/3/movie/popular")
    fun getMovies(@Query("api_key") key: String): Observable<PopularMovies>  // Observable<PopularMovies> beacuse api returns list

}


//--------------------------------------------------------------------------------------
    ApiClient
//--------------------------------------------------------------------------------------
import com.odhiambopaul.movies.util.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(TmdbEndpoints::class.java)

    fun buildService(): TmdbEndpoints {
        return retrofit
    }
}


//--------------------------------------------------------------------------------------
    MainActivity
//--------------------------------------------------------------------------------------
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.odhiambopaul.movies.network.PopularMovies
import com.odhiambopaul.movies.R
import com.odhiambopaul.movies.network.ServiceBuilder
import com.odhiambopaul.movies.util.api_key
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    //initializing disposable..........
        val compositeDisposable = CompositeDisposable()
    //adding disposal from observeable
        compositeDisposable.add(
            ServiceBuilder.buildService().getMovies(api_key)
            //returns Obserable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {response -> onResponse(response)}, 
                    {t -> onFailure(t) }
                    ))

    }

    private fun onFailure(t: Throwable) {
        Toast.makeText(this,t.message, Toast.LENGTH_SHORT).show()
    }

    private fun onResponse(response: PopularMovies) {
        progress_bar.visibility = View.GONE
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter =
                MoviesAdapter(response.results)
        }
    }
}







=======================================JAVA-RXJAVA1=============================================
Example: 1
Github star repo fitchinf Example:
link: 
https://www.toptal.com/android/functional-reactive-android-rxjava
Src code:
https://github.com/arriolac/GitHubRxJava
=======================================JAVA-RXJAVA1=============================================


//--------------------------------------------------------------------------------------
	Model 
//--------------------------------------------------------------------------------------
    public class GitHubRepo {

    public final int id;
    public final String name;
    public final String htmlUrl;
    public final String description;
    public final String language;
    public final int stargazersCount;

    public GitHubRepo(int id, String name, String htmlUrl, String description, String language, int stargazersCount) {
        this.id = id;
        this.name = name;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
    }
}




//--------------------------------------------------------------------------------------
	ApiINterface
//--------------------------------------------------------------------------------------
public interface GitHubService {

	//returns Observable<List<GitHubRepo>> 

        @GET("users/{user}/starred") Observable<List<GitHubRepo>> getStarredRepositories(@Path("user") String userName);
}



//--------------------------------------------------------------------------------------
	GitHubClient
//--------------------------------------------------------------------------------------

    public class GitHubClient {

        private static final String GITHUB_BASE_URL = "https://api.github.com/";

        private static GitHubClient instance;
        private GitHubService gitHubService;

        private GitHubClient() {

            final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            final Retrofit retrofit = 
            							new Retrofit.Builder().baseUrl(GITHUB_BASE_URL)
            							//adding retrofit as adapter factory
                                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create(gson))
                                                .build();

            gitHubService = retrofit.create(GitHubService.class);
        }

        public static GitHubClient getInstance() {
            if (instance == null) {
                instance = new GitHubClient();
            }
            return instance;
        }

        public Observable<List<GitHubRepo>> getStarredRepos(@NonNull String userName) {
            return gitHubService.getStarredRepositories(userName);
        }
    }


//--------------------------------------------------------------------------------------
	view data fetching from API
//--------------------------------------------------------------------------------------
     private void getStarredRepos(String username) {
        subscription = GitHubClient.getInstance()
                                   .getStarredRepos(username)
                                   
                                   //returns Observable<List<GitHubRepo>> 

                                   .subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread())
                                   .subscribe(new Observer<List<GitHubRepo>>() {
                                       @Override public void onCompleted() {
                                           Log.d(TAG, "In onCompleted()");
                                       }

                                       @Override public void onError(Throwable e) {
                                           e.printStackTrace();
                                           Log.d(TAG, "In onError()");
                                       }

                                       @Override public void onNext(List<GitHubRepo> gitHubRepos) {
                                           Log.d(TAG, "In onNext()");
                                           adapter.setGitHubRepos(gitHubRepos);
                                       }
                                   });
    }


@Override protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }


