package com.android.locationbasednotes.authenticating;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.locationbasednotes.FirebaseManager;
import com.android.locationbasednotes.R;
import com.android.locationbasednotes.data.User;
import com.android.locationbasednotes.utils.MySheredP;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class AuthenticateBaseActivity extends AppCompatActivity  {
    protected Button authenticate_base_BTN_do_action;
    protected TextInputLayout authenticate_base_EDT_email, authenticate_base_EDT_password;
    protected FirebaseAuth auth;
    protected TextView authenticate_base_TXT_title;
    protected FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected DatabaseReference myRef;
    protected Gson gson = new Gson();
    protected MySheredP msp;
    protected LinearLayout authenticate_base_LIY_layout;
    protected User currentUser;
    protected ProgressBar authenticate_base_PRB_progressBar;
    protected FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_base_activity);
        findViews();
        firebaseManager = FirebaseManager.GetInstance();
        getSupportActionBar().hide();
        authenticate_base_PRB_progressBar.setVisibility(View.INVISIBLE);
        myRef= database.getReference(getString(R.string.AllUsersFirebase));
        auth = FirebaseAuth.getInstance();
        msp = new MySheredP(this);
    }

    protected void closeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected boolean checkField(EditText editTextToCheck) {
        if (editTextToCheck.getText().toString().equals("")) {
            editTextToCheck.setError(getText(R.string.editTextError));
            return false;
        }
        return true;
    }

    protected void putOnMSP(User userToSave) {
        String user = gson.toJson(userToSave);
        msp.putString(getString(R.string.UserKey), user);
    }
    protected User getUserFromMSP() {
        Gson gson = new Gson();
        String data = msp.getString(getString(R.string.UserKey), "NA");
        if(!data.equals("NA"))
            currentUser = gson.fromJson(data, User.class);
        return currentUser;
    }

    private void findViews() {
        authenticate_base_BTN_do_action =  findViewById(R.id.authenticate_base_BTN_do_action);
        authenticate_base_EDT_email =  findViewById(R.id.authenticate_base_EDT_email);
        authenticate_base_EDT_password =  findViewById(R.id.authenticate_base_EDT_password);
        authenticate_base_TXT_title =  findViewById(R.id.authenticate_base_TXT_title);
        authenticate_base_LIY_layout =  findViewById(R.id.authenticate_base_LIY_layout);
        authenticate_base_PRB_progressBar =  findViewById(R.id.authenticate_base_PRB_progressBar);
    }

}