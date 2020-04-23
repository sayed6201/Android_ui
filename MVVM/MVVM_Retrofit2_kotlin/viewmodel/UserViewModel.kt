package com.sayed.ahmed.vu_kotlin.viewmodel

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