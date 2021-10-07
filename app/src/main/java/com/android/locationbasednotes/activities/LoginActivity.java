package com.android.locationbasednotes.activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.view.ViewGroup.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends SignupActivity {
    private FirebaseUser firebaseUser;
    public boolean isLoginAuth = false;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = getUserFromMSP();
        changeFieldsToLogin();

        signUp_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkField(signUp_EDT_email)&&checkField(signUp_EDT_password))
                    signUpUser(signUp_EDT_email.getText().toString(), signUp_EDT_password.getText().toString());
            }
        });
    }

    private void changeFieldsToLogin() {
        signUp_BTN_signUp.setText(getString(R.string.login));
        activity_main_TXT_title.setText(getString(R.string.login));

        LinearLayout linearLayout = new LinearLayout(this);
        TextView isLoginAuthText = new TextView(this);
        CheckBox isLoginAuthButton = new CheckBox(this);

        isLoginAuthButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLoginAuth = isChecked;
            }
        });
        isLoginAuthButton.setText(getString(R.string.loginAuthText));
        isLoginAuthButton.setId(R.id.signUp_BTN_loginAuth);

        linearLayout.addView(isLoginAuthButton);
        linearLayout.addView(isLoginAuthText);
        signUp_LIY_layout.addView(linearLayout);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if(currentUser!=null)
            if(currentUser.isLoginAuth())
                isLoginAuthButton.setChecked(true);
    }

    private void signUpUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = auth.getCurrentUser();
                            getFromFirebase();
                                if(currentUser.isLoginAuth()!=isLoginAuth)
                                    currentUser.setLoginAuth(isLoginAuth);
                                    saveToFirebase(currentUser);
                        } else
                            Toast.makeText(getApplicationContext(), getString(R.string.AuthenticationFailed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getFromFirebase() {
        myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                putOnMSP(currentUser);
                if (isLoginAuth){
                    currentUser.setLoginAuth(isLoginAuth);
                    saveToFirebase(currentUser);
                }
                finish();
                startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}