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

import java.util.ArrayList;

public class View_all_parent extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ListView allParents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_parent);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        allParents = findViewById(R.id.all_parent_list);

        findViewById(R.id.all_parents_back_btn).setOnClickListener((v)->{
            Toast.makeText(View_all_parent.this, "Admin Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(View_all_parent.this, Admin_dashboard.class));
        });


        firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<String> parent_ids = new ArrayList<>();
                ArrayList<String> parentNames = new ArrayList<>();
                ArrayList<String> emails = new ArrayList<>();
                ArrayList<String> contacts = new ArrayList<>();

                for (DocumentSnapshot docSnap : value){
                    parent_ids.add(docSnap.getId());
                    parentNames.add(docSnap.get("fName").toString() + " " + docSnap.get("mName").toString() + " " + docSnap.get("lName").toString());
                    emails.add(docSnap.get("email").toString());
                    contacts.add(docSnap.get("contact").toString());
                }

                ParentAdapter parents = new ParentAdapter(getApplicationContext(), parent_ids, parentNames, emails, contacts);
                allParents.setAdapter(parents);
            }
        });

        allParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(View_all_parent.this, Admin_Child_Details.class);
                intent.putExtra("parent_id", (String)view.getTag());
                Toast.makeText(View_all_parent.this, "Parent's Child Details", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }
}

class ParentAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> parent_ids;
    ArrayList<String> parent_name;
    ArrayList<String> emailid;
    ArrayList<String> contactno;

    ParentAdapter (Context c, ArrayList<String> parent_ids, ArrayList<String> parent_name, ArrayList<String> emailid, ArrayList<String> contactno) {
        super(c, R.layout.parent_adaptor, R.id.info_parent_name_content, parent_name);
        this.context = c;
        this.parent_ids = parent_ids;
        this.parent_name = parent_name;
        this.emailid = emailid;
        this.contactno = contactno;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.parent_adaptor, parent, false);

        TextView parentName = row.findViewById(R.id.info_parent_name_content);
        TextView email = row.findViewById(R.id.info_email_content);
        TextView contact_No = row.findViewById(R.id.contact_no_txt_content);
        TextView child_name = row.findViewById(R.id.info_description_details);

        // now set our resources on views

        row.setTag(parent_ids.get(position));
        parentName.setText(parent_name.get(position));
        email.setText(emailid.get(position));
        contact_No.setText(contactno.get(position));

        return row;
    }
}