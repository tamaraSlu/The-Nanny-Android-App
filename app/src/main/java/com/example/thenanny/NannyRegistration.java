package com.example.thenanny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thenanny.databinding.NannyRegistrationFormBinding;
import com.example.thenanny.databinding.ParentRegistrationFormBinding;
import com.example.thenanny.dto.NannyDetails;
import com.example.thenanny.dto.ParentDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NannyRegistration extends AppCompatActivity {
    private NannyRegistrationFormBinding binding;
    private final FirebaseFirestore storage= FirebaseFirestore.getInstance();
    private FirebaseAuth usersAuth;

    final Calendar myCalendar = Calendar.getInstance();
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = NannyRegistrationFormBinding.inflate(getLayoutInflater());
        usersAuth=FirebaseAuth.getInstance();
        View view = binding.getRoot();
        setContentView(view);

        //Date picker
        DatePickerDialog.OnDateSetListener date = (view12, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.birthDateField.setText(dayOfMonth +"/"+ monthOfYear +"/"+ year);
            updateLabel();
        };

        binding.birthDateField.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                binding.birthDateField.setShowSoftInputOnFocus(false);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                new DatePickerDialog(NannyRegistration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            return false;
        });




        binding.nextButton.setOnClickListener(view1 -> {
            String firstName= Objects.requireNonNull(binding.firstNameField.getText()).toString().trim();
            String lastName= Objects.requireNonNull(binding.lastNameField.getText()).toString().trim();
            String email= Objects.requireNonNull(binding.emailField.getText()).toString().trim();
            String phone= Objects.requireNonNull(binding.phoneField.getText()).toString().trim();
            String date_of_birth= Objects.requireNonNull(binding.birthDateField.getText()).toString().trim();
            String start_work_year= Objects.requireNonNull(binding.StartWorkYearField.getText()).toString().trim();
            String password= Objects.requireNonNull(binding.passwordField.getText()).toString().trim();
            String repeatPassword= Objects.requireNonNull(binding.repeatPasswordField.getText()).toString().trim();


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
            //start work date error
            else if(start_work_year.length()<1) {
                binding.StartWorkYearField.setError("Enter starting Year");
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
            else {
                String myFormat="dd/MM/yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                try {
                    registerUser(firstName,lastName,email,phone,password,null,null,dateFormat.parse(date_of_birth),start_work_year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Everything is fine", Toast.LENGTH_SHORT).show();

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
        binding.StartWorkYearField.setText("");
        binding.passwordField.setText("");
        binding.repeatPasswordField.setText("");



    }

    public int getAge(int year, int month, int dayOfMonth)
    {
        return Period.between(LocalDate.of(year, month, dayOfMonth),
                LocalDate.now()).getYears();
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void updateLabel()
    {
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        binding.birthDateField.setText(dateFormat.format(myCalendar.getTime()));
        binding.birthDateField.setError(null);
    }

    private void registerUser (String firstName, String lastName, String email, String phone, String password, String address, Integer hourlyWage, Date birthDate, String startWorkDate){

        usersAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(NannyRegistration.this, task -> {
            if(task.isSuccessful())
            {
                NannyDetails nanny= new NannyDetails(firstName,lastName, email, password,phone,hourlyWage,address,birthDate, startWorkDate);
                Intent intent=new Intent(NannyRegistration.this, NannyProfileAdd.class);
                intent.putExtra("userDetails",nanny);
                intent.putExtra("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());


                startActivity(intent);
            }
            else{
                Toast.makeText(NannyRegistration.this,"Error occurred in Registration. Please try again!",Toast.LENGTH_SHORT).show();
            }
        });
    }


}


