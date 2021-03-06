package com.customdialog.sayed.customdialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button popupButton;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popupButton = findViewById(R.id.popupButton);
        myDialog = new Dialog(this);
    }

    public void dialogPopUp(View view){
        TextView txtclose;
        myDialog.setContentView(R.layout.dialogbox_layout);
        Button btnFollow = (Button) myDialog.findViewById(R.id.followButton);
        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    }


===============================================================
Dialod
===============================================================
new AlertDialog.Builder(this)
.setTitle("Title")
.setMessage("Do you really want to whatever?")
.setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

    public void onClick(DialogInterface dialog, int whichButton) {
        Toast.makeText(MainActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
    }})
 .setNegativeButton(android.R.string.no, null).show();
