package com.example.care_driver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.INotificationSideChannel;
import android.text.format.DateFormat;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    List<Order> orderList;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.order_list, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        holder.NamaPenumpang.setText(orderList.get(position).getNama());
        holder.NamaProyek.setText(orderList.get(position).getProyek());
        holder.Dari.setText(orderList.get(position).getAlamat_Asal());
        holder.Tujuan.setText(orderList.get(position).getAlamat_Tujuan());
        holder.Perjalanan.setText(orderList.get(position).getPerjalanan());
        holder.Berangkat.setText(orderList.get(position).getTanggal_Berangkat());
        holder.Selesai.setText(orderList.get(position).getTanggal_Selesai());
        holder.mobil.setText(orderList.get(position).getMobil());

        mAuth       = FirebaseAuth.getInstance();

        final String OrderID = orderList.get(position).orderId;
        final String Nama = orderList.get(position).getNama();
        final String A_Asal = orderList.get(position).getAlamat_Asal();
        final String A_Tujuan = orderList.get(position).getAlamat_Tujuan();
        final String Proyek = orderList.get(position).getProyek();
        final String Perjalanan = orderList.get(position).getPerjalanan();
        final String Tgl_Berangkat = orderList.get(position).getTanggal_Berangkat();
        final String Tgl_Selesai = orderList.get(position).getTanggal_Selesai();
        final String Mobil   = orderList.get(position).getMobil();

        holder.bpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otw = new Intent(context, SelectCarActivity.class);
                otw.putExtra("OrderID", OrderID);
                otw.putExtra("Nama", Nama);
                otw.putExtra("Asal", A_Asal);
                otw.putExtra("Tujuan", A_Tujuan);
                otw.putExtra("Proyek", Proyek);
                otw.putExtra("Perjalanan", Perjalanan);
                otw.putExtra("Tgl_Berangkat", Tgl_Berangkat);
                otw.putExtra("Tgl_Selesai", Tgl_Selesai);
                otw.putExtra("Mobil", Mobil);
                otw.putExtra("UserID", mAuth.getCurrentUser().getUid());

                otw.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(otw);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DateNow = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date Datenow = simpleDateFormat.parse(DateNow);
                    Date Tanggal_Berangkat = simpleDateFormat.parse(Tgl_Berangkat);
                    Date Tanggal_Selesai = simpleDateFormat.parse(Tgl_Selesai);

                    String DateNow_Date     = (String) DateFormat.format("dd", Datenow);
                    String DateNow_Month    = (String) DateFormat.format("MM", Datenow);
                    String DateNow_Year     = (String) DateFormat.format("yyyy", Datenow);

                    String Tanggal_Berangkat_Date   = (String) DateFormat.format("dd", Tanggal_Berangkat);
                    String Tanggal_Berangkat_Month  = (String) DateFormat.format("MM", Tanggal_Berangkat);
                    String Tanggal_Berangkat_Year   = (String) DateFormat.format("yyyy", Tanggal_Berangkat);

                    String Tanggal_Selesai_Date     = (String) DateFormat.format("dd", Tanggal_Selesai);
                    String Tanggal_Selesai_Month    = (String) DateFormat.format("MM", Tanggal_Selesai);
                    String Tanggal_Selesai_Year     = (String) DateFormat.format("yyyy", Tanggal_Selesai);

                    String DateNow_ALL              = DateNow_Year+DateNow_Month+DateNow_Date;
                    String Tanggal_Berangkat_ALL    = Tanggal_Berangkat_Year+Tanggal_Berangkat_Month+Tanggal_Berangkat_Date;
                    String Tanggal_Selesai_ALL      = Tanggal_Selesai_Year+Tanggal_Selesai_Month+Tanggal_Selesai_Date;

                    int DateNow_Conv            = Integer.parseInt(DateNow_ALL);
                    int Tanggal_Berangkat_Conv  = Integer.parseInt(Tanggal_Berangkat_ALL);
                    int Tanggal_Selesai_Conv    = Integer.parseInt(Tanggal_Selesai_ALL);

                    if (DateNow_Conv >= Tanggal_Berangkat_Conv && DateNow_Conv <= Tanggal_Selesai_Conv ) {
                        Intent otw = new Intent(context, OnTheWayActivity.class);
                        otw.putExtra("OrderID", OrderID);
                        otw.putExtra("Nama", Nama);
                        otw.putExtra("Asal", A_Asal);
                        otw.putExtra("Tujuan", A_Tujuan);
                        otw.putExtra("Proyek", Proyek);
                        otw.putExtra("Perjalanan", Perjalanan);
                        otw.putExtra("Tgl_Berangkat", Tgl_Berangkat);
                        otw.putExtra("Tgl_Selesai", Tgl_Selesai);
                        otw.putExtra("UserID", mAuth.getCurrentUser().getUid());

                        otw.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(otw);
                    } else {
                        Toast.makeText(context, "Belum Waktunya", Toast.LENGTH_LONG).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        RefreshOrder();

    }

    private void RefreshOrder() {
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();

        mDatabase.collection("Users").document(userID).collection("Order")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d("OrderAdapter", "Error : " + e.getMessage());
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() != DocumentChange.Type.ADDED && doc.getType() != DocumentChange.Type.MODIFIED
                            && doc.getType() != DocumentChange.Type.REMOVED) {
                        String Order_id = doc.getDocument().getId();
                        Order order = doc.getDocument().toObject(Order.class).withId(Order_id);
                        if (orderList.size() > 0 ){
                            orderList.clear();
                        }
                        orderList.add(order);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
            return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView NamaPenumpang, NamaProyek, Dari, Tujuan, Perjalanan, Berangkat, Selesai;
        ImageButton bpm;
        TextView mobil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            NamaPenumpang       = mView.findViewById(R.id.namaPenumpang);
            NamaProyek          = mView.findViewById(R.id.namaProyek);
            Dari                = mView.findViewById(R.id.dari);
            Tujuan              = mView.findViewById(R.id.tujuan);
            Perjalanan          = mView.findViewById(R.id.perjalanan);
            Berangkat           = mView.findViewById(R.id.tglBerangkat);
            Selesai             = mView.findViewById(R.id.tglSelesai);

            bpm                 = mView.findViewById(R.id.bpm);
            mobil               = mView.findViewById(R.id.mobil);
        }
    }
}
