package com.example.care_driver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView NamaPenumpang, NamaProyek, Dari, Tujuan, Perjalanan, Berangkat, Selesai;
    Spinner SCar;
    String AOrderID, ANama, AProyek, AAsal, ATujuan, APerjalanan, ABerangkat, ASelesai;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;
    TextView M;
    Button submit;
    ImageView mPhoto;
    String TAG = "SelectCarActivity";

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);

        submit              = findViewById(R.id.submit);
//
        M                   = findViewById(R.id.m);

        mDatabase           = FirebaseFirestore.getInstance();
        mAuth               = FirebaseAuth.getInstance();

        mPhoto              = findViewById(R.id.foto);

//        NamaPenumpang       = findViewById(R.id.CnamaPenumpang);
//        NamaProyek          = findViewById(R.id.CnamaProyek);
//        Dari                = findViewById(R.id.Cdari);
//        Tujuan              = findViewById(R.id.Ctujuan);
//        Perjalanan          = findViewById(R.id.Cperjalanan);
//        Berangkat           = findViewById(R.id.CtglBerangkat);
//        Selesai             = findViewById(R.id.CtglSelesai);
//
        SCar                = findViewById(R.id.car);

        final String OrderID      = getIntent().getStringExtra("OrderID");
        final String UserID = getIntent().getStringExtra("UserID");
        AOrderID    = getIntent().getStringExtra("OrderID");
        ANama       = getIntent().getStringExtra("Nama");
        AProyek     = getIntent().getStringExtra("Proyek");
        AAsal       = getIntent().getStringExtra("Asal");
        ATujuan     = getIntent().getStringExtra("Tujuan");
        APerjalanan = getIntent().getStringExtra("Perjalanan");
        ABerangkat  = getIntent().getStringExtra("Tgl_Berangkat");
        ASelesai    = getIntent().getStringExtra("Tgl_Selesai");



        mDatabase.collection("Car").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    String namamobil = documentSnapshot.get("mobil").toString();
                    String ID = documentSnapshot.getId();

                    String Mobil = getIntent().getStringExtra("Mobil");

                    Map<String, Object> free = new HashMap<>();
                    free.put("status","free");

                    if (Mobil.equals(namamobil)){
                        mDatabase.collection("Car").document(ID).update(free).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Berhasil");
                            }
                        });
                    }
                    Query s = mDatabase.collection("Car").whereEqualTo("status", "free");
                    s.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<String> mobil = new ArrayList<>();
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
//                    String ID           = doc.get("no_polisi").toString();
                                String Nama         = doc.get("mobil").toString();
//                    String Nama_foto    = doc.get("foto").toString();
                                mobil.add(Nama);
                            }
                            ArrayAdapter<String> dsAdapter = new ArrayAdapter<String>( SelectCarActivity.this,
                                    android.R.layout.simple_spinner_item, mobil );

                            dsAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

                            SCar.setAdapter( dsAdapter );
                            dsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        SCar.setOnItemSelectedListener(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mobil = M.getText().toString();

                DocumentReference carref = mDatabase.collection("Users").document(UserID)
                        .collection("Order").document(OrderID);

                carref.update("mobil", mobil).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query m = mDatabase.collection("Car").whereEqualTo("mobil",mobil);
                        m.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                    String ID = doc.getId();
                                    Map<String, Object> status = new HashMap<>();
                                    status.put("status", "booked");
                                    mDatabase.collection("Car").document(ID).update(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent a = new Intent(SelectCarActivity.this, MainActivity.class);
                                            startActivity(a);
                                        }
                                    });
                                }
                            }
                        });

                    }
                });

            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String IDMobil = adapterView.getItemAtPosition(i).toString();
        M.setText(IDMobil);

        Query Picture = mDatabase.collection("Car").whereEqualTo("mobil", IDMobil);
        Picture.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    String foto = doc.get("foto").toString();

                    Picasso.get().load(foto).into(mPhoto);
                }
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
