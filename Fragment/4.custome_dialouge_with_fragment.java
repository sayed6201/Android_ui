
/*
======================================================================================================
======================================================================================================
                                Custom Dialouge with fragment
======================================================================================================
======================================================================================================
*/ 




/*
===================================================================================
ExampleDialog.java extends AppCompatDialogFragment 
===================================================================================
*/ 
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
 
 
public class ExampleDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private ExampleDialogListener listener;
 
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
 
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
 
        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
 
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = editTextUsername.getText().toString();
                        String password = editTextPassword.getText().toString();
                        listener.applyTexts(username, password);
                    }
                });
 
        editTextUsername = view.findViewById(R.id.edit_username);
        editTextPassword = view.findViewById(R.id.edit_password);
 
        return builder.create();
    }
 
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
 
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
 
    public interface ExampleDialogListener {
        void applyTexts(String username, String password);
    }
}





/*
===================================================================================
MainActivity extends AppCompatActivity
===================================================================================
*/ 
package com.example.application.example;
 
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 
public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    private TextView textViewUsername;
    private TextView textViewPassword;
    private Button button;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        textViewUsername = (TextView) findViewById(R.id.textview_username);
        textViewPassword = (TextView) findViewById(R.id.textview_password);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
 
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        
        //show() will display the dialouge, there is no frameLayout in MainActivity Layout
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
 
    @Override
    public void applyTexts(String username, String password) {
        textViewUsername.setText(username);
        textViewPassword.setText(password);
    }
}



/*
===================================================================================
MainActivity Layout
===================================================================================
*/ 
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.application.example.MainActivity">
 
    <TextView
        android:id="@+id/textview_username"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_centerVertical="true"
        android:gravity="center_horizontal"
        android:text="here will be our username"
        android:textSize="30sp" />
 
    <TextView
        android:id="@+id/textview_password"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/textview_username"
        android:layout_alignParentStart="true"
        android:gravity="center_horizontal"
        android:text="here will be our password"
        android:textSize="30sp" />
 
    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/textview_password"
        android:layout_centerHorizontal="true"
        android:text="open dialog" />
 
</RelativeLayout>





/*
===================================================================================
ExampleDialog Layout
===================================================================================
*/ 
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">
 
    <EditText
        android:id="@+id/edit_username"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Username" />
 
    <EditText
        android:id="@+id/edit_password"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/edit_username"
        android:layout_alignParentStart="true"
        android:hint="Password"
        android:inputType="textPassword" />
 
</RelativeLayout>