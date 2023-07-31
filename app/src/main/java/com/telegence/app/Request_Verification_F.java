package com.telegence.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.FileUtils;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Request_Verification_F extends AppCompatActivity implements View.OnClickListener {


    EditText username_edit,fullname_edit;
    TextView file_name_txt,other_file_name_txt;
    String base_64,base_64_others;
    long mBackPressed;
    Boolean isaadhar=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_request_varification);

        findViewById(R.id.Goback).setOnClickListener(this);

        username_edit=findViewById(R.id.username_edit);
        fullname_edit=findViewById(R.id.fullname_edit);

        //username_edit.setText(Variables.sharedPreferences.getString(Variables.u_name,""));
        fullname_edit.setText(Variables.sharedPreferences.getString(Variables.f_name,"")+" "+Variables.sharedPreferences.getString(Variables.l_name,""));

        file_name_txt=findViewById(R.id.file_name_txt);
        other_file_name_txt=findViewById(R.id.other_file_name_txt);
        findViewById(R.id.choose_file_btn).setOnClickListener(this);

        findViewById(R.id.choose_other_file_btn).setOnClickListener(this);
        findViewById(R.id.send_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Goback:
                this.onBackPressed();
                break;

            case R.id.choose_file_btn:
                isaadhar = true;
                selectImage();
                break;

            case R.id.choose_other_file_btn:
                isaadhar = false;
                selectImage();
                break;
            case R.id.send_btn:
                if(Check_Validation()){
                    Call_api();
                }
                break;
        }

    }

    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectImage() {

        final CharSequence[] options = { "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Request_Verification_F.this,R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    if(Functions.check_permissions(Request_Verification_F.this))
                        openCameraIntent();

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    if(Functions.check_permissions(Request_Verification_F.this)) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
                mBackPressed = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }

    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(this.getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Request_Verification_F.this, this.getPackageName()+".fileprovider", photoFile);
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
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public  String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = Request_Verification_F.this.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    File image_file;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(     imageFilePath);
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
                image_file=new File(imageFilePath);
                Uri selectedImage =(Uri.fromFile(image_file));

                InputStream imageStream = null;
                try {
                    imageStream =this.getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

                Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.7), (int)(rotatedBitmap.getHeight()*0.7), true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                if(image_file!=null) {
                    if(isaadhar){
                        base_64=Functions.Bitmap_to_base64(this,resized);
                        file_name_txt.setText(image_file.getName());
                    } else {
                        base_64_others=Functions.Bitmap_to_base64(this,resized);
                        other_file_name_txt.setText(image_file.getName());
                    }
                }
            }

            else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                try {
                    image_file= FileUtils.getFileFromUri(Request_Verification_F.this,selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InputStream imageStream = null;
                try {
                    imageStream =this.getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path=getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
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
                }

                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);


                Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.5), (int)(rotatedBitmap.getHeight()*0.5), true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                if(image_file!=null) {
                    if(isaadhar){
                        base_64=Functions.Bitmap_to_base64(this,resized);
                        file_name_txt.setText(image_file.getName());
                    } else {
                        base_64_others=Functions.Bitmap_to_base64(this,resized);
                        other_file_name_txt.setText(image_file.getName());
                    }
                }

            }

        }

    }


    // this will check the validations like none of the field can be the empty
    public boolean Check_Validation(){

        String uname=username_edit.getText().toString();
        String fullname=fullname_edit.getText().toString();

        if(TextUtils.isEmpty(uname)|| uname.length()!=12){
            Toast.makeText(getApplicationContext(), "Please enter Correct Aadhar Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(TextUtils.isEmpty(fullname)){
            Toast.makeText(getApplicationContext(), "Please enter full name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(base_64==null) {
            Toast.makeText(getApplicationContext(), "Please select the image", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(base_64_others==null) {
            Toast.makeText(getApplicationContext(), "Please select the image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void Call_api(){
        JSONObject params=new JSONObject();
        try {
            params.put("fb_id",Variables.user_id);
            params.put("fullname",fullname_edit.getText().toString());
            params.put("aadhar_no",username_edit.getText().toString());
            params.put("attachment",base_64);
            params.put("attachment2",base_64_others);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Functions.Show_loader(Request_Verification_F.this,false,false);
        ApiRequest.Call_Api(Request_Verification_F.this, Variables.getVerified, params, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code=jsonObject.optString("code");
                    if(code.equalsIgnoreCase("200")) {
                        Toast.makeText(getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Request_Verification_F.this, Processing_A.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


}
