
====================================================================================
MVVM Theory NOTE:
====================================================================================

MVP
    • In MVP child and parents have instance of one another, so it breaks clean code architecture
    • in MVP Presenter and model can not be reused
    
MVVM:
    • outward layers should have instance of inward layer or parent has instance of chils but 
    child has no intance of parents

    ViewModel:
    ----------
    • in MVVM viewmodel and models can be reused
    • viewModel stores data for view , viewmodel notifies view with LiveData

    LiveData: immutable only view should be passed with LiveData. 

    MutableLiveData: Mutablele repo and viewModel should have it, 
    	postValue: is used to pass values between different class
    	setValue: is used to pass values in same view/class

    Repo:
    -----
    • Model/repository fetches data from API or local storage , model notifies viewmodel with LiveData
    • use interface to pre-define methods for repository it will help in unit testing and depencey inversion

WHY MVVM:
    • on rotaion or other configuration change may cause the activity to restart, so the state may be lost, 
    in this case viewmodel can help
    • viewmodel is lifecycle aware of the activty, it can observe lifecycle changes in activty, 
    activty is lifecycle owner and viewmodel is lifecycle observer, 
    • doesn’t get cleared/instance recreated in view when activty is rotated
    • view model instance for particular activtiy only get detroyed when activity is destroyed

//--------------------------------------------------------------------------------------
ViewModel:
    # ViewModel is  LifecycleObserver, View is lifecycle owner,
    # implnt LifecycleObserver in ViewModel, 

    <<<<< @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) >>>>>>>>>
//--------------------------------------------------------------------------------------

// implementing LifecycleObserver.......................................>>>>>>>>>
public class MainActivityViewModel extends ViewModel implements LifecycleObserver{

    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    private Thread counterThread;
    private int count = 0;
    private boolean isCounterInProgress;

    private MutableLiveData<Integer> counterValue;

    public MainActivityViewModel(){
        isCounterInProgress = false;
        counterValue = new MutableLiveData<>();

        counterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isCounterInProgress) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        counterValue.postValue(count);
                        Log.i(TAG,"Counter in progress: "+count);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public LiveData<Integer> getCounterValue(){
        return counterValue;
    }

    public void startCounter(){
        if(!isCounterInProgress){
            isCounterInProgress = true;
            counterThread.start();
        }
    }

    public void stopCounter(){
        isCounterInProgress = false;
    }

//this will be executed when onResume() lifecycle methos is called in view
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void functionalityBasedOnSomeLifeCyclerEventInActivity(){
        Log.i(TAG,"Execute this method when Activity is resumed");
    }
}


//--------------------------------------------------------------------------------------
View: 
    # ViewModel is  LifecycleObserver, View is lifecycle owner,

     <<<< getLifecycle().addObserver(mainActivityViewModel); >>>>>>>
//--------------------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    MainActivityViewModel mainActivityViewModel;

    Button startButton, stopButton;

    TextView textViewCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders
                .of(this)
                .get(MainActivityViewModel.class);

        mainActivityViewModel.getCounterValue()
                .observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewCounter.setText("Counter: "+integer);
            }
        });

//this line will add viewmodel as the lifecylcle observer
        getLifecycle().addObserver(mainActivityViewModel);

        textViewCounter = (TextView)findViewById(R.id.textViewCunter);
        startButton = (Button)findViewById(R.id.start);
        stopButton = (Button)findViewById(R.id.stop);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityViewModel.startCounter();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityViewModel.stopCounter();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


====================================================================================
Doing Background thread task in MVVM
Example:1
====================================================================================
/*
#You should avoid running your AsyncTask in LiveData. 
LiveData should really only be concerned with the observation of data. Not the act of changing the data.

#The best way of dealing with this situation is to use the ViewModel / Repository pattern.

#Activity / Fragment observes LiveData from ViewModel, 
ViewModel observes LiveData from Repository. 
Changes are made in the repository, which are pushed to its LiveData. 
Those changes are delivered to the Activity / Fragment (through the ViewModel).

#I would avoid using AsyncTask in this situation. 
The bonus of AsyncTask is that you can get results on the UI thread after doing work. 
In this case though, that isn't necessary. LiveData will do that for you.


// ViewModel and LiveData de[emdecy]
    def lifecycle_version = "2.2.0"
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"

    link: https://developer.android.com/jetpack/androidx/releases/lifecycle

    MVVM blog: https://hellohasan.com/2020/01/22/mvvm-tutorial-in-bengali-kotlin-viewmodel-livedata-retrofit/

*/

