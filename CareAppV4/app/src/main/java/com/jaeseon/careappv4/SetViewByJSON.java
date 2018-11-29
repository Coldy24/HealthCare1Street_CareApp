package com.jaeseon.careappv4;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jaeseon Lee on 2017-11-19.
 */

public class SetViewByJSON extends Activity{

    public void SetViewByJSON(Context context){
        //context.getResources(R.layout.activity_main);
    }


    String age,smoke,disease,medicine,ect,weight,heigh,blood,hrm,spo2;
    //TextView ageTextView, smokingTextView, bloodTextView, heighTextView, weighTextView, diseaseTextView, medicineTextView, ectTextView, spo2TextView, hrmTextView;
    private View changeView;
    public void setData(String age,String smoke,String disease,String medicine,String ect,String weight,String heigh,String blood,String hrm,String spo2){
        this.age = age;
        this.smoke = smoke;
        this.disease = disease;
        this.medicine = medicine;
        this.ect = ect;
        this.weight = weight;
        this.heigh= heigh;
        this.blood = blood;
        this.hrm = hrm;
        this.spo2 = spo2;
    }

    public void setView(TextView ageTextView,TextView smokingTextView,TextView diseaseTextView,TextView medicineTextView,TextView ectTextView,
                        TextView weighTextView,TextView heighTextView,TextView bloodTextView,TextView hrmTextView, TextView spo2TextView){

        ageTextView.setText(age);
        smokingTextView.setText(smoke);
        diseaseTextView.setText(disease);
        medicineTextView.setText(medicine);
        ectTextView.setText(ect);
        weighTextView.setText(weight);
        heighTextView.setText(heigh);
        bloodTextView.setText(blood);
        hrmTextView.setText(hrm);
        if(Integer.parseInt(spo2) != 0)
            spo2TextView.setText(spo2);
        else
            spo2TextView.setText("정보없음");






    }
}
