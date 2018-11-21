package com.example.shanebowen.uberclone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.location.LocationListener, RoutingListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private Button mLogout, mRequest, mSettings;

    private LatLng pickupLocation, driverLocation;
    private LatLng destinationLocation;

    private int radius = 1;

    private String driverId, destination, serviceSelected;

    private Marker mDriverMarker, mDestinationMarker;

    private boolean loggingOut = false;
    private boolean driverFound = false;
    private boolean makePayment = false;

    private LinearLayout mDriverInfo;

    private TextView mDriverName, mRideDistance, mDriverCar, mRideFare, mDriverService;

    private ImageView mCarImage;

    private RadioGroup mRadioGroup;

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    private double rideDistance;
    private double ridePrice;

    private final int UBERX = 5;
    private final int UBERXL = 10;
    private final int UBERBLACK = 15;

    private int PAYPAL_CODE = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

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

        mDriverInfo =  findViewById(R.id.driverInfo);

        mDriverName = findViewById(R.id.driverName);
        mRideDistance = findViewById(R.id.rideDistance);
        mDriverCar = findViewById(R.id.driverCar);
        mRideFare = findViewById(R.id.rideFare);
        mDriverService = findViewById(R.id.driverService);
        mCarImage = findViewById(R.id.imageView);

        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup.check(R.id.UberX);

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

        mSettings = findViewById(R.id.settings);

        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return;
            }
        });


        mRequest = findViewById(R.id.request);
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(makePayment == false) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Request");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                    mRequest.setText("Looking for Driver...");

                    // Radio Button Services
                    int selectedId = mRadioGroup.getCheckedRadioButtonId();

                    final RadioButton radioButton = (RadioButton) findViewById(selectedId);

                    if (radioButton.getText() == null) {
                        return;
                    }

                    serviceSelected = radioButton.getText().toString();

                    getClosestDriver();

                }

                else if(makePayment == true){

                    payPalPayment();

                }
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

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

        //createDriver();
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Location");

        GeoFire geoFire = new GeoFire(mDatabase);
        geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
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

    private void getClosestDriver(){

        final DatabaseReference driverLoc = FirebaseDatabase.getInstance().getReference().child("Driver");

        GeoFire geoFire = new GeoFire(driverLoc);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if(driverFound == false) {
                    DatabaseReference mCustomerRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(key);

                    mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                                    if(driverFound == true){
                                        return;
                                    }
                                    if (dataSnap.getKey().equals("service")) {

                                        if(dataSnap.getValue(String.class).equals(serviceSelected)) {

                                            driverFound = true;
                                            driverId = dataSnapshot.getKey();

                                            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId).child("userRequest");
                                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            driverRef.child("userID").setValue(userId);
                                            driverRef.child("destination").setValue(destination);
                                            driverRef.child("destinationLatitude").setValue(destinationLocation.latitude);
                                            driverRef.child("destinationLongitude").setValue(destinationLocation.longitude);

                                            getDriverLocation(driverId);
                                            getDriverInfo(driverId);
                                            getDestination(driverId);
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

                if(driverFound == false){
                    radius = radius + 1;
                    getClosestDriver();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void getDriverLocation(String driverId){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //if(dataSnapshot.exists()) {

                double latitude = 0;
                double longitude = 0;
                String name = null;

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                    if (dataSnap.getKey().equals("l")) {
                        latitude = dataSnap.child("0").getValue(Double.class);
                        longitude = dataSnap.child("1").getValue(Double.class);
                    }

                    if (dataSnap.getKey().equals("name")) {
                        name = dataSnap.getValue(String.class);
                    }

                    driverLocation = new LatLng(latitude, longitude);

                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }

                    Location pickupLoc = new Location("");
                    pickupLoc.setLatitude(pickupLocation.latitude);
                    pickupLoc.setLongitude(pickupLocation.longitude);

                    Location driverLoc = new Location("");
                    driverLoc.setLatitude(driverLocation.latitude);
                    driverLoc.setLongitude(driverLocation.longitude);

                    float distance = pickupLoc.distanceTo(driverLoc);

                    if(distance < 100){
                        mRequest.setText("Driver Nearby");
                    }

                    else if(distance >= 100) {
                        mRequest.setText("Driver Found: " + String.valueOf(distance));
                    }

                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver " + name + " Location"));

                }

                //drawRoute(pickupLocation, driverLocation);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getDriverInfo(final String driverId){
        mDriverInfo.setVisibility(View.VISIBLE);
        DatabaseReference mDriverRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId);

        mDriverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                    if (dataSnap.getKey().equals("name")) {
                        mDriverName.setText(dataSnap.getValue(String.class));
                    }

//                    if (dataSnap.getKey().equals("phoneNumber")) {
//                        mDriverPhone.setText(dataSnap.getValue(String.class));
//                    }

                    if (dataSnap.getKey().equals("car")) {
                        mDriverCar.setText(dataSnap.getValue(String.class));
                    }

                    if (dataSnap.getKey().equals("service")) {
                        mDriverService.setText(dataSnap.getValue(String.class));

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

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDestination(String driverId){

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId);

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

                    mDestinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination"));

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
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void calculateRidePrice(){

        DatabaseReference mDriverRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId);

        mDriverRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void payPalPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(ridePrice), "EUR", "Uber Ride",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_CODE);
    }

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

    private void drawRoute(LatLng pickupLocation, LatLng driverLocation){

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(pickupLocation, driverLocation)
                .key("AIzaSyDLRnHvXy7s2zwiD_o5sCzyKAZOf2JzFiA")
                .build();
        routing.execute();

    }


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

    private void eraseRoute(){

        for(Polyline line: polylines){
            line.remove();
        }

        polylines.clear();
    }


    private void createDriver(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        String id = mDatabase.push().getKey();

        GeoFire geoFire = new GeoFire(mDatabase);
        geoFire.setLocation(id, new GeoLocation(51.904039, -8.5037761));

        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(id);
        driverRef.child("name").setValue("Tim");
        driverRef.child("phoneNumber").setValue("086-1234567");
        driverRef.child("car").setValue("Sedan");
        driverRef.child("service").setValue("UberBlack");

    }

        @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}