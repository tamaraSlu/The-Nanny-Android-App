package com.example.thenanny;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class NannyProfileAdd extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    EditText cityField,agesField,wageField;
    Button uploadIdBtn,submitButton ;
    ImageView profilePicture,editProfilePicture;
    String UserName;
    ProgressDialog progressDialog;
    private  static  final  int REQUEST_CAMERA = 200;
    private  static  final  int REQUEST_STORAGE = 300;

    private  static  final  int CAMERA = 1;
    private  static  final  int STORAGE = 2;


    Uri uri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_profile_add);

        if(!isConnectedToInternet())
        {
            Toast.makeText(NannyProfileAdd.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
        UserName = getIntent().getStringExtra("UserName");
        profilePicture=(ImageView) findViewById(R.id.profilePicture);
        //profilePicture.setOnTouchListener((View.OnTouchListener) this);
        progressDialog = new ProgressDialog(NannyProfileAdd.this);
        agesField=(EditText)findViewById(R.id.agesField);
        wageField=(EditText)findViewById(R.id.wageField);
        cityField=(EditText)findViewById(R.id.cityField);


        submitButton =(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener((View.OnClickListener) this);
    }

    // Check  internet connection
    public boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(v==profilePicture)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(NannyProfileAdd.this);
            adb.setCancelable(true);
            adb.setTitle("Profile photo");
            adb.setIcon(R.drawable.profile);

            adb.setPositiveButton("Take a picture", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermission(CAMERA);
                }
            });
            adb.setNegativeButton("Choose from gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermission(STORAGE);
                }
            });
            adb.setNeutralButton("delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    profilePicture.setImageBitmap(null);
                    profilePicture.setImageURI(null);
                    // FirebaseStorage storage = FirebaseStorage.getInstance();
                    //StorageReference storageRef = storage.getReference();
                    //StorageReference ProfilePhoto = storageRef.child("ProfilePhoto/users/" + UserName + ".jpg");
                    //   ProfilePhoto.delete();
                }
            });
            adb.create().show();
        }
        return false;

    }
    @Override
    public void onClick(View v)
    {
        if(v==submitButton)
            Done();
    }
    public void Done()
    {
        if(cityField.getText().toString().isEmpty())
        {
            cityField.setError("Error,try again");
            cityField.setText("");
        }
        else  if(agesField.getText().toString().isEmpty())
        {
            agesField.setError("Error,try again");
            agesField.setText("");
        }
        else if(wageField.getText().toString().equals(""))
        {
            wageField.setError("Error,try again");
            wageField.setText("");
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Everything is fine", Toast.LENGTH_SHORT).show();

            progressDialog.show();
            // PotInFirebase();
        }
    }



    public void requestPermission(int permission)
    {
        if(permission == CAMERA)
        {
            ActivityCompat.requestPermissions(NannyProfileAdd.this,
                    new String[]{android.Manifest.permission.CAMERA},REQUEST_CAMERA);
        }
        else if(permission == STORAGE)
        {
            ActivityCompat.requestPermissions(NannyProfileAdd.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            uri =  getImageUri (NannyProfileAdd.this ,bitmap);
            profilePicture.setImageURI(uri);
        }

        if (requestCode == REQUEST_STORAGE)
        {
            uri = data.getData();
            profilePicture.setImageURI(uri);
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CAMERA == requestCode)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);

            }
            else
                Toast.makeText(this, "You Do not have camera permission", Toast.LENGTH_SHORT).show();
        }
        else if(REQUEST_STORAGE == requestCode)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Title"), REQUEST_STORAGE);
            }
            else
                Toast.makeText(this, "You Do not have storage permission", Toast.LENGTH_SHORT).show();
        }
    }

}