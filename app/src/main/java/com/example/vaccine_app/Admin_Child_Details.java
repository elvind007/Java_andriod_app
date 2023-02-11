package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class Admin_Child_Details extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ListView childs_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__child__details);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        childs_details = findViewById(R.id.all_childs_list);

        Bundle bundle = getIntent().getExtras();

        String parent_id = bundle.getString("parent_id");


        firebaseFirestore.collection("users").document(parent_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    ((TextView)findViewById(R.id.admin_parent_name_content))
                            .setText(value.get("fName").toString() + " " + value.get("mName").toString() + " " + value.get("lName").toString());
                }
            }
        });

      firebaseFirestore.collection("users").document(parent_id).collection("childern").addSnapshotListener(new EventListener<QuerySnapshot>() {
          @Override
          public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

              ArrayList<String> child_ids = new ArrayList<>();
              ArrayList<String> child_names = new ArrayList<>();
              ArrayList<String> genders = new ArrayList<>();
              ArrayList<String> dobs = new ArrayList<>();

              for (DocumentSnapshot docSnap : value){
                  child_ids.add(docSnap.getId());
                  child_names.add(docSnap.getString("child_name"));
                  genders.add(docSnap.getString("child_gender"));
                  dobs.add(docSnap.getString("child_dob"));
              }

              Child_Details_Adapter child_details_adapter = new Child_Details_Adapter(getApplicationContext(), child_ids, child_names, genders, dobs);
              childs_details.setAdapter(child_details_adapter);
          }
      });

      findViewById(R.id.admin_child_details_backbtn).setOnClickListener((v)->{
          Intent intent = new Intent(Admin_Child_Details.this, View_all_parent.class);
          intent.putExtra("parent_id", parent_id);
          Toast.makeText(Admin_Child_Details.this, "Parent's Child Details", Toast.LENGTH_SHORT).show();
          startActivity(intent);
      });
    }
}

class Child_Details_Adapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> child_ids;
    ArrayList<String> child_names;
    ArrayList<String> genders;
    ArrayList<String> dobs;

    Child_Details_Adapter (Context c, ArrayList<String> child_ids, ArrayList<String> child_names, ArrayList<String> genders, ArrayList<String> dobs) {
        super(c, R.layout.admin_child_details_layout, R.id.parent_child_name_text_content, child_names);
        this.context = c;
        this.child_ids = child_ids;
        this.child_names = child_names;
        this.genders = genders;
        this.dobs = genders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.admin_child_details_layout, parent, false);

        TextView childName = row.findViewById(R.id.parent_child_name_text_content);
        TextView gender = row.findViewById(R.id.parent_child_gender_content);
        TextView dob = row.findViewById(R.id.parent_child_date_of_birth);


        // now set our resources on views

        row.setTag(child_ids.get(position));
        childName.setText(child_names.get(position));
        gender.setText(genders.get(position));
        dob.setText(dobs.get(position));

        return row;
    }
}