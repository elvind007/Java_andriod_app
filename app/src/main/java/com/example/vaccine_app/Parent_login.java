package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Parent_login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextInputLayout usernameLayout;
    String username_validator;
    TextInputLayout passwordLayout;
    String password_validator;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        findViewById(R.id.parent_back_btn).setOnClickListener((v)->{
            Toast.makeText(Parent_login.this, "Homepage", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Parent_login.this, HomePage.class));
        });

        findViewById(R.id.signuppage_btn).setOnClickListener((v) ->{
            Toast.makeText(Parent_login.this, "Parent Registration", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Parent_login.this, Parent_reg.class));
        });


        usernameLayout = findViewById(R.id.parent_username_layout);
        passwordLayout = findViewById(R.id.parent_password_layout);
        signIn = findViewById(R.id.parent_login_btn);

        DocumentReference docRef = firebaseFirestore.collection("admin").document("0YbwXZHE45RBO0oEjltS");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    username_validator = value.getString("username");
                    password_validator = value.getString("password");
                }
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameLayout.getEditText().getText().toString().trim();
                String password = passwordLayout.getEditText().getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(Parent_login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else {
                    if (!username.equals(username_validator)) {
                        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(Parent_login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Parent_login.this, Parent_dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(Parent_login.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(Parent_login.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}