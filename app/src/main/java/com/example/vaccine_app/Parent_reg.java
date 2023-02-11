package com.example.e_vaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Parent_reg extends AppCompatActivity {

    FirebaseAuth fAuth;             // Class Provided by fb using which we can register user
    FirebaseFirestore fStore;
    Button registrationBtn;
    EditText fname, mname, lname, contactNo, emailId, pass, cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_reg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        findViewById(R.id.vaccine_details_admin_backbtn).setOnClickListener((v)->{
            Toast.makeText(Parent_reg.this, "Homepage", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Parent_reg.this, HomePage.class));
        });

        findViewById(R.id.loginpage_btn).setOnClickListener((v) ->{
            Toast.makeText(Parent_reg.this, "Parent Registration", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Parent_reg.this, Parent_login.class));
        });


        registrationBtn = (Button)findViewById(R.id.registrationBtn);
        fname = (EditText) findViewById(R.id.editText_fname);
        mname = (EditText) findViewById(R.id.editText_mname);
        lname = (EditText) findViewById(R.id.editText_lname);
        contactNo = (EditText) findViewById(R.id.editText_contact);
        emailId = (EditText) findViewById(R.id.editText_emailid);
        pass = (EditText) findViewById(R.id.editText_pass);
        cpass = (EditText) findViewById(R.id.editText_cpass);


        registrationBtn.setOnClickListener((v)->{

            String first_name = fname.getText().toString().trim();
            String middle_name = mname.getText().toString().trim();
            String last_name = lname.getText().toString().trim();
            String contact_no = contactNo.getText().toString().trim();
            String email_id = emailId.getText().toString().trim();
            String password = pass.getText().toString().trim();
            String c_password = cpass.getText().toString().trim();


            if (first_name.isEmpty() && middle_name.isEmpty() && last_name.isEmpty()
            && contact_no.isEmpty() && email_id.isEmpty() && password.isEmpty() && c_password.isEmpty()){
                Toast.makeText(this, "Please Fill all the Fields", Toast.LENGTH_SHORT).show();
            }else if(!password.equals(c_password)){
                cpass.setError("Password doesn't match");
            }else {
                fAuth.createUserWithEmailAndPassword(email_id ,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            String userId = fAuth.getCurrentUser().getUid(); //used to get user id of currently registered or logined user
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName", first_name);
                            user.put("mName", middle_name);
                            user.put("lName", last_name);
                            user.put("email", email_id);
                            user.put("contact", contact_no);
                            user.put("password", password);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: User Profile is Created for "+userId);
                                }
                            });

                            Toast.makeText(Parent_reg.this, "You have Registered Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Parent_reg.this,Parent_login.class));
                        }
                        else {
                            Toast.makeText(Parent_reg.this, "Error ! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}