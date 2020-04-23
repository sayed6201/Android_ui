
====================================================================================
ApiInterface for retrofit 2
====================================================================================
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface{
    @GET("/api/users")
    fun getUserData (@Query("page") page: Int) : Call<UserData>;
}


====================================================================================
RetrofitApiClient:
====================================================================================

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiClient {

//companion object worjs as singletone class, static types

    companion object {
        val BASE_URL : String = "https://reqres.in";

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create();

        var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    }

}


   //=======================================EXAMPLE 2========================================
//Kotlin APi CLinet with interceptor example:

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "6e63c2317fbe963d76c3bdc2b785f6d1"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

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


====================================================================================
Models class
====================================================================================

import com.google.gson.annotations.SerializedName

class UserData{
    @SerializedName("page")
    public val page: Int ?= null;

    @SerializedName("per_page")
    public val perPage : Int ?= 0;

    @SerializedName("total")
    public val total : Int ?= 0;

    @SerializedName("total_pages")
    public val totalPages: Int ?= 0;

    @SerializedName("data")
    public val data : ArrayList<User> ?= null;

    @SerializedName("ad")
    public val ad : Ad ?= null;
}


public class User {

    @SerializedName("id")
    public val id: Int ? =null;

    @SerializedName("email")
    public val email: String ? =null;

    @SerializedName("first_name")
    public val firstName: String ? = null;

    @SerializedName("last_name")
    public val lastName : String ? = null;

    @SerializedName("avatar")
    public val avatar: String ? =null;

}

public class Ad {

    @SerializedName("company")
    public val company : String ?= null;

    @SerializedName("url")
    public val url : String ? = null;

    @SerializedName("text")
    public val text : String ? = null;
}




====================================================================================
UserRepository: repository for MVVM
====================================================================================
import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.sayed.ahmed.vu_kotlin.model.UserData
import com.sayed.ahmed.vu_kotlin.myinterfacr.ApiInterface
import com.sayed.ahmed.vu_kotlin.network.RetrofitApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(application: Application) {

    companion object{
        var userRepository : UserRepository ?= null;

        fun getInstance( application : Application) : UserRepository{
            if (userRepository == null){
                userRepository =  UserRepository(application);
            }
            return userRepository as UserRepository;
        }
    }

    //userData live
    var userDataLiveData : MutableLiveData<UserData>  = MutableLiveData();
    //user
    var userDataProgressbar : MutableLiveData<Boolean> =  MutableLiveData();
    //userData error
    var  userDataError : MutableLiveData<Throwable>  = MutableLiveData();
    var application: Application ? = application;


    fun callUserDataApi() {

        Log.d("callUserDataApi","callUserDataApi called");


        userDataProgressbar.postValue(true);

        val apiInterface : ApiInterface = RetrofitApiClient.retrofit.create(ApiInterface::class.java);
        val call : Call<UserData> = apiInterface.getUserData(1);

        call.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                userDataProgressbar.postValue(false);

                val userData: UserData? = response.body();

                if (response.code() == 200  && userData != null) {
                    userDataLiveData.postValue(userData);
                    Log.d("DATA","Page: "+ userData.page+" total page: "+userData.totalPages);
                }else{
                    userDataError.postValue(Exception(response.message()));
                }
            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                userDataProgressbar.postValue(false);
                userDataError.postValue(t);
                Log.d("ERR","errr: "+t.message);
            }
        });

    }
}





====================================================================================
UserViewModel: ViewModel for MVVM
====================================================================================

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.sayed.ahmed.vu_kotlin.MainActivity
import com.sayed.ahmed.vu_kotlin.model.UserData
import com.sayed.ahmed.vu_kotlin.repository.UserRepository

class UserViewModel: AndroidViewModel {

    var  userRepository : UserRepository;
    var myApplication: Application;

    constructor(application: Application) : super(application) {
        this.myApplication = application;
        userRepository = UserRepository.getInstance(application);

        Log.i("UserViewModel","constructor intialized");
    }

    fun getUserDataLiveData(): LiveData<UserData> {
        return userRepository.userDataLiveData;
    }

    fun getUserDataProgressbar(): LiveData<Boolean> {
        return userRepository.userDataProgressbar;
    }

    fun getUserDataError(): LiveData<Throwable>{
        return userRepository.userDataError;
    }

    fun callUserDataFromApi() {
        userRepository.callUserDataApi();
    }
}




====================================================================================
Activity:
====================================================================================
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sayed.ahmed.vu_kotlin.adapter.UserDataRvAdapter
import com.sayed.ahmed.vu_kotlin.model.UserData
import com.sayed.ahmed.vu_kotlin.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    lateinit var pb_layout: LinearLayout;
    lateinit var user_rv: RecyclerView;
    lateinit var userViewModel : UserViewModel;
    lateinit var staggeredGridLayoutManager : StaggeredGridLayoutManager;

    lateinit var userDataRvAdapter: UserDataRvAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        pb_layout = findViewById(R.id.pb_layout);
        user_rv = findViewById(R.id.user_rv);

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java);

        userDataRvAdapter = UserDataRvAdapter(this);
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        user_rv.setLayoutManager(staggeredGridLayoutManager);
        user_rv.setAdapter(userDataRvAdapter);

        userViewModel.callUserDataFromApi();

        userViewModel.getUserDataLiveData().observe(this, Observer<UserData> {
            userData ->
            userData.data?.let { userDataRvAdapter.setList(it) };
        })

        userViewModel.getUserDataProgressbar().observe(this, Observer<Boolean> {
            value->
            if(value){
                pb_layout.visibility = View.VISIBLE;
            }else{
                pb_layout.visibility = View.GONE;
            }

        });

        userViewModel.getUserDataError().observe(this, Observer<Throwable> {
            err->
            Log.d("ERR","showing error");
        })
    }
}




====================================================================================
Asapter for recyclerview
====================================================================================

import android.annotation.SuppressLint
import android.content.Context
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sayed.ahmed.vu_kotlin.R
import com.sayed.ahmed.vu_kotlin.model.User


class UserDataRvAdapter (val context: Context): RecyclerView.Adapter<UserDataRvAdapter.ViewHolder>() {

    var lists:ArrayList<User> = ArrayList();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.user_rv_item, parent, false)

        val viewHolder: ViewHolder =  ViewHolder(v);
//        v.setOnClickListener{
//            var position: Int = viewHolder.getAdapterPosition();
//            Toast.makeText(context, "Your Selected item: "+lists[position].toString(),Toast.LENGTH_SHORT).show();
//        }

        return viewHolder;
    }

    fun setList(list:ArrayList<User>){
        this.lists.addAll(list);
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return lists.size;
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.txtName?.text = lists[position].firstName + " " + lists[position].firstName;
        holder?.txtEmail?.text = lists[position].email;

        Glide
            .with(context)
            .load(lists[position].avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder?.imageView);

//        holder?.txtAge.setOnClickListener {
//            Toast.makeText(context,""+position +" Your Age is "+holder?.txtAge.text.toString(), Toast.LENGTH_SHORT ).show();
//        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.name_tv);
        val txtEmail = itemView.findViewById<TextView>(R.id.email_tv);
        val imageView = itemView.findViewById<ImageView>(R.id.user_iv);

    }

}
