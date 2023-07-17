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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.ktx.Firebase;

//private lateinit var analytics: FirebaseAnalytics
public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    private FirebaseAuth mAuth;
    private FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progess_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());
        mAuth = FirebaseAuth.getInstance();
       // createAccountBtn.setOnClickListener((v) -> startActivity(new Intent(CreateAccountActivity.this, MainActivity.class)));

    }

    void createAccount(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmpassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email,password,confirmpassword);

        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);

    }

    void createAccountInFirebase(String email,String password)
    {
        changeInProgressOn(true);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this,"email and password Registered",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    createAccountBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(CreateAccountActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    createAccountBtn.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    void changeInProgressOn(boolean inProgress)
    {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email , String password , String confirmpassword){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }

        if(password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }

        if(!password.equals(confirmpassword)){
            confirmPasswordEditText.setError("Password not matched");
            return false;
        }

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Email cannot be empty");
            emailEditText.requestFocus();
        }
        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Password cannot be Empty");
            passwordEditText.requestFocus();
        }

        return true;
    }
}