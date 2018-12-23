package com.example.shanebowen.uberclone.historyRecyclerView;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shanebowen.uberclone.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class HistorySingleActivity extends AppCompatActivity {

    private String rideId, vehicleId;

    private TextView locationRide;
    private TextView distanceRide;
    private TextView priceRide;

    private TextView vehicleMake;
    private TextView vehicleModel;
    private TextView vehicleService;

    private RatingBar ratingBar;

    private DatabaseReference historyRideInfo;

    private LatLng destinationLocation, pickupDestination;

    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_single);

        rideId = getIntent().getExtras().getString("rideId");

        locationRide = (TextView) findViewById(R.id.rideLocation);
        distanceRide = (TextView) findViewById(R.id.rideDistance);
        priceRide = (TextView) findViewById(R.id.ridePrice);

        vehicleMake = (TextView) findViewById(R.id.make);
        vehicleModel = (TextView) findViewById(R.id.model);
        vehicleService = (TextView) findViewById(R.id.service);

        ratingBar = (RatingBar) findViewById(R.id.rating);

        saveBtn = (Button) findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });


        historyRideInfo = FirebaseDatabase.getInstance().getReference().child("History").child(rideId);
        getRideInformation();


    }

    private void getRideInformation() {
        historyRideInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("vehicle")) {
                            vehicleId = child.getValue().toString();
                            getVehicleInformation(vehicleId);
                            displayVehicleRating();
                        }

                        if(child.getKey().equals("destination")){
                            locationRide.setText("Destination: " + child.getValue().toString());
                        }

                        if(child.getKey().equals("distance")){
                            distanceRide.setText("Distance: " + child.getValue().toString() + "km");
                        }

                        if(child.getKey().equals("price")){
                            priceRide.setText("Price: €" + child.getValue().toString());
                        }

                        if(child.getKey().equals("rating")){
                            ratingBar.setRating(Integer.valueOf(child.getValue().toString()));
                        }

                        if(child.getKey().equals("location")){
                            pickupDestination = new LatLng(Double.valueOf(child.child("from").child("lat").getValue().toString()),
                                    Double.valueOf(child.child("from").child("lng").getValue().toString()));

                            destinationLocation = new LatLng(Double.valueOf(child.child("to").child("lat").getValue().toString()),
                                    Double.valueOf(child.child("to").child("lng").getValue().toString()));

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayVehicleRating() {
        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                historyRideInfo.child("rating").setValue(rating);
                DatabaseReference driverRatingDB = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId).child("rating");
                driverRatingDB.child(rideId).setValue(rating);
            }
        });
    }

    private void getVehicleInformation(String vehicleId) {
        DatabaseReference vehicleDatabase = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId);
        vehicleDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    vehicleMake.setText("Make: " + map.get("make").toString());
                    vehicleModel.setText("Model: " + map.get("model").toString());
                    vehicleService.setText("Service: " + map.get("service").toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
