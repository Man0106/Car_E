package com.example.car_e_verifikator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRequest_list;
    RequestAdapter requestAdapter;
    List<Request> requestList;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Logout");
                alertDialogBuilder.setMessage("Are you sure to logout?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String,Object> tokenMapRemove = new HashMap<>();
                        tokenMapRemove.put( "token_id", FieldValue.delete() );

                        String UserId = mAuth.getCurrentUser().getUid();

                        mDatabase.collection( "Users" ).document(UserId).update( tokenMapRemove ).addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mAuth.signOut();
                                Intent intent = new Intent( MainActivity.this, LoginActivity.class );
                                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity( intent );
                            }
                        } );

                    }
                });
                alertDialogBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);

        mDatabase   = FirebaseFirestore.getInstance();
        mAuth       = FirebaseAuth.getInstance();

        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(getApplicationContext(), requestList);

        mRequest_list = (RecyclerView) findViewById(R.id.Request_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRequest_list.setHasFixedSize(true);
        mRequest_list.setLayoutManager(manager);
        mRequest_list.setAdapter(requestAdapter);

        UpdateRequest();
    }

    private void UpdateRequest() {
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (requestList.size() > 0){
            requestList.clear();
        }

        final String userID = mAuth.getCurrentUser().getUid();
        Query Req = mDatabase.collection("Users").document(userID).collection("Request")
                .whereEqualTo("Status", "Pending");
        Req.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("MainActivity", "Error : " + e.getMessage());
                }
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED ) {
                        String req_id = doc.getDocument().getId();
                        Request request = doc.getDocument().toObject( Request.class).withId(req_id);

                        requestList.add(request);
                        requestAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

//        mDatabase.collection("Users").document(userID).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                String D = documentSnapshot.get("Departemen").toString();
//                Query q = mDatabase.collection("Request").whereEqualTo("Departemen", D);
//
//                q.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null){
//                            Log.d("MainActivity", "Error : " + e.getMessage());
//                        }
//                        for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
//                            if (doc.getType() == DocumentChange.Type.ADDED ) {
//                                String req_id = doc.getDocument().getId();
//                                Request request = doc.getDocument().toObject( Request.class).withId(req_id);
//
//                                requestList.add(request);
//                                requestAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });
//
//            }
//        });



//        mDatabase.collection("Request").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null){
//                    Log.d("MainActivity", "Error : " + e.getMessage());
//                }
//                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
//                    if (doc.getType() == DocumentChange.Type.ADDED ) {
//                        String req_id = doc.getDocument().getId();
//                        Request request = doc.getDocument().toObject( Request.class).withId(req_id);
////                        if (requestList.size() > 0) {
////                            requestList.clear();
////                        }
//
//                        requestList.add(request);
//                        requestAdapter.notifyDataSetChanged();
//                    }
//                }
//
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
