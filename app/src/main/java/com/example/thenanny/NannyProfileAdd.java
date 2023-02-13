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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thenanny.dto.NannyDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NannyProfileAdd extends AppCompatActivity implements View.OnClickListener {
    //View.OnTouchListener
    EditText cityField,agesField,wageField;
    Button uploadIdBtn,submitButton ;
    ImageView profilePicture,editProfilePicture;
    String UserName;
    String userID;
    NannyDetails nannyDetails;
    ProgressDialog progressDialog;
    private  static  final  int REQUEST_CAMERA = 100;
    private  static  final  int REQUEST_STORAGE = 300;

    Uri uri;
    StorageReference storageReference;
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

        userID= getIntent().getStringExtra("userId");
        nannyDetails= (NannyDetails) getIntent().getSerializableExtra("userDetails");
        UserName = getIntent().getStringExtra("UserName");
        profilePicture=(ImageView) findViewById(R.id.profilePicture);
        //profilePicture.setOnTouchListener((View.OnTouchListener) this);
        progressDialog = new ProgressDialog(NannyProfileAdd.this);
        agesField=(EditText)findViewById(R.id.agesField);
        wageField=(EditText)findViewById(R.id.wageField);
        cityField=(EditText)findViewById(R.id.cityField);


        editProfilePicture=findViewById(R.id.editProfilePicture);
        editProfilePicture.setOnClickListener((View.OnClickListener) this);


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
    public void onClick(View v)
    {
        if(v==submitButton)
            Done();
        else if(v==editProfilePicture)
        {
            selectImage();
        }

    }

   private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && data!=null && data.getData()!=null)
        {
            //bitmap = (Bitmap) data.getExtras().get("data");
            //uri =  getImageUri (NannyProfileAdd.this ,bitmap);
            uri=data.getData();
            profilePicture.setImageURI(uri);
        }

        if (requestCode == REQUEST_STORAGE)
        {
            uri = data.getData();
            profilePicture.setImageURI(uri);
        }

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

            UploadImage();
        }
    }

    private void UploadImage() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading file...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("profile_image", Locale.US);
        Date now=new Date();
        String fileName=formatter.format(now);

        storageReference= FirebaseStorage.getInstance().getReference("images/"+userID+fileName);
        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //profilePicture.setImageURI(null);
                        Toast.makeText(NannyProfileAdd.this,"Successfully Uploaded profile photo",Toast.LENGTH_SHORT).show();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(NannyProfileAdd.this,"Profile photo uploading failure",Toast.LENGTH_SHORT).show();

                    }
                });
    }







    // unrelevant for now

    // @Override
/*    public boolean onTouch(View v, MotionEvent event)
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
    }*/

}