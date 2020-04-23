package com.sayed.ahmed.vu_kotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sayed.ahmed.vu_kotlin.R
import com.sayed.ahmed.vu_kotlin.model.User


class UserDataRvAdapter (val context: Context): RecyclerView.Adapter<UserDataRvAdapter.ViewHolder>() {

    var lists:ArrayList<User> = ArrayList();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.user_rv_item, parent, false)

        val viewHolder: ViewHolder =  ViewHolder(v);
//        v.setOnClickListener{
//            var position: Int = viewHolder.getAdapterPosition();
//            Toast.makeText(context, "Your Selected item: "+lists[position].toString(),Toast.LENGTH_SHORT).show();
//        }

        return viewHolder;
    }

    fun setList(list:ArrayList<User>){
        this.lists.addAll(list);
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return lists.size;
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.txtName?.text = lists[position].firstName + " " + lists[position].firstName;
        holder?.txtEmail?.text = lists[position].email;

        Glide
            .with(context)
            .load(lists[position].avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder?.imageView);

//        holder?.txtAge.setOnClickListener {
//            Toast.makeText(context,""+position +" Your Age is "+holder?.txtAge.text.toString(), Toast.LENGTH_SHORT ).show();
//        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.name_tv);
        val txtEmail = itemView.findViewById<TextView>(R.id.email_tv);
        val imageView = itemView.findViewById<ImageView>(R.id.user_iv);

    }

}
