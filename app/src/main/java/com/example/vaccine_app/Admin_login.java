package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Admin_login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextInputLayout usernameLayout;
    TextInputLayout passwordLayout;
    Button signIn;

    String username_validator;
    String password_validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        usernameLayout = findViewById(R.id.admin_username_layout);
        passwordLayout = findViewById(R.id.admin_password_layout);
        signIn = findViewById(R.id.admin_login_btn);


        findViewById(R.id.admin_back_btn).setOnClickListener((v)->{
            Toast.makeText(Admin_login.this, "Homepage", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Admin_login.this, HomePage.class));
        });

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
                    Toast.makeText(Admin_login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    if (username.equals(username_validator) && password.equals(password_validator)){
                        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(Admin_login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Admin_login.this, Admin_dashboard.class));
                                    finish();
                                }else {
                                    System.out.println("Error : " + task.getException().getMessage());
                                    Toast.makeText(Admin_login.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(Admin_login.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}