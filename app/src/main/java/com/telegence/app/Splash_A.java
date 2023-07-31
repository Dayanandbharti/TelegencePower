package com.telegence.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.telegence.app.Authentication.Login_A;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONObject;

public class Splash_A extends AppCompatActivity {

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
        TextView txtname = findViewById(R.id.txtname);
        String Message = "Telegence";

        txtname.setText(Html.fromHtml(Message));


        countDownTimer = new CountDownTimer(1500, 300) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                runOnUiThread(() -> {
                    PackageInfo packageInfo = null;
                    Integer appversion=12;
                    try {
                        packageInfo =getPackageManager().getPackageInfo(getPackageName(), 0);
                        appversion=packageInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    broadcast_option(appversion);
                });
            }
        }.start();
        final String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor2 =  Variables.sharedPreferences.edit();
        editor2.putString(Variables.device_id, android_id).commit();
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    private void broadcast_option(Integer currentversion){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(this, Variables.return_version, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    Integer Version=currentversion;
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");
                    if (code.equals("200")) {
                        if (msg!="") Version=Integer.parseInt(msg);
                    }
                    Intent intent;
                    if(currentversion<Version)
                        intent = new Intent(Splash_A.this, Update_A.class);
                    else
                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        intent = new Intent(Splash_A.this, MainMenuActivity.class);
                    } else{
                        intent = new Intent(Splash_A.this, Login_A.class);
                    }

                    if(getIntent().getExtras()!=null) {
                        intent.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_bottom, R.anim.fade_out);
                    finish();
                } catch (Exception e) {
                    Intent intent;
                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        intent = new Intent(Splash_A.this, MainMenuActivity.class);
                    } else{
                        intent = new Intent(Splash_A.this, Login_A.class);
                    }
                    if(getIntent().getExtras()!=null) {
                        intent.putExtras(getIntent().getExtras());
                        setIntent(null);
                    }
                    startActivity(intent);

                    overridePendingTransition(R.anim.in_from_bottom, R.anim.fade_out);
                    finish();
                    e.printStackTrace();
                }
            }
        });
    }


}
