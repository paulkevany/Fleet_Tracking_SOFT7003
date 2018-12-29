package com.example.shanebowen.uberclone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    //Declaring the Global variables
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;
    LocationRequest mLocationRequest;

    private Button mLogout, mRequest, mSettings, mHistory;

    private LatLng pickupLocation, vehicleLocation, destinationLocation;

    private int radius = 1;

    private String vehicleId, destination, serviceSelected;

    private Marker mVehicleMarker, mDestinationMarker;

    private boolean loggingOut = false;
    private boolean vehicleFound = false;
    private boolean makePayment = false;

    private LinearLayout mVehicleInfo;

    private TextView mVehicleName, mRideDistance, mVehicleCar, mRideFare, mVehicleService;

    private ImageView mCarImage;

    private RadioGroup mRadioGroup;

    private RatingBar mRatingBar;

    boolean getVehicles = false;
    List<Marker> markerList = new ArrayList<Marker>();

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{
            R.color.primary_dark_material_light
    };

    private double rideDistance;
    private double ridePrice;

    private final int UBERX = 3;
    private final int UBERXL = 5;
    private final int UBERBLACK = 10;

    private String CHANNEL_ID  = "1";

    private static String PAYPAL_CLIENT_ID = "ATu1jRVIUk6NfFir_tDRu5tmj904iku5wD_AxidJM9tzfbLCA1ZRaPcgE3l9Bg_LghrTFLKPOEtRC68d";

    private int PAYPAL_CODE = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        destinationLocation = new LatLng(0.0, 0.0);

        polylines = new ArrayList<>();

        mVehicleInfo =  findViewById(R.id.vehicleInfo);

        mVehicleName = findViewById(R.id.vehicleName);
        mRideDistance = findViewById(R.id.rideDistance);
        mVehicleCar = findViewById(R.id.vehicleCar);
        mRideFare = findViewById(R.id.rideFare);
        mVehicleService = findViewById(R.id.vehicleService);
        mCarImage = findViewById(R.id.imageView);

        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup.check(R.id.UberX);

        mRatingBar = findViewById(R.id.rating);

        //The logout button exits the maps activity and returns the user to the main activity
        mLogout = findViewById(R.id.log_out);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggingOut = true;
                logOut();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
             }
        });

        //The settings button sends the user to the settings activity
        mSettings = findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return;
            }
        });

        //The history button sends the user to the history activity
        mHistory  = findViewById(R.id.history);
        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, HistoryActivity.class);
                startActivity(intent);
                return;
            }
        });

        //The request button will request a new ride for the user
        mRequest = findViewById(R.id.request);
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If the user doesn't have to make a payment for ride, then it will look for a ride
                if(makePayment == false) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PickupRequest");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

                    mRequest.setText("Looking for Vehicle...");

                    //Creating the radio buttons
                    int selectedId = mRadioGroup.getCheckedRadioButtonId();

                    final RadioButton radioButton = (RadioButton) findViewById(selectedId);

                    if (radioButton.getText() == null) {
                        return;
                    }

                    serviceSelected = radioButton.getText().toString();

                    getClosestVehicle();

                }

                //If the user has to make payment then it will send the user to the paypal page
                else if(makePayment == true){

                    payPalPayment();

                }
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        //Set the value of the destination from the placeautocomplete
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place.getName().toString();
                destinationLocation = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
            }
        });

        //createVehicle();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    //Whenever the user gps location changes then it will update it on the map
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Location");

        GeoFire geoFire = new GeoFire(mDatabase);
        geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

        if(!getVehicles) {
            displayVehicles();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    //Logout button remove your location from the map and also on firebase
    private void logOut(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Location");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!loggingOut) {
            logOut();
        }
    }

    //Get the closest vehicle to your current location
    private void getClosestVehicle(){

        final DatabaseReference vehicleLoc = FirebaseDatabase.getInstance().getReference().child("Vehicle");

        GeoFire geoFire = new GeoFire(vehicleLoc);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if(vehicleFound == false) {
                    DatabaseReference mCustomerRef = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(key);

                    mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                                    if(vehicleFound == true){
                                        return;
                                    }

                                    if (dataSnap.getKey().equals("service")) {

                                        //Check if the service of the vehicle found equals to the service you selected
                                        if(dataSnap.getValue(String.class).equals(serviceSelected)) {

                                            vehicleFound = true;
                                            vehicleId = dataSnapshot.getKey();

                                            DatabaseReference vehicleRef = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId)
                                                    .child("userRequest");
                                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            vehicleRef.child("userID").setValue(userId);
                                            vehicleRef.child("destination").setValue(destination);
                                            vehicleRef.child("destinationLatitude").setValue(destinationLocation.latitude);
                                            vehicleRef.child("destinationLongitude").setValue(destinationLocation.longitude);

                                            getVehicleLocation(vehicleId);
                                            getVehicleInfo(vehicleId);
                                            getDestination(vehicleId);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if(vehicleFound == false){
                    radius = radius + 1;
                    getClosestVehicle();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    //Getting the vehicle location and displaying it on the map
    private void getVehicleLocation(String vehicleId){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    double latitude = 0;
                    double longitude = 0;
                    String name = null;

                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                        if (dataSnap.getKey().equals("l")) {
                            latitude = dataSnap.child("0").getValue(Double.class);
                            longitude = dataSnap.child("1").getValue(Double.class);
                        }

                        if (dataSnap.getKey().equals("registration")) {
                            name = dataSnap.getValue(String.class);
                        }

                        vehicleLocation = new LatLng(latitude, longitude);

                        if (mVehicleMarker != null) {
                            mVehicleMarker.remove();
                        }

                        Location pickupLoc = new Location("");
                        pickupLoc.setLatitude(pickupLocation.latitude);
                        pickupLoc.setLongitude(pickupLocation.longitude);

                        Location vehicleLoc = new Location("");
                        vehicleLoc.setLatitude(vehicleLocation.latitude);
                        vehicleLoc.setLongitude(vehicleLocation.longitude);

                        float distance = pickupLoc.distanceTo(vehicleLoc);

                        //If vehicle less than 100m to the user location then the vehicle is nearby
                        if (distance < 100) {
                            mRequest.setText("Vehicle Nearby");
                        }

                        //If the vehicle is greater than 100m then we display the distance the vehicle is to our current location
                        else if (distance >= 100) {
                            mRequest.setText("Vehicle Found: " + String.valueOf(distance));

                            mVehicleMarker = mMap.addMarker(new MarkerOptions().position(vehicleLocation).title("Vehicle Location")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                            createNotificationChannel();

                            //Passing the values that will be displayed in the notification
                            NotificationCompat.Builder myNotification = new NotificationCompat.Builder(MapsActivity.this, CHANNEL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentText("Vehicle found " + distance + "km away")
                                    .setContentTitle(getString(R.string.phone_notification));

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0, myNotification.build());
                        }

                        //Removing the other vehicle markers from the map
                        for (int i = 0; i < markerList.size(); i++) {
                            markerList.get(i).remove();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Getting the information of the vehicle such as registration, service, rating
    private void getVehicleInfo(final String vehicleId){

        mVehicleInfo.setVisibility(View.VISIBLE);
        DatabaseReference vehicleRef = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId);

        vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String carName = "";
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                    if (dataSnap.getKey().equals("make") || dataSnap.getKey().equals("model") ) {
                        carName += dataSnap.getValue(String.class) + " ";
                        mVehicleName.setText(carName);
                    }

                    if (dataSnap.getKey().equals("registration")) {
                        mVehicleCar.setText(dataSnap.getValue(String.class));
                    }

                    if (dataSnap.getKey().equals("service")) {
                        mVehicleService.setText(dataSnap.getValue(String.class));

                        if(dataSnap.getValue(String.class).equals("UberX")) {
                            mCarImage.setImageResource(R.drawable.uberx);
                        }

                        else if(dataSnap.getValue(String.class).equals("UberXL")){
                            mCarImage.setImageResource(R.drawable.uberxl);
                        }

                        else if(dataSnap.getValue(String.class).equals("UberBlack")){
                            mCarImage.setImageResource(R.drawable.uberblack);
                        }
                    }

                    float sumRatings = 0;
                    float numRatings = 0;
                    float avgRatings;

                    //Get the sum of all the vehicle's ratings and the number of ratings
                    for(DataSnapshot child : dataSnapshot.child("rating").getChildren()){
                        sumRatings = sumRatings + Integer.valueOf(child.getValue().toString());
                        numRatings ++;
                    }

                    //If the user has more than 0 ratings then we get the avg. ratings by dividing the sum by the num.
                    if(numRatings > 0){
                        avgRatings = sumRatings/numRatings;
                        mRatingBar.setRating(avgRatings);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Get the destination that the user entered and display it on the map
    private void getDestination(String vehicleId){

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                double latitude = 0;
                double longitude = 0;

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                    if (dataSnap.getKey().equals("userRequest")) {
                        latitude = dataSnap.child("destinationLatitude").getValue(Double.class);
                        longitude = dataSnap.child("destinationLongitude").getValue(Double.class);
                    }

                    destinationLocation = new LatLng(latitude, longitude);

                    if (mDestinationMarker != null) {
                        mDestinationMarker.remove();
                    }

                    mDestinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

                }

                if(destination != null) {
                    Location pickupLoc = new Location("");
                    pickupLoc.setLatitude(pickupLocation.latitude);
                    pickupLoc.setLongitude(pickupLocation.longitude);

                    Location destinationLoc = new Location("");
                    destinationLoc.setLatitude(destinationLocation.latitude);
                    destinationLoc.setLongitude(destinationLocation.longitude);

                    rideDistance = pickupLoc.distanceTo(destinationLoc) / 1000;
                    mDatabase.child("userRequest").child("distance").setValue(rideDistance);

                    rideDistance = Math.round(rideDistance*100)/100D;

                    mRideDistance.setText(rideDistance + "km");

                    drawRoute(pickupLocation, destinationLocation);

                    mRequest.setText("Make Payment");
                    makePayment = true;

                    calculateRidePrice();
                    createNotificationChannel();

                    //Passing the values that will be displayed in the notification
                    NotificationCompat.Builder myNotification = new NotificationCompat.Builder(MapsActivity.this, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText("You have arrived at your destination")
                            .setContentTitle(getString(R.string.phone_notification));

                    NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, myNotification.build());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Getting the cost of the ride by multiplying the ride distance and the service
    private void calculateRidePrice(){

        DatabaseReference vehicleRef = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId);

        vehicleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                    if (dataSnap.getKey().equals("service")) {

                        if(dataSnap.getValue(String.class).equals("UberX")){
                            ridePrice = rideDistance * UBERX;
                        }

                        else if(dataSnap.getValue(String.class).equals("UberXL")){
                            ridePrice = rideDistance * UBERXL;
                        }

                        else if(dataSnap.getValue(String.class).equals("UberBlack")){
                            ridePrice = rideDistance * UBERBLACK;
                        }
                    }

                }

                ridePrice = Math.round(ridePrice*100)/100D;

                mRideFare.setText("€" + ridePrice);

                recordRide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Get the information about the ride and store it in firebase
    private void recordRide(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference vehicleRef = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(vehicleId).child("history");
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("history");
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("History");

        String requestId = historyRef.push().getKey();
        vehicleRef.child(requestId).setValue(true);
        customerRef.child(requestId).setValue(true);

        //Storing the details of the ride to the history child in firebase
        historyRef = FirebaseDatabase.getInstance().getReference().child("History").child(requestId);
        historyRef.child("vehicle").setValue(vehicleId);
        historyRef.child("customer").setValue(userId);
        historyRef.child("rating").setValue(0);

        historyRef.child("destination").setValue(destination);
        historyRef.child("distance").setValue(rideDistance);
        historyRef.child("price").setValue(ridePrice);

    }

    //Displaying all the vehicles on the map
    private void displayVehicles(){

        getVehicles = true;
        DatabaseReference vehicleLocation = FirebaseDatabase.getInstance().getReference().child("Vehicle");

        vehicleLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double latitude = 0;
                double longitude = 0;

                //Getting all the vehicles stored in firebase & setting a marker to their position
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                        if (dataSnap.getKey()!=null) {

                            if(dataSnap.child("l").exists()) {

                                latitude = dataSnap.child("l").child("0").getValue(Double.class);
                                longitude = dataSnap.child("l").child("1").getValue(Double.class);

                                LatLng vehicleLocation = new LatLng(latitude, longitude);
                                mVehicleMarker = mMap.addMarker(new MarkerOptions().position(vehicleLocation).title("Vehicle").
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                                mVehicleMarker.setTitle("Vehicle");

                                markerList.add(mVehicleMarker);
                            }
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //CreateNotificationChannel creates the channel for the notification
    private void createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //Starts the paypal activity, we pass in the price of the ride, currency, name of transaction
    private void payPalPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(ridePrice), "EUR", "Uber Ride",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_CODE);
    }

    //Checks to see if the payment was successful or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_CODE){
            PaymentConfirmation confirmation = data .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if(confirmation != null){
                try{
                    JSONObject jsonObject = new JSONObject(confirmation.toJSONObject().toString());

                    String paymentResult = jsonObject.getJSONObject("response").getString("state");

                    if(paymentResult.equals("approved")){
                        Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                        mRequest.setEnabled(false);

                        createNotificationChannel();

                        //Passing the values that will be displayed in the notification
                        NotificationCompat.Builder myNotification = new NotificationCompat.Builder(MapsActivity.this, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentText("Thank you for payment")
                                .setContentTitle(getString(R.string.phone_notification));

                        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(2, myNotification.build());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        else{
            Toast.makeText(getApplicationContext(), "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    //Drawing the route between the pickup location and the destination
    private void drawRoute(LatLng pickupLocation, LatLng destinationLoc){

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(pickupLocation, destinationLoc)
                .key("AIzaSyDLRnHvXy7s2zwiD_o5sCzyKAZOf2JzFiA")
                .build();
        routing.execute();
    }

    //If routing fails then we display error message
    @Override
    public void onRoutingFailure(RouteException e) {

        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {
    }

    //If route is success then we draw the line between the two points
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() +
                    ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onRoutingCancelled(){

    }

    //Create a vehicle and store it in firebase
    private void createVehicle(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Vehicle");
        String id = mDatabase.push().getKey();

        GeoFire geoFire = new GeoFire(mDatabase);
        geoFire.setLocation(id, new GeoLocation(51.934039, -8.4837761));

        DatabaseReference vehicleRef = FirebaseDatabase.getInstance().getReference().child("Vehicle").child(id);
        vehicleRef.child("make").setValue("Ford");
        vehicleRef.child("model").setValue("Mondeo");
        vehicleRef.child("registration").setValue("11-C-5129");
        vehicleRef.child("service").setValue("UberX");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}