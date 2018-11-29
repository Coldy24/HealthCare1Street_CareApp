package com.jaeseon.careappv4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by USER on 2017-11-23.
 */

public class AlarmControlGPS extends AsyncTask<String, String, String> {

    private Context context;
    String Lat, Long;

    private AlarmControlGPS(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        Lat = null;
        Long = null;
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        Lat = params[0];
        Long = params[1];
        Intent intent;

       /* if (Integer.parseInt(spo2) < 95 && Integer.parseInt(spo2) > 1) {
            intent = new Intent(context, AlarmActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        if (Integer.parseInt(hrm) < 120 && Integer.parseInt(hrm) > 50) {
            intent = new Intent(context, AlarmActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }*/
        return null;
    }




}
