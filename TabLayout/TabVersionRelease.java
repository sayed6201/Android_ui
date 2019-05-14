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

public class TabVersionRelease extends Fragment {
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

        versionRelease();
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


    private void versionRelease() {
        list = new ArrayList<>();
        /**Add Items in List**/
        list.add("September 23, 2008");
        list.add("February 9, 2009");
        list.add("April 27, 2009");
        list.add("September 15, 2009");
        list.add("October 26, 2009");
        list.add("May 20, 2010");
        list.add("December 6, 2010");
        list.add("February 22, 2011");
        list.add("October 18, 2011");
        list.add("July 9, 2012");
        list.add("October 31, 2013");
        list.add("November 12, 2014");
        list.add("October 5, 2015");
        list.add("August 22, 2016");
        list.add("August 21, 2017");
    }
}
