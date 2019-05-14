
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAutoCompleteTextView extends AppCompatActivity {

    AutoCompleteTextView autoCom;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_auto_complete_text_view);

        autoCom = (AutoCompleteTextView) findViewById(R.id.autoCom);

        prepareList();

        /**Creating the instance of ArrayAdapter containing list of fruit names**/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, list);

        /**Getting the instance of AutoCompleteTextView**/
        autoCom.setThreshold(1);    /**will start working from first character**/
        autoCom.setAdapter(adapter);    /**setting the adapter data into the AutoCompleteTextView**/
        autoCom.setTextColor(Color.RED);
    }

    private void prepareList() {
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
        list.add("IceCream Sandwich");
        list.add("JellyBean");
        list.add("KitKat");
        list.add("Lollipop");
        list.add("MarshMellow");
        list.add("Nougat");
        list.add("Oreo");
    }
}
