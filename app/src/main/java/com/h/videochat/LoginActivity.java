package com.h.videochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout email,password;
    Button submit;
    TextView signup;

    FirebaseAuth auth;
    FirebaseUser user;


    SweetAlertDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        submit=findViewById(R.id.submit);
        signup=findViewById(R.id.signup);

        submit.bringToFront();
        loading=new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Please wait....");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validatedata()){
                    login();
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void login() {

        loading.show();
        final String emails;
        final String passwords;
        emails=email.getEditText().getText().toString();
        passwords=password.getEditText().getText().toString();


        auth=FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                loading.dismiss();
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean validatedata(){

        if(!Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText().toString()).matches()){
            email.setError("InvalidEmail");
            return false;
        }
        if(password.getEditText().getText().toString().length()<6){
            password.setError("Password too short");
            return false;
        }else if(password.getEditText().getText().toString().length()>14){
            password.setError("Password too long");
            return false;
        }




        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finishAffinity();
        }

    }
}