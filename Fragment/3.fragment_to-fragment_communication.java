/*
======================================================================================================
======================================================================================================
                    Fragment to Fagment Data Transection using Interface
======================================================================================================
======================================================================================================
*/ 
 



/*
======================================================================================================
FragmentA.java 
======================================================================================================
*/ 
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
 
 
public class FragmentA extends Fragment {
    private FragmentAListener listener;
    private EditText editText;
    private Button buttonOk;
 
 //this interface will pass the input to the Activity that implements it,
//here Charsequence is used , CharSequence input = editText.getText(), no need of toString()
    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }
 
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_a, container, false);
 
        editText = v.findViewById(R.id.edit_text);
        buttonOk = v.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = editText.getText();
                //data is send to the Interface method
                listener.onInputASent(input);
            }
        });
 
        return v;
    }
 
 //this method will receive data from Activity and update Edittext
    public void updateEditText(CharSequence newText) {
        editText.setText(newText);
    }
 
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //checking if context has implemented FragmentAListener interface
        if (context instanceof FragmentAListener) {
            listener = (FragmentAListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }
 
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}




/*
======================================================================================================
FragmentB.java 
======================================================================================================
*/ 
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
 
 
public class FragmentB extends Fragment {
    private FragmentBListener listener;
    private EditText editText;
    private Button buttonOk;
 
    public interface FragmentBListener {
        void onInputBSent(CharSequence input);
    }
 
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_b, container, false);
 
        editText = v.findViewById(R.id.edit_text);
        buttonOk = v.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = editText.getText();
                listener.onInputBSent(input);
            }
        });
 
        return v;
    }
 
    public void updateEditText(CharSequence newText) {
        editText.setText(newText);
    }
 
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBListener) {
            listener = (FragmentBListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentBListener");
        }
    }
 
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}





/*
======================================================================================================
MainActivity.java 
======================================================================================================
*/  
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
 
public class MainActivity extends AppCompatActivity implements FragmentA.FragmentAListener, FragmentB.FragmentBListener {
    private FragmentA fragmentA;
    private FragmentB fragmentB;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
 
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_a, fragmentA)
                .replace(R.id.container_b, fragmentB)
                .commit();
    }
 
    @Override
    public void onInputASent(CharSequence input) {
        fragmentB.updateEditText(input);
    }
 
    @Override
    public void onInputBSent(CharSequence input) {
        fragmentA.updateEditText(input);
    }
}




/*
======================================================================================================
MainActivity Layout
======================================================================================================
*/  
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.codinginflow.fragmentcommunicationexample.MainActivity">
 
    <FrameLayout
        android:id="@+id/container_a"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
 
    <FrameLayout
        android:id="@+id/container_b"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
 
</LinearLayout>




/*
======================================================================================================
Fragment A and B Layout
======================================================================================================
*/  
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/holo_green_light"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    android:padding="16dp">
 
    <EditText
        android:id="@+id/edit_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
 
    <Button
        android:id="@+id/button_ok"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Ok" />
 
</LinearLayout>