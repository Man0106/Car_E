package com.example.car_e_verifikator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    Context context;
    List<Request> requestList;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    public RequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.req_list, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, final int position) {
        holder.mNama.setText(requestList.get(position).getNama_Pengirim());
        holder.mTglBerangkat.setText(requestList.get(position).getTanggal_Berangkat());
        holder.mTglSelesai.setText(requestList.get(position).getTanggal_Selesai());
        holder.mProyek.setText(requestList.get(position).getProyek());


        final String RequestID = requestList.get(position).requestId;
        final String Nama = requestList.get(position).getNama_Pengirim();
        final String A_Asal = requestList.get(position).getAlamat_Asal();
        final String A_Tujuan = requestList.get(position).getAlamat_Tujuan();
        final String Proyek = requestList.get(position).getProyek();
        final String Perjalanan = requestList.get(position).getPerjalanan();
        final String Tgl_Berangkat = requestList.get(position).getTanggal_Berangkat();
        final String Tgl_Selesai = requestList.get(position).getTanggal_Selesai();

        mAuth = FirebaseAuth.getInstance();

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d = new Intent(context, ListDriverActivity.class);
                d.putExtra("Nama", Nama);
                d.putExtra("req_id", RequestID);
                d.putExtra("Asal", A_Asal);
                d.putExtra("Tujuan", A_Tujuan);
                d.putExtra("Proyek", Proyek);
                d.putExtra("Perjalanan", Perjalanan);
                d.putExtra("Tgl_Berangkat", Tgl_Berangkat);
                d.putExtra("Tgl_Selesai", Tgl_Selesai);

                d.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(d);
            }
        });

        holder.Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = mAuth.getCurrentUser().getUid();
                Map<String, Object> CC = new HashMap<>();
                CC.put("Status", "Cancel");
                mDatabase.collection("Users").document(userID).collection("Request")
                        .document(RequestID).update(CC).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Anda Menolak Permintaan "+Nama, Toast.LENGTH_LONG).show();
                        requestList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, requestList.size());
                    }
                });
            }
        });

        RefreshRequest();
    }

    private void RefreshRequest() {
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();
        Query req = mDatabase.collection("Users").document(userID).collection("Request")
                ;

        req.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                        Log.d("RequestAdapter", "Error : " + e.getMessage());
                }
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() != DocumentChange.Type.ADDED && doc.getType() != DocumentChange.Type.MODIFIED && doc.getType() != DocumentChange.Type.REMOVED ) {
                        String req_id = doc.getDocument().getId();
                        Request request = doc.getDocument().toObject( Request.class).withId(req_id);
                        if (requestList.size() > 0){
                            requestList.clear();
                        }
                        requestList.add(request);
                        notifyDataSetChanged();
                    }
                }
            }
        });

//        mDatabase.collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                String D = documentSnapshot.get("Departemen").toString();
//                Query q = mDatabase.collection("Request").whereEqualTo("Departemen", D);
//
//                q.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null){
//                            Log.d("RequestAdapter", "Error : " + e.getMessage());
//                        }
//                        for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
//                            if (doc.getType() != DocumentChange.Type.ADDED && doc.getType() != DocumentChange.Type.MODIFIED && doc.getType() != DocumentChange.Type.REMOVED ) {
//                                String req_id = doc.getDocument().getId();
//                                Request request = doc.getDocument().toObject( Request.class).withId(req_id);
//                                if (requestList.size() > 0){
//                                    requestList.clear();
//                                }
//                                requestList.add(request);
//                                notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });
//
//            }
//        });
//        mDatabase.collection("Request").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null){
//                    Log.d("RequestAdapter", "Error : " + e.getMessage());
//                }
//                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
//                    if (doc.getType() != DocumentChange.Type.ADDED && doc.getType() != DocumentChange.Type.MODIFIED && doc.getType() != DocumentChange.Type.REMOVED) {
//                        String req_id = doc.getDocument().getId();
//                        Request request = doc.getDocument().toObject( Request.class).withId(req_id);
//                        if (requestList.size() > 0) {
//                            requestList.clear();
//                        }
//
//                        requestList.add(request);
//                        notifyDataSetChanged();
//                    }
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mNama, mTglBerangkat, mTglSelesai, mProyek;
        ImageButton Accept, Cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            mNama           = mView.findViewById(R.id.txtreqnama);
            mTglBerangkat   = mView.findViewById(R.id.txtreqtglberangkat);
            mTglSelesai     = mView.findViewById(R.id.txtreqtglselesai);
            mProyek         = mView.findViewById(R.id.txtreqproyek);

            Accept          = mView.findViewById(R.id.imgbtnAccept);
            Cancel          = mView.findViewById(R.id.imgbtnCancel);

        }
    }
}
