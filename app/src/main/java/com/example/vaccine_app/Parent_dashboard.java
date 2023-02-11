package com.example.e_vaccinationapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Parent_dashboard extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private AppBarConfiguration mAppBarConfiguration;
    Dialog add_child_dialog;
    TextView nav_header_parent_name, nav_header_email_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        // creating instances of database
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.add_faq_fab);

        add_child_dialog = new Dialog(this);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Child's Details", Snackbar.LENGTH_LONG).show();
                showAddPopUp(view);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View nav_header = navigationView.getHeaderView(0);

        nav_header_parent_name = nav_header.findViewById(R.id.parent_name_txtview);
        nav_header_email_id = nav_header.findViewById(R.id.parent_email_txtview);

        firebaseFirestore.collection("users")
            .document(firebaseAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null){

                        String fullName = value.getString("fName") + " " + value.getString("lName");
                        String emailid = value.getString("email");

                        System.out.println("Name : " + fullName + " " + emailid);

                        nav_header_parent_name.setText(fullName);
                        nav_header_email_id.setText(emailid);
                    }
                }
            });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.nav_logout:
                        firebaseAuth.signOut();
                        Toast.makeText(Parent_dashboard.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Parent_dashboard.this, HomePage.class));
                        finish();
                        break;
                }
                return false;
            }
        });

    }

    public void showAddPopUp(View v){
        EditText childName;
        EditText childDob;
        RadioGroup rg;
        Button addBtn, closeBtn;

        add_child_dialog.setContentView(R.layout.custom_child_add_popup);
        rg = (RadioGroup) add_child_dialog.findViewById(R.id.radiogp_child_gender) ;
        childName = (EditText) add_child_dialog.findViewById(R.id.child_name_edt);
        childDob = (EditText) add_child_dialog.findViewById(R.id.child_dob_edt);
        addBtn = (Button) add_child_dialog.findViewById(R.id.addbtn);
        closeBtn = (Button) add_child_dialog.findViewById(R.id.closebtn);

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

                childDob.setText(format.format(myCalendar.getTime()));

            }

        };

        childDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Parent_dashboard.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childName.getText().toString().isEmpty() && childDob.getText().toString().isEmpty() && rg.getCheckedRadioButtonId() == -1){
                    Toast.makeText(Parent_dashboard.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else{

                    String gender_child = "";

                    if (rg.getCheckedRadioButtonId() != -1){
                        int id= rg.getCheckedRadioButtonId();
                        View radioButton = rg.findViewById(id);
                        int radioId = rg.indexOfChild(radioButton);
                        RadioButton btn = (RadioButton) rg.getChildAt(radioId);
                        gender_child = (String) btn.getText();
                    }

                    Map<String, Object> child_details = new HashMap<>();
                    child_details.put("child_name", childName.getText().toString().trim());
                    child_details.put("child_gender", gender_child);
                    child_details.put("child_dob", childDob.getText().toString().trim());


                    Task<DocumentReference> docRef = firebaseFirestore.collection("users")
                            .document(firebaseAuth.getUid()).collection("childern").add(child_details)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("Child Added : ", "Child details added successfully");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Child Not Added : ", "Child details does not successfully");
                                }
                            });

                    add_child_dialog.dismiss();
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_child_dialog.dismiss();
            }
        });

        add_child_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        add_child_dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent_dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}