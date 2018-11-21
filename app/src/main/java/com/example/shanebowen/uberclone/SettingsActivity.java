package com.example.shanebowen.uberclone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private EditText mNameText, mPhoneText;

    private Button mSave, mBack;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    private String userId;
    private String mName, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mNameText = (EditText) findViewById(R.id.name);
        mPhoneText = (EditText) findViewById(R.id.phone);

        mSave = (Button) findViewById(R.id.save);
        mBack = (Button) findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

        getUserInformation();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
                 return;
            }
        });

    }

    private void getUserInformation() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {

                        if (dataSnap.getKey().equals("name")) {
                            mName = dataSnap.getValue(String.class);
                            mNameText.setText(mName);
                        }

                        if (dataSnap.getKey().equals("phoneNumber")) {
                            mPhone = dataSnap.getValue(String.class);
                            mPhoneText.setText(mPhone);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void saveInformation() {

        mName = mNameText.getText().toString();
        mPhone = mPhoneText.getText().toString();

        mDatabaseReference.child("name").setValue(mName);
        mDatabaseReference.child("phoneNumber").setValue(mPhone);

        finish();
    }
}