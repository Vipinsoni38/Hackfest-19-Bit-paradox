package com.goldenboat.waymart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.goldenboat.waymart.Login.LoginActivity;
import com.goldenboat.waymart.SharedPrefrence.PrefrenceHelper;

public class MainActivity extends AppCompatActivity {

    PrefrenceHelper prefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefrenceHelper = new PrefrenceHelper(this);
        if (!prefrenceHelper.isLogin()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
}