//Here is an (untested) example:

//Activity+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class MyActivity extends AppCompatActivity {

    private MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModelProviders.of(this) is deprecated..
        //viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        //new way of viewmodel initialization..
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe the view model
        viewModel.getMyLiveData().observe(this, s -> {
            // You work with the data provided through the view model here.
            // You should only really be delivering UI updates at this point. Updating
            // a RecyclerView for example.
            Log.v("LIVEDATA", "The livedata changed: "+s);
        });

        //without lambda expresiion
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setNotes(notes);
            }
        });


        // This will start the off-the-UI-thread work that we want to perform.
        MyRepository.getInstance().doSomeStuff();
    }
}


//ViewModel+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class MyViewModel extends AndroidViewModel {

    @NonNull
    private MyRepository repo = MyRepository.getInstance();

    @NonNull
    private LiveData<String> myLiveData;

    public MyViewModel(@NonNull Application application) {
        super(application);
        // The local live data needs to reference the repository live data
        myLiveData = repo.getMyLiveData();
    }

    @NonNull
    public LiveData<String> getMyLiveData() {
        return myLiveData;
    }
}

//Repository+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class MyRepository {

    private static MyRepository instance;

    // Note the use of MutableLiveData, this allows changes to be made
    @NonNull
    private MutableLiveData<String> myLiveData = new MutableLiveData<>();

    public static MyRepository getInstance() {
        if(instance == null) {
            synchronized (MyRepository.class) {
                if(instance == null) {
                    instance = new MyRepository();
                }
            }
        }
        return instance;
    }

    // The getter upcasts to LiveData, this ensures that only the repository can cause a change
    @NonNull
    public LiveData<String> getMyLiveData() {
        return myLiveData;
    }

    // This method runs some work for 3 seconds. It then posts a status update to the live data.
    // This would effectively be the "doInBackground" method from AsyncTask.
    public void doSomeStuff() {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }

            myLiveData.postValue("Updated time: "+System.currentTimeMillis());
        }).start();
    }

}




====================================================================================
Example:2 
#  Call api and feed to recyclerView
#display progressbar
====================================================================================

//--------------------------------------------------------------------------------------
// Repository: UserRepository
//--------------------------------------------------------------------------------------
package com.sayed.ahmed.vu.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sayed.ahmed.vu.cache.Dataholder;
import com.sayed.ahmed.vu.models.UserData;
import com.sayed.ahmed.vu.network.ApiInterface;
import com.sayed.ahmed.vu.network.RetrofitApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static UserRepository userRepository;
    //userData live
    private MutableLiveData<UserData> userDataLiveData ;
    //user
    private MutableLiveData<Boolean> userDataProgressbar ;
    //userData error
    private MutableLiveData<Throwable> userDataError ;

    public UserRepository(Application application) {
        userDataLiveData = new MutableLiveData<>();
        userDataProgressbar = new MutableLiveData<>();
        userDataError = new MutableLiveData<>();
    }

    public static UserRepository getInstance(Application application){
        if (userRepository == null){
            synchronized (UserRepository.class){
                userRepository = new UserRepository(application);
            }
        }
        return userRepository;
    }

    public LiveData<UserData> getUserDataLiveData(){
        return userDataLiveData;
    }

    public LiveData<Boolean> getUserDataProgressbar() {
        return userDataProgressbar;
    }

    public LiveData<Throwable> getuserDataError() {
        return userDataError;
    }

    public void callUserDataApi() {

        Log.d("callUserDataApi","callUserDataApi called");

        if(Dataholder.getInstance().getCurrentPageNumber() > Dataholder.getInstance().getTotalPageNumber() ){
            Log.d("MSG","ALL data Loaded, getData returned");
            return;
        }

        userDataProgressbar.postValue(true);

        ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<UserData> call = apiInterface.getUserData(Dataholder.getInstance().getCurrentPageNumber());
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {

                //progressbar hide..
                userDataProgressbar.postValue(false);

                UserData UserData = response.body();

                if (response.code() == 200 && UserData != null) {

                    Log.d("MSG", response.message());
                    Log.d("USER_DATA", "getPage: " + UserData.getPage().toString() + " -getTotalPage: " + UserData.getTotalPages().toString() + " - getTotal: " + UserData.getTotal().toString());
//                    Log.d("USER_LIST", "Length: " + UserData.getData().size() + " -1st: " + UserData.getData().get(1).getAvatar());

                    Dataholder.getInstance().setCurrentPageNumber(UserData.getPage() + 1);
                    Dataholder.getInstance().setTotalPageNumber(UserData.getTotalPages());

                    Log.d("DATA_HOLDER", "hasmore " + Dataholder.getInstance().hasMoreData());

                    userDataLiveData.postValue(UserData);

                } else {
                    Log.d("MSG", response.message());
                    userDataError.postValue(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                //progressbar hide..
                userDataProgressbar.postValue(false);

                Log.d("ERR", t.getMessage());

                //displaying errors..
                userDataError.postValue(t);
            }
        });

    }

}



