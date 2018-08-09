package ru.prog_edu.thronethingshablon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private Button mRegisterButton;
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String USER_ID = "user_id";
    private SharedPreferences sharedPreferences;
    private String userId;

    private FirebaseAuth mAuth;
    private static String TAG = "LOGIN_ACTIVITY: ";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        mEmailField = findViewById(R.id.et_login_enter_mail);
        mPasswordField = findViewById(R.id.et_login_enter_pass);
        mLoginButton = findViewById(R.id.btn_login);
        mRegisterButton = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkState networkState = new NetworkState(LoginActivity.this);
                boolean isOnline = networkState.isOnline();
                if(isOnline) {
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

                }else{
                    Toast.makeText(LoginActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                }
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkState networkState = new NetworkState(LoginActivity.this);
                boolean isOnline = networkState.isOnline();
                if(isOnline){
                    signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

                }else{
                    Toast.makeText(LoginActivity.this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password){

        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            userId = user.getUid();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USER_ID, userId);
                            editor.apply();


                            updateUI(user);

                            Intent intent = new Intent(LoginActivity.this, MainGoalsActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {

                        }
                    }

                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null) {
                                userId = user.getUid();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USER_ID, userId);
                                editor.apply();
                                updateUI(user);
                                Intent intent = new Intent(LoginActivity.this, MainGoalsActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mRegisterButton.setVisibility(View.GONE);
            mLoginButton.setVisibility(View.VISIBLE);

        } else {
            mRegisterButton.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.GONE);
        }
    }
}
