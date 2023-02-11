package com.example.e_vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.StatusBarManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        
        findViewById(R.id.card_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePage.this, "Admin Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomePage.this, Admin_login.class));
            }
        });

        findViewById(R.id.card_parent).setOnClickListener((v) ->{
            Toast.makeText(HomePage.this, "Parent Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomePage.this, Parent_login.class));
        });
    }
}