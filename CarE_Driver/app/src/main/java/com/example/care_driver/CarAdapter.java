package com.example.care_driver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    Context context;
    List<Car> carList;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.select_car_list, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNoPolisi.setText(carList.get(position).getNo_polisi());
        holder.mNamaMobil.setText(carList.get(position).getNama_mobil());
        holder.mProyek.setText(carList.get(position).getProyek());

        final String OrderID  = carList.get(position).getOrderID();
        final String IdMob    = carList.get(position).carId;
        final String NoPol    = carList.get(position).getNo_polisi();
        final String NaMob    = carList.get(position).getNama_mobil();

        final String All = NoPol + " " +NaMob;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                DocumentReference carref = mDatabase.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Order").document(OrderID);

                carref.update("mobil", All).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent mobil = new Intent(context, MainActivity.class);
                        mobil.putExtra("NoPol", NoPol);
                        mobil.putExtra("mobil", All);
//                mobil.putExtra("NaMob", NaMob);
//                mobil.putExtra("IdMob", IdMob);
                        mobil.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(mobil);
                    }
                });

            }
        });

        RefreshCar();
    }

    private void RefreshCar() {
        mDatabase   = FirebaseFirestore.getInstance();
        Query statusMobil = mDatabase.collection("Car").whereEqualTo("status", "free");

        statusMobil.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("CarAdapter", "Error : " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() != DocumentChange.Type.ADDED && doc.getType() != DocumentChange.Type.MODIFIED
                            && doc.getType() != DocumentChange.Type.REMOVED) {
                        String Car_id = doc.getDocument().getId();
                        Car car = doc.getDocument().toObject(Car.class).withId(Car_id);
                        if (carList.size() > 0 ){
                            carList.clear();
                        }
                        carList.add(car);
                        notifyDataSetChanged();
                    }
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mNoPolisi, mNamaMobil,mProyek;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            mNoPolisi   = mView.findViewById(R.id.no_polisi);
            mNamaMobil  = mView.findViewById(R.id.nama_mobil);
            mProyek      = mView.findViewById(R.id.order);
        }
    }
}
