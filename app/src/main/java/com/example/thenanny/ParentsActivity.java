package com.example.thenanny;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.thenanny.dto.NannyDetails;
import com.example.thenanny.dto.ParentDetails;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentsActivity extends AppCompatActivity {
    private String userID;
    private ParentDetails userDetails;
    private Toolbar mActionBarToolbar;
    private FirebaseFirestore storage;
    StorageReference storageReference;
    private ArrayList<NannyDetails> nannyList;
    private SearchView searchView;
    private RecyclerView recyclerView;
    ParentNannyCardAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);

        userID = getIntent().getStringExtra("userId");
        userDetails = (ParentDetails) getIntent().getSerializableExtra("userDetails");
        nannyList = new ArrayList<>();
        storage = FirebaseFirestore.getInstance();

        mActionBarToolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchField);
        adapter=new ParentNannyCardAdapter(nannyList);
        recyclerView = findViewById(R.id.nannyList);
        recyclerView.setAdapter(adapter);
        getNanny();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mActionBarToolbar.setTitle("Hello " + userDetails.getFirstname() + " " + userDetails.getLastname() + "!");



        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                filterList(newText);
                return true;
            }
        });
    }
    private void filterList(String queryText){
        List<NannyDetails>filteredList=new ArrayList<>();
        for(NannyDetails nanny:nannyList){
            if(nanny.getAddress().toLowerCase().contains(queryText.toLowerCase())){
                filteredList.add(nanny);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"No results found",Toast.LENGTH_LONG).show();
        }
        else{

        }
    }

    private void getNanny(){
        storage.collection("users.nanny").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
//                     if (progressDialog.isShowing())
//                      progressDialog.dismiss();
                    Toast.makeText(ParentsActivity.this, "Error accured", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            if ((Boolean) dc.getDocument().getData().get("approved")) {
                                NannyDetails user = new NannyDetails();
                                storageReference = FirebaseStorage.getInstance().getReference().child("nanny_files/" + dc.getDocument().getId() + "/profile_image");
                                storageReference.getBytes(1024 * 1024).addOnSuccessListener(bytes -> {
                                    user.setProfile_image(bytes);
                                    user.setId(dc.getDocument().getId());
                                    user.setAddress((String) dc.getDocument().getData().get("address"));
                                    user.setBirthDate(new Date((Long) dc.getDocument().getData().get("birthDate") * 1000));
                                    user.setEmail((String) dc.getDocument().getData().get("email"));
                                    user.setFirstname((String) dc.getDocument().getData().get("firstname"));
                                    user.setLastname((String) dc.getDocument().getData().get("lastname"));
                                    user.setHourlyWage(Math.round((Long) dc.getDocument().getData().get("hourlyWage")));
                                    user.setMinAge(Math.round((Long) dc.getDocument().getData().get("minAge")));
                                    user.setMaxAge(Math.round((Long) dc.getDocument().getData().get("maxAge")));
                                    user.setPhone((String) dc.getDocument().getData().get("phone"));
                                    user.setStartWorkDate((String) dc.getDocument().getData().get("startWorkDate"));
                                    nannyList.add(user);

                                });
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }
                }

            }
        });
    }
}