package com.h.videochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ImageView logout;

    String gender="",name="";

    TextView usercoins;
    FirebaseAuth auth;
    String currentuserid;
    DatabaseReference reference;
    CircleImageView male,female,both;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    auth=FirebaseAuth.getInstance();
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        both=findViewById(R.id.both);
    currentuserid=auth.getCurrentUser().getUid();
    logout=findViewById(R.id.logout);
    usercoins=findViewById(R.id.coins);
    reference= FirebaseDatabase.getInstance().getReference();

    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        }
    });
    male.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            createcalltomale();
            searchmale();
        }
    });
    female.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            createcalltofemale();
        }
    });

    both.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            createcalltoboth();
        }
    });

    reference.child("User").child(currentuserid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String coins=snapshot.child("coins").getValue().toString();
            gender=snapshot.child("gender").getValue().toString();
            name=snapshot.child("name").getValue().toString();

            usercoins.setText(coins);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    private void createcalltomale() {
        Map<String , String> params=new HashMap<String, String>();
        params.put("name",name);
        params.put("gender",gender);
        params.put("id",currentuserid);

        if(gender.equals("")){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else if(gender.equals("male")){
            reference.child("Calls").child("male_to_male").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }else{
                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(gender.equals(female)){
            reference.child("Calls").child("female_to_male").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }else{
                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
        }
    }



    private void createcalltofemale() {
        Map<String , String> params=new HashMap<String, String>();
        params.put("name",name);
        params.put("gender",gender);
        params.put("id",currentuserid);

        if(gender.equals("")){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else if(gender.equals("male")){
            reference.child("Calls").child("male_to_female").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }else{
                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(gender.equals(female)){
            reference.child("Calls").child("female_to_female").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }else{
                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
        }
    }



    private void createcalltoboth() {
        Map<String , String> params=new HashMap<String, String>();
        params.put("name",name);
        params.put("gender",gender);
        params.put("id",currentuserid);


            reference.child("Calls").child("both").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }else{
                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


    public  void searchmale(){
        if(gender.equals("")){

        }else if(gender.equals("male")){

            reference.child("Calls").child("male_to_male").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

//                    Toast.makeText(MainActivity.this, snapshot+"", Toast.LENGTH_SHORT).show();

                    if(snapshot.getValue() == null){
                        createcalltomale();
                        return;
                    }
                    for (DataSnapshot dataSnapshot :snapshot.getChildren() ){


                        String id=dataSnapshot.child("id").getValue().toString();

                        Toast.makeText(MainActivity.this, id+"\n"+currentuserid, Toast.LENGTH_SHORT).show();
                        if(!id.equals(currentuserid)){
                            reference.child("Calls").child("male_to_male").child(id).removeValue();
                            return;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}