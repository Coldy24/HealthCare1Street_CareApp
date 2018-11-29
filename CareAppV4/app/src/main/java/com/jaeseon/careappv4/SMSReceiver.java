package com.jaeseon.careappv4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.StringTokenizer;
import javax.crypto.NullCipher;

/**
 * Created by Jaeseon Lee on 2017-11-16.
 */


public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";
    String Lat= null;   String Long = null;
    ImageView map;
    // String phoneNum =  new MainActivity().getPhone();
    String InMessageAdd = null;
    String findText = "[PATIENT]";
    String[] inputText = {"\0","\0","\0","\0","\0","\0","\0","\0"};
    private Context context;

    // public void getNumber(){
    //    this.phoneNum = phoneNum;
    // }


    public int locationLat(){
        return Integer.getInteger(Lat);
    }

    public int locationLong(){
        return Integer.getInteger(Long);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BroadcastReceiver Received");


        if ("android.provider.Telephony.SMS_RECEIVED".equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {

                return;

            }
            Object[] messages = (Object[]) bundle.get("pdus");
            if (messages == null) {
                return;
            }
            SmsMessage[] smsMessage = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
                InMessageAdd = smsMessage[i].getOriginatingAddress();
            }


            String message = smsMessage[0].getMessageBody().toString();
            Log.d(TAG, "SMS Message: " + message);

            StringTokenizer st = new StringTokenizer(message," ");
            Lat = null;     Long = null;

            for(int i = 0;  st.hasMoreTokens(); i++) {
                inputText[i] = st.nextToken();
                Log.d(TAG, "INPUTTEXT :" + inputText[i]);
            }
            if(inputText[0].equals(findText)){

                if(inputText[1].equals("위치") &&inputText[2].equals("이탈")){ Log.d(TAG, "11111111111: " );
                    Lat = inputText[4].substring(0,inputText[4].length()-1);
                    Long = inputText[6].substring(0,inputText[6].length()-1);

                    intent.putExtra("Latitude", Lat);
                    intent.putExtra("Longitude",Long);
                    intent.setClass(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);

                }
            }
            else return;

        }

    }

}