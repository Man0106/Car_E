package com.example.care_driver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Pindah;
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;
    RecyclerView recyclerView;
    List<Order> orderList;
    OrderAdapter orderAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null){
            GoToLogin();
        }
    }

    private void GoToLogin() {
        Intent Login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(Login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseFirestore.getInstance();
        orderList   = new ArrayList<>();
        orderAdapter= new OrderAdapter(getApplicationContext(), orderList);
        recyclerView= findViewById(R.id.orderrec);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(orderAdapter);

        UpdateOrder(mDatabase, mAuth);
    }

    private void UpdateOrder(FirebaseFirestore mDatabase, FirebaseAuth mAuth) {
        if (orderList.size() > 0){
            orderList.clear();
        }

        mDatabase.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Order")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.d("MainActivity", "Error : " + e.getMessage());
                        }
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String order_id = doc.getDocument().getId();
                                Order order = doc.getDocument().toObject(Order.class).withId(order_id);

                                orderList.add(order);
                                orderAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
