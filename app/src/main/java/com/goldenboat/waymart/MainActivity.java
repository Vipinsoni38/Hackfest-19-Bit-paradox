package com.goldenboat.waymart;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import com.goldenboat.waymart.Fragments.Cart;
import com.goldenboat.waymart.Login.LoginActivity;
import com.goldenboat.waymart.OrderHistory.OrderHistory;
import com.goldenboat.waymart.SharedPrefrence.PrefrenceHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    PrefrenceHelper prefrenceHelper;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        prefrenceHelper = new PrefrenceHelper(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (!prefrenceHelper.isLogin()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.prev_orders:
                Intent intent = new Intent(MainActivity.this, OrderHistory.class);
                startActivity(intent);
                break;
            case R.id.logout:
                Builder logoutDialog= new Builder(MainActivity.this);
                logoutDialog.setMessage("Log Out?");
                logoutDialog.setCancelable(true);
                logoutDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefrenceHelper.setIsLogin(false);
                        mauth.signOut();

                    }
                }).setNegativeButton("NO", null);
                AlertDialog d = logoutDialog.create();
                d.show();

                break;

        }

        return super.onOptionsItemSelected(item);

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
    @Override
    public void onBackPressed() {
        Builder buildd = new Builder(MainActivity.this);

        buildd.setMessage("Do You Want To Exit");
        buildd.setTitle("Way Mart");
        buildd.setCancelable(true);
        buildd.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        }).setNegativeButton("NO", null);
        AlertDialog fgi = buildd.create();
        fgi.show();
    }
}
