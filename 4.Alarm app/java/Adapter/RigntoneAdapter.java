package com.sayed.alarm_app.Adapter;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sayed.alarm_app.R;



public class RigntoneAdapter extends RecyclerView.Adapter<RigntoneAdapter.MyHolder> {

    Ringtone ringtone;


    SelectedRingtone selectedRingtone;

    public interface SelectedRingtone{
        public void onRigntoneSelected(int position, Uri uri);
    }

    public void setSelectedRingtone(SelectedRingtone selectedRingtone){
        this.selectedRingtone = selectedRingtone;
    }

    Context context;
    LayoutInflater inflater;
    Uri[] model;

    public RigntoneAdapter(Context context, Uri[] model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    public int getItemCount() {
        return model.length;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_ringtone_list, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        holder.title.setText("Ringtone: " + (position + 1));

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedRingtone!=null){
                    selectedRingtone.onRigntoneSelected(position,model[position]);
                    if(ringtone != null){
                        ringtone.stop();
                        ringtone = null;
                    }
                }
            }
        });

        holder.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ringtone!=null){
                    ringtone.stop();
                    ringtone = null;
                    holder.playPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                }else {
                    Uri ringtoneUri = model[position];
                    ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
                    ringtone.play();
                    holder.playPause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView select, playPause;

        public MyHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.rintone_title_tv);
            select = view.findViewById(R.id.select);
            playPause = view.findViewById(R.id.play_pause);
        }
    }
}
