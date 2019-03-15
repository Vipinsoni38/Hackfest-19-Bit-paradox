package com.goldenboat.waymart.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.goldenboat.waymart.MainActivity;
import com.goldenboat.waymart.R;
import com.goldenboat.waymart.SharedPrefrence.PrefrenceHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 100;
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    PrefrenceHelper prefrenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.gsignin);
        configureSignIn();
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        prefrenceHelper = new PrefrenceHelper(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        //AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        //builder.setTitle("Logging in");
        //builder.setCancelable(false);
        //final AlertDialog dialog = builder.create();
        //dialog.show();
        android.app.AlertDialog.Builder buildd = new android.app.AlertDialog.Builder(LoginActivity.this);
        View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_loading,null);
        buildd.setView(v);
        buildd.setMessage("Logging in..");
        buildd.setCancelable(false);
        final AlertDialog fgi = buildd.create();
        fgi.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    fgi.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        prefrenceHelper.setIsLogin(true);
                        prefrenceHelper.setUID(task.getResult().getUser().getUid());
                        prefrenceHelper.setEmail(task.getResult().getUser().getEmail());

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Sign In Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }



    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    public void configureSignIn() {

        // Configure sign-in to request the user’s basic profile like name and email
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("55292560883-e8kaikds7dukrqe2kf35qf8cgdb1et5p.apps.googleusercontent.com")
            .requestEmail()
            .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

    }


}
