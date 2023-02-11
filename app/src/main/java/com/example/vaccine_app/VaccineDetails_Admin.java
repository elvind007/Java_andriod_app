package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class VaccineDetails_Admin extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    TextView vac_name, vac_agegp, vac_description, vac_when_to_use, vac_dose, vac_route, vac_body_site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_details__admin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        vac_name = findViewById(R.id.admin_vac_details_name);
        vac_agegp = findViewById(R.id.admin_vac_age_gp_content);
        vac_description = findViewById(R.id.admin_vac_details_descirption_txt);
        vac_when_to_use = findViewById(R.id.admin_vac_when_to_give_content);
        vac_dose = findViewById(R.id.vac_dose_admin_content);
        vac_route = findViewById(R.id.route_admin_content);
        vac_body_site = findViewById(R.id.admin_vac_body_site_content);

        Bundle bundle = getIntent().getExtras();
        String vac_id = bundle.getString("vac_id");
        System.out.println("VaccineDetails_Admin.java : vaccine id : " + vac_id);

        firebaseFirestore.collection("vaccines").document(vac_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    vac_name.setText(value.getString("vaccine_name"));
                    vac_agegp.setText(value.getString("age_group"));
                    vac_description.setText(value.getString("vaccine_description"));
                    vac_when_to_use.setText(value.getString("when_to_use"));
                    vac_dose.setText(value.getString("dose_quantity"));
                    vac_route.setText(value.getString("route"));
                    vac_body_site.setText(value.getString("body_site"));
                }
            }
        });


        BottomNavigationView bottom = findViewById(R.id.bottom_nav);
        bottom.setSelectedItemId(R.id.vac_details);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.faq :
                        Intent intent = new Intent(getApplicationContext(), Faq_Admin.class);
                        intent.putExtra("vac_id", vac_id);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.vac_details: return true;
                }
                return false;
            }
        });

        findViewById(R.id.vaccine_details_admin_backbtn).setOnClickListener((v)->{
            Toast.makeText(this, "All Vaccines", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), View_all_vaccine.class);
            startActivity(intent);
        });

    }
}