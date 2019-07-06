
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyHolder> {

    RecyclerTouchListener listener;
    /**Interface for OnClickListener of RecyclerView**/
    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<AndroidPOJO> model;

    public CustomAdaptor(Context context, ArrayList<AndroidPOJO> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
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

    /**Holder Class for Row Items**/
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
}