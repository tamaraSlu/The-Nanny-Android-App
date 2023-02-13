package com.example.thenanny;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.thenanny.dto.NannyDetails;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminHomePage extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<NannyDetails> nannyArrayList;
    Adapter myAdapter;
    FirebaseFirestore db;
    //ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        //ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Fetching Fata...");
       // progressDialog.show();

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        nannyArrayList=new ArrayList<NannyDetails>();
        myAdapter=new Adapter(AdminHomePage.this,nannyArrayList);
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();

    }

    private void EventChangeListener() {
        db.collection("users.nanny").orderBy("firstname", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     if(error!=null){
                        // if (progressDialog.isShowing())
                           //  progressDialog.dismiss();
                         Toast.makeText(AdminHomePage.this, "Error accured", Toast.LENGTH_SHORT).show();
                         return;
                     }

                     for(DocumentChange dc:value.getDocumentChanges()) {

                         if(dc.getType()==DocumentChange.Type.ADDED){
                             NannyDetails user = new NannyDetails();
                             user.setId(dc.getDocument().getId());
                             user.setFirstname((String) dc.getDocument().getData().get("firstname"));
                             user.setLastname((String) dc.getDocument().getData().get("lastname"));
//                             nannyArrayList.add(dc.getDocument().toObject(NannyDetails.class));
                             nannyArrayList.add(user);


                         }

                         myAdapter.notifyDataSetChanged();
                        // if (progressDialog.isShowing())
                           //  progressDialog.dismiss();
                     }

                    }
                });


    }
}