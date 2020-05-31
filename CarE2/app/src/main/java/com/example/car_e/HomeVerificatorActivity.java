package com.example.car_e;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeVerificatorActivity extends AppCompatActivity {
    RecyclerView mRequest_list;
    RequestAdapter requestAdapter;
    List<Request> requestList;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_verificator);
    }
}
