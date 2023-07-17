package com.example.noteapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progess_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        createAccountBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

        String s="Srv";
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    signin(email,password);
                    changeInProgressOn(true);
                }
            });
        }
    private void signin(String email,String password) {
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Email is invalid");
            emailEditText.requestFocus();
            changeInProgressOn(false);
        }
        else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Password cannot be Empty");
            passwordEditText.requestFocus();
            changeInProgressOn(false);
        }
        else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String e=email+" login Sucessfull";
                        changeInProgressOn(false);
                        Toast.makeText(LoginActivity.this,e,Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity2.this,MainActivity3.class));
                        Intent inte  = new Intent(LoginActivity.this,MainActivity.class);
                        inte.putExtra("namehe",email);
                        startActivity(inte);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"login failed",Toast.LENGTH_SHORT).show();
                        changeInProgressOn(false);
                    }
                }
            });
        }
    }

    void changeInProgressOn(boolean inProgress)
    {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
    }
    }


