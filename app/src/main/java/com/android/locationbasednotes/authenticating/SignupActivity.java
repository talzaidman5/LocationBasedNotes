package com.android.locationbasednotes.authenticating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.locationbasednotes.R;
import com.android.locationbasednotes.activities.MainScreenActivity;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.firebase.OnUserSignCallback;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.gms.tasks.Task;

public class SignupActivity extends AuthenticateBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        changeFieldsToLogin(getString(R.string.signup));

        authenticate_base_BTN_do_action.setOnClickListener(view -> {
            closeKeyboard(view);
            EditText currentEmail =authenticate_base_EDT_email.getEditText(),
                    currentPassword =authenticate_base_EDT_password.getEditText();
            if(checkField(currentEmail) && checkField(currentPassword))
              register(currentEmail.getText().toString(), currentPassword.getText().toString());
        });
    }


    private void register(String email, String password) {
        if (email != null && password != null) {
            dbManager.createUserWithEmailAndPassword(email, password , task -> {

            if (task.isSuccessful()) {
                    authenticate_base_PRB_progressBar.setVisibility(View.VISIBLE);
                    String userID = dbManager.getCurrentUserIDFromDB();
                    assert userID != null;
                    User user = new User(email, password, userID);
                    dbManager.writeToDB(user);
                    putOnMSP(user);
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                } else
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.emptyFields), Toast.LENGTH_SHORT).show();
    }



}