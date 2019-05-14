import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TabVersionCode extends Fragment {

    List<String> list;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tab, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        versionCode();
        /**Ready Adaptor with List**/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        /**Set The Adaptor with ListView**/
        listView.setAdapter(adapter);

        /**When Click on List Item shows a Snackbar saying item was Clicked**/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, parent.getItemAtPosition(position).toString() + " was Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        /**When Long Click on List Item shows a Snackbar saying Long Clicked on item**/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Long Clicked on " + parent.getItemAtPosition(position).toString(), Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void versionCode() {
        list = new ArrayList<>();
        /**Add Items in List**/
        list.add("1.0");
        list.add("1.1");
        list.add("1.5");
        list.add("1.6");
        list.add("2.1");
        list.add("2.2");
        list.add("2.3");
        list.add("3.0");
        list.add("4.0");
        list.add("4.1");
        list.add("4.4");
        list.add("5.0");
        list.add("6.0");
        list.add("7.0");
        list.add("8.0");
    }
}
