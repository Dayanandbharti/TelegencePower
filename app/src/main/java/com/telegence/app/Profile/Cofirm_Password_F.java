package com.telegence.app.Profile;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.telegence.app.R;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.MainMenuFragment;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.SimpleClasses.API_CallBack;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
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
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */


public class Cofirm_Password_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    String olduseranme;
    String olduseranmechange="1";
    public Cofirm_Password_F() {

    }

    Fragment_Callback fragment_callback;

    public Cofirm_Password_F(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }

    ImageView profile_image;
    TextView countrytxt, DOB;
    EditText edt_nom_name, edt_nom_relation, edt_nom_mobile, username_edit, firstname_edit, lastname_edit, user_IG_edit,user_PAN_edit,user_bio_edit;
    Spinner country;

    RadioButton male_btn, female_btn;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        context = getContext();
        MainMenuActivity.mainbar.setText("Edit Profile");
        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.save_btn).setOnClickListener(this);
        view.findViewById(R.id.upload_pic_btn).setOnClickListener(this);
        view.findViewById(R.id.btndeleteprofile).setOnClickListener(this);

        edt_nom_name = view.findViewById(R.id.nom_name);
        edt_nom_relation = view.findViewById(R.id.nom_relation);
        edt_nom_mobile = view.findViewById(R.id.nom_mob);
        username_edit = view.findViewById(R.id.username_edit);
        profile_image = view.findViewById(R.id.profile_image);
        firstname_edit = view.findViewById(R.id.firstname_edit);
        lastname_edit = view.findViewById(R.id.lastname_edit);
        DOB = view.findViewById(R.id.DOB);
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        DOB.setText(s);
        DOB.setOnClickListener(this);

        user_IG_edit = view.findViewById(R.id.userIG_edit);
        user_PAN_edit = view.findViewById(R.id.user_PAN_edit);
        user_bio_edit = view.findViewById(R.id.user_bio_edit);
        username_edit.setText(Variables.sharedPreferences.getString(Variables.u_name, ""));
        String OldUserName=username_edit.getText().toString();
        firstname_edit.setText(Variables.sharedPreferences.getString(Variables.f_name, ""));
        lastname_edit.setText(Variables.sharedPreferences.getString(Variables.l_name, ""));

        String pic = Variables.sharedPreferences.getString(Variables.u_pic, "");
        if (pic != null && !pic.equalsIgnoreCase(""))
            Picasso.get()
                    .load(Variables.sharedPreferences.getString(Variables.u_pic, ""))
                    .placeholder(R.drawable.profile_image_placeholder)
                    .resize(200, 200)
                    .centerCrop()
                    .into(profile_image);

        male_btn = view.findViewById(R.id.male_btn);
        female_btn = view.findViewById(R.id.female_btn);

        countrytxt = view.findViewById(R.id.countrytxt);
        country = view.findViewById(R.id.spncountry);
        Call_api_All_IndianState();
        //Call_api_All_Country();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                countrytxt.setText(country.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        Call_Api_For_User_Details();
        return view;
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
            DOB.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
            //updateLabel();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Goback:
                getActivity().onBackPressed();
                break;
            case R.id.save_btn:
                if (Check_Validation()) {

                    Call_Api_For_Edit_profile();
                }
                break;

            case R.id.upload_pic_btn:
                selectImage();
                break;
            case R.id.DOB:
                new DatePickerDialog(getContext(),R.style.DialogTheme,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                /*String[] DateData = new String[0]
                if (DOB.getText().toString().equals(""))
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                DateData = DOB.getText().toString().split("-");
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(DateData[2]), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
                break;
            case R.id.btndeleteprofile:

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                builder.setTitle("Confirmation Dialog");
                builder.setMessage("Are you sure?\nIt is Non-reversible, your all data will be lost permanently.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, but close the dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                        builder.setTitle("Confirmation Dialog");
                        builder.setMessage("Are you really sure?\nIt is Non-reversible, your all data will be lost permanently.");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing, but close the dialog
                                Call_Api_For_delete_profile();
                                //Toast.makeText(getContext(),"Yes Pressed",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                Toast.makeText(getContext(), "Well Done! you saved your profile data.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        Toast.makeText(getContext(), "Well Done! you saved your profile data.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }


    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (Functions.check_permissions(getActivity()))
                        openCameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    if (Functions.check_permissions(getActivity())) {
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
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
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
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
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
    public boolean Check_Validation() {

        String uname = username_edit.getText().toString();
        String firstname = firstname_edit.getText().toString();
        String lastname = lastname_edit.getText().toString();

        if (TextUtils.isEmpty(uname) || uname.length() < 2) {
            Toast.makeText(context, "Please enter correct username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(edt_nom_name.getText().toString().trim())) {
            Toast.makeText(context, "Please enter Nominee name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(edt_nom_relation.getText().toString().trim())) {
            Toast.makeText(context, "Please enter Nominee Relation", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(edt_nom_mobile.getText().toString().trim())) {
            Toast.makeText(context, "Please enter Nominee Mobile ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!male_btn.isChecked() && !female_btn.isChecked()) {
            Toast.makeText(context, "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    String image_bas64;
    private void beginCrop(Uri source) {

        CropImage.activity(source)
                .setAspectRatio(1,1)
                .setMinCropWindowSize(100,100)
                .setFixAspectRatio(true)
                .start(getActivity());
    }

    private void handleCrop(Uri userimageuri) {

        InputStream imageStream = null;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(userimageuri);
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

        image_bas64 = Functions.Bitmap_to_base64(getActivity(), rotatedBitmap);

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

        Functions.Show_loader(context, false, false);
        ApiRequest.Call_Api(context, Variables.uploadImage, parameters, new Callback() {
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
                                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                    .resize(200, 200).centerCrop().into(profile_image);


                        Toast.makeText(context, "Image Update Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // this will update the latest info of user in database
    public void Call_Api_For_Edit_profile() {

        Functions.Show_loader(context, false, false);

        String uname = username_edit.getText().toString().toLowerCase().replaceAll("\\s", "");
        JSONObject parameters = new JSONObject();
        try {
            if(!uname.equalsIgnoreCase(olduseranme)  && olduseranmechange.equalsIgnoreCase("0") ){
                olduseranmechange="1";
            }

            parameters.put("username", uname.replaceAll("@", ""));
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("first_name", firstname_edit.getText().toString());
            parameters.put("last_name", lastname_edit.getText().toString());
            parameters.put("bio", user_bio_edit.getText().toString());
            parameters.put("instagram", user_IG_edit.getText().toString());
            parameters.put("youtube", user_PAN_edit.getText().toString());
            parameters.put("pan", user_PAN_edit.getText().toString());
            parameters.put("nominee_name",  edt_nom_name.getText().toString());
            parameters.put("nominee_relation", edt_nom_relation.getText().toString());
            parameters.put("nominee_mobile_no", edt_nom_mobile.getText().toString());
            parameters.put("can_chng_username", olduseranmechange);
            if (male_btn.isChecked()) {
                parameters.put("gender", "Male");

            } else if (female_btn.isChecked()) {
                parameters.put("gender", "Female");
            }
            parameters.put("dob", DOB.getText().toString());
            if (countrytxt.getText().toString().equals("Select Country")) {
                parameters.put("country", "");
            } else {
                parameters.put("country", spinnerMap.get(country.getSelectedItemPosition()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.edit_profile, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    JSONArray msg = response.optJSONArray("msg");
                    if (code.equals("200")) {
                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
                        String u_name = username_edit.getText().toString();
                        if (!u_name.contains("@"))
                            u_name = "@" + u_name;
                        editor.putString(Variables.u_name, u_name);
                        editor.putString(Variables.f_name, firstname_edit.getText().toString());
                        editor.putString(Variables.l_name, lastname_edit.getText().toString());
                        editor.commit();
                        Variables.user_name = u_name;
                        MainMenuFragment mainMenuFragment = new MainMenuFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                        transaction.replace(R.id.nav_host_fragment, mainMenuFragment).commit();
                    } else {
                        if (msg != null) {
                            JSONObject jsonObject = msg.optJSONObject(0);
                            Toast.makeText(context, jsonObject.optString("response"), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Call_Api_For_delete_profile() {

        Functions.Show_loader(context, false, false);
        String User_ID = Variables.sharedPreferences.getString(Variables.u_id, "0");
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id", User_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.delete_profile, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");
                    if (code.equals("200")) {
                        Toast.makeText(getContext(),"Profile Deleted Successfully", Toast.LENGTH_LONG).show();
                        // this will erase all the user info store in locally and logout the user
                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();
                        editor.putString(Variables.u_id, "");
                        editor.putString(Variables.u_name, "");
                        editor.putString(Variables.u_pic, "");
                        editor.putBoolean(Variables.islogin, false);
                        editor.commit();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), MainMenuActivity.class));
                    } else {
                        if (msg != null) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
        Functions.Show_loader(getActivity(), false, false);
        Functions.Call_Api_For_Get_User_data(getActivity(),
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
                username_edit.setText(data.optString("username"));
                olduseranme=username_edit.getText().toString();
                olduseranmechange=data.optString("can_change_username");
                if(olduseranmechange!=null && !olduseranmechange.equalsIgnoreCase("") && olduseranmechange.equalsIgnoreCase("0")){
                    username_edit.setEnabled(true);
                }
                String picture = data.optString("profile_pic");
                if (picture != null && !picture.equalsIgnoreCase(""))
                    Picasso.get().load(picture).placeholder(R.drawable.profile_image_placeholder).into(profile_image);
                String gender = data.optString("gender");
                if (gender.equalsIgnoreCase("male")) {
                    male_btn.setChecked(true);
                } else if (gender.equalsIgnoreCase("female")) {
                    female_btn.setChecked(true);
                }
                user_bio_edit.setText(data.optString("bio"));

                if(data.optString("instagram").equals("null") || data.optString("instagram").equals(""))
                    user_IG_edit.setText("");
                else
                    user_IG_edit.setText(data.optString("instagram"));

                if(data.optString("pan").equals("null") || data.optString("pan").equals(""))
                    user_PAN_edit.setText("");
                else
                    user_PAN_edit.setText(data.optString("pan"));

                DOB.setText(data.optString("dob").toString());

                user_PAN_edit.setText(data.optString("pan_no").toString());
                edt_nom_name.setText(data.optString("nom_name").toString());
                edt_nom_relation.setText(data.optString("nom_relation").toString());
                edt_nom_mobile.setText(data.optString("nom_mobile").toString());
                /*if(spinnerMap1!=null) {
                    Integer ValuePos = spinnerMap1.get(data.optString("country"));
                    country.setSelection(ValuePos);
                }else{
                    Call_api_All_Country();
                }*/
                if(spinnerMap1!=null) {
                    Integer ValuePos = spinnerMap1.get(data.optString("country"));
                    country.setSelection(ValuePos);
                }
            } else {
                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Call_api_All_Country() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getallcountry, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_Countrydata(resp);
            }
        });
    }

    public void Call_api_All_IndianState() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getallindianstate, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_Countrydata(resp);
            }
        });
    }
    HashMap<Integer,String> spinnerMap=null;
    HashMap<String,Integer> spinnerMap1=null;

    public void parse_Countrydata(String resp) {
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msg = jsonObject.getJSONArray("msg");
                if (msg.length() <= 0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else  {
                    String[] spinnerArray = new String[msg.length()];
                    spinnerMap = new HashMap< Integer,String>();
                    spinnerMap1 = new HashMap<String, Integer>();
                    for (int i = 0; i < msg.length(); i++) {
                        JSONObject data = msg.getJSONObject(i);
                        spinnerMap.put(i,data.getString("id"));
                        spinnerMap1.put(data.getString("id"),i);
                        String DStr=data.getString("name") + " ("+ data.getString("sortname") + ")";
                        spinnerArray[i]=DStr;
                    }

                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(context,R.layout.spinner_text, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    country.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}