
/*
======================================================================================================
======================================================================================================
                Activity to Fagment Data Transection using bundle/ Factory method
======================================================================================================
======================================================================================================
*/ 




/*
======================================================================================================
MainActivity.java 
======================================================================================================
*/ 
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
 
public class MainActivity extends AppCompatActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        //newInstance is a static menthod, containing bundle object...
        ExampleFragment fragment = ExampleFragment.newInstance("example text ", 123);

        //FrameLayuout is used, it id replaced by fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}



/*
======================================================================================================
ExampleFragment.java 
======================================================================================================
*/
 
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
 
public class ExampleFragment extends Fragment {
    private static final String ARG_TEXT = "argText";
    private static final String ARG_NUMBER = "argNumber";
 
    private String text;
    private int number;
 
    public static ExampleFragment newInstance(String text, int number) {
        ExampleFragment fragment = new ExampleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }
 
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.example_fragment, container, false);
        TextView textView = v.findViewById(R.id.text_view_fragment);
 
        if (getArguments() != null) {
            text = getArguments().getString(ARG_TEXT);
            number = getArguments().getInt(ARG_NUMBER);
        }
 
        textView.setText(text + number);
 
        return v;
    }
}



/*
======================================================================================================
MainActivity Layout............... 
======================================================================================================
*/ 
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.codinginflow.newinstanceexample.MainActivity" />