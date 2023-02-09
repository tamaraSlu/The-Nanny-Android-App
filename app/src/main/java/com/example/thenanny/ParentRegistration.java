package com.example.thenanny;

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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Locale;

public class ParentRegistration extends AppCompatActivity {
    private ParentRegistrationFormBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ParentRegistrationFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Date picker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                binding.birthDateField.setText(dayOfMonth +"/"+ monthOfYear + year);
                updateLabel();
            }
        };

        binding.birthDateField.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                binding.birthDateField.setShowSoftInputOnFocus(false);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                new DatePickerDialog(ParentRegistration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            return false;
        });


        //Submit Button
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName= binding.firstNameField.getText().toString().trim();
                String lastName= binding.lastNameField.getText().toString().trim();
                String email= binding.emailField.getText().toString().trim();
                String phone= binding.phoneField.getText().toString().trim();
                String date_of_birth= binding.birthDateField.getText().toString().trim();
                String password= binding.passwordField.getText().toString().trim();
                String repeatPassword= binding.repeatPasswordField.getText().toString().trim();
                String adress= binding.adressField.getText().toString().trim();
                String num_of_children= binding.numOfChildrenField.getText().toString().trim();


                //First name error
                if (firstName.equals("")) {
                    binding.firstNameField.setError("Enter your first name");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //Last name error
                else if(lastName.equals("")) {
                    binding.lastNameField.setError("Enter your last name");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                else if(date_of_birth.length()<1) {
                    binding.birthDateField.setError("Enter Date of Birth");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                else if(myCalendar.get(Calendar.YEAR) > 2004)
                {
                    binding.birthDateField.setError("This app is for adults only");
                    binding.birthDateField.setText("");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();

                }
                else if(getAge(myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)) < 18)
                {
                    binding.birthDateField.setError("This app is for adults only");
                    binding.birthDateField.setText("");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //EmailAddress error
                else if(email.equals("")) {
                    binding.emailField.setError("You mast enter an email address");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();

                }
                else if(!isEmailValid(email)) {
                    binding.emailField.setError("This email address doesn't exist");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                    //phone number error
                else if(phone.length()<10 ) {
                    binding.phoneField.setError("Incorrect phone number");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                    //Password error
                else if(password.length()<6)
                {
                    binding.passwordField.setError("Password must contain at least 6 characters");
                    binding.passwordField.setText("");
                    binding.repeatPasswordField.setText("");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                //CheckPassword error
                else if(!repeatPassword.equals(binding.repeatPasswordField.getText().toString()))
                {
                    binding.repeatPasswordField.setError("Passwords doesn't match");
                    binding.repeatPasswordField.setText("");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                else if (adress.equals("")) {
                    binding.adressField.setError("Enter your adress");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                else if (num_of_children.equals("")) {
                    binding.numOfChildrenField.setError("Enter number of childen");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                else if(num_of_children.length()>1) {
                    binding.numOfChildrenField.setError("Number of children is incorrect");
                    Toast.makeText(getApplicationContext(), "Some fields are incorrect or missing", Toast.LENGTH_SHORT).show();
                }
                else  {
                    Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();

                }


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
        binding.birthDateField.setText("");
        binding.passwordField.setText("");
        binding.repeatPasswordField.setText("");
        binding.adressField.setText("");


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
    private void updateLabel()
    {
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.birthDateField.setText(dateFormat.format(myCalendar.getTime()));
        binding.birthDateField.setError(null);
    }


}