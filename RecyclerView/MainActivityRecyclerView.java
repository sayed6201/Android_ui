
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MainActivityRecyclerView extends AppCompatActivity {

    ArrayList<AndroidPOJO> list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_recycler_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        /**Set Fixed Size to true**/
        recyclerView.setHasFixedSize(true);
        /**Set a Default Layout Manager**/
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivityRecyclerView.this));
        /**Set a ArrayList**/
        addData();
        /**Initialize the Custom Adaptor**/
        final CustomAdaptor adaptor = new CustomAdaptor(getApplicationContext(), list);
        /**Set The Adaptor to RecyclerView**/
        recyclerView.setAdapter(adaptor);


        //StaggeredGridLayoutManager -------------------------------------------------------------------
        // gallaryAdapter = new GallaryAdapter(GalleryActivity.this,list);
        // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // gallaryRv.setLayoutManager(staggeredGridLayoutManager);
        // gallaryRv.setAdapter(gallaryAdapter);
        // readFromDb();

        /**On Click will invoke the interface RecyclerTouchListener**/
        adaptor.setClickListener(new CustomAdaptor.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                /**Show the Snacbar with Android Name**/
                String androidName = list.get(position).getName();
                Snackbar.make(v, androidName, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void addData() {
        list = new ArrayList<AndroidPOJO>();

        list.add(new AndroidPOJO("1.0", "Alpha", "September 23, 2008"));
        list.add(new AndroidPOJO("1.1", "Beta", "February 9, 2009"));
        list.add(new AndroidPOJO("1.5", "CupCake", "April 27, 2009"));
        list.add(new AndroidPOJO("1.6", "Donut", "September 15, 2009"));
        list.add(new AndroidPOJO("2.0 - 2.1", "Eclair", "October 26, 2009"));
        list.add(new AndroidPOJO("2.2 - 2.2.3", "Froyo", "May 20, 2010"));
        list.add(new AndroidPOJO("2.3 - 2.3.7", "GingerBread", "December 6, 2010"));
        list.add(new AndroidPOJO("3.0 - 3.2.6", "HoneyComb", "February 22, 2011"));
        list.add(new AndroidPOJO("4.0 - 4.0.4", "Ice Cream SandWhich", "October 18, 2011"));
        list.add(new AndroidPOJO("4.1 - 4.3.1", "Jelly Bean", "July 9, 2012"));
        list.add(new AndroidPOJO("4.4 - 4.4.4", "KitKat", "October 31, 2013"));
        list.add(new AndroidPOJO("5.0 - 5.1.1", "Lollipop", "November 12, 2014"));
        list.add(new AndroidPOJO("6.0 - 6.0.1", "MarshMallow", "October 5, 2015"));
        list.add(new AndroidPOJO("7.0 - 7.1.2", "Nougat", "August 22, 2016"));
        list.add(new AndroidPOJO("8.0 - 8.1", "Oreo", "August 21, 2017"));

    }
}
