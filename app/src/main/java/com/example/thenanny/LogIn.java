package com.example.thenanny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thenanny.dto.NannyDetails;
import com.example.thenanny.dto.ParentDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private Button signInBtn;
    private Button forgotPasswordBtn;
    private Button signUpParentBtn;
    private Button signUpBabysitterBtn;
    private EditText email;
    private EditText password;
    private FirebaseAuth usersAuth;
    private FirebaseFirestore storage= FirebaseFirestore.getInstance();
    private String userId;
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
        usersAuth=FirebaseAuth.getInstance();

        forgotPasswordBtn= findViewById(R.id.forgotPassBtn);
        forgotPasswordBtn.setOnClickListener(v -> startActivity(new Intent(LogIn.this, ForgotPasswordActivity.class)));

        signInBtn=findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(v->signIn() );

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

    private void signIn(){
        email= findViewById(R.id.emailField);
        password=findViewById(R.id.passwordField);
        usersAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(LogIn.this,task->{
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(), "Signed in successfully!", Toast.LENGTH_LONG).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                storage= FirebaseFirestore.getInstance();
                if (user != null) {
                    userId=user.getUid();
                    DocumentReference documentReference= storage.collection("users.parents").document(userId);
                    documentReference.get().addOnCompleteListener(taskParent -> {
                        if (taskParent.isSuccessful()) {
                            if (taskParent.getResult().exists()) {
                                ParentDetails parent = taskParent.getResult().toObject(ParentDetails.class);
                                Log.i("",parent.toString());
                            } else {
                                DocumentReference documentReference1 = storage.collection("users.nanny").document(userId);
                                documentReference1.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        if (task1.getResult().exists()) {
                                            NannyDetails nanny = task1.getResult().toObject(NannyDetails.class);
                                            Log.i("",nanny.toString());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
//                startActivity(new Intent(LogIn.this, HomePage.class));
            }else{
                Toast.makeText(getApplicationContext(),"Error occurred:"+Objects.requireNonNull(task.getException()) , Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}
