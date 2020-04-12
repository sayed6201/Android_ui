====================================================================================
Dependecy:
====================================================================================
https://hellohasan.com/2017/07/31/publisher-subscriber-pattern-eventbus-android-tutorial/
https://github.com/greenrobot/EventBus

implementation 'org.greenrobot:eventbus:3.2.0'



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
                EventBus.getDefault().post(new DataReceiveEvent("data_received", response.message()));
            }
 
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Logger.d("Failure: " + t.toString());
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




====================================================================================
Registering An Event in Activity:
====================================================================================

@Subscribe(threadMode = ThreadMode.MAIN)
public void onEvent(DataReceiveEvent event) throws ClassNotFoundException {
    if (event.isTagMatchWith("data_received")) {
        /*
        data_received, is an event tag,
        you can use DataReceiveEvent to publish different events, just use seperate Even tag
        */
        textView.setText(event.getResponseMessage());
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