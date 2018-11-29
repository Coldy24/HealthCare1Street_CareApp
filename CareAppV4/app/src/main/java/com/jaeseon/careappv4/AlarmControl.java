package com.jaeseon.careappv4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by USER on 2017-11-21.
 */

public class AlarmControl extends AsyncTask<String, String, String> {

    private Context context;
    String spo2, hrm;

    public AlarmControl(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        spo2 = null;
        hrm = null;
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        spo2 = params[0];
        hrm = params[1];
        Intent intent;
        {
            if ((Integer.parseInt(spo2) < 95 && Integer.parseInt(spo2) > 1) && Integer.parseInt(spo2) != 0) {
                intent = new Intent(context, AlarmActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }
        {
            if (Integer.parseInt(hrm) < 120 && Integer.parseInt(hrm) > 50) {
                ;
            } else {
                intent = new Intent(context, AlarmActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }
        return null;
    }


}
