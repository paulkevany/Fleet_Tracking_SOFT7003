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
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private Button mLogout, mRequest;

    private LatLng pickupLocation, driverLocation;

    private int radius = 1;

    private String driverId;
    private String destination;

    private Marker mDriverMarker;

    private boolean loggingOut = false;
    private boolean driverFound = false;

    private LinearLayout mDriverInfo;

    private TextView mDriverName, mDriverPhone, mDriverCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDriverInfo = (LinearLayout) findViewById(R.id.driverInfo);

        mDriverName = (TextView) findViewById(R.id.driverName);
        mDriverPhone = (TextView) findViewById(R.id.driverPhone);
        mDriverCar = (TextView) findViewById(R.id.driverCar);

        mLogout = (Button) findViewById(R.id.log_out);
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

        mRequest = (Button) findViewById(R.id.request);
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Request");

                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                mRequest.setText("Looking for Driver...");

                getClosestDriver();
                getDriverLocation();
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place.getName().toString();
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

    private void getDriverLocation(){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Driver");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //if(dataSnapshot.exists()) {

                    double latitude = 0;
                    double longitude = 0;
                    String name = null;

                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                        if (dataSnap.child("l").child("0").getValue(Double.class) != null) {
                            latitude = dataSnap.child("l").child("0").getValue(Double.class);
                        }

                        if (dataSnap.child("l").child("1").getValue(Double.class) != null) {
                            longitude = dataSnap.child("l").child("1").getValue(Double.class);
                        }

                        name = dataSnap.child("name").getValue(String.class);

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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getClosestDriver(){

        DatabaseReference driverLoc = FirebaseDatabase.getInstance().getReference().child("Driver");

        GeoFire geoFire = new GeoFire(driverLoc);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if(driverFound == false) {
                    driverFound = true;
                    driverId = key;

                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(driverId).child("userRequest");
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    driverRef.child("userID").setValue(userId);
                    driverRef.child("destination").setValue(destination);

                    getDriverInfo(driverId);
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

                    if (dataSnap.getKey().equals("phoneNumber")) {
                        mDriverPhone.setText(dataSnap.getValue(String.class));
                    }

                    if (dataSnap.getKey().equals("car")) {
                        mDriverCar.setText(dataSnap.getValue(String.class));
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createDriver(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
        String id = mDatabase.push().getKey();

        GeoFire geoFire = new GeoFire(mDatabase);
        geoFire.setLocation(id, new GeoLocation(51.904039, -8.5037761));

        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Driver").child(id);
        driverRef.child("name").setValue("John");
        driverRef.child("phoneNumber").setValue("086-1234567");
        driverRef.child("car").setValue("Sedan");
        //driverRef.child("service").setValue("UberX");

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