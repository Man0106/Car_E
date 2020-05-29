package com.example.car_e_verifikator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDriverActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView mNama, mAsal, mTujuan, mProyek, mPerjalanan, mTgl_B, mTgl_S;
    TextView mDriver;
    TextView Call;
    Button mSubimt;
//    Spinner sDriver;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;
    ImageButton BackButton;
    RadioButton A,B,C,D;
    TextView KoBe, KoSe;


    @Override
    protected void onStart() {
        super.onStart();

        final String Tgl_B = getIntent().getStringExtra("Tgl_Berangkat");
        final String Tgl_S = getIntent().getStringExtra("Tgl_Selesai");
//        final int Kode_Berangkat = getIntent().getIntExtra("Kode_Berangkat", 0);
//        final int Kode_Selesai = getIntent().getIntExtra("Kode_Selesai", 0);
//        final int KodeB = Integer.parseInt(Kode_Berangkat);
//        final int KodeS = Integer.parseInt(Kode_Selesai);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date tglb = sdf.parse(Tgl_B);
            Date tgls = sdf.parse(Tgl_S);

            final String bday      = (String) DateFormat.format( "dd", tglb);
            final String bmonth    = (String) DateFormat.format("MM", tglb);
            final String byear     = (String) DateFormat.format("yyyy", tglb);

            KoBe.setText(byear+bmonth+bday);

            final String sday     = (String) DateFormat.format("dd", tgls);
            final String smonth   = (String) DateFormat.format("MM", tgls);
            final String syear    = (String) DateFormat.format("yyyy", tgls);

            KoSe.setText(syear+smonth+sday);

            final int Kode_berangkat  = Integer.parseInt(KoBe.getText().toString());
            final int Kode_selesai    = Integer.parseInt(KoSe.getText().toString());

        mDatabase.collection("Users").document("NFqgwUHvLeQAEDJjDaDJNDr9cm02").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String nama = documentSnapshot.get("Nama").toString();

                            A.setText(nama);

                            mDatabase.collection("Users").document("NFqgwUHvLeQAEDJjDaDJNDr9cm02")
                                    .collection("Order").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()){
                                        String TGL_B = documentSnapshot1.get("Tanggal_Berangkat").toString();
                                        String TGL_S = documentSnapshot1.get("Tanggal_Selesai").toString();

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        try {
                                            Date Ber = simpleDateFormat.parse(TGL_B);
                                            Date Sel = simpleDateFormat.parse(TGL_S);

                                            String BDAY     = (String) DateFormat.format("dd", Ber);
                                            String BMONTH   = (String) DateFormat.format("MM", Ber);
                                            String BYEAR    = (String) DateFormat.format("yyyy", Ber);

                                            String SDAY     = (String) DateFormat.format("dd", Sel);
                                            String SMONTH   = (String) DateFormat.format("MM", Sel);
                                            String SYEAR    = (String) DateFormat.format("yyyy", Sel);

                                            String GabBerangkat = BYEAR + BMONTH + BDAY;
                                            String GabSelesai   = SYEAR + SMONTH + SDAY;

                                            int Berangkat   = Integer.parseInt(GabBerangkat);
                                            int Selesai     = Integer.parseInt(GabSelesai);

                                            Log.d("ListDriverActivity", "Berangkat : "+ Berangkat);
                                            Log.d("ListDriverActivity", "Selesai : "+ Selesai);
//                                        int KoBerDriver = Integer.parseInt(TGL_B);
//                                        int KoSelDriver = Integer.parseInt(TGL_S);
                                            if (Kode_berangkat >= Berangkat && Kode_selesai <= Selesai){
                                                A.setClickable(false);
                                                A.setVisibility(View.INVISIBLE);
                                            } else {
                                                A.setClickable(true);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    });

            mDatabase.collection("Users").document("NFqgwUHvLeQAEDJjDaDJNDr9cm02").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String nama = documentSnapshot.get("Nama").toString();

                            B.setText(nama);

                            mDatabase.collection("Users").document("NFqgwUHvLeQAEDJjDaDJNDr9cm02")
                                    .collection("Order").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()){
                                        String TGL_B = documentSnapshot1.get("Tanggal_Berangkat").toString();
                                        String TGL_S = documentSnapshot1.get("Tanggal_Selesai").toString();

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        try {
                                            Date Ber = simpleDateFormat.parse(TGL_B);
                                            Date Sel = simpleDateFormat.parse(TGL_S);

                                            String BDAY     = (String) DateFormat.format("dd", Ber);
                                            String BMONTH   = (String) DateFormat.format("MM", Ber);
                                            String BYEAR    = (String) DateFormat.format("yyyy", Ber);

                                            String SDAY     = (String) DateFormat.format("dd", Sel);
                                            String SMONTH   = (String) DateFormat.format("MM", Sel);
                                            String SYEAR    = (String) DateFormat.format("yyyy", Sel);

                                            String GabBerangkat = BYEAR + BMONTH + BDAY;
                                            String GabSelesai   = SYEAR + SMONTH + SDAY;

                                            int Berangkat   = Integer.parseInt(GabBerangkat);
                                            int Selesai     = Integer.parseInt(GabSelesai);

                                            Log.d("ListDriverActivity", "Berangkat : "+ Berangkat);
                                            Log.d("ListDriverActivity", "Selesai : "+ Selesai);
//                                        int KoBerDriver = Integer.parseInt(TGL_B);
//                                        int KoSelDriver = Integer.parseInt(TGL_S);
                                            if (Kode_berangkat >= Berangkat && Kode_selesai <= Selesai){
                                                B.setClickable(false);
                                                B.setVisibility(View.INVISIBLE);

                                            } else {
                                                B.setClickable(true);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_driver);

        KoBe        = findViewById(R.id.KoBe);
        KoSe        = findViewById(R.id.KoSe);

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

        A           = findViewById(R.id.A);
        B           = findViewById(R.id.B);

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


        Calendar calendar = Calendar.getInstance();



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

//        sDriver.setOnItemSelectedListener(this);
//        final Query driver = mDatabase.collection("Users").whereEqualTo("Role", "Driver")
//                .whereEqualTo("Status", "ON");
//        driver.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                final List<String> d = new ArrayList<>();
//                for (final DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
//
//                    String IDD = doc.getId();
//                    String ND = doc.get( "Nama" ).toString();
//                    d.add( ND );
//                    mDatabase.collection("Users").document(IDD).collection("Request")
//                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
//                                String TanggB = documentSnapshot.get("Tanggal_Berangkat").toString();
//                                String TanggS = documentSnapshot.get("Tanggal_Selesai").toString();
//
//                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                                try {
//                                    Date DateTB = format.parse(TanggB);
//                                    Date DateTS = format.parse(TanggS);
//                                    Date DateTgl_B = format.parse(Tgl_B);
//                                    Date DateTgl_S = format.parse(Tgl_S);
//                                    String a = format.format(DateTB);
//                                    Call.setText(a);
//
//                                        if ( DateTgl_B.before(DateTB) || DateTgl_B.after(DateTS)  ) {
//                                            driver.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()) {
//
//                                                    }
//
//                                                }
//                                            });
//                                        }
//
////                                        String nama = doc.get("Nama").toString();
//
//
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }
//
//                    });



//                }
//                ArrayAdapter<String> dsAdapter = new ArrayAdapter<String>( ListDriverActivity.this, android.R.layout.simple_spinner_item, d );
//
//                dsAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
//
//                sDriver.setAdapter( dsAdapter );
//                dsAdapter.notifyDataSetChanged();
//
//            }
//        });



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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String driver = parent.getItemAtPosition( position ).toString();
        mDriver.setText(driver);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
