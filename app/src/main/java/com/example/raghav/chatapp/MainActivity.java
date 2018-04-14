package com.example.raghav.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    EditText email,pass;
    RelativeLayout chat_screen;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        chat_screen= findViewById(R.id.chat_relative_screen);
        findViewById(R.id.signup).setOnClickListener(this);
        findViewById(R.id.signin).setOnClickListener(this);
        email= findViewById(R.id.email1);
        pass= findViewById(R.id.pass);
        if (mAuth.getCurrentUser()!=null)
        {
            Intent i = new Intent(this, ChatScreen.class);
            startActivity(i);
        }
    }


@Override
public void onClick(View v) {
        switch (v.getId())
        {
        case R.id.signup:
        startActivity(new Intent(this, SignUpActivity.class));
        break;
        case R.id.signin:
        //Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();
        login();
        break;
        }

        }

private void login() {
        String mEmail= email.getText().toString().trim();
        String mPass= pass.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful())
        {
        Toast.makeText(getApplicationContext(),"logged in", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, ChatScreen.class);
        startActivity(i);
        }
        else {
        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_LONG).show();
        }
        }
        });
        }
}
