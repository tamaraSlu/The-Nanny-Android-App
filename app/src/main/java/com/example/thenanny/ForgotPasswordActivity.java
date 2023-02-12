package com.example.thenanny;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.thenanny.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth usersAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersAuth=FirebaseAuth.getInstance();
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.submitButton.setOnClickListener(v ->  {
            String email = Objects.requireNonNull(binding.emailField.getText()).toString().trim();
            if (email.isEmpty()) {
                binding.emailField.setError("You mast enter an email address");
                Toast.makeText(getApplicationContext(), "Enter correct Email", Toast.LENGTH_SHORT).show();

            } else if (!isEmailValid(email)) {
                binding.emailField.setError("Invalid email address");
                Toast.makeText(getApplicationContext(), "Enter correct Email", Toast.LENGTH_SHORT).show();
            }else{
                usersAuth.sendPasswordResetEmail(email).addOnCompleteListener(ForgotPasswordActivity.this, task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Reset password request was sent to your Email", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                    }


                });
            }
        });
        clear();
    }

    private void clear()
    {
        binding.emailField.setText("");
    }

    private boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}