package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Child_Vaccine_Info extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ListView chlid_vaccine_list;

    TextView child_name_txt, gender_txt, dob_txt;
    String dob;
    Date date_of_birth;

    int age_year, age_month;

    String age_range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child__vaccine__info);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        chlid_vaccine_list = findViewById(R.id.child_vaccine_list);

        child_name_txt = findViewById(R.id.child_name_txt);
        gender_txt = findViewById(R.id.child_gender_txt);
        dob_txt = findViewById(R.id.child_dob_txt);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String parent_id = firebaseAuth.getUid();
        String child_id = bundle.getString("child_id");


        firebaseFirestore.collection("users")
                .document(parent_id).collection("childern")
                .document(child_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    child_name_txt.setText(value.getString("child_name"));
                    gender_txt.setText(value.getString("child_gender"));
                    dob_txt.setText(value.getString("child_dob"));

                    dob = value.getString("child_dob");

                    try {
                        System.out.println("Date of birth : try : " + dob);
                        date_of_birth = new SimpleDateFormat("dd/MM/yyyy").parse(dob);

                        Calendar date_dob = Calendar.getInstance();
                        date_dob.setTime(date_of_birth);

                        age_year = Calendar.getInstance().get(Calendar.YEAR) - date_dob.get(Calendar.YEAR);
                        age_month = Calendar.getInstance().get(Calendar.MONTH) - date_dob.get(Calendar.MONTH);

                        System.out.println("age-yar : " + age_year + "age-mon : " + age_month);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    age_range = retAgeRange(age_month, age_year);
                    System.out.println("age range of chiled : " + age_range);
                }
            }
        });

        firebaseFirestore.collection("vaccines").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                ArrayList<String> id = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();

                for (DocumentSnapshot docSnap : value) {

                    System.out.println("boolean of age_raneg" + age_range.equals(docSnap.getString("age_group")));
                    if(age_range.equals(docSnap.getString("age_group"))){
                        id.add(docSnap.getId());
                        name.add(docSnap.getString("vaccine_name"));

                    }
                }

                System.out.println("No. of namees : " + name.size());
                if (name.size() == 0){
                    id.add("No vaccine");
                    name.add("No Vaccine is Availble According to your Child age");
                }

                Child_Vaccine_Adapter child_vaccines = new Child_Vaccine_Adapter(getApplicationContext(), id, name);
                chlid_vaccine_list.setAdapter(child_vaccines);
            }
        });


        chlid_vaccine_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String vac_id = (String) view.getTag();

                if (!vac_id.equals("No vaccine")){
                    Toast.makeText(Child_Vaccine_Info.this, vac_id, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Child_Vaccine_Info.this, VaccineDetails.class);
                    intent.putExtra("child_id", child_id);
                    intent.putExtra("vac_id", vac_id);
                    Toast.makeText(Child_Vaccine_Info.this, "Details of Vaccine", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

            }
        });


        findViewById(R.id.vaccine_details_admin_backbtn).setOnClickListener((v)->{
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Child_Vaccine_Info.this, Parent_dashboard.class));
        });



    }

    public String retAgeRange(int age_month, int age_year){
        String age_gp = "";

        if(age_year < 1){
            if(age_month >= 0 && age_month < 3){
                age_gp = "0 - 3 Months";
            }else if(age_month >= 3 && age_month < 6){
                age_gp = "3 - 6 Months";
            }else if(age_month >= 6 && age_month < 12){
                age_gp = "6 - 12 Months";
            }
        }else{
            if(age_year >= 1 && age_year <= 2){
                age_gp = "1 - 2 years";
            }else if(age_year >= 4 && age_year <= 6){
                age_gp = "4 - 6 years";
            }else if (age_year >= 10 && age_year <= 16){
                age_gp = "10 - 16 years";
            }
        }
        return age_gp;
    }
}


class Child_Vaccine_Adapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> id;
    ArrayList<String> name_of_vaccine;

    Child_Vaccine_Adapter (Context c, ArrayList<String> id, ArrayList<String> name_of_vaccine) {
        super(c, R.layout.vaccine_adaptor, R.id.info_vac_name, name_of_vaccine);
        this.context = c;
        this.id = id;
        this.name_of_vaccine = name_of_vaccine;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.child_vaccine_info_layout, parent, false);

        TextView name_vaccine = row.findViewById(R.id.child_vaccine_name);
        // now set our resources on views
        name_vaccine.setText(name_of_vaccine.get(position));
        row.setTag(id.get(position));

        return row;
    }
}