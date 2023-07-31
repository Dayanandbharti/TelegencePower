package com.telegence.app.Authentication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.telegence.app.R;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.SimpleClasses.API_CallBack;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import xdroid.toaster.Toaster;


/**
 * A simple {@link Fragment} subclass.
 */
public class Sign_up extends Activity {
    
    Context context;
    final Calendar myCalendar = Calendar.getInstance();
    SharedPreferences sharedPreferences;
    public Sign_up() {}
    View view_signup,view_otp,signupsection;
    Fragment_Callback fragment_callback;

    public Sign_up(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }
    String mypassword;
    Button update_btn;
    ImageView profile_image;
    TextView txt_DOB,txtMobileNo,txtforgotMobileNo,hiddenOTP_txt,lblrefer_name,txt_sent_mob;
    EditText  firstname_edit, lastname_edit,user_pass_edit,user_pass_edit_Confirm,user_refer_edit,cp_ser_pass_edit,cp_user_pass_edit_Confirm;
    EditText et1,et2,et3,et4;
    String group = "student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_signup);
        context = getApplicationContext();

        sharedPreferences = getSharedPreferences(Variables.pref_name,MODE_PRIVATE);

        try {
            group = getIntent().getStringExtra("group");

            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString(Variables.u_id,"");
            editor.putString(Variables.u_name,"");
            editor.putString(Variables.u_pic,"");
            editor.putBoolean(Variables.islogin,false);
            editor.commit();
        } catch (Exception e){
            e.printStackTrace();
        }

        hiddenOTP_txt=findViewById(R.id.hiddenOTP_txt);
        lblrefer_name=findViewById(R.id.lblrefer_name);
        txt_sent_mob=findViewById(R.id.txt_sent_mob);

        et1=findViewById(R.id.editText1);
        et2=findViewById(R.id.editText2);
        et3=findViewById(R.id.editText3);
        et4=findViewById(R.id.editText4);

        et1.addTextChangedListener(new GenericOTPTextWatcher(et1));
        et2.addTextChangedListener(new GenericOTPTextWatcher(et2));
        et3.addTextChangedListener(new GenericOTPTextWatcher(et3));
        et4.addTextChangedListener(new GenericOTPTextWatcher(et4));


