package com.sayed.alarm_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sayed.alarm_app.Entity.Alarm;
import com.sayed.alarm_app.R;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyHolder> {

    onClickDeleteListener listener;

    /**
     * Interface for OnClickListener of RecyclerView
     **/
    public interface onClickDeleteListener {
        void onClickDelete(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Alarm> model;

    public AlarmAdapter(Context context, ArrayList<Alarm> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.layout_alarm_view,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        holder.time.setText(model.get(position).getHour()+" : "+model.get(position).getMinute()+" "+model.get(position).getShift());

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickDelete(v, position);
            }
        });
    }

    public void setClickListener(onClickDeleteListener listener) {
        this.listener = listener;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView time;
        ImageView close;


        public MyHolder(final View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.alarm_time);
            close = view.findViewById(R.id.delete);


        }
    }
}
