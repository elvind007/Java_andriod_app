package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Faq_Admin extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FloatingActionButton fab;
    ListView faqList;
    String vac_id;
    Faq_Adapter faq_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq__admin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        faqList = findViewById(R.id.faq_listview);

        Bundle bundle = getIntent().getExtras();
        vac_id = bundle.getString("vac_id");
        System.out.println("Faq.java123 : vaccine id : " + vac_id);

        firebaseFirestore.collection("vaccines").document(vac_id).collection("FAQ")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<String> faq_ids = new ArrayList<>();
                        ArrayList<String> faq_questions = new ArrayList<>();
                        ArrayList<String> faq_answers = new ArrayList<>();

                        System.out.println("value size : " + value.size());
                        for (DocumentSnapshot docSnap : value){
                            faq_ids.add(docSnap.getId());
                            faq_questions.add(docSnap.getString("question"));
                            faq_answers.add(docSnap.getString("answer"));
                        }

                        System.out.println("size of faq : " + faq_questions.size());
                        faq_adapter = new Faq_Adapter(getApplicationContext(), faq_ids, faq_questions, faq_answers);
                        faqList.setAdapter(faq_adapter);
                    }
                });

        BottomNavigationView bottom= findViewById(R.id.bottom_nav);
        bottom.setSelectedItemId(R.id.faq);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.vac_details :
                        Intent intent = new Intent(getApplicationContext(), VaccineDetails_Admin.class);
                        intent.putExtra("vac_id", vac_id);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.faq: return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_faq_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Faq_Admin.this, "Add FAQ", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Faq_Admin.this, Admin_Add_FAQ.class);
                intent.putExtra("vac_id", vac_id);
                startActivity(intent);
            }
        });


    }
}


