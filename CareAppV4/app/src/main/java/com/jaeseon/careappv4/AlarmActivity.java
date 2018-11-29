package com.jaeseon.careappv4;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Vibrator vibrate = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100,300,100,400,300,2000};
        vibrate.vibrate(pattern,0);


        // notify.sound = Uri.parse("file:/system/media/audio/ringtones/test.mp3"); // test.mp3재생
        //notify.flags |= Notification.FLAG_INSISTENT; // 반복


    }

    public void onClickOK(View v){
        finish();
    }
}
