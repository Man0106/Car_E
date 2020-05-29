package com.example.car_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText mNama, mEmail, mPassword, mConPassword;
    Button mSignUp;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNama       = findViewById(R.id.edtRegisterNama);
        mEmail      = findViewById(R.id.edtRegisterEmail);
        mPassword   = findViewById(R.id.edtRegisterPassword);
        mConPassword= findViewById(R.id.edtRegisterConPassword);

        mSignUp     = findViewById(R.id.btnSignUp);

        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseFirestore.getInstance();

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama = mNama.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                String conpass = mConPassword.getText().toString();

                if (nama.isEmpty() | email.isEmpty() | password.isEmpty() | conpass.isEmpty()){
                    Toast.makeText(getBaseContext(), "Masukan data terlebih dahulu!..", Toast.LENGTH_LONG).show();
                } else if(!password.equals(conpass)){
                    Toast.makeText(getBaseContext(), "Password tidak sesuai!..", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String IDuser = mAuth.getCurrentUser().getUid();

                                Map<String, Object> Users = new HashMap<>();
                                Users.put("Nama", nama);
                                Users.put("Email", email);
                                Users.put("Password", password);

                                mDatabase.collection("Users").document(IDuser).set(Users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getBaseContext(), "Pendaftaran berhasil", Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                        } else {
                                            Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
