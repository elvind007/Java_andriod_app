package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Admin_Add_FAQ extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    EditText question, answer;
    Button add_faq, clear_faq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__f_a_q);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        question = findViewById(R.id.faq_question);
        answer = findViewById(R.id.faq_answer);
        add_faq = findViewById(R.id.faq_add_btn);
        clear_faq = findViewById(R.id.faq_clear_btn);

        Bundle bundle = getIntent().getExtras();
        String vac_id = bundle.getString("vac_id");

        findViewById(R.id.add_faq_backbtn).setOnClickListener((v)->{
            Toast.makeText(Admin_Add_FAQ.this, "FAQ's", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Admin_Add_FAQ.this, Faq_Admin.class);
            intent.putExtra("vac_id", vac_id);
            startActivity(intent);
        });

        clear_faq.setOnClickListener((v)->{
            question.setText("");
            answer.setText("");
        });


        add_faq.setOnClickListener((v)->{

            if (question.getText().toString().isEmpty() || answer.getText().toString().isEmpty()){
                Snackbar.make(v, "Please fill all the Fields", 15000).show();
            }else{

                Map<String, Object> faqMap = new HashMap<>();
                faqMap.put("question", question.getText().toString());
                faqMap.put("answer", answer.getText().toString());

                firebaseFirestore.collection("vaccines")
                        .document(vac_id).collection("FAQ").document().set(faqMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(v, "FAQ Added Successfully", 15000).show();
                                Log.d("FAQ Success : ", "faq is added successfully");
                                question.setText("");
                                answer.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(v, "FAQ does not added", 15000).show();
                        Log.d("FAQ Failure : ", "faq does not added successfully");
                    }
                });
            }

        });

    }
}