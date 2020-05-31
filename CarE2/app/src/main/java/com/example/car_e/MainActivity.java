package com.example.car_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLogin, mRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;

    @Override
    protected void onStart() {
        super.onStart();

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null ){
                            mDatabase.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String Role = documentSnapshot.get("Role").toString();
                                            sendToMain(Role);
                                        }
                                    });

                        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail      = findViewById(R.id.edtEmail);
        mPassword   = findViewById(R.id.edtPassword);
        mLogin      = findViewById(R.id.btnLogin);
        mRegister   = findViewById(R.id.btnRegister);
        
        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseFirestore.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(r);
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                                    final String token_id = FirebaseInstanceId.getInstance().getToken();
                                    final String current_id = mAuth.getCurrentUser().getUid();

                                    mDatabase.collection("Users").document(current_id).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    final String Role = documentSnapshot.get("Role").toString();

                                                    Map<String, Object> tokenMap = new HashMap<>(  );
                                                    tokenMap.put( "token_id", token_id );

                                                    mDatabase.collection( "Users" ).document(current_id).update( tokenMap ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            sendToMain(Role);
                                                        }
                                                    } );
                                                }
                                            });


                                } else {
                                    Toast.makeText(MainActivity.this, "Login Gagal : "+task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void sendToMain(String role) {
        if (role.equals("user")) {
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(i);
        }
        if (role.equals("Kepala_Bidang")){
            Intent k = new Intent(MainActivity.this, HomeVerificatorActivity.class);
            startActivity(k);
        }
    }
}
