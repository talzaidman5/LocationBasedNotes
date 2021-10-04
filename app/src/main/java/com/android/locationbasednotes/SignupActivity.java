package com.android.locationbasednotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class SignupActivity extends AppCompatActivity {
    private Button signUp_BTN_signUp;
    private EditText signUp_EDT_email,signUp_EDT_password;
    private FirebaseAuth auth;
    private TextView signUp_TXT_error;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private Gson gson = new Gson();
    private MySheredP msp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FindViews();

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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else
                        signUp_TXT_error.setText(task.getException().getMessage());
                }
            });
        } else
            signUp_TXT_error.setText("Please Enter fields");
    }

    private void SaveToFirebase(User userToSave) {
        myRef.child(userToSave.getUid()).setValue(userToSave);

    }
    private void putOnMSP(User userToSave) {
        String user = gson.toJson(userToSave);
        msp.putString(getString(R.string.UserKey), user);
    }

    private void FindViews() {
        signUp_BTN_signUp =  findViewById(R.id.signUp_BTN_signUp);
        signUp_EDT_email =  findViewById(R.id.signUp_EDT_email);
        signUp_EDT_password =  findViewById(R.id.signUp_EDT_password);
        signUp_TXT_error =  findViewById(R.id.signUp_TXT_error);
    }
}