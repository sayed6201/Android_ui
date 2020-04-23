====================================================================================
Dependecy:
====================================================================================
https://hellohasan.com/2017/07/31/publisher-subscriber-pattern-eventbus-android-tutorial/
https://github.com/greenrobot/EventBus

implementation 'org.greenrobot:eventbus:3.2.0'

# this can be used in place of interface to publish events

# best option is to use LiveData to update view



====================================================================================
NetworkCall:
get request to get data from server
====================================================================================
public class NetworkCall {
    public static void getData(){
        Logger.addLogAdapter(new AndroidLogAdapter());
        String myUrl = "https://raw.githubusercontent.com/hasancse91/EventBus-Android-Tutorial/master/Data/data.json";
 
        ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getDataFromServer(myUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Logger.d("Response: " + response.message());

                //publising event+++++++++++++++++++++++++++++++++++++++++++++++++++
                EventBus.getDefault().post(new DataReceiveEvent("data_received", response.message()));
            }
 
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Logger.d("Failure: " + t.toString());

                //publising event+++++++++++++++++++++++++++++++++++++++++++++++++++
                EventBus.getDefault().post(new DataReceiveEvent("data_received", t.toString()));
            }
        });
    }
}


====================================================================================
DataReceiveEvent:
get request to get data from server
====================================================================================

public class DataReceiveEvent {
    private String eventTag;
    private String responseMessage;
 
    public DataReceiveEvent(String eventTag, String responseMessage) {
        this.eventTag = eventTag;
        this.responseMessage = responseMessage;
    }
 
    public boolean isTagMatchWith(String tag){
        return eventTag.equals(tag);
    }
 
    public String getResponseMessage() {
        return responseMessage;
    }
}


//----------------------------------------------------------------------------
//you can add different objects inside the class, use it in different cases
//----------------------------------------------------------------------------
public class DataReceiveEvent {
    private String eventTag;
    private UserData userData;
    private Throwable err;


    public DataReceiveEvent(String eventTag) {
        this.eventTag = eventTag;
    }


    public boolean isTagMatchWith(String tag){
        return eventTag.equals(tag);
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return this.userData;
    }

    //errors
    public void setError(Throwable err) {
        this.err = err;
    }

    public Throwable getError() {
        return this.err;
    }
}



====================================================================================
Registering An Event in Activity:
====================================================================================

@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataReceiveEvent event) throws ClassNotFoundException {
        if (event.isTagMatchWith("user_data_received") && event.getError() == null) {
        /*
        data_received, is an event tag,
        you can use DataReceiveEvent to publish different events, just use seperate Even tag
        */
            tv.setText("Total data found: "+event.getUserData().getData().size());
        }

        if (event.isTagMatchWith("user_data_received") && event.getError() != null) {
        /*
        if error is set this condition is executed...
        */
            Log.d("user_data_received",event.getError().toString()+" Error occured");
        }
    }



====================================================================================
Registering Event and unregister Event inside Activity 
====================================================================================
@Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
 
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



====================================================================================
Registering Event and unregister Event inside Activity:
---------------------------------------------------------
if you want to call the network request in 2nd activity and update the UI in 1st Activity
then regiester in OnCreate and unRegister in onDestroy class..
====================================================================================
@Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }