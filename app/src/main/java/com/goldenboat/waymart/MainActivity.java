package com.goldenboat.waymart;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.goldenboat.waymart.Fragments.Cart;
import com.goldenboat.waymart.Login.LoginActivity;
import com.goldenboat.waymart.SharedPrefrence.PrefrenceHelper;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    PrefrenceHelper prefrenceHelper;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefrenceHelper = new PrefrenceHelper(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (!prefrenceHelper.isLogin()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new Cart();
                break;
            default:
                fragment = new Cart();

        }
        getSupportFragmentManager().beginTransaction().
            replace(R.id.fragment_container, fragment)
            .commit();
        return false;
    }
}
