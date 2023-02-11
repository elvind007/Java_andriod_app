package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class View_all_vaccine extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ListView allVaccines;
    VaccineAdapter vaccines = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vaccine);

        findViewById(R.id.vaccine_details_admin_backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(View_all_vaccine.this, "Admin Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(View_all_vaccine.this, Admin_dashboard.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        allVaccines = findViewById(R.id.child_vaccine_list);
//        searchView = (SearchView) findViewById(R.id.search_vaccine_view_all_admin);

        firebaseFirestore.collection("vaccines").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<String> ids = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();

                for (DocumentSnapshot docSnap : value){
                    ids.add(docSnap.getId());
                    name.add(docSnap.getString("vaccine_name"));
                }

                if (name.isEmpty()){
                    Toast.makeText(View_all_vaccine.this, "Admin Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(View_all_vaccine.this, Admin_dashboard.class));
                    finish();
                }

                vaccines = new VaccineAdapter(getApplicationContext(), ids, name);
                allVaccines.setTextFilterEnabled(true);
                allVaccines.setAdapter(vaccines);

            }
        });

        allVaccines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String vac_id = (String) view.getTag();

                Intent intent =  new Intent(View_all_vaccine.this, VaccineDetails_Admin.class);
                intent.putExtra("vac_id", vac_id);
                Toast.makeText(View_all_vaccine.this, "Vaccine Details", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
    }
}

class VaccineAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> ids;
    ArrayList<String> name_of_vaccine;


    VaccineAdapter (Context c, ArrayList<String> ids, ArrayList<String> name_of_vaccine) {
        super(c, R.layout.vaccine_adaptor, R.id.info_vac_name, name_of_vaccine);
        this.ids = ids;
        this.context = c;
        this.name_of_vaccine = name_of_vaccine;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.vaccine_adaptor, parent, false);

        TextView name_vaccine = row.findViewById(R.id.info_vac_name);

        TextView popup = row.findViewById(R.id.popupbtn);

        popup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ViewHolder")
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getContext(), popup);
                
                popupMenu.getMenuInflater().inflate(R.menu.popup_layout, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        if (itemId == R.id.delete){
                            Task<Void> documentReference = FirebaseFirestore.getInstance()
                                    .collection("vaccines").document(ids.get(position)).delete();
                            Snackbar.make(v,  name_of_vaccine.get(position)+" : Deleted Successfully", 2000).show();
                            return true;
                        }else{
                            return onMenuItemClick(item);
                        }
                    }
                });
            }
        });

        row.setTag(ids.get(position));
        // now set our resources on views
        name_vaccine.setText(name_of_vaccine.get(position));

        return row;
    }


}