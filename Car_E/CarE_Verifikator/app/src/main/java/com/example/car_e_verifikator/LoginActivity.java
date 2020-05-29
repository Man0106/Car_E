package com.example.car_e_verifikator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLogin;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase   = FirebaseFirestore.getInstance();
        mAuth       = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.edtEmail);
        mPassword = findViewById(R.id.edtPassword);

        mLogin    = findViewById(R.id.btnLogin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String pass  = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            String current_id = mAuth.getCurrentUser().getUid();

                            Map<String, Object> tokenMap = new HashMap<>(  );
                            tokenMap.put( "token_id", token_id );

                            mDatabase.collection( "Users" ).document(current_id).update( tokenMap ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getBaseContext(), "Login Berhasil", Toast.LENGTH_LONG).show();
                                    GoToMain();
                                }
                            } );


                        } else {
                            Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void GoToMain() {
        Intent l = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(l);
    }
}
