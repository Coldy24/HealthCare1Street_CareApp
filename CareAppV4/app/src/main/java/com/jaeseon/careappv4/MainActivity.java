package com.jaeseon.careappv4;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.os.SystemClock.sleep;


public class MainActivity extends AppCompatActivity {

    String age,smoke,disease,medicine,ect,weight,heigh,blood,hrm,spo2,Lat,Long;
    private static final String TAG = "MainAct";
    private Uri imageURI;
    TextView nameTextView, phoneTextView;
    ImageView mapView;
    ImageView profilePhoto;
    boolean flag = false;
    final int GET_INFO = 1, GET_PHOTO = 2, IMAGE_CROP =3;
    Drawable drawable;
    private SharedPreferences savedData;
    private ProgressDialog pDialog;
    // URL to get contacts JSON
    String phone = ""; String name = "";
    String urlForm = "http://fhirtest.uhn.ca/baseDstu2/Patient/_search?telecom=";
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTextView = findViewById(R.id.homeName);
        phoneTextView= findViewById(R.id.homePhone);
        profilePhoto = findViewById(R.id.profilePhoto);
        mapView = findViewById(R.id.mapID);

        savedData = getSharedPreferences("USER_INFO",MODE_PRIVATE);

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,Manifest.permission.VIBRATE,Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                Background.class); // 이동할 컴포넌트
        startService(intent);


        nameTextView.setText(savedData.getString("first",""));     phoneTextView.setText(savedData.getString("second",""));
        //  new SMSReceiver(MainActivity.this,mapView);



        try{
            String imgpath = "data/data/com.test.SDCard_Ani/files/profilePic.png";
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            Drawable drawable = new BitmapDrawable(bm);
            profilePhoto.setBackground(drawable);
            Toast.makeText(getApplicationContext(), "load ok", Toast.LENGTH_SHORT).show();
        }catch(Exception e){Toast.makeText(getApplicationContext(), "load error", Toast.LENGTH_SHORT).show();}



        ///////////////////////////////////////////////////////
        /// Glide.with(this).load("URL").into();
        ///////////////////////////////////////////////////////////


        phone = phoneTextView.getText().toString();
        url = urlForm+phone;
        Log.d(TAG,"url = " + url);
        new GetContacts().execute();

    }

    public String getPhone(){
        return phone;
    }

    public void onClickMenu(View v){
        switch (v.getId()){
            case R.id.profilePhoto:
                Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,GET_PHOTO);
                break;
            case R.id.infoEdit:
                Intent in = new Intent(MainActivity.this, InfoChangePopup.class);
                startActivityForResult(in,GET_INFO);
                break;
            case R.id.callTo:
                String callForm = "tel:"+phone;
                Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse(callForm));
                startActivity(callIntent);
                break;
            case R.id.messageTo:
                String smsForm = "tel:"+phone;
                Intent messageIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(smsForm));
                messageIntent.putExtra("address",phone);
                messageIntent.setType("vnd.android-dir/mms-sms");
                startActivity(messageIntent);

                break;
            case R.id.getJSON:
                new SendMessage(phone,"[HELPER] 동기화를 부탁합니다.");
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("정보 받아오는 중...");
                pDialog.setCancelable(false);
                pDialog.show();
                sleep(30000);
                pDialog.dismiss();
                new GetContacts().execute();
                break;

            case R.id.getLocation:
                new SendMessage(phone,"[HELPER] 위치정보 부탁합니다.");
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("정보 받아오는 중...");
                pDialog.setCancelable(false);
                pDialog.show();
                sleep(5000);
                pDialog.dismiss();

                //Glide.with(this).load("https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=14&size=400x400&key=AIzaSyA9NX2lWVQgUuRHRaesFTRM7vE5idcZcqg").into(mapView);
                /*Intent getGPS = getIntent();
                String Lat = getGPS.getStringExtra("Lat");
                String Long = getGPS.getStringExtra("Long");
                String mapUriBasicForm = "https://maps.googleapis.com/maps/api/staticmap?center=";
                String mapUriBasicForm2 = "&zoom=15&size=400x400&key=AIzaSyA9NX2lWVQgUuRHRaesFTRM7vE5idcZcqg";
                Glide.with(this).load(mapUriBasicForm+Lat+","+Long+mapUriBasicForm2).into((ImageView)findViewById(R.id.mapID));*/

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_INFO){
            if(resultCode == RESULT_OK) {
                nameTextView.setText(data.getStringExtra("name"));
                phoneTextView.setText(data.getStringExtra("phone"));
                phone = phoneTextView.getText().toString();
                url = urlForm + phone;
                Log.d(TAG, "first = " +nameTextView.getText().toString());
                Log.d(TAG, "second = " +phoneTextView.getText().toString());
                new GetContacts().execute();
            }
        }
        else if(requestCode == GET_PHOTO) {
            if (resultCode == RESULT_OK) {
                dispatchCropPictureIntent(data.getData());
            }
        }
        else if(requestCode ==  IMAGE_CROP) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String fileName = "profilePhoto.png";
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/careApp", fileName);
            OutputStream out = null;
            try {
                f.createNewFile();//파일생성
                out = new FileOutputStream(f);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/careApp" + "/profilePhoto.png";
                Bitmap bm = BitmapFactory.decodeFile(path);
                drawable = new BitmapDrawable(bm);
                profilePhoto.setBackground(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            drawable = new BitmapDrawable(imageBitmap);
            profilePhoto.setBackground(drawable);



        }

    }


    private void dispatchCropPictureIntent(Uri uri) {
        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
        cropPictureIntent.setDataAndType(uri, "image/*");
        cropPictureIntent.putExtra("aspectX", 3); // crop 박스의 x축 비율 (integer)
        cropPictureIntent.putExtra("aspectY", 4); // crop 박스의 y축 비율 (integer)
        cropPictureIntent.putExtra("scale", true);
        cropPictureIntent.putExtra("return-data", true);
        if (cropPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cropPictureIntent, IMAGE_CROP);
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this,"권한 얻음",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(MainActivity.this,"권한 거부됨\n" + deniedPermissions.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Lat = intent.getStringExtra("Latitude");
        Long = intent.getStringExtra("Longitude");
        Log.d ("hihihihi","Lat:" + Lat);
        Log.d ("hihihihi","Long:" + Long);
        pDialog.dismiss();
        String UriBasicForm = "https://maps.googleapis.com/maps/api/staticmap?center=";
        String UrlBasicForm2= "&zoom=16&size=800x800&markers=color:blue%7Clabel:위치%7C";
        String UrlBasicForm3 = "&key=AIzaSyA9NX2lWVQgUuRHRaesFTRM7vE5idcZcqg";
        Glide.with(this).load(UriBasicForm+Lat+","+Long+UrlBasicForm2 + Lat + ","+Long + UrlBasicForm3).into(mapView);

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("phone_", phone);
        savedInstanceState.putString("name_", name);
    }



    public class GetContacts extends AsyncTask<Void, Void, Void> {
        String TAG = MainActivity.class.getSimpleName();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("정보 받아오는 중...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();

            String jsonStr = httpHandler.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray entry = jsonObj.getJSONArray("entry");
                    JSONObject entryObj = entry.getJSONObject(0);
                    JSONObject resource = entryObj.getJSONObject("resource");
                    JSONArray contained = resource.getJSONArray("contained");

                    for(int i = 0; i<contained.length(); i++) {
                        JSONObject containedObj = contained.getJSONObject(0);
                        JSONObject category = containedObj.getJSONObject("category");
                        JSONArray coding = category.getJSONArray("coding");
                        ///////////////////////////////////////////////////////////
                        JSONObject ageData = coding.getJSONObject(0);
                        age = ageData.getString("display");

                        JSONObject smokeData = coding.getJSONObject(1);
                        smoke = smokeData.getString("display");

                        JSONObject diseaseData = coding.getJSONObject(2);
                        disease = diseaseData.getString("display");

                        JSONObject medicineData = coding.getJSONObject(3);
                        medicine = medicineData.getString("display");

                        JSONObject ectData = coding.getJSONObject(4);
                        ect = ectData.getString("display");
                        ////////////////////////////////////////////////////////////

                        JSONObject bodySite = containedObj.getJSONObject("bodySite");
                        JSONArray coding2 = bodySite.getJSONArray("coding");
                        //////////////////////////////////////////////////////////
                        JSONObject weightData = coding2.getJSONObject(0);
                        weight = weightData.getString("display");

                        JSONObject heighData = coding2.getJSONObject(1);
                        heigh = heighData.getString("display");

                        JSONObject bloodData = coding2.getJSONObject(2);
                        blood = bloodData.getString("display");

                        JSONObject hrmData = coding2.getJSONObject(3);
                        hrm = hrmData.getString("display");

                        JSONObject spo2Data = coding2.getJSONObject(4);
                        spo2 = spo2Data.getString("display");

                    }
                    flag = true;

                } catch (final JSONException e) {
                    Log.e(TAG, "parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "환자의 정보가 없습니다! 전화번호를 확인하세요! " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Cannot bring JSON from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "환자의 정보가 없습니다! 전화번호를 확인하세요!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (flag == true) {
                SetViewByJSON setView = new SetViewByJSON();

                TextView ageTextView = findViewById(R.id.homeAge);
                TextView smokingTextView = findViewById(R.id.homeSmoking);
                TextView diseaseTextView = findViewById(R.id.homeDisease);
                TextView medicineTextView = findViewById(R.id.homeMedicine);
                TextView ectTextView = findViewById(R.id.homeEct);
                TextView weighTextView = findViewById(R.id.homeWeight);
                TextView heighTextView = findViewById(R.id.homeHeigh);
                TextView bloodTextView = findViewById(R.id.homeBloodType);
                TextView hrmTextView = findViewById(R.id.hrmValue);
                TextView spo2TextView = findViewById(R.id.spo2Value);
                //ImageView stateImage=findViewById(R.id.stateColor);

                setView.setData(age, smoke, disease, medicine, ect, weight, heigh, blood, hrm, spo2);
                setView.setView(ageTextView, smokingTextView, diseaseTextView, medicineTextView, ectTextView, weighTextView,
                        heighTextView, bloodTextView, hrmTextView, spo2TextView);


                if (Integer.parseInt(spo2) < 95 && Integer.parseInt(spo2) > 1) {
                    ImageView spo2Image = findViewById(R.id.stateColor);
                    Drawable dangerImage = getResources().getDrawable(R.drawable.danger_big);
                    spo2Image.setBackground(dangerImage);
                } else {
                    ImageView spo2Image = findViewById(R.id.stateColor);
                    Drawable normalImage = getResources().getDrawable(R.drawable.normal_big);
                    spo2Image.setBackground(normalImage);
                }

                if (Integer.parseInt(hrm) < 120 && Integer.parseInt(hrm) > 55) {
                    ImageView hrmImage = findViewById(R.id.stateColor);
                    Drawable dangerImage = getResources().getDrawable(R.drawable.normal_big);
                    hrmImage.setBackground(dangerImage);
                } else {
                    ImageView hrmImage = findViewById(R.id.stateColor);
                    Drawable normalImage = getResources().getDrawable(R.drawable.danger_big);
                    hrmImage.setBackground(normalImage);
                }
                new AlarmControl(MainActivity.this).execute(spo2,hrm);
                flag = false;
            }
        }

    }
}




