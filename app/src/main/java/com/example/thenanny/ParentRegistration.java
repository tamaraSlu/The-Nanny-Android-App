package com.example.thenanny;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.AsyncQueryHandler;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.thenanny.databinding.ParentRegistrationFormBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ParentRegistration extends AppCompatActivity {
    private ParentRegistrationFormBinding binding;
//    private FirebaseStorage storage= FirebaseStorage.getInstance();
//    private FirebaseAuth usersAuth;
    final Calendar myCalendar = Calendar.getInstance();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        usersAuth=FirebaseAuth.getInstance();
        binding = ParentRegistrationFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //Submit Button
        binding.submitButton.setOnClickListener(view1 -> {
                String firstName = Objects.requireNonNull(binding.firstNameField.getText()).toString().trim();
                String lastName = Objects.requireNonNull(binding.lastNameField.getText()).toString().trim();
                String email = Objects.requireNonNull(binding.emailField.getText()).toString().trim();
                String phone = Objects.requireNonNull(binding.phoneField.getText()).toString().trim();
                String password = Objects.requireNonNull(binding.passwordField.getText()).toString().trim();
                String repeatPassword = Objects.requireNonNull(binding.repeatPasswordField.getText()).toString().trim();
                String address = Objects.requireNonNull(binding.addressField.getText()).toString().trim();
                String num_of_children = Objects.requireNonNull(binding.numOfChildrenField.getText()).toString().trim();
                //First name error
                if (firstName.equals("")) {
                    binding.firstNameField.setError("Enter your first name");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //Last name error
                else if (lastName.equals("")) {
                    binding.lastNameField.setError("Enter your last name");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //EmailAddress error
                else if (email.equals("")) {
                    binding.emailField.setError("You mast enter an email address");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();

                } else if (!isEmailValid(email)) {
                    binding.emailField.setError("This email address doesn't exist");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //phone number error
                else if (phone.length() < 10) {
                    binding.phoneField.setError("Incorrect phone number");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //Password error
                else if (password.length() < 6) {
                    binding.passwordField.setError("Password must contain at least 6 characters");
                    binding.passwordField.setText("");
                    binding.repeatPasswordField.setText("");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //CheckPassword error
                else if (!repeatPassword.equals(binding.repeatPasswordField.getText().toString())) {
                    binding.repeatPasswordField.setError("Passwords doesn't match");
                    binding.repeatPasswordField.setText("");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                } else if (address.equals("")) {
                    binding.addressField.setError("Enter your address");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                } else if (num_of_children.equals("")) {
                    binding.numOfChildrenField.setError("Enter number of childen");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                } else if (num_of_children.length() > 1) {
                    binding.numOfChildrenField.setError("Number of children is incorrect");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();
//                    registerUser(email,password);

                }
        });


        Clear();
    }

    public void Clear()
    {
        binding.firstNameField.setText("");
        binding.lastNameField.setText("");
        binding.emailField.setText("");
        binding.phoneField.setText("");
        binding.passwordField.setText("");
        binding.repeatPasswordField.setText("");
        binding.addressField.setText("");
        binding.numOfChildrenField.setText("");


    }

    public int getAge(int year, int month, int dayOfMonth)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            return Period.between(LocalDate.of(year, month, dayOfMonth),
                    LocalDate.now()).getYears();
        }
        return -1;
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
//    private void registerUser (String email,String password){
//        usersAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(ParentRegistration.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(ParentRegistration.this,"Registration Success",Toast.LENGTH_SHORT).show();
//                    //TODO: insert here next screen
////                    startActivity();
//                }
//                else{
//                    Toast.makeText(ParentRegistration.this,"Error occurred in Registration. Please try again!",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


}