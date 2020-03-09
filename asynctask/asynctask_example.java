++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Codein flow example , link: https://codinginflow.com/tutorials/android/asynctask
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
------------------------------------------------------------------------------------------------------------
UI 
------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?> 

	<LinearLayout 
	    xmlns:android="http://schemas.android.com/apk/res/android" 
	    xmlns:app="http://schemas.android.com/apk/res-auto" 
	    xmlns:tools="http://schemas.android.com/tools" 
	    android:layout_width="match_parent" 
	    android:layout_height="match_parent" 
	    android:gravity="center" 
	    android:orientation="vertical" 
	    tools:context="com.codinginflow.asynctaskexample.MainActivity">

    <ProgressBar
        android:id="@+id/progress_bar" 
        style="@style/Widget.AppCompat.ProgressBar.Horizontal" 
        android:layout_width="match_parent" 
        android:layout_height="wrap_content" 
        android:layout_margin="16dp" 
        android:visibility="invisible" 
        tools:visibility="visible" />

 tool visibility is used to set it visible in IDE 

    <Button
        android:layout_width="wrap_content" 
        android:layout_height="wrap_content" 
        android:onClick="startAsyncTask" 
        android:text="Start" />
</LinearLayout> 
  
------------------------------------------------------------------------------------------------------------
MainActivity.java
------------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity { 

    private ProgressBar progressBar; 

    @Override 
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main); 
        progressBar = findViewById(R.id.progress_bar); 
    } 

  
    public void startAsyncTask(View v) { 
	//this keyword is passing the mainactivity object 
        ExampleAsyncTask task = new ExampleAsyncTask(this); 
	//task.execute will call doInBackground(), 10 is the time for progressbar to run 
        task.execute(10); 
    }


/* 

Asynctask  

in AsyncTask only doInBackground runs in no UI thread, other methods run in MainThread 

when the app terminates we don't want the asynctask to run, to avoid memory leakage static keyword is used 

after using static we can't access progressbar without mainactivity object 

AsyncTask<Integer, Integer, String>  first one indicates the passed data type in task.execute(10)|(doInBackground); , second one indicate the passedValue in onProgress method, 3rd param indicate doInBackground return type 

*/ 

    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String> { 

//WeakReference is used access the MainActivity Object 
        private WeakReference<MainActivity> activityWeakReference; 

        ExampleAsyncTask(MainActivity activity) {
//initializing the weakreference and passing the Mainactivity Object 
            activityWeakReference = new WeakReference<MainActivity>(activity); 
        } 

  
        @Override 
        protected void onPreExecute() { 

            super.onPreExecute(); 
            MainActivity activity = activityWeakReference.get(); 

//if MainActivity is finished then there should not be any asynctask running 
            if (activity == null || activity.isFinishing()) { 
                return; 
            } 
            activity.progressBar.setVisibility(View.VISIBLE); 
        } 

  

        @Override 
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++) { 
//this methos will call onProgressUpdate method 
                publishProgress((i * 100) / integers[0]); 
                try { 
                    Thread.sleep(1000); 
                } catch (InterruptedException e) { 
                    e.printStackTrace(); 
                } 
            } 
            return "Finished!"; 
        } 

  

        @Override 
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values); 
            MainActivity activity = activityWeakReference.get(); 

            if (activity == null || activity.isFinishing()) { 
                return; 
            } 
            activity.progressBar.setProgress(values[0]); 
        } 

  
        @Override 
        protected void onPostExecute(String s) {
            super.onPostExecute(s); 

            MainActivity activity = activityWeakReference.get(); 

            if (activity == null || activity.isFinishing()) { 
                return; 
            } 

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show(); 
            activity.progressBar.setProgress(0); 
            activity.progressBar.setVisibility(View.INVISIBLE); 
        } 

    } 

} 