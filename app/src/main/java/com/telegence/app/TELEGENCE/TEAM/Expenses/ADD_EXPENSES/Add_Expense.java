package com.telegence.app.TELEGENCE.TEAM.Expenses.ADD_EXPENSES;


import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Expense extends RootFragment {

    View view;
    Context context;
    ProgressBar pbar;
    RelativeLayout no_data_layout;
    ImageView image;
    TextView txtaddress,mailid,name;
    EditText mob,message;
    Button btnsendquery;
    CheckBox chkfood, chkgas, chktravel, chkfuel, chkstay, chkvehicle,chkpurchase, chkmiscelleneous;
    Spinner spn_manager;

    public Add_Expense() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Add_Expense(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_expense, container, false);
        context = getContext();
        MainMenuActivity.mainbar.setText("Add Expense");
        view.setClickable(true);
        no_data_layout = view.findViewById(R.id.no_data_layout);
        image=view.findViewById(R.id.image);
        txtaddress=view.findViewById(R.id.txtphone);
        chkfood = view.findViewById(R.id.chk_food);
        spn_manager =view.findViewById(R.id.spn_manager);
        chkgas = view.findViewById(R.id.chk_gas);
        chktravel = view.findViewById(R.id.chk_travel);
        chkfuel = view.findViewById(R.id.chk_fuel);
        chkstay = view.findViewById(R.id.chk_stay);
        chkvehicle = view.findViewById(R.id.chk_repairing);
        chkpurchase = view.findViewById(R.id.chk_purchases);
        chkmiscelleneous = view.findViewById(R.id.chk_miscelleneous);
        Call_api_for_managers();
        name = view.findViewById(R.id.edt_name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.DialogTheme,fromdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mob = view.findViewById(R.id.edt_phn);
        mailid = view.findViewById(R.id.edt_mail);
        message = view.findViewById(R.id.edt_message);
        view.findViewById(R.id.btnsendquery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chkfood.isChecked() && !chkfuel.isChecked() && !chkgas.isChecked() && !chkmiscelleneous.isChecked() && !chkpurchase.isChecked() && !chkstay.isChecked() && !chktravel.isChecked() && !chkvehicle.isChecked()){
                    Toaster.toast("Please Select Expense Type");
//                } else if(message.getText().toString().equalsIgnoreCase("")){
//                    Toaster.toast("Enter Message");
                } else if(name.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Select Date");
                } else if(mob.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Amount");
                } else if(Integer.parseInt(mob.getText().toString())<=0){
                    Toaster.toast("Enter Amount greater than 0");
//                } else if(mailid.getText().toString().equalsIgnoreCase("")){
//                    Toaster.toast("Upload Bill");
//                } else if(message.getText().toString().equalsIgnoreCase("")){
//                    Toaster.toast("Enter Message");
                } else {
                    Call_api();
                }
            }
        });
        mailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        pbar=view.findViewById(R.id.pbar);
        return view;
    }

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear = monthOfYear + 1;
            name.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
        }
    };


    HashMap<Integer,String> spinner1Map=null;
    public void Call_api_for_managers() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getallManager, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_Project_ManagerData(resp);
            }
        });
    }
    public void parse_Project_ManagerData(String resp) {
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msg = jsonObject.getJSONArray("msg");
                if (msg.length() <= 0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else  {
                    String[] spinnerArray = new String[msg.length()];
                    spinner1Map = new HashMap< Integer,String>();
                    for (int i = 0; i < msg.length(); i++) {
                        JSONObject data = msg.getJSONObject(i);
                        spinner1Map.put(i,data.getString("manager_id"));
                        spinnerArray[i]=data.getString("manager_name");
                    }
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(context,R.layout.spinner_text, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_manager.setAdapter(adapter);
                }
            } else{
                Toaster.toast(jsonObject.optString("code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Call_api() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
            jsonObject.put("option", options());
            jsonObject.put("date", name.getText().toString());
            jsonObject.put("amount", mob.getText().toString().trim());
            jsonObject.put("bill", uploadedPath);
            jsonObject.put("project_manager", spinner1Map.get(spn_manager.getSelectedItemPosition()));
            jsonObject.put("message", message.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.add_expense, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    name.setText("");
                    mob.setText("");
                    mailid.setText("");
                    message.setText("");
                    Toaster.toast(jsonObject.optString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String options(){
        String option = "";
        if(chkfood.isChecked()) {
            if(option.equalsIgnoreCase("")){
                option = "Food";
            } else{
                option = option + ",Food";
            }
        }

        if(chkgas.isChecked()) {
            if(option.equalsIgnoreCase("")){
                option = "Gas";
            } else{
                option = option + ",Gas";
            }
        }

        if(chktravel.isChecked()) {
            if(option.equalsIgnoreCase("")) {
                option = "Food";
            } else {
                option = option + ",Travel";
            }
        }

        if(chkfuel.isChecked()) {
            if(option.equalsIgnoreCase("")) {
                option = "Fuel";
            } else{
                option = option + ",Fuel";
            }
        }

        if(chkstay.isChecked()) {
            if(option.equalsIgnoreCase("")) {
                option = "Stay";
            } else{
                option = option + ",Stay";
            }
        }

        if(chkvehicle.isChecked()) {
            if(option.equalsIgnoreCase("")) {
                option = "Vehicle";
            } else{
                option = option + ",Vehicle";
            }
        }

        if(chkpurchase.isChecked()) {
            if(option.equalsIgnoreCase("")) {
                option = "Purchase";
            } else{
                option = option + ",Purchase";
            }
        }

        if(chkmiscelleneous.isChecked()) {
            if(option.equalsIgnoreCase("")) {
                option = "Miscellaneous";
            } else {
                option = option + ",Miscellaneous";
            }
        }
        return option;
    }

    String image_bas64, imageFilePath, uploadedPath;
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
                handleCrop(selectedImage);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                handleCrop(selectedImage);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                handleCrop(result.getUri());
            }

        }

    }
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
        ApiRequest.Call_Api(context, Variables.uploadExpenseImage, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    JSONArray msg = response.optJSONArray("msg");
                    if (code.equals("200")) {
                        JSONObject data = msg.optJSONObject(0);
                        uploadedPath =  data.optString("profile_pic");
                        mailid.setText("Bill Uploaded");
                        Toast.makeText(context, "Bill Uploaded", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fragment_callback!=null){
            fragment_callback.Responce(new Bundle());
        }
    }

}
