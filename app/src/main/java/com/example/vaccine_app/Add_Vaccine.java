package com.example.e_vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_Vaccine extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextInputLayout vaccine_name;
    TextView description;
    RadioGroup age_gp;
    EditText when_to_use, site_vaccine;
    Button add_vaccine_btn, clear_details_btn;
    Spinner dose_spinner, route_spinner;

    ArrayAdapter<CharSequence> dose_spinner_adpater;
    ArrayAdapter<CharSequence> route_spinner_adpater;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__vaccine);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        findViewById(R.id.add_faq_backbtn).setOnClickListener((v)->{
            Snackbar.make(v,"Vaccine Added Successfully", 1500).show();
            startActivity(new Intent(Add_Vaccine.this, Admin_dashboard.class));
        });

        // widget initializaion
        vaccine_name = findViewById(R.id.vaccine_name_layout);
        description = findViewById(R.id.faq_answer);
        age_gp = findViewById(R.id.rg_age_grp);
        route_spinner = findViewById(R.id.route_spinner);
        dose_spinner = findViewById(R.id.dose_spinner);
        when_to_use = findViewById(R.id.when_to_give_edt);
        site_vaccine = findViewById(R.id.site_edt);
        add_vaccine_btn = findViewById(R.id.faq_add_btn);
        clear_details_btn = findViewById(R.id.faq_clear_btn);

        // adapter setting

        // route adapter
        route_spinner_adpater = ArrayAdapter.createFromResource(this, R.array.route_vaccine, R.layout.spinner_layout);
        route_spinner_adpater.setDropDownViewResource(R.layout.spinner_layout);
        route_spinner.setAdapter(route_spinner_adpater);

        // dose adapter
        dose_spinner_adpater = ArrayAdapter.createFromResource(this, R.array.dose_quantity, R.layout.spinner_layout);
        dose_spinner_adpater.setDropDownViewResource(R.layout.spinner_layout);
        dose_spinner.setAdapter(dose_spinner_adpater);


        add_vaccine_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = firebaseFirestore.collection("vaccines").document();

                int radioButtonID = age_gp.getCheckedRadioButtonId();
                View radioButton = age_gp.findViewById(radioButtonID);
                int idx = age_gp.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) age_gp.getChildAt(idx);
                String ageGroup;

                try {
                    ageGroup = (String) btn.getText();
                }catch (Exception e){
                    ageGroup = "";
                }

                // testing sout's
                System.out.println("route selection id : " + route_spinner.getSelectedItemPosition());
                System.out.println("1:" + vaccine_name.getEditText().getText().toString().trim().isEmpty() );
                System.out.println("2:" + ageGroup.trim().isEmpty());
                System.out.println("3:" + description.getText().toString().trim().isEmpty());
                System.out.println("4:" + when_to_use.getText().toString().trim().isEmpty());
                System.out.println("5:" + site_vaccine.getText().toString().trim().isEmpty());
                System.out.println("6:" + dose_spinner.getSelectedItemPosition());

                if (vaccine_name.getEditText().getText().toString().trim().isEmpty() || ageGroup.trim().isEmpty() ||
                        description.getText().toString().trim().isEmpty() || when_to_use.getText().toString().trim().isEmpty() ||
                        site_vaccine.getText().toString().trim().isEmpty() || route_spinner.getSelectedItemPosition() == 0 ||
                        dose_spinner.getSelectedItemPosition() == 0){

                    Snackbar.make(v, "Please fill all the fields", 1500).show();
                }else{
                    Map<String, String> vaccineMap = new HashMap<>();
                    vaccineMap.put("vaccine_name", vaccine_name.getEditText().getText().toString().trim());
                    vaccineMap.put("vaccine_description", description.getText().toString().trim());
                    vaccineMap.put("age_group", ageGroup);
                    vaccineMap.put("when_to_use", when_to_use.getText().toString());
                    vaccineMap.put("body_site", site_vaccine.getText().toString());
                    vaccineMap.put("route", route_spinner.getSelectedItem().toString());
                    vaccineMap.put("dose_quantity", dose_spinner.getSelectedItem().toString());

                    documentReference.set(vaccineMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "Vaccine is Added");
                            Snackbar.make(v, "Vaccine is Added Successfully", 1500).show();
                            startActivity(new Intent(Add_Vaccine.this, Admin_dashboard.class));
                            finish();
                        }
                    });
                }
            }
        });


        clear_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccine_name.getEditText().setText("");
                description.setText("");
                age_gp.clearCheck();
                when_to_use.setText("");
                dose_spinner.setSelection(0);
                route_spinner.setSelection(0);
                site_vaccine.setText("");
            }
        });
    }
}