package com.jaeseon.careappv4;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InfoChangePopup extends AppCompatActivity {
    EditText EdName;
    EditText EdPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_change_popup);
        EdName = (EditText)findViewById(R.id.changeName);
        EdPhone = (EditText)findViewById(R.id.changePhone);
    }

    public void onClickPopup(View v){
        switch (v.getId()){
            case R.id.changeOK:
                Intent intent = new Intent();
                intent.putExtra("name",EdName.getText().toString());
                intent.putExtra("phone",EdPhone.getText().toString());
                SharedPreferences pref = getSharedPreferences( "USER_INFO", MODE_PRIVATE );
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("first" , EdName.getText().toString());
                editor.putString("second" , EdPhone.getText().toString());
                editor.commit();
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.returnHome:
                finish();
                break;
        }
    }
}

