
====================================================================================
Doing Background thread task in MVVM
Example:1
====================================================================================
/*
You should avoid running your AsyncTask in LiveData. LiveData should really only be concerned with the observation of data. Not the act of changing the data.

The best way of dealing with this situation is to use the ViewModel / Repository pattern.

Activity / Fragment observes LiveData from ViewModel, ViewModel observes LiveData from Repository. Changes are made in the repository, which are pushed to its LiveData. Those changes are delivered to the Activity / Fragment (through the ViewModel).

I would avoid using AsyncTask in this situation. The bonus of AsyncTask is that you can get results on the UI thread after doing work. In this case though, that isn't necessary. LiveData will do that for you.
*/

//Here is an (untested) example:

//Activity+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class MyActivity extends AppCompatActivity {

    private MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up your view model
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        // Observe the view model
        viewModel.getMyLiveData().observe(this, s -> {
            // You work with the data provided through the view model here.
            // You should only really be delivering UI updates at this point. Updating
            // a RecyclerView for example.
            Log.v("LIVEDATA", "The livedata changed: "+s);
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