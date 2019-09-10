package com.sayed.kotlintesterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var tv: TextView?=null;
    lateinit var et: EditText;
    lateinit var btn: Button;
    var num1:Double = 20.5;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
        btn = findViewById(R.id.button);

//        tv?.setText("sayed");
        tv?.text = "hellow";
        et.setHint("Enter Your Text Here");

        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv?.setText(s);
            }
        })

        btn.setOnClickListener {
            num1 = num1 + et.text.toString().toDouble();
            Toast.makeText(this@MainActivity,"Result: "+num1, Toast.LENGTH_SHORT).show();
        }
        
    }

    fun clicker(v: View){
        if(v.id == R.id.imageView){
            Toast.makeText(this@MainActivity," Imageview ", Toast.LENGTH_SHORT).show();
        }
    }

}
