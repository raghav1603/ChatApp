package com.example.raghav.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    EditText email,pass,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email= findViewById(R.id.email0);
        pass= findViewById(R.id.pass0);
        name=findViewById(R.id.name0);
        mAuth= FirebaseAuth.getInstance();
        findViewById(R.id.create).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.create:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String mEmail= email.getText().toString().trim();
        String mPass= pass.getText().toString().trim();
        final String mName= name.getText().toString().trim();
        if (mEmail.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (mPass.isEmpty()) {
            pass.setError("pass  is required");
            pass.requestFocus();
            return;
        }
        if (mName.isEmpty()) {
            name.setError("Please enter your Username");
            name.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mName)
                            .build();
                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, mAuth.getCurrentUser().getDisplayName() + " is sucsessfully registered", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, ChatScreen.class);
                    startActivity(i);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "user already registered", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

}
