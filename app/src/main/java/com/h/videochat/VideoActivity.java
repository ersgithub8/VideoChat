package com.h.videochat;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


public class VideoActivity extends AppCompatActivity {

    private RtcEngine mRtcEngine;
    private IRtcEngineEventHandler mRtcEventHandler;

    String callid,calltype;
    DatabaseReference reference;
    FloatingActionButton end;
    ImageView switchcamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        end=findViewById(R.id.floatab);
        switchcamera=findViewById(R.id.switchcamera);
        callid=getIntent().getStringExtra("callid");
        calltype=getIntent().getStringExtra("calltype");


        mRtcEventHandler = new IRtcEngineEventHandler() {



            @Override
            public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
                Log.i("uid video",uid+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRemoteVideo(uid);
                    }
                });
            }


            @Override
            public void onUserOffline(final int uid, int reason) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mLogView.logI("User offline, uid: " + (uid & 0xFFFFFFFFL));
//                        onRemoteUserLeft();
                    endeverything();
                    }
                });
            }

        };
        initializeAgoraEngine();


        reference= FirebaseDatabase.getInstance().getReference().child("Calls").child(calltype).child(callid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String calt=
                snapshot.child("state").getValue().toString();

                if(!(calt.equals("calling") || calt.equals("wait"))){
//                    onBackPressed();
                    leaveChannel();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endeverything();
            }
        });

        switchcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.switchCamera();
            }
        });
    }

    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.appid), mRtcEventHandler);
            joinChannel();
            setupLocalVideo();
            setupVideoProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_480P, false);
    }

    private void setupLocalVideo() {
        FrameLayout container = (FrameLayout) findViewById(R.id.ourvid);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }




    private void joinChannel() {
        mRtcEngine.joinChannel(null, callid, "Extra Optional Data", new Random().nextInt(10000000)+1); // if you do not specify the uid, Agora will assign one.
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote);

        if (container.getChildCount() >= 1) {
            return;
        }

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        surfaceView.setTag(uid);

    }

    private void leaveChannel() {

        mRtcEngine.leaveChannel();
    }



    public void endeverything(){
        reference.child("state").setValue("End").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else{

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        leaveChannel();
//        finish();
    }
}