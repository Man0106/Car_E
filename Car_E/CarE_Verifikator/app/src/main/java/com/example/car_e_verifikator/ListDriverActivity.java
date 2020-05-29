package com.example.car_e_verifikator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDriverActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView mNama, mAsal, mTujuan, mProyek, mPerjalanan;
    TextView mDriver;
    Button mSubimt;
    Spinner sDriver;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_driver);

        mNama       = findViewById(R.id.txtNama);
        mAsal       = findViewById(R.id.txtAsal);
        mTujuan     = findViewById(R.id.txtTujuan);
        mProyek     = findViewById(R.id.txtProyek);
        mPerjalanan = findViewById(R.id.txtPerjalanan);

        mDriver     = findViewById(R.id.txtDriver);

        mSubimt     = findViewById(R.id.btnSubmit);

        sDriver     = findViewById(R.id.spDriver);

        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseFirestore.getInstance();

        final String nama = getIntent().getStringExtra("Nama");

        mNama.setText(nama);

        final String Driver = mDriver.getText().toString();


        sDriver.setOnItemSelectedListener(this);
        Query driver = mDatabase.collection("Users").whereEqualTo("Role", "Driver");
        driver.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final List<String> d = new ArrayList<>();
                for (final DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    String IDD = doc.getId();
                    mDatabase.collection("Users").document(IDD).collection("Request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                String Status = documentSnapshot.get("Status").toString();
                                if (!Status.equals("occupied")){
                                    String ND = doc.get( "Name" ).toString();
                                    d.add( ND );
                                }
                            }
                        }
                    });

//                        String DID = doc.getId();
//                        d.add( DID );
                }
                ArrayAdapter<String> dsAdapter = new ArrayAdapter<String>( ListDriverActivity.this, android.R.layout.simple_spinner_item, d );

                dsAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

                sDriver.setAdapter( dsAdapter );
                dsAdapter.notifyDataSetChanged();
            }
        });


        mSubimt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> ACC = new HashMap<>();
                ACC.put("Nama", nama);
                ACC.put("Driver", Driver);
                ACC.put("Status", "occupied");

                mDatabase.collection("Users").document(IDD)

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String driver = parent.getItemAtPosition( position ).toString();
        mDriver.setText(driver);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
