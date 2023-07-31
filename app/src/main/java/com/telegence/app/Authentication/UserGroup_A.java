package com.telegence.app.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.Variables;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import xdroid.toaster.Toaster;

public class UserGroup_A extends Activity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    SharedPreferences sharedPreferences;
    EditText txtmobileno;
    EditText txtpassword;
    View login_main;
    Button btn_student, btn_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(0x0E3F21));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        try {
            setContentView(R.layout.login_usergroup);

            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();

            // if the user is already login through facebook then we will logout the user automatically
            //LoginManager.getInstance().logOut();

            sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

            login_main=findViewById(R.id.login_main);
            //login_pass=findViewById(R.id.login_pass);

            txtmobileno = findViewById(R.id.txtMobileNo);
            txtpassword = findViewById(R.id.txtloginpass);

            btn_student = findViewById(R.id.btn_student);
            btn_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserGroup_A.this,Login_A.class);
                    intent.putExtra("group","student");
                    startActivity(intent);
                }
            });

            btn_teacher = findViewById(R.id.btn_teacher);
            btn_teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserGroup_A.this,Login_A.class);
                    intent.putExtra("group","teacher");
                    startActivity(intent);
                }
            });

            findViewById(R.id.txtsignup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserGroup_A.this, Sign_up.class));
                }
            });


            findViewById(R.id.txt__frgtpass).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserGroup_A.this, Forgot_Password.class));
                }
            });

            findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            printKeyHash();
        } catch (Exception e){
            Toaster.toast(e.getMessage());
        }
    }


    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
    }

    // Bottom two function are related to Fb implimentation
    private CallbackManager mCallbackManager;

    public void printKeyHash()  {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName() , PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("keyhash" , Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}


