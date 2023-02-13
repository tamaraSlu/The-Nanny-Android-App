package com.example.thenanny;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.thenanny.dto.NannyDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.List;

public class NannyProfileAdd extends AppCompatActivity implements View.OnClickListener {
    EditText cityField, wageField;
    RangeSlider agesField;
    Button uploadIdBtn, submitButton;
    ImageView profilePicture, editProfilePicture;
    String userID;
    NannyDetails nannyDetails;
    ProgressDialog progressDialog;
    private static final int PICKIMAGE_REQUEST = 100;
    private static final int PICKFILE_REQUEST = 300;
    Uri imageUri;
    Uri fileUri;
    private FirebaseFirestore storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_profile_add);

        if (!isConnectedToInternet()) {
            Toast.makeText(NannyProfileAdd.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
        storage= FirebaseFirestore.getInstance();
        userID = getIntent().getStringExtra("userId");
        nannyDetails = (NannyDetails) getIntent().getSerializableExtra("userDetails");
        profilePicture = findViewById(R.id.profilePicture);
        progressDialog = new ProgressDialog(NannyProfileAdd.this);
        agesField = findViewById(R.id.ageSliderField);
        wageField = (EditText) findViewById(R.id.wageField);
        cityField = (EditText) findViewById(R.id.cityField);


        editProfilePicture = findViewById(R.id.editProfilePicture);
        editProfilePicture.setOnClickListener((View.OnClickListener) this);

        uploadIdBtn = (Button) findViewById(R.id.uploadIdBtn);
        uploadIdBtn.setOnClickListener((View.OnClickListener) this);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener((View.OnClickListener) this);
    }

    // Check  internet connection
    public boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

    @Override
    public void onClick(View v) {
        if (v == submitButton)
            Done();
        else if (v == editProfilePicture)
            selectImage();

        else if (v == uploadIdBtn)
            selectFile();

    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICKFILE_REQUEST);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICKIMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKIMAGE_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePicture.setImageURI(imageUri);
        }

        if (requestCode == PICKFILE_REQUEST && data != null && data.getData() != null) {
            fileUri = data.getData();
        }

    }
    public void Done() {
        List<Float> ageRange=agesField.getValues();
        if (cityField.getText().toString().isEmpty()) {
            cityField.setError("Error,try again");
            cityField.setText("");
        } else if (wageField.getText().toString().equals("")) {
            wageField.setError("Error,try again");
            wageField.setText("");
        } else {
            UploadImage();
            UploadID();
            registerUser(nannyDetails,cityField.getText().toString(),Math.round(ageRange.get(0)),Math.round(ageRange.get(1)),Integer.parseInt(wageField.getText().toString()));
        }
    }

    private void UploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        String fileName = "profile_image";

        storageReference = FirebaseStorage.getInstance().getReference("nanny_files/" + userID + "/" + fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    //profilePicture.setImageURI(null);
                    Toast.makeText(NannyProfileAdd.this, "Successfully Uploaded profile photo", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(NannyProfileAdd.this, "Profile photo uploading failure", Toast.LENGTH_SHORT).show());
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    private void UploadID() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        String fileName = "id";
        storageReference = FirebaseStorage.getInstance().getReference("nanny_files/" + userID + "/" + fileName);
        storageReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(NannyProfileAdd.this, "Successfully Uploaded id file", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> Toast.makeText(NannyProfileAdd.this, "ID uploading failure", Toast.LENGTH_SHORT).show());
        if (progressDialog.isShowing())
            progressDialog.dismiss();

    }

    private void registerUser(NannyDetails nanny, String address, Integer minAge, Integer maxAge, Integer hourlyWage) {

        DocumentReference document= storage.collection("users.nanny").document(userID);
        nanny.setAddress(address);
        nanny.setMinAge(minAge);
        nanny.setMaxAge(maxAge);
        nanny.setHourlyWage(hourlyWage);
        document.set(nanny.useDetailsToMap()).addOnSuccessListener(unused -> Toast.makeText(NannyProfileAdd.this, "User's profile created successfully", Toast.LENGTH_LONG).show());
        Log.i("",nanny.toString());
        //TODO: insert here next screen
//                    startActivity();

    }

}