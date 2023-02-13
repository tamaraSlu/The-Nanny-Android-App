package com.example.thenanny;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

import com.example.thenanny.dto.NannyDetails;
import com.example.thenanny.dto.ParentDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentsActivity extends AppCompatActivity {
    private String userID;
    private ParentDetails userDetails;
    private Toolbar mActionBarToolbar;
    private FirebaseAuth usersAuth;
    private FirebaseFirestore storage = FirebaseFirestore.getInstance();
    private List<NannyDetails> nannyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);

        userID = getIntent().getStringExtra("userId");
        userDetails = (ParentDetails) getIntent().getSerializableExtra("userDetails");
        nannyList = new ArrayList<>();

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("Hello " + userDetails.getFirstname() + " " + userDetails.getLastname() + "!");

        storage.collection("users.nanny").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if ((Boolean) document.getData().get("approved")) {
                            NannyDetails user = new NannyDetails();
                            user.setId(document.getId());
                            user.setAddress((String) document.getData().get("address"));
                            user.setBirthDate(new Date((Long) document.getData().get("birthDate") * 1000));
                            user.setEmail((String) document.getData().get("email"));
                            user.setFirstname((String) document.getData().get("firstname"));
                            user.setLastname((String) document.getData().get("lastname"));
                            user.setHourlyWage(Math.round((Long) document.getData().get("hourlyWage")));
                            user.setMinAge(Math.round((Long) document.getData().get("minAge")));
                            user.setMaxAge(Math.round((Long) document.getData().get("maxAge")));
                            user.setPhone((String) document.getData().get("phone"));
                            user.setStartWorkDate((String) document.getData().get("startWorkDate"));
                            nannyList.add(user);

                        }

                    }
                    Log.d(TAG, " => " + nannyList.toString());
                }
            }
        });
    }
}