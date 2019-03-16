package com.goldenboat.waymart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;
import com.goldenboat.waymart.DataTypes.ProductDetails;
import com.goldenboat.waymart.Adapters.CartRecyclerAdapter;
import com.goldenboat.waymart.Login.LoginActivity;
import com.goldenboat.waymart.Server.Server;
import com.goldenboat.waymart.SharedPrefrence.PrefrenceHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int QR_CODE_INT = 100;
    PrefrenceHelper prefrenceHelper;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mauth;
    RelativeLayout relative_layout;
    boolean isFetched = false;
    RecyclerView recyclerView;
    ArrayList<ProductDetails> data;
    CartRecyclerAdapter adapter;
    Button start_session;
    FirebaseDatabase database;
    ArrayList<ProductDetails> allData;
    TextView total_amount,totalitem;
    ArrayList<String> list;
    AlertDialog fgi;
    ArrayList<ProductDetails> productDetails;
    Button checkout;
    int amount=0;
    int session=0;
    String myCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        prefrenceHelper = new PrefrenceHelper(this);
        relative_layout = findViewById(R.id.relative_layout);
        relative_layout.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance();
        total_amount=findViewById(R.id.total_amount);
        list = new ArrayList<>();
        totalitem = findViewById(R.id.totalitem);
        if (!prefrenceHelper.isLogin()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        checkout=findViewById(R.id.checkout);
        start_session = findViewById(R.id.start_session);
        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartRecyclerAdapter(this);
        data = new ArrayList<>();
        adapter.givedata(data);
        recyclerView.setAdapter(adapter);


        fetchAllData();

        start_session.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QRCodeScan.class);
                startActivityForResult(i, QR_CODE_INT);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                View checkout_view = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.dialog_checkout,null);



                Builder checkoutDialog= new Builder(MainActivity.this);
                checkoutDialog.setCancelable(true);
                checkoutDialog.setView(checkout_view);
                final RecyclerView rv=checkout_view.findViewById(R.id.checkout_recycler);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv.setAdapter(adapter);
                final RelativeLayout layout = checkout_view.findViewById(R.id.relative_layout_successful);
                layout.setVisibility(View.GONE);
                final Button checkout_button= checkout_view.findViewById(R.id.proceed_pay);
                checkout_button.setText("       Proceed to Pay : "+amount+"        ");
                checkout_button.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        DatabaseReference ref = database.getReference("carts").child(myCart);
                        ArrayList<ProductDetails> d=new ArrayList<>();
                        ref.setValue(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override public void onSuccess(Void aVoid) {
                                layout.setVisibility(View.VISIBLE);
                                checkout_button.setVisibility(View.GONE);
                                rv.setVisibility(View.GONE);
                                ArrayList<ProductDetails> dd=new ArrayList<>();
                                adapter.givedata(dd);
                                total_amount.setText("0");
                                totalitem.setText("0 items in Cart");

                            }
                        });


                    }
                });
                AlertDialog checkoutDialogmain = checkoutDialog.create();
                checkoutDialogmain .show();










            }
        });
    }

    public void startSession(String s){
        session=1;
        start_session.setVisibility(View.GONE);
        android.app.AlertDialog.Builder buildd = new android.app.AlertDialog.Builder(MainActivity.this);
        View vv = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_loading,null);
        buildd.setView(vv);
        buildd.setMessage("Loading Your Cart...");
        buildd.setCancelable(false);
        fgi = buildd.create();
        fgi.show();
        myCart = s;
        startTheSession(myCart);
    }

    private void fetchAllData() {
        DatabaseReference ref = database.getReference("items");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<ProductDetails>> t =
                        new GenericTypeIndicator<ArrayList<ProductDetails>>() {};
                allData = dataSnapshot.getValue(t);
                for (int i=0;i<allData.size();i++){
                    Log.e("f",allData.get(i).getId());
                }
                isFetched = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void startTheSession(final String s) {

        DatabaseReference ref = database.getReference("carts").child(s);
        relative_layout.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                if (list != null)
                list.clear();
                list = dataSnapshot.getValue(t);
                goForIt(s);
                fgi.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void goForIt(final String s) {

        if (!isFetched){
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    goForIt(s);
                }
            }, 1000);
        } else {
            data.clear();
            int i,j=0,k;
            if (list == null || list.size()==0)return;

            boolean isSame=false;
            for (i=0; i<list.size(); i++) {
                for (j=0; j<list.size(); j++) {
                    if (i==j)continue;
                    if (list.get(i).equals(list.get(j))){
                        isSame = true;
                        break;
                    }
                }
                if (isSame) break;
            }
            if (isSame) {
                list.remove(i);
                list.remove(j - 1);
                DatabaseReference ref = database.getReference("carts").child(s);
                ref.setValue(list);
            }

            Log.e("TAG", String.valueOf(list.size()));

            for (i=0; i<list.size(); i++) {
                for (j = 0; j<allData.size(); j++) {
                    if (allData.get(j).getId().substring(4,12).equals(list.get(i).substring(4,12))) {


                        Log.e("TAAg",String.valueOf(data.size())+":"+String.valueOf(list.size()));

                        for (k=0;k<data.size();k++) {
                            Log.e("TAG",data.get(k).getId().substring(4,12)+":"+list.get(i).substring(4,12));
                            if (data.get(k).getId().substring(4,12).equals(list.get(i).substring(4,12))) {
                                data.get(k).countplus();
                                Log.e("TAG",String.valueOf(data.get(k).getCount())+" Added 1");

                                break;
                            }
                        }
                        if (data.size()>0)
                        Log.e("3rd", String.valueOf(data.get(data.size()-1).getCount()));

                        if (k==data.size()) {
                            ProductDetails productDetails = new ProductDetails();
                            productDetails.setCount(allData.get(j).getCount());
                            productDetails.setPic_url(allData.get(j).getPic_url());
                            productDetails.setId(allData.get(j).getId());
                            productDetails.setPrice(allData.get(j).getPrice());
                            productDetails.setProduct_title(allData.get(j).getProduct_title());
                            data.add(productDetails);
                        }
                    }
                }
            }

            adapter.givedata(data);
            int price=0,c=0;
            for (i=0;i<data.size();i++) {
                price+=data.get(i).getPrice()*data.get(i).getCount();
                c+=data.get(i).getCount();
            }
            totalitem.setText(String.valueOf(c)+" items in Cart");
            total_amount.setText(String.valueOf(price));
            amount=price;
        }

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
                Intent intent = new Intent(MainActivity.this, Server.class);
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
        if (session==0) {
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
        } else {
            Builder buildd = new Builder(MainActivity.this);
            buildd.setMessage("Do You Want To Exit this Cart");
            buildd.setCancelable(true);
            buildd.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    total_amount.setText("0");
                    totalitem.setText("0 items in Cart");
                    session=0;
                    start_session.setVisibility(View.VISIBLE);
                    data.clear();
                    relative_layout.setVisibility(View.GONE);
                    adapter.givedata(data);
                }
            }).setNegativeButton("NO", null);
            AlertDialog fgi = buildd.create();
            fgi.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK && requestCode==QR_CODE_INT) {
            String s = data.getStringExtra("QR_Result");
            if (s.length()<8) {
                Toast.makeText(getApplicationContext(),"Not a Valid Cart",Toast.LENGTH_SHORT)
                    .show();
                return;
            }
            if (s.substring(0,4).equals("CART")) {
                startSession(s);
            } else {
                Toast.makeText(getApplicationContext(),"Not a Valid Cart",Toast.LENGTH_SHORT)
                        .show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
