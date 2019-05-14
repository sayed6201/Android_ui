
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyHolder> implements Filterable {

    RecyclerTouchListener listener;

    /**
     * Interface for OnClickListener of RecyclerView
     **/
    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<AndroidPOJO> model;
    ArrayList<AndroidPOJO> arrayList;
    CustomFilter filter;

    public CustomAdaptor(Context context, ArrayList<AndroidPOJO> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.arrayList = model;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.listview_row_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        /**Bind Text in the TextView**/
        holder.version.setText(model.get(position).getVersion());
        holder.name.setText(model.get(position).getName());
        holder.release.setText(model.get(position).getRelease());
    }

    public void setClickListener(RecyclerTouchListener listener) {
        this.listener = listener;
    }

    /**
     * Holder Class for Row Items
     **/
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView version;
        TextView name;
        TextView release;

        public MyHolder(final View view) {
            super(view);

            version = (TextView) view.findViewById(R.id.version);
            name = (TextView) view.findViewById(R.id.androidName);
            release = (TextView) view.findViewById(R.id.release);

            version.setTextColor(Color.RED);
            name.setTextColor(Color.RED);
            release.setTextColor(Color.RED);
            view.setTag(view);

            /**OnClick Listener on Row Items**/
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickItem(view, getAdapterPosition());
                }
            });
        }
    }

    public ArrayList<AndroidPOJO> currentList() {
            return model;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<AndroidPOJO> filter = new ArrayList<AndroidPOJO>();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getName().toUpperCase().contains(constraint)) {
                        AndroidPOJO l = new AndroidPOJO(arrayList.get(i).getVersion(), arrayList.get(i).getName(), arrayList.get(i).getRelease());
                        filter.add(l);
                    }
                }
                results.count = filter.size();
                results.values = filter;
            } else {
                results.count = arrayList.size();
                results.values = arrayList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            model = (ArrayList<AndroidPOJO>) results.values;
            notifyDataSetChanged();
        }
    }
}