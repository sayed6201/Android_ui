package com.sayed.ahmed.vu_kotlin.repository


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