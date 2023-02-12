package com.example.thenanny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {
    Button signInBtn;
    Button forgotPasswordBtn;
    Button signUpParentBtn;
    Button signUpBabysitterBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if(!isConnectedToInternet())
        {
            Toast.makeText(LogIn.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }

        forgotPasswordBtn= findViewById(R.id.forgotPassBtn);
        forgotPasswordBtn.setOnClickListener(v -> startActivity(new Intent(LogIn.this, ForgotPasswordActivity.class)));

        signUpParentBtn= findViewById(R.id.signUpParentBtn);
        signUpParentBtn.setOnClickListener(v -> startActivity(new Intent(LogIn.this, ParentRegistration.class)));
        signUpBabysitterBtn= findViewById(R.id.signUpBabysitterBtn);
        signUpBabysitterBtn.setOnClickListener(v -> startActivity(new Intent(LogIn.this, NannyRegistration.class)));


    }



  public boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return  (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

}
