package com.android.locationbasednotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class SignupActivity extends AppCompatActivity  {
    protected Button signUp_BTN_signUp;
    protected EditText signUp_EDT_email,signUp_EDT_password;
    protected FirebaseAuth auth;
    protected TextView activity_main_TXT_title;
    protected FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected DatabaseReference myRef;
    protected Gson gson = new Gson();
    protected MySheredP msp;
    protected LinearLayout signUp_LIY_layout;
    protected User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FindViews();
        getSupportActionBar().hide();

        myRef= database.getReference(getString(R.string.AllUsersFirebase));
        auth = FirebaseAuth.getInstance();
        msp = new MySheredP(this);

        signUp_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(signUp_EDT_email.getText().toString(), signUp_EDT_password.getText().toString());
            }
        });
    }

    private void register(String email, String password) {
        if (email != null && password != null) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        User user = new User(email, password, firebaseUser.getUid());
                        SaveToFirebase(user);
                        putOnMSP(user);
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                    } else
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.emptyFields), Toast.LENGTH_SHORT).show();
    }

    protected void SaveToFirebase(User userToSave) {
        myRef.child(userToSave.getUid()).setValue(userToSave);
    }
    protected void putOnMSP(User userToSave) {
        String user = gson.toJson(userToSave);
        msp.putString(getString(R.string.UserKey), user);
    }
    protected User getFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        currentUser = gson.fromJson(data, User.class);
        return currentUser;
    }

    private void FindViews() {
        signUp_BTN_signUp =  findViewById(R.id.signUp_BTN_signUp);
        signUp_EDT_email =  findViewById(R.id.signUp_EDT_email);
        signUp_EDT_password =  findViewById(R.id.signUp_EDT_password);
        activity_main_TXT_title =  findViewById(R.id.activity_main_TXT_title);
        signUp_LIY_layout =  findViewById(R.id.signUp_LIY_layout);
    }

}