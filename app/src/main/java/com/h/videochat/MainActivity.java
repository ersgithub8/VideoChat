package com.h.videochat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ImageView logout;

    FirebaseAuth auth;
    String currentuserid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    auth=FirebaseAuth.getInstance();
    currentuserid=auth.getCurrentUser().getUid();
    logout=findViewById(R.id.logout);

    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        }
    });
    }
}