package com.sayed.ahmed.vu_kotlin

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