        findViewById(R.id.txtsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });
        findViewById(R.id.iconcalender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Sign_up.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    is_valid_refer();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_verify_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1.getText().toString().trim().equalsIgnoreCase("") ||
                        et2.getText().toString().trim().equalsIgnoreCase("") ||
                        et3.getText().toString().trim().equalsIgnoreCase("") ||
                        et1.getText().toString().trim().equalsIgnoreCase("")){

                    Toaster.toast("Enter OTP");

                } else {

                    String vinputOTP=et1.getText().toString().trim() + et2.getText().toString().trim() + et3.getText().toString().trim()+ et4.getText().toString().trim();
                    String vgeneratedOTP=hiddenOTP_txt.getText().toString();

                    if(vinputOTP.equals("") )
                    {
                        Toaster.toast("Enter OTP");
                    }
                    else
                    {
                        if(vinputOTP.equals(vgeneratedOTP))
                        {

                            Toaster.toast("Welcome " + firstname_edit.getText().toString() + " " + lastname_edit.getText().toString());
                            Sign_in_with_Mobileno();
                        }
                        else
                        {
                            Toaster.toast("Incorrect OTP");
                        }
                    }
                }
            }
        });
        findViewById(R.id.txtlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up.this, Login_A.class);
                intent.putExtra("group",group);
                startActivity(intent);
            }
        });

        /*findViewById(R.id.upload_pic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/

        view_signup=findViewById(R.id.view_signup);
        view_signup.setVisibility(View.VISIBLE);

        view_otp=findViewById(R.id.view_otp);
        signupsection=findViewById(R.id.signupsection);
        view_otp.setVisibility(View.GONE);

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_updatepass_validation();
            }
        });
        profile_image = findViewById(R.id.profile_image);
        user_refer_edit = findViewById(R.id.user_refer_edit);
        cp_ser_pass_edit = findViewById(R.id.cp_ser_pass_edit);
        cp_user_pass_edit_Confirm = findViewById(R.id.cp_user_pass_edit_Confirm);
        user_refer_edit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                lblrefer_name.setText("");
            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
                lblrefer_name.setText("");
            }

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                lblrefer_name.setText("");
            }

        });
        firstname_edit = findViewById(R.id.firstname_edit);
        lastname_edit = findViewById(R.id.lastname_edit);
        txt_DOB = findViewById(R.id.txt_DOB);
        txtMobileNo = findViewById(R.id.txtMobileNo);
        txtforgotMobileNo = findViewById(R.id.txtforgotMobileNo);
        user_pass_edit = findViewById(R.id.user_pass_edit);
        user_pass_edit_Confirm = findViewById(R.id.user_pass_edit_Confirm);

        Intent bundle = getIntent();
        if(bundle!=null) {
            Variables.sharedPreferences = sharedPreferences;
            Uri appLinkData = bundle.getData();
            if (appLinkData != null) {
                String link = appLinkData.toString();
                String[] data = link.split("/");
                signupsection.setVisibility(View.GONE);
                user_refer_edit.setText(data[data.length - 1]);
                user_refer_edit.setEnabled(false);
            }
        }
    }

    private void signin_main(){
        String firstname = firstname_edit.getText().toString();
        String lastname = lastname_edit.getText().toString();
        String mobile = txtMobileNo.getText().toString();
        String DOB = txt_DOB.getText().toString();
        String password = user_pass_edit.getText().toString();
        String password_confirm = user_pass_edit_Confirm.getText().toString();
        DOB = String.valueOf(System.currentTimeMillis());
        if(firstname.equalsIgnoreCase("")) {
            Toaster.toast("Enter First Name");
            firstname_edit.requestFocus();
        }
        else if(lastname.equalsIgnoreCase("") ) {
            Toaster.toast("Enter Last Name");
            lastname_edit.requestFocus();
        }
        else if(mobile.equalsIgnoreCase("") ) {
            Toaster.toast("Enter Mobile Number");
            txtMobileNo.requestFocus();
        } else if(DOB.equalsIgnoreCase("") ) {
            Toaster.toast("Select DOB");
            txt_DOB.requestFocus();
        } else if(password.equalsIgnoreCase("")){
            Toaster.toast("Enter Password");
            user_pass_edit.requestFocus();
        } else if(password.length()<4) {
            Toaster.toast("Password must be of 4 Letters");
            user_pass_edit.requestFocus();
        } else if(!password.equalsIgnoreCase(password_confirm)){
            Toaster.toast("Password not matching.");
            user_pass_edit_Confirm.requestFocus();
        } else {
            return_hasPassword(txtMobileNo.getText().toString());
        }
    }

    private void signin_updatepass_validation(){
        String password = cp_ser_pass_edit.getText().toString();
        String password_confirm = cp_user_pass_edit_Confirm.getText().toString();
        if(password.equalsIgnoreCase("")){
            Toaster.toast("Enter Password");
            user_pass_edit.requestFocus();
        }
        else if(password.length()<4) {
            Toaster.toast("Password must be of 4 Letters");
            user_pass_edit.requestFocus();
        }
        else if(!password.equalsIgnoreCase(password_confirm)){
            Toaster.toast("Password not matching.");
            user_pass_edit_Confirm.requestFocus();
        }
        else {
            return_hasPassword(txtMobileNo.getText().toString());
        }
    }
    private void Call_Api_For_Update_Pass(String id,String f_name,String l_name,String picture,String singnup_type) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", id);
            parameters.put("password",cp_ser_pass_edit.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.Show_loader(this,false,false);
        ApiRequest.Call_Api(this, Variables.update_pass, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try{
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");

                    if (code.equals("200")) {
                        if (msg!="") {
                            Toaster.toast(msg);
                        }
                        startActivity(new Intent(Sign_up.this, Login_A.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void return_hasPassword(String FB_ID) {
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
                            mypassword=msg;
                        }
                    }
                    if(mypassword.equalsIgnoreCase("") || mypassword.equalsIgnoreCase("null"))
                        new MyTask().execute();
                    else {
                        Toaster.toast("Account already Exists.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear = monthOfYear + 1;
            txt_DOB.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
            //updateLabel();
        }
    };

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
            String recipient=txtMobileNo.getText().toString();
            String message = val;
            txt_sent_mob.setText("Enter the OTP sent to " + txtMobileNo.getText().toString());
            sendmsg(recipient,message);

           /* try
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
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            hiddenOTP_txt.setText(result);
            view_signup.setVisibility(View.GONE);
            view_otp.setVisibility(View.VISIBLE);
            et1.requestFocus();
            Toaster.toast("OTP Sent Successfully ");
            //textMessage.setText(result);
            //textLoad.setText("Finished");
            super.onPostExecute(aVoid);
        }
    }

    public void sendmsg(String Mobile,String MSg) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("msg", MSg);
            parameters.put("mobile_no",Mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(getApplicationContext(), Variables.sendOTP, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    //JSONObject response = new JSONObject(resp);
                    /*String code = response.optString("code");
                    if (code.equals("200")) {
                        //String msg = response.optString("msg");
                        //Toaster.toast(msg);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Sign_in_with_Mobileno(){

        String id= txtMobileNo.getText().toString();
        String fname=firstname_edit.getText().toString();
        String lname=lastname_edit.getText().toString();

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

    public void Change_Url_to_base64(final String user_id,
                                     final String f_name,final String l_name,String picture,final String singnup_type){

        Functions.Show_loader(this,false,true);
        Log.d(Variables.tag,picture);

        if(picture.equalsIgnoreCase("null")){
            Call_Api_For_Signup(user_id, f_name, l_name, picture, singnup_type);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(picture)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            String image_base64 = Functions.Bitmap_to_base64(Sign_up.this, resource);
                            Call_Api_For_Signup(user_id, f_name, l_name, image_base64, singnup_type);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    // this function call an Api for Signin
    private void Call_Api_For_Signup(String id,String f_name,String l_name,String picture,String singnup_type) {

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
            parameters.put("first_name","" + f_name);
            parameters.put("last_name", "" + l_name);
            parameters.put("gender","Male");
            parameters.put("dob",txt_DOB.getText().toString().trim());
            parameters.put("password",user_pass_edit.getText().toString());
            parameters.put("group",group);
            parameters.put("version",appversion);
            parameters.put("signup_type",singnup_type);
            parameters.put("device",Variables.device);
            parameters.put("refer",user_refer_edit.getText().toString());

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
                Functions.cancel_loader();
                Parse_signup_data(resp);
            }
        });

    }

    public void Parse_signup_data(String loginData) {
        try {
            //Toaster.toast("Parse Sign up Data");
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
                //Toaster.toast("Finished");
                startActivity(new Intent(this, MainMenuActivity.class));
            } else {
                Toast.makeText(this, ""+ jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up.this, R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (Functions.check_permissions(Sign_up.this))
                        openCameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    if (Functions.check_permissions(Sign_up.this)) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });

        builder.show();

    }


    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(Sign_up.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Sign_up.this.getApplicationContext(), Sign_up.this.getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 1);
            }
        }
    }

    String imageFilePath;

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                Sign_up.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = Sign_up.this.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
                beginCrop(selectedImage);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                beginCrop(selectedImage);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                handleCrop(result.getUri());
            }

        }

    }

    // this will check the validations like none of the field can be the empty



    String image_bas64;

    private void beginCrop(Uri source) {

        CropImage.activity(source).start(Sign_up.this);


    }

    private void handleCrop(Uri userimageuri) {

        InputStream imageStream = null;
        try {
            imageStream = Sign_up.this.getContentResolver().openInputStream(userimageuri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path = userimageuri.getPath();
        Matrix matrix = new Matrix();
        android.media.ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new android.media.ExifInterface(path);
                int orientation = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        image_bas64 = Functions.Bitmap_to_base64(Sign_up.this, rotatedBitmap);

        Call_Api_For_image();
    }

    public void Call_Api_For_image() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));

            JSONObject file_data = new JSONObject();
            file_data.put("file_data", image_bas64);
            parameters.put("image", file_data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.Show_loader(Sign_up.this, false, false);
        ApiRequest.Call_Api(Sign_up.this, Variables.uploadImage, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    JSONArray msg = response.optJSONArray("msg");
                    if (code.equals("200")) {

                        JSONObject data = msg.optJSONObject(0);

                        Variables.sharedPreferences.edit().putString(Variables.u_pic, data.optString("profile_pic")).commit();
                        Variables.user_pic = data.optString("profile_pic");

                        if (Variables.user_pic != null && !Variables.user_pic.equals(""))
                            Picasso.get()
                                    .load(Variables.user_pic)
                                    .placeholder(Sign_up.this.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                    .resize(200, 200).centerCrop().into(profile_image);

                        Toast.makeText(Sign_up.this, "Image Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void is_valid_refer() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", txtMobileNo.getText().toString().trim());
            parameters.put("refer_id", user_refer_edit.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.Show_loader(Sign_up.this, false, false);
        ApiRequest.Call_Api(context, Variables.is_valid_signup, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String data = response.optString("msg");
                    if(code.equals("200")) {
                        lblrefer_name.setText(data);
                        signin_main();
                    } else {
                        Toaster.toast(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // this will update the latest info of user in database
    public void Call_Api_For_Edit_profile() {

        Functions.Show_loader(Sign_up.this, false, false);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("first_name", firstname_edit.getText().toString());
            parameters.put("last_name", lastname_edit.getText().toString());
            parameters.put("password", user_pass_edit.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(Sign_up.this, Variables.updatepass, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    JSONArray msg = response.optJSONArray("msg");
                    if (code.equals("200")) {
                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
                        editor.putString(Variables.f_name, firstname_edit.getText().toString());
                        editor.putString(Variables.l_name, lastname_edit.getText().toString());
                        editor.commit();
                        startActivity(new Intent(Sign_up.this, MainMenuActivity.class));
                    } else {
                        if (msg != null) {
                            JSONObject jsonObject = msg.optJSONObject(0);
                            Toast.makeText(Sign_up.this, jsonObject.optString("response"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Call_Api_For_delete_profile() {

        Functions.Show_loader(Sign_up.this, false, false);
        String User_ID = Variables.sharedPreferences.getString(Variables.u_id, "0");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id", User_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(Sign_up.this, Variables.delete_profile, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");
                    if (code.equals("200")) {
                        Toast.makeText(Sign_up.this,"Profile Deleted Successfully", Toast.LENGTH_LONG).show();
                        // this will erase all the user info store in locally and logout the user
                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
                        editor.putString(Variables.u_id, "");
                        editor.putString(Variables.u_name, "");
                        editor.putString(Variables.u_pic, "");
                        editor.putBoolean(Variables.islogin, false);
                        editor.commit();
                        Sign_up.this.finish();
                        startActivity(new Intent(Sign_up.this, MainMenuActivity.class));
                    } else {
                        if (msg != null) {
                            Toast.makeText(Sign_up.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // this will get the user data and parse the data and show the data into views
    public void Call_Api_For_User_Details() {
        Functions.Show_loader(Sign_up.this, false, false);
        Functions.Call_Api_For_Get_User_data(Sign_up.this,
                Variables.sharedPreferences.getString(Variables.u_id, ""),
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {

                    }

                    @Override
                    public void OnSuccess(String responce) {
                        Functions.cancel_loader();
                        Parse_user_data(responce);
                    }

                    @Override
                    public void OnFail(String responce) {

                    }
                });
    }

    public void Parse_user_data(String responce) {
        try {
            JSONObject jsonObject = new JSONObject(responce);

            String code = jsonObject.optString("code");

            if (code.equals("200")) {
                JSONArray msg = jsonObject.optJSONArray("msg");
                JSONObject data = msg.getJSONObject(0);
                firstname_edit.setText(data.optString("first_name"));
                lastname_edit.setText(data.optString("last_name"));
                String picture = data.optString("profile_pic");
                if (picture != null && !picture.equalsIgnoreCase(""))
                    Picasso.get().load(picture).placeholder(R.drawable.profile_image_placeholder).into(profile_image);
                user_pass_edit.setText(data.optString("password"));
            } else {
                Toast.makeText(Sign_up.this, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    class GenericOTPTextWatcher implements TextWatcher
    {
        private View view;
        public GenericOTPTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.editText1:
                    if(text.length()==1)
                        et2.requestFocus();
                    break;
                case R.id.editText2:
                    if(text.length()==1)
                        et3.requestFocus();
                    else if(text.length()==0)
                        et1.requestFocus();
                    break;
                case R.id.editText3:
                    if(text.length()==1)
                        et4.requestFocus();
                    else if(text.length()==0)
                        et2.requestFocus();
                    break;
                case R.id.editText4:
                    if(text.length()==0)
                        et3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }


}