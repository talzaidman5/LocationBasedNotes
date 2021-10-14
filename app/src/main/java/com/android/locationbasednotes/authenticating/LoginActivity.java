package com.android.locationbasednotes.authenticating;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.locationbasednotes.firebase.FirebaseManager;
import com.android.locationbasednotes.firebase.OnUserFetchedCallback;
import com.android.locationbasednotes.R;
import com.android.locationbasednotes.activities.MainScreenActivity;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.firebase.OnUserSignCallback;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AuthenticateBaseActivity {
    public boolean isLoginAuth = false;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = getUserFromMSP();
        changeFieldsToLogin();

        authenticate_base_BTN_do_action.setOnClickListener(view -> {
            closeKeyboard(view);
            EditText currentEmail =authenticate_base_EDT_email.getEditText(),
                    currentPassword =authenticate_base_EDT_password.getEditText();

            if(checkField(currentEmail)&&checkField(currentPassword))
                signInUser(currentEmail.getText().toString(), currentPassword.getText().toString());
        });
    }


    private void changeFieldsToLogin() {

        changeFieldsToLogin(getString(R.string.login));

        LinearLayout linearLayout = new LinearLayout(this);
        TextView isLoginAuthText = new TextView(this);
        CheckBox isLoginAuthButton = new CheckBox(this);

        isLoginAuthButton.setOnCheckedChangeListener((buttonView, isChecked) -> isLoginAuth = isChecked);
        isLoginAuthButton.setText(getString(R.string.loginAuthText));
        isLoginAuthButton.setId(R.id.signUp_BTN_loginAuth);

        linearLayout.addView(isLoginAuthButton);
        linearLayout.addView(isLoginAuthText);
        authenticate_base_LIY_layout.addView(linearLayout);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if(currentUser!=null)
            if(currentUser.isLoginAuth())
                isLoginAuthButton.setChecked(true);
    }

    private void signInUser(String email, String password) {
        dbManager.signInWithEmailAndPassword(email, password, task -> {
            if (task.isSuccessful()) {
                authenticate_base_PRB_progressBar.setVisibility(View.VISIBLE);
                getFromFirebase(dbManager.getCurrentUserIDFromDB());
            } else
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void getFromFirebase(String currentUserIDFromDB) {
        dbManager.readFromDB(currentUserIDFromDB, this, user -> {
            currentUser = user;
            if (currentUser.isLoginAuth() != isLoginAuth) {
                currentUser.setLoginAuth(isLoginAuth);
                dbManager.writeToDB(currentUser);
            }
            putOnMSP(currentUser);
            finish();
            startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
        });

    }
}