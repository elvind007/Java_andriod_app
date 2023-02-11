package com.example.e_vaccinationapp.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_vaccinationapp.Child_Vaccine_Info;
import com.example.e_vaccinationapp.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    ListView childList;

    ChildAdapter childs;

    private ViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        childList = root.findViewById(R.id.child_list);

//        final ListView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
////                textView.setText(s);
//            }
//        });

        firebaseFirestore.collection("users")
            .document(firebaseAuth.getUid()).collection("childern")
                .orderBy("child_dob")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    ArrayList<String> id = new ArrayList<>();
                    ArrayList<String> childNames = new ArrayList<>();
                    if (value != null){
                        for(DocumentSnapshot docSnap : value){
                            id.add(docSnap.getId());
                            childNames.add(docSnap.getString("child_name"));
                        }

                        for (String name : childNames){
                            System.out.println("Name of Child : " + name);
                        }

                        childs = new ChildAdapter(getActivity(), id, childNames);
                        childList.setAdapter(childs);

                    }
                }
            });

        childList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String childId = (String) view.getTag();

                System.out.println("on item click listerner child id : " + childId);

                Intent intent = new Intent(getParentFragment().getContext(), Child_Vaccine_Info.class);
                intent.putExtra("parent_id", firebaseAuth.getUid());
                intent.putExtra("child_id", childId);

                Toast.makeText(getParentFragment().getContext(),"Child Details", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        return root;
    }
}

class ChildAdapter extends ArrayAdapter<String> {

    FirebaseAuth firebaseAuth;
    Context context;
    ArrayList<String> id;
    ArrayList<String> child_name;

    ChildAdapter (Context c, ArrayList<String> id, ArrayList<String> child_name) {
        super(c, R.layout.child_name_adapter, R.id.child_name_txtview, child_name);
        this.context = c;
        this.id = id;
        this.child_name = child_name;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.child_name_adapter, parent, false);

        TextView name_child = row.findViewById(R.id.child_name_txtview);

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

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE :
                                            Task<Void> documentReference = FirebaseFirestore.getInstance()
                                                    .collection("users")
                                                    .document(firebaseAuth.getUid())
                                                    .collection("childern").
                                                            document(id.get(position)).delete();

                                            Snackbar.make(v,  child_name.get(position)+" : Deleted Successfully", 2000).show();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };

                            new AlertDialog.Builder(getContext()).setMessage("Are you sure, do you want to delete?")
                                    .setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No",  dialogClickListener).show();

                            return true;
                        }else{
                            return onMenuItemClick(item);
                        }
                    }
                });
            }
        });

        // now set our resources on views
        name_child.setText(child_name.get(position));
        row.setTag(id.get(position));

        return row;
    }

    public void filter(String chartxt){
        chartxt = chartxt.toLowerCase();
    }
}

