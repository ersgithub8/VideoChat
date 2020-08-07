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
import com.skydoves.elasticviews.ElasticImageView;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ImageView logout;

    String gender="",name="";

    TextView usercoins,nametv;
    FirebaseAuth auth;
    String currentuserid;
    String coins;
    int coinss;
    DatabaseReference reference;
    ElasticImageView male,female,both;

    SweetAlertDialog alertDialog;
    boolean callcheck=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    auth=FirebaseAuth.getInstance();
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        nametv=findViewById(R.id.name);
        both=findViewById(R.id.both);
    currentuserid=auth.getCurrentUser().getUid();
    logout=findViewById(R.id.logout);
    usercoins=findViewById(R.id.coins);
    reference= FirebaseDatabase.getInstance().getReference();

    alertDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
    alertDialog.setTitleText("Loading");
    alertDialog.setCancelable(false);
    alertDialog.show();

        reference.child("User").child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coins=snapshot.child("coins").getValue().toString();
                coinss=Integer.parseInt(coins);
                gender=snapshot.child("gender").getValue().toString();
                name=snapshot.child("name").getValue().toString();

                usercoins.setText(coins);

                if(gender.equals("male")){
                    nametv.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.malesign),null,null,null);
                }else{
                    nametv.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.femalesign),null,null,null);

                }
                nametv.setText(name);
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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


            if(coinss > 5){
            callcheck=true;
            if(gender.equals("male")){
                searchmale();
            }else{
                searchmalefemale();
            }
            }else{
                Toast.makeText(MainActivity.this, "You don't have sufficent coins to make this call", Toast.LENGTH_SHORT).show();
            }
