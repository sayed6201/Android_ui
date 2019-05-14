
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityFloatingSearchView extends AppCompatActivity {

    List<String> list;
    FloatingSearchView searchView;
    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_floating_search_view);

        /**initiate ListView**/
        listView = (ListView) findViewById(R.id.listView);

        /**Prepare List**/
        prepareData();

        /**Set the prepared list into Adaptor**/
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        /**Init FloatingSearchView**/
        searchView = (FloatingSearchView) findViewById(R.id.searchView);
        /**Set Color Hint Text Color to Red**/
        searchView.setHintTextColor(getResources().getColor(android.R.color.holo_red_dark));
        /**Set Hint**/
        searchView.setSearchHint("Search...");

        /**Search Listener**/
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                List<String> newList = new ArrayList<>();
                /**For Loop to Check String in List one by one**/
                for (int i = 0; i < list.size(); i++) {

                    /**If typed string match with the data in a List then add it into new List**/
                    if (list.get(i).contains(newQuery)) {
                        newList.add(list.get(i));
                    }
                    /**After Perparing a newList, Set the List into ListView**/
                    adapter = new ArrayAdapter(MainActivityFloatingSearchView.this, android.R.layout.simple_list_item_1, newList);
                    listView.setAdapter(adapter);
                }

            }
        });

        /**When Click on Item**/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Single CLicked on : " + adapter.getItem(position).toString(), Snackbar.LENGTH_SHORT).show();
            }
        });

        /**When Long Click on Item**/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Long CLicked on : " + adapter.getItem(position).toString(), Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void prepareData() {
        list = new ArrayList<>();
        /**Add Items in List**/
        list.add("Alpha");
        list.add("Beta");
        list.add("CupCake");
        list.add("Donut");
        list.add("Eclair");
        list.add("Froyo");
        list.add("GingerBread");
        list.add("HoneyComb");
        list.add("Ice Cream Sandwich");
        list.add("JellyBean");
        list.add("KitKat");
        list.add("Lollipop");
        list.add("MarshMellow");
        list.add("Nougat");
        list.add("Oreo");
    }
}
