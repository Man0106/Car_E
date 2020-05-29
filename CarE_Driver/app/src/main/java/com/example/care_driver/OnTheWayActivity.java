package com.example.care_driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OnTheWayActivity extends AppCompatActivity {
    TextView timer;
    Button Start, Stop;
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, Hours, MilliSeconds ;

    private StringBuilder stringBuilder;
    private boolean isGPS = false;
    private boolean isContinue = false;
    private static String locationStart;
    private static String locationStop;

    private FusedLocationProviderClient mFusedLocationClient;
    private FusedLocationProviderClient mFusedLocationClientStop;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationRequest locationRequestStop;
    private LocationCallback locationCallback;

    String TravelID = null;
    FirebaseFirestore mDatabase;

    TextView tID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_the_way);

        tID         = findViewById(R.id.travelId);
        timer       = findViewById(R.id.txtcountdown);

        Start       = findViewById(R.id.btnstartcd);
        Stop        = findViewById(R.id.btnstopcd);

        mDatabase = FirebaseFirestore.getInstance();

        handler     = new Handler();



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClientStop = LocationServices.getFusedLocationProviderClient(this);


        locationRequestStop = LocationRequest.create();
        locationRequestStop.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequestStop.setInterval(10 * 1000); // 10 seconds
        locationRequestStop.setFastestInterval(5 * 1000);


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

//        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000 );

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            OnTheWayActivity.locationStart =  String.format( Locale.US, "%s - %s", wayLatitude, wayLongitude);
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GpsUtils( OnTheWayActivity.this ).turnGPSOn( new GpsUtils.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        // turn on GPS
                        isGPS = isGPSEnable;
                    }
                } );

                if (isGPS == false) {
                    Toast.makeText( OnTheWayActivity.this, "Please turn on GPS to use this app!", Toast.LENGTH_SHORT ).show();
//                    mAuth.signOut();
                    redirectPage( OnTheWayActivity.this, MainActivity.class );
//                    finish();
                } else {

                    Toast.makeText(getBaseContext(),"Pejalanan dimulai",Toast.LENGTH_LONG).show();
                    getLocationStart();
                }
            }

            private void getLocationStart() {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                if (ActivityCompat.checkSelfPermission(OnTheWayActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(OnTheWayActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OnTheWayActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            AppConstants.LOCATION_REQUEST);
                } else {
                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {

                        mFusedLocationClient.getLastLocation().addOnSuccessListener(OnTheWayActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

//                        String coorLocation; String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude);
                                String StartAt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                                if (location != null) {
                                    wayLatitude = location.getLatitude();
                                    wayLongitude = location.getLongitude();
                                    OnTheWayActivity.locationStart = String.format(Locale.US, "%s,%s", wayLatitude, wayLongitude);
//                            txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));

                                    if (OnTheWayActivity.locationStart != null){
                                        Map<String, Object> strt = new HashMap<>();
                                        strt.put("Mulai", StartAt);
                                        strt.put("Lokasi_Awal", OnTheWayActivity.locationStart);
//                                        strt.put("Pengendara", m)m

                                        mDatabase.collection("Travel").add(strt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                String TravelId = documentReference.getId();
                                                TravelID = TravelId;
                                                tID.setText(TravelId);
//                                                Toast.makeText(getBaseContext(), "Perjalanan dimulai", Toast.LENGTH_LONG).show();
                                            }

                                        });

                                    }
                                } else {
                                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
                    }
                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);
//                String StopAt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                new GpsUtils( OnTheWayActivity.this ).turnGPSOn( new GpsUtils.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        // turn on GPS
                        isGPS = isGPSEnable;
                    }
                } );

                if (isGPS == false) {
                    Toast.makeText( OnTheWayActivity.this, "Please turn on GPS to use this app!", Toast.LENGTH_SHORT ).show();
//                    mAuth.signOut();
                    redirectPage( OnTheWayActivity.this, MainActivity.class );
//                    finish();
                } else {
                    GetLocationStop();
                }
            }

            private void GetLocationStop() {
                if (ActivityCompat.checkSelfPermission(OnTheWayActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(OnTheWayActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OnTheWayActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            AppConstants.LOCATION_REQUEST);
                } else {
                    if (isContinue) {
                        mFusedLocationClientStop.requestLocationUpdates(locationRequestStop, locationCallback, null);
                    } else {

                        mFusedLocationClientStop.getLastLocation().addOnSuccessListener(OnTheWayActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location locationStop) {

//                        String coorLocation; String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude);
                                String StopAt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                                if (locationStop != null) {
                                    wayLatitude = locationStop.getLatitude();
                                    wayLongitude = locationStop.getLongitude();
                                    OnTheWayActivity.locationStop = String.format(Locale.US, "%s,%s", wayLatitude, wayLongitude);
//                            txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));

                                    if (OnTheWayActivity.locationStop != null){
                                        Map<String, Object> stop = new HashMap<>();
                                        stop.put("Berhenti", StopAt);
                                        stop.put("Lokasi_Akhir", OnTheWayActivity.locationStop);
                                        stop.put("Waktu_Perjalanan", timer.getText().toString());
//                                        strt.put("Pengendara", m)m

                                        mDatabase.collection("Travel").document(tID.getText().toString())
                                                .update(stop)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getBaseContext(), "Perjalanan dihentikan", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                    }
                                } else {
                                    mFusedLocationClientStop.requestLocationUpdates(locationRequestStop, locationCallback, null);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void redirectPage(OnTheWayActivity onTheWayActivity, Class<MainActivity> mainActivityClass) {
        Intent main = new Intent(OnTheWayActivity.this, MainActivity.class);
        startActivity(main);
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Hours = Minutes / 60;

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" + String.format("%02d", Hours) + ":"
                    + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

}
