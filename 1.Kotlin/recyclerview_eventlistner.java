package com.sayed.kotlintesterapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sayed.kotlintesterapp.R
import com.sayed.kotlintesterapp.models.User

class MyRv (val context: Context, val lists:ArrayList<User>): RecyclerView.Adapter<MyRv.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.rv_item_layout, parent, false)

        val viewHolder: ViewHolder =  ViewHolder(v);
        v.setOnClickListener{
            var position: Int = viewHolder.getAdapterPosition();
            Toast.makeText(context, "Your Selected item: "+lists[position].toString(),Toast.LENGTH_SHORT).show();
        }

        return viewHolder;
    }

    override fun getItemCount(): Int {
        return lists.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.txtName?.text = lists[position].name
        holder?.txtAge?.text = lists[position].age.toString()

        holder?.txtAge.setOnClickListener {
            Toast.makeText(context,""+position +" Your Age is "+holder?.txtAge.text.toString(), Toast.LENGTH_SHORT ).show();
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txt_name)
        val txtAge = itemView.findViewById<TextView>(R.id.txt_age)
    }

}



==============================================================================================
MainActivity.kt
==============================================================================================
package com.sayed.kotlintesterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayed.kotlintesterapp.adapters.MyRv
import com.sayed.kotlintesterapp.models.User

class MainActivity : AppCompatActivity() {

    var tv: TextView?=null;
    lateinit var rv: RecyclerView;
    lateinit var lists: ArrayList<User>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        rv = findViewById(R.id.rv);
        
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        tv?.text = "Welcome to Rv Example";

        lists = ArrayList<User>();
        lists.add( User(name="Sayed", age = 25) );
        lists.add( User(name="salman", age = 18) );
        lists.add( User(name="samir", age = 10) );
        lists.add( User(name="sumiya", age = 13) );
        lists.add( User(name="Marium", age = 13) );

        val adapter = MyRv(this, lists);
        rv.adapter = adapter;

    }


}
