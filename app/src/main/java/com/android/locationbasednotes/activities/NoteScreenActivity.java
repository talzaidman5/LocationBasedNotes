package com.android.locationbasednotes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import com.android.locationbasednotes.data.Note;
import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NoteScreenActivity extends AppCompatActivity {
    protected Button activity_note_screen_BTN_save, activity_note_screen_BTN_delete,activity_note_screen_BTN_uploadImage;
    protected EditText activity_note_screen_EDT_title, activity_note_screen_EDT_body;
    protected TextView activity_note_screen_TXT_title;
    private MySheredP msp;
    private ProgressBar activity_note_screen_PRB_progressBar;
    protected User currentUser;
    protected Note currentNote;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private LocationRequest locationRequest;
    private List<Double> vetLocation = new ArrayList<>();
    protected ImageView activity_note_screen_IMG_image;
    private boolean isAddImage = false;
    private Uri fileUri;
    protected StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_screen);
        getSupportActionBar().hide();

        initData();
        findViews();
        getUserFromMSP();
        activity_note_screen_PRB_progressBar.setVisibility(View.INVISIBLE);

        activity_note_screen_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkField(activity_note_screen_EDT_title) && checkField(activity_note_screen_EDT_body))
                    activity_note_screen_PRB_progressBar.setVisibility(View.VISIBLE);
                saveNote();
            }
        });
        activity_note_screen_BTN_uploadImage.setOnClickListener(v -> getImage());
    }

    private void initData() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        msp = new MySheredP(this);
        myRef = database.getReference(getString(R.string.AllUsersFirebase));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled())
                    saveNote();
                else
                    turnOnGPS();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK)
                saveNote();
        } else {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data.getData();
                isAddImage = true;
            } else if (resultCode == ImagePicker.RESULT_ERROR)
                Toast.makeText(this, new ImagePicker().Companion.getError(data), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveNote() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(NoteScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(getApplicationContext())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(NoteScreenActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        vetLocation = new ArrayList<>();
                                        vetLocation.add(locationResult.getLocations().get(index).getLatitude());
                                        vetLocation.add(locationResult.getLocations().get(index).getLongitude());
                                        createNewNote();

                                    }
                                }
                            }, Looper.getMainLooper());

                } else
                    turnOnGPS();
            } else
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());
        result.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoteScreenActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

            }
        });
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(NoteScreenActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(NoteScreenActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    private void createNewNote() {
        Note note = new Note(activity_note_screen_EDT_title.getText().toString(), activity_note_screen_EDT_body.getText().toString(), vetLocation);
        if (isAddImage) {
            note.setImage(true);
            saveToFirebase(currentUser);
            saveImage(note);
        }
        addNoteToUser(note);
        Toast.makeText(getApplicationContext(), "New note successfully added!", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this, MainScreenActivity.class));
    }

    private boolean checkField(EditText editTextToCheck) {
        if (editTextToCheck.getText().toString().equals("")) {
            editTextToCheck.setError(getText(R.string.editTextError));
            return false;
        }
        return true;
    }

    protected void saveImage(Note note) {
        mStorageRef.child(currentUser.getUid()).child(note.getID()).putFile(fileUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(getApplicationContext(), "uploaded ", Toast.LENGTH_SHORT).show();
                        note.setImage(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addNoteToUser(Note note) {
        currentUser.addToNoteList(note);
        saveToFirebase(currentUser);
    }

    protected void saveToFirebase(User userToSave) {
        myRef.child(userToSave.getUid()).setValue(userToSave);
    }

    private void findViews() {
        activity_note_screen_BTN_delete = findViewById(R.id.activity_note_screen_BTN_delete);
        activity_note_screen_BTN_save = findViewById(R.id.activity_note_screen_BTN_save);
        activity_note_screen_EDT_title = findViewById(R.id.activity_note_screen_EDT_title);
        activity_note_screen_EDT_body = findViewById(R.id.activity_note_screen_EDT_body);
        activity_note_screen_TXT_title = findViewById(R.id.activity_note_screen_TXT_title);
        activity_note_screen_BTN_uploadImage = findViewById(R.id.activity_note_screen_BTN_uploadImage);
        activity_note_screen_IMG_image = findViewById(R.id.activity_note_screen_IMG_image);
        activity_note_screen_PRB_progressBar = findViewById(R.id.activity_note_screen_PRB_progressBar);
    }

    protected User getUserFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
        return currentUser;
    }

    protected Note getNoteFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.noteKey), "NA");
        currentNote = gson.fromJson(data, Note.class);
        return currentNote;
    }

    protected void getImage() {
        ImagePicker.Companion
                .with(this)
                .crop()
                .cropOval()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

}