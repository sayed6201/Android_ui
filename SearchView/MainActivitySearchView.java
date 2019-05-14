
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivitySearchView extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    Toolbar toolbar;
    ArrayList<AndroidPOJO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_search_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        /**Set ActionBar to Toolbar**/
        setSupportActionBar(toolbar);

        /**setDisplayHomeAsUpEnabled will display a back arrow button in Toolbar**/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Set a Title to Toolbar**/
        getSupportActionBar().setTitle("SearchView");

        /**Set a Subtitle to Our Toolbar**/
        getSupportActionBar().setSubtitle("Search by Tapping on Search Icon");

        /**setNavigationOnClickListener will invloke when click on Back Button in Toolbar**/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        /**Set Fixed Size to true**/
        recyclerView.setHasFixedSize(true);

        /**Set a Default Layout Manager**/
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivitySearchView.this));

        /**Set a ArrayList**/
        addData();

        /**Initialize the Custom Adaptor**/
        final CustomAdaptor adaptor = new CustomAdaptor(getApplicationContext(), list);

        /**Set The Adaptor to RecyclerView**/
        recyclerView.setAdapter(adaptor);

        /**When Click On List Item**/
        onClickList();

        /**Init SearchView**/
        searchView = (findViewById(R.id.searchView));

        /**Set a Hint Text for SearchView**/
        searchView.setQueryHint("Search Here...");

        /**If iconified set to true then the SearchView is Collapsed, else Expand**/
        searchView.setIconified(false);

        /**When Close Icon is Clicked on SearchView**/
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                /**Hide the Keyboard If Close Icon is Clicked**/
                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethod.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                Toast.makeText(getApplicationContext(), "SearchView Closed!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /**When change text in SearchView, onClick Listener will execute**/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /**Hide the Keyboard When Submit**/
                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethod.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                Toast.makeText(getApplicationContext(), "Search Completed...", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /**Get the Current RecyclerView Adaptor**/
                CustomAdaptor adap = (CustomAdaptor) recyclerView.getAdapter();
                /**Now filter adaptor according to the Search Query(newText)**/
                adap.getFilter().filter(newText);
                onClickList();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**Inflate the menu in Toolbar with Menu Layout**/
        getMenuInflater().inflate(R.menu.menu_search, menu);

        /**Find the Menu Item using Item Id**/
        MenuItem searchItem = menu.findItem(R.id.example_search);
        final SearchView toolbarSearch = (SearchView) searchItem.getActionView();
        toolbarSearch.setQueryHint("Search...");
        toolbarSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                /**Hide the Keyboard If Close Icon is Clicked**/
                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethod.hideSoftInputFromWindow(toolbarSearch.getWindowToken(),0);
                Toast.makeText(getApplicationContext(), "SearchView Closed!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        /**When Click on Search Icon in Toolbar**/
        toolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /**Hide the Keyboard When Submit**/
                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethod.hideSoftInputFromWindow(toolbarSearch.getWindowToken(),0);
                Toast.makeText(getApplicationContext(), "Search Completed...", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /**Get the Current RecyclerView Adaptor**/
                CustomAdaptor adap = (CustomAdaptor) recyclerView.getAdapter();

                /**Now filter adaptor according to the Search Query(newText)**/
                adap.getFilter().filter(newText);
                onClickList();
                return true;
            }
        });
        return true;
    }

    private void onClickList() {
        /**On Click will invoke the interface RecyclerTouchListener**/
        final CustomAdaptor adapNew = (CustomAdaptor) recyclerView.getAdapter();

        adapNew.setClickListener(new CustomAdaptor.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {

                /**Rebind or Refresh The List Before Showing Toast**/
                list = adapNew.currentList();

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
