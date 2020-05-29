package com.example.car_e;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    EditText mNama, mAlamatAsal, mAlamatTujuan, mProyek;
    RadioButton mOneWay, mTwoWay;
    RadioButton rb;
    ImageButton mBerangkat, mSelesai;
    Button mRequest;
    TextView txtBerangkat, txtSelesai;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    RadioGroup rg;

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
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
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
                                Intent intent = new Intent( HomeActivity.this, MainActivity.class );
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
    protected void onStart() {
        super.onStart();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser == null){
            GoToLogin();
        }
        String UID = mAuth.getCurrentUser().getUid();
        mDatabase.collection("Users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            String Role = documentSnapshot.get("Role").toString();
            if (!Role.equals("user")){
                Toast.makeText(getBaseContext(), "Akun anda bukan untuk penumpang", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                GoToLogin();
            }
            }

        });
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            GoToLogin();
        }
    }

    private void GoToLogin() {
        Intent l = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(l);
        mAuth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);

        mNama           = findViewById(R.id.edtNama);
        mAlamatAsal     = findViewById(R.id.edtAlamatAsal);
        mAlamatTujuan   = findViewById(R.id.edtAlamatTujuan);
        mProyek         = findViewById(R.id.edtProjek);

        rg              = findViewById(R.id.RGWay);
        mOneWay         = findViewById(R.id.rdoOne);
        mTwoWay         = findViewById(R.id.rdoTwo);

        mRequest        = findViewById(R.id.btnRequest);

        mBerangkat      = findViewById(R.id.imgbtnberangkat);
        txtBerangkat    = findViewById(R.id.txtTanggalBerangkat);
        mSelesai        = findViewById(R.id.imgbtnselesai);
        txtSelesai      = findViewById(R.id.txtTanggalSelesai);

        mDatabase       = FirebaseFirestore.getInstance();
        mAuth           = FirebaseAuth.getInstance();

        mBerangkat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                calendar    = Calendar.getInstance();
                year        = calendar.get(Calendar.YEAR);
                month       = calendar.get(Calendar.MONTH);
                dayOfMonth  = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(HomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtBerangkat.setText(dayOfMonth + "/" + (month + 1 ) + "/" +year);
                    }
                },year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        mSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar    = Calendar.getInstance();
                year        = calendar.get(Calendar.YEAR);
                month       = calendar.get(Calendar.MONTH);
                dayOfMonth  = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(HomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txtSelesai.setText(dayOfMonth + "/" + (month + 1) + "/" +year);
                            }
                        },year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama             = mNama.getText().toString();
                String alamat_asal      = mAlamatAsal.getText().toString();
                String alamat_tujuam    = mAlamatTujuan.getText().toString();
                String proyek           = mProyek.getText().toString();
                String tgl_berangkat    = txtBerangkat.getText().toString();
                String tgl_selesai      = txtSelesai.getText().toString();
                String perjalanan       = rb.getText().toString();

                final Map<String, Object> request = new HashMap<>();
                request.put("ID_Pengirim", mAuth.getCurrentUser().getUid());
                request.put("Nama", nama);
                request.put("Alamat_Asal", alamat_asal);
                request.put("Alamat_Tujuan", alamat_tujuam);
                request.put("Proyek", proyek);
                request.put("Tanggal_Berangkat", tgl_berangkat);
                request.put("Tanggal_Selesai", tgl_selesai);
                request.put("Perjalanan", perjalanan);
                request.put("Status", "Pending");
                mDatabase.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String Dep = documentSnapshot.get("Departemen").toString();

                        if (Dep.equals("A")){
                            final String RequestTo = "JznIsUs5ptbVqFVfywiFskhc3dA3";
                            mDatabase.collection("Users").document(RequestTo).collection("Request").add(request)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getBaseContext(), "Berhasil", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            String RequestTo = "NFqgwUHvLeQAEDJjDaDJNDr9cm02";
                            mDatabase.collection("Users").document(RequestTo).collection("Request").add(request)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getBaseContext(), "Berhasil", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }



                    }
                });

                mDatabase.collection("Request").add(request).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getBaseContext(), "Data telah terkirim, Menunggu konfirmasi", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void RadioButtonClicked(View view) {
        int SelectedId = rg.getCheckedRadioButtonId();
        rb = findViewById(SelectedId);
        Toast.makeText(getBaseContext(), "" + rb.getText(), Toast.LENGTH_LONG).show();
    }
}
