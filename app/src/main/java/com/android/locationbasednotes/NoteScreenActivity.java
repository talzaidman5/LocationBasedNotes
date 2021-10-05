package com.android.locationbasednotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NoteScreenActivity extends AppCompatActivity implements LocationListener {
    private Button activity_note_screen_BTN_save, activity_note_screen_BTN_delete;
    private EditText activity_note_screen_EDT_title, activity_note_screen_EDT_body;
    private MySheredP msp;
    private User currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FusedLocationProviderClient fusedLoactionProviderClient;
    private final int REQUEST_CODE = 44;
    private LocationManager locationManager;
    private double[] currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);
        FindViews();
        msp = new MySheredP(this);
        myRef = database.getReference(getString(R.string.AllUsersFirebase));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fusedLoactionProviderClient = LocationServices.getFusedLocationProviderClient(this);
          //  getFromMSP();
        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetLocation();
              //  CreateNewNote();
            }
        });
    }

    private void GetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void CreateNewNote() {
        Note note = new Note(activity_note_screen_EDT_title.getText().toString(),activity_note_screen_EDT_body.getText().toString());
        AddNoteToUser(note);
        Toast.makeText(getApplicationContext(), "New note successfully added!", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this, MainScreenActivity.class));

    }

    private void AddNoteToUser(Note note) {
        currentUser.addToNoteList(note);
        SaveToFirebase(currentUser);
    }
    private void SaveToFirebase(User userToSave) {
        myRef.child(userToSave.getUid()).setValue(userToSave);

    }

    private void FindViews() {
        activity_note_screen_BTN_delete = findViewById(R.id.activity_note_screen_BTN_delete);
        activity_note_screen_BTN_save = findViewById(R.id.activity_note_screen_BTN_save);
        activity_note_screen_EDT_title = findViewById(R.id.activity_note_screen_EDT_title);
        activity_note_screen_EDT_body = findViewById(R.id.activity_note_screen_EDT_body);
    }
    private void getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation[0] =  location.getLatitude();
        currentLocation[1] = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}

