package com.h.videochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout email,password,name;
    Button submit;
    CircleImageView male,female;
    String gender="",initialcoins="100";


    FirebaseAuth auth;
    DatabaseReference reference;

    SweetAlertDialog loading;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        male=findViewById(R.id.male);
        female=findViewById(R.id.female);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);
        submit=findViewById(R.id.submit);


        loading=new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        loading.setCancelable(false);
        loading.setTitleText("Registering");

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setImageResource(R.drawable.male_select);
                female.setImageResource(R.drawable.female_unselect);
                gender="male";
            }
        });


        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setImageResource(R.drawable.male_unselect);
                female.setImageResource(R.drawable.female_select);
                gender="female";
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validatedata()){
                    register();
                }
            }
        });


    }

    private void register() {

        loading.show();
        final String emails;
        final String passwords;
        final String names;
        final String[] currentuserid = new String[1];

        emails=email.getEditText().getText().toString();
        passwords=password.getEditText().getText().toString();
        names=name.getEditText().getText().toString();

        auth.createUserWithEmailAndPassword(emails,passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                loading.dismiss();
                if(task.isSuccessful()){

                    currentuserid[0] =auth.getCurrentUser().getUid();
                    Map<String , String> params= new HashMap<String, String>();
                    params.put("email",emails);
                    params.put("password",passwords);
                    params.put("name",names);
                    params.put("gender",gender);
                    params.put("id",currentuserid[0]);
                    params.put("coins",initialcoins);


                    reference.child("User").setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public boolean validatedata(){

        if(!Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText().toString()).matches()){
            email.setError("InvalidEmail");
            return false;
        }

        if(name.getEditText().getText().toString().isEmpty()){
            name.setError("Invalid Name");
            return false;
        }

        if(password.getEditText().getText().toString().length()<6){
            password.setError("Password too short");
            return false;
        }else if(password.getEditText().getText().toString().length()>14){
            password.setError("Password too long");
            return false;
        }




        if(gender.equals("")){
            Toast.makeText(this, "Please Select your Gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}