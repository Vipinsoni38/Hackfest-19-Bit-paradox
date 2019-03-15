package com.goldenboat.waymart;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import com.goldenboat.waymart.DataTypes.ProductDetails;
import com.goldenboat.waymart.Adapters.CartRecyclerAdapter;
import com.goldenboat.waymart.Login.LoginActivity;
import com.goldenboat.waymart.OrderHistory.OrderHistory;
import com.goldenboat.waymart.SharedPrefrence.PrefrenceHelper;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PrefrenceHelper prefrenceHelper;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mauth;
    RelativeLayout relative_layout;

    RecyclerView recyclerView;
    ArrayList<ProductDetails> data;
    CartRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        prefrenceHelper = new PrefrenceHelper(this);
        relative_layout = findViewById(R.id.relative_layout);
        //relative_layout.setVisibility(View.GONE);


        if (!prefrenceHelper.isLogin()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        data = new ArrayList<>();

        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        adapter.givedata(data);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }).setNegativeButton("NO", null);
                AlertDialog d = logoutDialog.create();
                d.show();

                break;

        }

        return super.onOptionsItemSelected(item);

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
