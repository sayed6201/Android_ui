
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMultiAutoCompleteTextView extends AppCompatActivity {

    MultiAutoCompleteTextView autoCom;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_multi_auto_complete_text_view);

        autoCom = (MultiAutoCompleteTextView) findViewById(R.id.autoCom);
        prepareList();

        /**Creating the instance of ArrayAdapter containing list of fruit names**/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        /**Getting the instance of MultiAutoCompleteTextView**/
        autoCom.setThreshold(1);    /**will start working from first character**/
        autoCom.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());   /**Set a Tokenizer**/
        autoCom.setAdapter(adapter);    /**setting the adapter data into the MultiAutoCompleteTextView**/
        autoCom.setTextColor(Color.RED);
    }

    private void prepareList() {
        list = new ArrayList<>();
        /**Add Items in List**/
        list.add("Aestro");
        list.add("Blender");
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
