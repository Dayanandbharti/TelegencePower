package com.telegence.app.Authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import xdroid.toaster.Toaster;

public class Login_A extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    SharedPreferences sharedPreferences;
    EditText txtmobileno;
    EditText txtpassword;
    View login_main;
    Button btn_verify_password;
    String group = "student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        /* requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(0x0E3F21));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); */
        try {
            group = getIntent().getStringExtra("group");

            setContentView(R.layout.login_first);

            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();

            // if the user is already login through facebook then we will logout the user automatically
            //LoginManager.getInstance().logOut();

            sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

            login_main= findViewById(R.id.login_main);
            //login_pass=findViewById(R.id.login_pass);

            txtmobileno = findViewById(R.id.txtMobileNo);
            txtpassword = findViewById(R.id.txtloginpass);

            btn_verify_password = findViewById(R.id.btn_verify_password);

            findViewById(R.id.facebook_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Loginwith_FB();
                }
            });

            findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sign_in_with_gmail();
                }
            });

            findViewById(R.id.txtsignup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Login_A.this, Sign_up.class);
                    intent.putExtra("group",group);
                    startActivity(intent);
                }
            });

            findViewById(R.id.btn_verify_password).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(txtmobileno.getText().toString().trim().equalsIgnoreCase("")){
                        Toaster.toast("Please Enter Mobile Number.");
//                    } else if(txtmobileno.getText().toString().length()!=10){
//                        Toaster.toast("Enter Valid Mobile Number.");
                    } else if(txtpassword.getText().toString().equalsIgnoreCase("")) {
                        Toaster.toast("Enter Password.");
                    } else {
                        return_hasPassword(txtmobileno.getText().toString());
                    }
                }
            });
            findViewById(R.id.txt__frgtpass).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Login_A.this, Forgot_Password.class));
                }
            });
        /*findViewById(R.id.btn_verify_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtmobileno.getText().toString().trim().equalsIgnoreCase("")){
                    Toaster.toast("Please Enter Mobile Number.");
                } else if(txtmobileno.getText().toString().length()!=10){
                    Toaster.toast("Enter Valid Mobile Number.");
                } else {
                    return_hasPassword(txtmobileno.getText().toString());
                }
            }
        });*/

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

    String mypassword;
    private void return_hasPassword(String FB_ID){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", FB_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(this, Variables.returnhasPassword, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");

                    if (code.equals("200")) {
                        if (msg!="") {
                            mypassword = msg;
                        }
                    }
                    if(mypassword.equalsIgnoreCase("") || mypassword.equalsIgnoreCase("null"))
                        //new MyTask().execute();
                        Toaster.toast("Account Not Exist.");
                    else {
                        //hiddenOTP_txt.setText(mypassword);
                        String vinputOTP=txtpassword.getText().toString();
                        String vgeneratedOTP=mypassword;

                        if(vinputOTP.equals(""))
                        {
                            Toast.makeText(Login_A.this, "Enter Password", Toast.LENGTH_LONG).show();
                        } else {
                            if(vinputOTP.equals(vgeneratedOTP))
                            {


                                Sign_in_with_Mobileno();
                            }
                            else
                            {
                                Toast.makeText(Login_A.this, "Password  Not Verified !!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Open_Privacy_policy(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Variables.privacy_policy));
        startActivity(browserIntent);
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

    //facebook implimentation
    public void Loginwith_FB(){

        LoginManager.getInstance()
                .logInWithReadPermissions(Login_A.this,
                        Arrays.asList("public_profile","email"));

        // initialze the facebook sdk and request to facebook for login
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.fullyInitialize();
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()  {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("resp_token",loginResult.getAccessToken()+"");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(Login_A.this, "Login Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("resp",""+error.toString());
                Toast.makeText(Login_A.this, "Login Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void handleFacebookAccessToken(final AccessToken token) {
        // if user is login then this method will call and
        // facebook will return us a token which will user for get the info of user
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d("resp_token",token.getToken()+"");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Functions.Show_loader(Login_A.this,false,false);
                             final String id = Profile.getCurrentProfile().getId();
                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject user, GraphResponse graphResponse) {

                                    Functions.cancel_loader();
                                    Log.d("resp",user.toString());
                                    //after get the info of user we will pass to function which will store the info in our server

                                    String fname=""+user.optString("first_name");
                                    String lname=""+user.optString("last_name");


                                    if(fname.equals("") || fname.equals("null"))
                                        fname=getResources().getString(R.string.app_name);

                                    if(lname.equals("") || lname.equals("null"))
                                        lname="";

                                    Change_Url_to_base64(""+id,fname
                                            ,lname,
                                            "https://graph.facebook.com/"+id+"/picture?width=500&width=500",
                                            "facebook");

                                }
                            });

                            // here is the request to facebook sdk for which type of info we have required
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "last_name,first_name,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } else {
                            Functions.cancel_loader();
                            Toast.makeText(Login_A.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        if(requestCode==123){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if(mCallbackManager!=null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    //Change_Url_to_base64
    //Mobile Login Implimentation
    public void Sign_in_with_Mobileno(){

        String id= txtmobileno.getText().toString();
        String fname="Telegence";
        String lname="User";

        String pic_url;
        pic_url="null";

        Log.println(Log.INFO, "Mobile Sign In: ","1");

        if(fname.equals("") || fname.equals("null"))
            fname=getResources().getString(R.string.app_name);

        if(lname.equals("") || lname.equals("null"))
            lname="User";
        Change_Url_to_base64(id,fname,lname,pic_url,"mobile");
        Log.println(Log.INFO, "Mobile Sign In: ","2");

    }


    // Start Code by Raju Baghel //////////////////////////////////////////
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;

        protected void onPreExecute() {
            //display progress dialog.
        }
        @Override
        protected Void doInBackground(Void... voids) {

            int randomPIN = (int)(Math.random()*9000)+1000;
            String val = ""+randomPIN;
            result=val;
            String recipient=txtmobileno.getText().toString();
            String message = " OTP : "+val;
            String un = getResources().getString(R.string.UN);
            String p =  getResources().getString(R.string.P);
            String se =  getResources().getString(R.string.se);;

            try
            {
                String requestUrl  = "http://mobicomm.dove-sms.com//submitsms.jsp?" +
                        "user=" + un +
                        "&key=" + p +
                        "&mobile=" + recipient +
                        "&message=" + message +
                        "&senderid=" + se +
                        "&accusage=1";

                URL url = new URL(requestUrl);
                HttpURLConnection uc = (HttpURLConnection)url.openConnection();

                uc.getResponseMessage();
                uc.disconnect();
            }
            catch(Exception ex)
            {
                Toaster.toast("SMS Error ! "+ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //hiddenOTP_txt.setText(result);
            login_main.setVisibility(View.GONE);
            //login_otp.setVisibility(View.VISIBLE);
            //et1.requestFocus();
            Toaster.toast("OTP Sent Successfully ");
            //textMessage.setText(result);
            //textLoad.setText("Finished");
            super.onPostExecute(aVoid);
        }
    }
    // End Code by Raju Baghel //////////////////////////////////////////


    //google Implimentation
    GoogleSignInClient mGoogleSignInClient;
    public void Sign_in_with_gmail(){
        Log.println(Log.INFO, "Google Sign In: ","1");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        Log.println(Log.INFO, "Google Sign In: ","2");
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.println(Log.INFO, "Google Sign In: ","3");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Login_A.this);
        Log.println(Log.INFO, "Google Sign In: ","4");
        if (account != null) {
            String id=account.getId();
            String fname=""+account.getGivenName();
            String lname=""+account.getFamilyName();

            String pic_url;
            if(account.getPhotoUrl()!=null) {
                 pic_url = account.getPhotoUrl().toString();
            }else {
                pic_url="null";
            }

            Log.println(Log.INFO, "Google Sign In: ","5");

            if(fname.equals("") || fname.equals("null"))
                fname=getResources().getString(R.string.app_name);

            if(lname.equals("") || lname.equals("null"))
                lname="User";
            Change_Url_to_base64(id,fname,lname,pic_url,"gmail");
            Log.println(Log.INFO, "Google Sign In: ","6");

        }
        else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);
        }

    }


    //Relate to google login
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            Log.println(Log.INFO, "Google Sign In: ","1");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.println(Log.INFO, "Google Sign In: ","2");
            if (account != null) {
                Log.println(Log.INFO, "Google Sign In: ","3");
                String id=account.getId();
                String fname=""+account.getGivenName();
                String lname=""+account.getFamilyName();
                Log.println(Log.INFO, "Google Sign In: ","4");
                // if we do not get the picture of user then we will use default profile picture

                String pic_url;
                if(account.getPhotoUrl()!=null) {
                    pic_url = account.getPhotoUrl().toString();
                }else {
                    pic_url="null";
                }
                Log.println(Log.INFO, "Google Sign In: ","5");

                if(fname.equals("") || fname.equals("null"))
                    fname=getResources().getString(R.string.app_name);

                if(lname.equals("") || lname.equals("null"))
                    lname="User";
                Log.println(Log.INFO, "Google Sign In: ","6");
                Change_Url_to_base64(id,fname,lname,pic_url,"gmail");
                Log.println(Log.INFO, "Google Sign In: ","7");

            }
        } catch (ApiException e) {
            Log.w("Error message", "signInResult:failed code=" + e.getStatusCode());
        }

    }



    public void Change_Url_to_base64(final String user_id,
                                     final String f_name,final String l_name,String picture,final String singnup_type){

        Functions.Show_loader(this,false,true);
        Log.d(Variables.tag,picture);

        if(picture.equalsIgnoreCase("null")){
            Call_Api_For_Signup(user_id, f_name, l_name, picture, singnup_type);
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(picture)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String image_base64 = Functions.Bitmap_to_base64(Login_A.this, resource);
                            Call_Api_For_Signup(user_id, f_name, l_name, image_base64, singnup_type);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }




    // this function call an Api for Signin
    private void Call_Api_For_Signup(String id,
                                     String f_name,
                                     String l_name,
                                     String picture,
                                     String singnup_type) {


        PackageInfo packageInfo = null;
        try {
            packageInfo =getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appversion=packageInfo.versionName;

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", id);
            parameters.put("first_name",""+f_name);
            parameters.put("last_name", ""+l_name);
            parameters.put("gender","m");
            parameters.put("version",appversion);
            parameters.put("group",group);
            parameters.put("signup_type",singnup_type);
            parameters.put("device",Variables.device);

            JSONObject file_data=new JSONObject();
            file_data.put("file_data",picture);
            parameters.put("image",file_data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.Show_loader(this,false,false);
        ApiRequest.Call_Api(this, Variables.SignUp, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Toast.makeText(Login_A.this, "Welcome to Telegence", Toast.LENGTH_LONG).show();
                Functions.cancel_loader();
                Parse_signup_data(resp);
            }
        });

    }




    // if the signup successfull then this method will call and it store the user info in local
    public void Parse_signup_data(String loginData) {
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray jsonArray=jsonObject.getJSONArray("msg");
                JSONObject userdata = jsonArray.getJSONObject(0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(Variables.u_id,userdata.optString("fb_id"));
                editor.putString(Variables.f_name,userdata.optString("first_name"));
                editor.putString(Variables.l_name,userdata.optString("last_name"));
                editor.putString(Variables.u_name,userdata.optString("username"));
                editor.putString(Variables.u_grp,userdata.optString("user_group"));
                editor.putString(Variables.gender,userdata.optString("gender"));
                editor.putString(Variables.go_live,userdata.optString("golive"));
                String signup_type=userdata.optString("signup_type");

                editor.putString(Variables.u_pic,userdata.optString("profile_pic"));
                editor.putString(Variables.u_pic1,userdata.optString("profile_pic1"));
                editor.putString(Variables.u_pic2,userdata.optString("profile_pic2"));
                editor.putString(Variables.u_pic3,userdata.optString("profile_pic3"));
                editor.putString(Variables.u_pic4,userdata.optString("profile_pic4"));
                editor.putString(Variables.u_pic5,userdata.optString("profile_pic5"));
                editor.putString(Variables.u_vid,userdata.optString("profile_video"));
                editor.putString(Variables.api_token,userdata.optString("tokon"));
                editor.putBoolean(Variables.islogin,true);
                editor.commit();
                Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
                Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
                Variables.GoliveStatus=Variables.sharedPreferences.getString(Variables.go_live,"No");
                Variables.Level=Variables.sharedPreferences.getString(Variables.go_live,"0");
                Variables.Reload_my_videos=true;
                Variables.Reload_my_videos_inner=true;
                Variables.Reload_my_likes_inner=true;
                Variables.Reload_my_notification=true;
                finish();
                startActivity(new Intent(this, MainMenuActivity.class));
            }else {
                Toast.makeText(this, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // this function will print the keyhash of your project
    // which is very helpfull during Fb login implimentation
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


