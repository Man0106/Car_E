package com.example.car_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ListDriverActivity extends AppCompatActivity {
    TextView mNama, mAsal, mTujuan, mProyek, mPerjalanan, mTgl_B, mTgl_S;
    TextView mDriver;
    TextView Call;
    Button mSubimt;
    //    Spinner sDriver;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;
    ImageButton BackButton;
    RadioButton A,B,C,D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_driver);

        mNama       = findViewById(R.id.txtNama);
        mAsal       = findViewById(R.id.txtAsal);
        mTujuan     = findViewById(R.id.txtTujuan);
        mProyek     = findViewById(R.id.txtProyek);
        mPerjalanan = findViewById(R.id.txtPerjalanan);
        mTgl_B      = findViewById(R.id.txtTgl_B);
        mTgl_S      = findViewById(R.id.txtTgl_S);
        Call        = findViewById(R.id.Callender);
        mDriver     = findViewById(R.id.txtDriver);

        BackButton  = findViewById(R.id.backButton);

        mSubimt     = findViewById(R.id.btnSubmit);

//        sDriver     = findViewById(R.id.spDriver);

        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseFirestore.getInstance();

        String Nama = getIntent().getStringExtra("Nama");
        final String Asal = getIntent().getStringExtra("Asal");
        final String Tujuan = getIntent().getStringExtra("Tujuan");
        final String Proyek = getIntent().getStringExtra("Proyek");
        final String Perjalanan = getIntent().getStringExtra("Perjalanan");
        final String Tgl_B = getIntent().getStringExtra("Tgl_Berangkat");
        final String Tgl_S = getIntent().getStringExtra("Tgl_Selesai");
        final String req_id = getIntent().getStringExtra("req_id");






        mNama.setText(Nama);
        mAsal.setText(Asal);
        mTujuan.setText(Tujuan);
        mProyek.setText(Proyek);
        mPerjalanan.setText(Perjalanan);
        mTgl_B.setText(Tgl_B);
        mTgl_S.setText(Tgl_S);

        final String nama = mNama.getText().toString();

        final String userID = mAuth.getCurrentUser().getUid();

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ListDriverActivity.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });


        mSubimt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Drive = mDriver.getText().toString();
                final Map<String, Object> ACC = new HashMap<>();
                ACC.put("Nama", nama);
                ACC.put("Nama_Driver", Drive);
                ACC.put("Alamat_Asal", Asal);
                ACC.put("Alamat_Tujuan", Tujuan);
                ACC.put("Tanggal_Berangkat", Tgl_B);
                ACC.put("Tanggal_Selesai", Tgl_S);
                ACC.put("Perjalanan", Perjalanan);
                ACC.put("Proyek", Proyek);

                mDatabase.collection("Order").add(ACC).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        final String ID_ORDER = documentReference.getId();


                        Map<String, Object> UpACC = new HashMap<>();
                        UpACC.put("Status", "Accept");
                        mDatabase.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Request").document(req_id)
                                .update(UpACC).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {



                                Query IDD = mDatabase.collection("Users").whereEqualTo("Nama", Drive);
                                IDD.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                            final String ID = doc.getId();

                                            mDatabase.collection("Users").document(ID).collection("Order").document(ID_ORDER)
                                                    .set(ACC).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Map<String, Object> s = new HashMap<>();
                                                        s.put("Status", "Booked");
                                                        mDatabase.collection("Users").document(ID).update(s).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getBaseContext(), "Berhasil melakukan pemesanan", Toast.LENGTH_LONG).show();
                                                            }
                                                        });


                                                    } else {
                                                        Toast.makeText(getBaseContext(), "Gagal", Toast.LENGTH_LONG).show();
                                                    }
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
        });
    }
}
