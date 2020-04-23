
link:
-----
https://proandroiddev.com/exploring-rxjava-in-android-e52ed7ef32e2
https://www.toptal.com/android/functional-reactive-android-rxjava

kotlin:
https://medium.com/@gabrieldemattosleon/fundamentals-of-rxjava-with-kotlin-for-absolute-beginners-3d811350b701


AsyncTAsk vs Rxjava:
--------------------
 # memory/context leaks are easily created in AsyncTAsk
 since NetworkRequestTask is an inner class and thus holds an implicit reference to the outer class.

 # if we want to chain another long operation after the network call? 
 We’d have to nest two AsyncTasks which can significantly reduce readability.


4 types of Observable Rxjava:
-----------------------------------
	1. Flowable and Observable can represent finite or infinite streams. Flowable support back-pressure.
	2. Single are streams with a single element, Single represent Observable that emit single value or error.
	3. Maybe are streams with either 0 or one element.
	4. Finally a Completable represents a stream with no elements, i.e it can only complete without a value or fail, 
	Completable represent Observable that emits no value, but only terminal events, either onError or onCompleted



Dependency for Rxjava and retrofit:
-------------------------------------
	compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'io.reactivex:rxandroid:1.2.0'
    compile 'io.reactivex:rxjava:1.1.8'




====================================================================================
Example: 1
Github star repo fitchinf Example:
link: 
https://www.toptal.com/android/functional-reactive-android-rxjava
Src code:
https://github.com/arriolac/GitHubRxJava
====================================================================================


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





====================================================================================
Example: 2
KOtlin Movie App movie data fetch

====================================================================================


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