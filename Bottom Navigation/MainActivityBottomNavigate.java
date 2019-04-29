package com.example.daffodilpc.bottomnavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivityBottomNavigate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_bottom_navigate);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        BottomNavFrag frag = new BottomNavFrag();
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.fav:
                                bundle.putString("text", "Favourite Fragment");
                                break;
                            case R.id.add:
                                bundle.putString("text", "Add Fragment");
                                break;
                            case R.id.msg:
                                bundle.putString("text", "Message in Fragment");
                                break;
                        }
                        frag.setArguments(bundle);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, frag);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        BottomNavFrag initFrag = new BottomNavFrag();
        Bundle bundle = new Bundle();
        bundle.putString("text", "Favourite Fragment");
        initFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, initFrag);
        transaction.commit();

    }
}
