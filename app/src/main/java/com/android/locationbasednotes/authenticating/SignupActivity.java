package com.android.locationbasednotes.authenticating;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.activities.MainScreenActivity;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AuthenticateBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        msp = new MySheredP(this);
        changeFieldsToLogin();

        authenticate_base_BTN_do_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(view);
                if(checkField(authenticate_base_EDT_email.getEditText()) && checkField(authenticate_base_EDT_password.getEditText()))
                  register(authenticate_base_EDT_email.getEditText().getText().toString(), authenticate_base_EDT_password.getEditText().getText().toString());
            }
        });
    }
    protected boolean checkField(EditText editTextToCheck) {
        if (editTextToCheck.getText().toString().equals("")) {
            editTextToCheck.setError(getText(R.string.editTextError));
            return false;
        }
        return true;
    }


    private void register(String email, String password) {
        if (email != null && password != null) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        authenticate_base_PRB_progressBar.setVisibility(View.VISIBLE);
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        User user = new User(email, password, firebaseUser.getUid());
                        firebaseManager.writeToFirebase(user);
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

    private void changeFieldsToLogin() {
        authenticate_base_BTN_do_action.setText(getString(R.string.signup));
        authenticate_base_TXT_title.setText(getString(R.string.signup));
    }

}