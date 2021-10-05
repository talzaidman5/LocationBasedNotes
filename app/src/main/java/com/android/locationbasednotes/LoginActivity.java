package com.android.locationbasednotes;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.view.ViewGroup.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends  SignupActivity {
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUp_BTN_signUp.setText(getString(R.string.login));
        activity_main_TXT_title.setText(getString(R.string.login));

        signUp_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(signUp_EDT_email.getText().toString(), signUp_EDT_password.getText().toString());
            }
        });
        ChangeFieldsToLogin();
    }

    private void ChangeFieldsToLogin() {
        LinearLayout linearLayout = new LinearLayout(this);
        TextView isLoginAuthText = new TextView(this);
        Button isLoginAuthButton = new Button(this);
        isLoginAuthText.setText(getString(R.string.loginAuthText));

        isLoginAuthButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        isLoginAuthButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_background));
        isLoginAuthButton.setId(R.id.signUp_BTN_loginAuth);

        linearLayout.addView(isLoginAuthButton);
        linearLayout.addView(isLoginAuthText);
        signUp_LIY_layout.addView(linearLayout);
    }

    private void register(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            getFromFirebase();
                            startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                        } else
                            Toast.makeText(getApplicationContext(), getString(R.string.AuthenticationFailed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getFromFirebase() {
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                putOnMSP(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
