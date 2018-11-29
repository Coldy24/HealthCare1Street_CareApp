package com.jaeseon.careappv4;
import android.content.Context;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * Created by USER on 2017-11-22
 */

public class SendMessage {


    public SendMessage(String number, String msg){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number,null,msg,null,null);
    }

}