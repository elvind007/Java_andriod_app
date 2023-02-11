package com.example.e_vaccinationapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Admin_dashboard extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextInputLayout usernameLayout;
    TextInputLayout passwordLayout;
    CardView logout, addVaccine, view_all_parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        logout = findViewById(R.id.card_view_admin_logout);

        addVaccine = findViewById(R.id.card_addVaccine);
        view_all_parents = findViewById(R.id.card_viewallparents);

        view_all_parents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<String> name = new ArrayList<>();

                        for (DocumentSnapshot docSnap : value){
                            name.add(docSnap.getString("fName"));
                        }
                        if (!name.isEmpty()){

                            Toast.makeText(Admin_dashboard.this, "View All Parents", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Admin_dashboard.this, View_all_parent.class));
                        }else {
                            Snackbar.make(v, "No User Details is Available", 1500).show();
                        }

                    }
                });

            }
        });

        addVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Admin_dashboard.this, "Add Vaccine", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_dashboard.this, Add_Vaccine.class));
            }
        });

        (findViewById(R.id.card_viewallvacine)).setOnClickListener((v)->{

            firebaseFirestore.collection("vaccines").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    ArrayList<String> name = new ArrayList<>();

                    for (DocumentSnapshot docSnap : value){
                        name.add(docSnap.getString("vaccine_name"));
                    }
                    if (!name.isEmpty()){
                        Toast.makeText(Admin_dashboard.this, "All Vaccine Details", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Admin_dashboard.this, View_all_vaccine.class));
                    }else {
                        Snackbar.make(v, "No Vaccine is Available", 1500).show();
                    }

                }
            });


        });


        addVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Admin_dashboard.this, "Add Vaccine", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_dashboard.this, Add_Vaccine.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(Admin_dashboard.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_dashboard.this, HomePage.class));
                finish();
            }
        });
    }
}