//            startActivity(new Intent(MainActivity.this,VideoActivity.class).putExtra("callid",currentuserid));
        }
    });
    female.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(coinss > 5){


            callcheck=true;
            if(gender.equals("male")){
                searchmalefemale();
            }else {
                searchfemale();
            }
            }else{
                Toast.makeText(MainActivity.this, "You don't have sufficent coins to make this call", Toast.LENGTH_SHORT).show();
            }
        }
    });

    both.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            callcheck=true;
            searchboth();
        }
    });


    }

    private void createcalltomale() {
        Map<String , String> params=new HashMap<String, String>();
        params.put("name",name);
        params.put("gender",gender);
        params.put("id",currentuserid);
        params.put("state","wait");
        params.put("coins",coins);

        if(gender.equals("")){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else if(gender.equals("male")){
            reference.child("Calls").child("male_to_male").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                        intent.putExtra("callid",currentuserid);
                        intent.putExtra("calltype","male_to_male");
                        startActivity(intent);
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

                        Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                        intent.putExtra("callid",currentuserid);
                        intent.putExtra("calltype","female_to_male");
                        startActivity(intent);

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
        params.put("state","wait");
        params.put("coins",coins);

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
        }else if(gender.equals("female")){
            reference.child("Calls").child("female_to_female").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                        intent.putExtra("callid",currentuserid);
                        intent.putExtra("calltype","female_to_female");
                        startActivity(intent);

                    }else{
                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
        }
    }


    public void createcallmalefemale(){
        Map<String , String> params=new HashMap<String, String>();
        params.put("name",name);
        params.put("gender",gender);
        params.put("id",currentuserid);
        params.put("state","wait");
        params.put("coins",coins);

        reference.child("Calls").child("malefemale").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                    intent.putExtra("callid",currentuserid);
                    intent.putExtra("calltype","malefemale");
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createcalltoboth() {
        Map<String , String> params=new HashMap<String, String>();
        params.put("name",name);
        params.put("gender",gender);
        params.put("id",currentuserid);
        params.put("state","wait");
        params.put("coins",coins);


        reference.child("Calls").child("both").child(currentuserid).setValue(params).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                    intent.putExtra("callid",currentuserid);
                    intent.putExtra("calltype","both");
                    startActivity(intent);

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

                    if (callcheck) {

                        callcheck=false;
                        int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                        if (snapshot.getValue() == null) {
                            createcalltomale();
                            return;
                        }
                        int i = 1;
                        boolean check = false;
                        for (final DataSnapshot dataSnapshot : snapshot.getChildren()) {


                            final String id = dataSnapshot.child("id").getValue().toString();
                            final String othercoins=dataSnapshot.child("coins").getValue().toString();
                            final int othercoinss=Integer.parseInt(othercoins);
                            if (!id.equals(currentuserid)) {
                                if (dataSnapshot.child("state").getValue().toString().equals("wait")) {
                                    reference.child("Calls").child("male_to_male").child(id).child("state").setValue("calling")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){


                                                        reference.child("User").child(currentuserid)
                                                                .child("coins").setValue((coinss-5)+"")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            reference.child("User").child(id)
                                                                                    .child("coins").setValue((othercoinss-5)+"")
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                                                                                                intent.putExtra("callid",id);
                                                                                                intent.putExtra("calltype","male_to_male");
                                                                                                startActivity(intent);
                                                                                            }else{

                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }else{

                                                                        }
                                                                    }
                                                                });



                                                    }else{
                                                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    return;
                                }else{
                                    if(i==count){
                                        createcalltomale();
                                    }
                                }
                            } else {
                                check = true;
                            }

                            if (i == count) {
                                if (check) {
                                    Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
                                    createcalltomale();
                                }
                            }

                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(gender.equals("female")){



        }

    }

    public void searchfemale(){
        if(gender.equals("female")){
            reference.child("Calls").child("female_to_female").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (callcheck) {

                        callcheck=false;
                        int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                        if (snapshot.getValue() == null) {
                            createcalltofemale();
                            return;
                        }
                        int i = 1;
                        boolean check = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                            final String id = dataSnapshot.child("id").getValue().toString();
                            final String  othercoins=dataSnapshot.child("coins").getValue().toString();
                            final  int othercoinss=Integer.parseInt(othercoins);
                            if (!id.equals(currentuserid)) {
                                if (dataSnapshot.child("state").getValue().toString().equals("wait")) {
                                    reference.child("Calls").child("female_to_female").child(id).child("state").setValue("calling")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){


                                                        reference.child("User").child(currentuserid)
                                                                .child("coins").setValue((coinss-5)+"")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                   if(task.isSuccessful()){
                                                                       reference.child("User").child(id)
                                                                               .child("coins").setValue((othercoinss-5)+"")
                                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                                  if(task.isSuccessful()){
                                                                                      Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                                                                                      intent.putExtra("callid",id);
                                                                                      intent.putExtra("calltype","female_to_female");
                                                                                      startActivity(intent);
                                                                                  }else{

                                                                                  }
                                                                                   }
                                                                               });
                                                                   }else{

                                                                   }
                                                                    }
                                                                });



                                                    }else{
                                                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    return;
                                }else{
                                    if(i==count){
                                        createcalltofemale();
                                    }
                                }
                            } else {
                                check = true;
                            }

                            if (i == count) {
                                if (check) {
                                    Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
                                    createcalltofemale();
                                }
                            }

                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void searchboth(){


        reference.child("Calls").child("both").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (callcheck) {

                    callcheck=false;
                    int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                    if (snapshot.getValue() == null) {
                        createcalltoboth();
                        return;
                    }
                    int i = 1;
                    boolean check = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                        final String id = dataSnapshot.child("id").getValue().toString();

                        if (!id.equals(currentuserid)) {
                            if (dataSnapshot.child("state").getValue().toString().equals("wait")) {
                                reference.child("Calls").child("both").child(id).child("state").setValue("calling")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){
                                                    Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                                                    intent.putExtra("callid",id);
                                                    intent.putExtra("calltype","both");
                                                    startActivity(intent);
                                                }else{
                                                    Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                return;
                            }else{
                                if(i==count){
                                    createcalltoboth();
                                }
                            }
                        } else {
                            check = true;
                        }

                        if (i == count) {
                            if (check) {
                                Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
                                createcalltoboth();
                            }
                        }

                        i++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void  searchmalefemale(){
        if(gender.equals("male")){


            reference.child("Calls").child("malefemale").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (callcheck) {

                        callcheck=false;
                        int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                        if (snapshot.getValue() == null) {
                            createcallmalefemale();
                            return;
                        }
                        int i = 1;
                        boolean check = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                            final String id = dataSnapshot.child("id").getValue().toString();
                            final String  othercoins=dataSnapshot.child("coins").getValue().toString();
                            final  int othercoinss=Integer.parseInt(othercoins);
                            if (!id.equals(currentuserid)) {
                                if (dataSnapshot.child("state").getValue().toString().equals("wait")
                                        &&
                                        dataSnapshot.child("gender").getValue().toString().equals("female")) {
                                    reference.child("Calls").child("malefemale").child(id).child("state").setValue("calling")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){



                                                        reference.child("User").child(currentuserid)
                                                                .child("coins").setValue((coinss-5)+"")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            reference.child("User").child(id)
                                                                                    .child("coins").setValue((othercoinss-5)+"")
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                                                                                                intent.putExtra("callid",id);
                                                                                                intent.putExtra("calltype","malefemale");
                                                                                                startActivity(intent);
                                                                                            }else{

                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }else{

                                                                        }
                                                                    }
                                                                });


                                                    }else{
                                                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    return;
                                }else{
                                    if(i==count){
                                        createcallmalefemale();
                                    }
                                }
                            } else {
                                check = true;
                            }

                            if (i == count) {
                                if (check) {
                                    Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
                                    createcallmalefemale();
                                }
                            }

                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }else if(gender.equals("female")){

            reference.child("Calls").child("malefemale").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (callcheck) {

                        callcheck=false;
                        int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                        if (snapshot.getValue() == null) {
                            createcallmalefemale();
                            return;
                        }
                        int i = 1;
                        boolean check = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                            final String id = dataSnapshot.child("id").getValue().toString();
                            final String  othercoins=dataSnapshot.child("coins").getValue().toString();
                            final  int othercoinss=Integer.parseInt(othercoins);
                            if (!id.equals(currentuserid)) {
                                if (dataSnapshot.child("state").getValue().toString().equals("wait")
                                        &&
                                        dataSnapshot.child("gender").getValue().toString().equals("male")) {
                                    reference.child("Calls").child("malefemale").child(id).child("state").setValue("calling")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){
                                                        reference.child("User").child(currentuserid)
                                                                .child("coins").setValue((coinss-5)+"")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            reference.child("User").child(id)
                                                                                    .child("coins").setValue((othercoinss-5)+"")
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                Intent intent=new Intent(MainActivity.this,VideoActivity.class);
                                                                                                intent.putExtra("callid",id);
                                                                                                intent.putExtra("calltype","malefemale");
                                                                                                startActivity(intent);
                                                                                            }else{

                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }else{

                                                                        }
                                                                    }
                                                                });
                                                    }else{
                                                        Toast.makeText(MainActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    return;
                                }else{
                                    if(i==count){
                                        createcallmalefemale();
                                    }
                                }
                            } else {
                                check = true;
                            }

                            if (i == count) {
                                if (check) {
                                    Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
                                    createcallmalefemale();
                                }
                            }

                            i++;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{

        }
    }

}