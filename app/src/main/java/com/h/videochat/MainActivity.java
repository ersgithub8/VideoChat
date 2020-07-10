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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ImageView logout;

    TextView usercoins;
    FirebaseAuth auth;
    String currentuserid;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    auth=FirebaseAuth.getInstance();
    currentuserid=auth.getCurrentUser().getUid();
    logout=findViewById(R.id.logout);
    usercoins=findViewById(R.id.coins);
    reference= FirebaseDatabase.getInstance().getReference().child("User");

    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        }
    });


    reference.child(currentuserid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {


//            Toast.makeText(MainActivity.this, snapshot+"", Toast.LENGTH_SHORT).show();

            String coins=snapshot.child("coins").getValue().toString();
            usercoins.setText(coins);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
}