//--------------------------------------------------------------------------------------
// ViewModel: UserViewModel
//--------------------------------------------------------------------------------------

package com.sayed.ahmed.vu.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sayed.ahmed.vu.models.UserData;
import com.sayed.ahmed.vu.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private static UserRepository userRepository ;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
    }


    public LiveData<UserData> getUserDataLiveData() {
        return userRepository.getUserDataLiveData();
    }

    public LiveData<Boolean> getUserDataProgressbar() {
        return userRepository.getUserDataProgressbar();
    }

    public LiveData<Throwable> getUserDataError() {
        return userRepository.getuserDataError();
    }

    public void callUserDataFromApi(){
        userRepository.callUserDataApi();
    }

}



//--------------------------------------------------------------------------------------
// Activity: MainActivity
//--------------------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity{

    TextView tv;
    RecyclerView userRv;
    AdapterRecyclerView adapterRecyclerView;
    LinearLayout pbLayout;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int pastVisibleItems;
    boolean isLoading = false;
    boolean isRefreshing = false;
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        userRv = findViewById(R.id.user_rv);
        pbLayout = findViewById(R.id.pb_layout);
        tv = findViewById(R.id.tv);

        adapterRecyclerView = new AdapterRecyclerView(MainActivity.this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        userRv.setLayoutManager(staggeredGridLayoutManager);
        userRv.setAdapter(adapterRecyclerView);
        adapterRecyclerView.setClickListener(new AdapterRecyclerView.RecyclerClickListener() {
            @Override
            public void onRecyclerClickListener(String link) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("LINK", link);
                startActivity(intent);
            }

        });
        userRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager manager =
                        (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int[] firstVisibleItems = manager.findFirstVisibleItemPositions(null);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isLoading) {
                    isLoading = true;
                    if (Dataholder.getInstance().hasMoreData() && !isRefreshing) {
                        Toast.makeText(MainActivity.this, "Loading data from page "+Dataholder.getInstance().getCurrentPageNumber(), Toast.LENGTH_SHORT).show();
                        getData();
                    }
                } else {
                    isLoading = false;
                }
            }
        });

        // Set up your view model
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //calling api
        viewModel.callUserDataFromApi();

        //updating progreesbar and other var..
        viewModel.getUserDataProgressbar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isRefreshing = aBoolean;
                if(aBoolean){
                    pbLayout.setVisibility(View.VISIBLE);
                }else {
                    pbLayout.setVisibility(View.GONE);
                }
                Log.d("UI","isrefreshing: "+isRefreshing+" "+"aboolen "+aBoolean);
            }
        });

        //updating recyclerview
        viewModel.getUserDataLiveData().observe(this, new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                adapterRecyclerView.setListData(userData.getData());
                adapterRecyclerView.setAdData(userData.getAd());
            }
        });

    }

  }