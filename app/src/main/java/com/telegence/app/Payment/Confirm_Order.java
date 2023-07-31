package com.telegence.app.Payment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.telegence.app.Blocked_A;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.MainMenuFragment;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.Orders.Order_F;
import com.telegence.app.Processing_A;
import com.telegence.app.Profile.Edit_Profile_F;
import com.telegence.app.R;
import com.telegence.app.Request_Verification_F;
import com.telegence.app.SimpleClasses.API_CallBack;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.SimpleClasses.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import xdroid.toaster.Toaster;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */

public class Confirm_Order extends RootFragment  {

    View view,view_responce,qr_view;
    Context context;
    String myType;
    Spinner spn_mode_of_payment;
    ImageButton Goback,add_moments;
    protected TabLayout tabLayout;
    protected ViewPager menu_pager;
    ViewPagerAdapter adapter;
    EditText desc;
    ImageView img_status,output,image_qr_code;
    TextView  desccount,ImagePath;
    Activity a;
    LinearLayout payment_view;

    String Payment_QR = Variables.base_url + "assets/images/qr.jpeg";
    public Confirm_Order() {
        // Required empty public constructor
    }
    String add_to_wallet = "0";
    String Gold_Price = "0";
    String Gold_Quantity = "0";
    String Tax = "0";
    CheckBox apply_chk;
    String Amount = "";
    String Amount_to_pay = "";
    String Order_id = "";
    String Quantity = "";
    String wallet = "0";
    Boolean iswalletapplied=false,iswalletFullApplied=false;
    Boolean is_Added_to_Wallet=false;
    TextView txt_current_wallet,txtview_msg,txt_goldprice,txt_tax,txt_amount,txt_amount_to_pay,txt_goldquantity,txt_bank_details;
    ArrayList<String> mode_of_payment;
    Integer mode_of_payment_opt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_confirm, container, false);
        context=getContext();
        try {
            payment_view = view.findViewById(R.id.payment_view);
            payment_view.setVisibility(View.VISIBLE);
            txt_current_wallet = view.findViewById(R.id.txt_current_wallet);
            txt_current_wallet.setText("0");
            apply_chk = view.findViewById(R.id.apply_chk);
            apply_chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    // Check which checkbox was clicked
                    payment_view.setVisibility(View.VISIBLE);
                    if (checked) {
                        iswalletapplied=true;
                        iswalletFullApplied=false;
                        Double walletAmt = Double.parseDouble(wallet.trim());
                        Double cartValue = Double.parseDouble(Amount.trim());
                        if(walletAmt>=cartValue) {
                            iswalletFullApplied = true;
                            payment_view.setVisibility(View.GONE);
                            Amount_to_pay = "0";
                        } else {

                            Amount_to_pay=  String.valueOf(cartValue - walletAmt);
                            iswalletFullApplied = false;
                        }
                    } else {
                        iswalletapplied=false;
                        iswalletFullApplied=false;
                        Amount_to_pay = Amount;
                    }
                    txt_amount_to_pay.setText("Amount to pay : " + Amount_to_pay);
                }
            });
            Goback = view.findViewById(R.id.Goback);
            view_responce=view.findViewById(R.id.view_responce);
            call_Api_MyBalance();
            txt_bank_details =  view.findViewById(R.id.txt_bank_details);
            qr_view =  view.findViewById(R.id.qr_view);
            txt_bank_details.setVisibility(View.GONE);
            qr_view.setVisibility(View.VISIBLE);
            spn_mode_of_payment=view.findViewById(R.id.spn_mode_of_payment);
            mode_of_payment =new ArrayList<>();
            mode_of_payment.add("Qr Code");
            mode_of_payment.add("NEFT/ IMPS/ RTGS");
            mode_of_payment.add("Paytm Number");
            mode_of_payment.add("PhonePe Number");
            mode_of_payment.add("Google pay Number");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_text,mode_of_payment);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_mode_of_payment.setAdapter(adapter);
            spn_mode_of_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mode_of_payment_opt = position;
                    if(position==0) {
                        txt_bank_details.setVisibility(View.GONE);
                        qr_view.setVisibility(View.VISIBLE);
                    } else if(position==1) {
                        txt_bank_details.setVisibility(View.VISIBLE);
                        txt_bank_details.setText("Account Np 7146633888\nIFSC kkbk0000811\nName Mainuddin\nBranch Noida sector 16");
                        qr_view.setVisibility(View.GONE);
                    } else {
                        txt_bank_details.setText("8076294758");
                        txt_bank_details.setVisibility(View.VISIBLE);
                        qr_view.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            txtview_msg=view.findViewById(R.id.txtview_msg);
            txt_goldprice=view.findViewById(R.id.txt_goldprice);
            txt_tax=view.findViewById(R.id.txt_tax);
            txt_amount=view.findViewById(R.id.txt_amount);
            txt_amount_to_pay=view.findViewById(R.id.txt_amount_to_pay);
            txt_goldquantity=view.findViewById(R.id.txt_goldquantity);
            Goback=view.findViewById(R.id.Goback);
            Goback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.onBackPressed();
                }
            });
            view.findViewById(R.id.go_to_home).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainMenuFragment mainMenuFragment = new MainMenuFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                    //transaction.addToBackStack(null);
                    transaction.replace(R.id.nav_host_fragment, mainMenuFragment).commit();
                }
            });

            view.findViewById(R.id.go_to_orders).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order_F order_f = new Order_F();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                    //transaction.addToBackStack(null);
                    transaction.replace(R.id.nav_host_fragment, order_f).commit();
                }
            });

            Bundle bundle = getArguments();
            if(bundle!=null) {
                myType = bundle.getString("type");
                add_to_wallet = bundle.getString("add_to_wallet");
                Gold_Price = bundle.getString("gold_price");
                Gold_Quantity = bundle.getString("quantity");
                Order_id = bundle.getString("order_id");
                Tax =  bundle.getString("tax");
                Amount = bundle.getString("amount");

                Double gold_price_wid_tax = Double.parseDouble(Gold_Price);
                Double Gold_Price_without_gst = gold_price_wid_tax * (100.0/103.0);
                Double Gold_Price_without_tax = Gold_Price_without_gst * (100.0/102.0);

                txt_goldprice.setText("Gold Price : " + String.format("%.2f",Gold_Price_without_tax) + "(without Tax)");
                txt_amount.setText("Amount    : " + Amount);
                if(!add_to_wallet.trim().equalsIgnoreCase("") && Double.parseDouble(add_to_wallet)>0){
                    is_Added_to_Wallet = true;
                    txt_amount.setText(txt_amount.getText() + "\n" + String.format("Added to wallet : ₹ %.2f" , Double.parseDouble(add_to_wallet)));
                }

                txt_amount_to_pay.setText("Amount to pay : " + Amount);
                txt_tax.setText("Gold Tax   : " + Tax + "%");
                txt_goldquantity.setText(String.format("Quantity : %.3f g", Double.parseDouble(Gold_Quantity)));

                /*txt_goldquantity.setText("Gold Price : " + bundle.getString("amount"));
                Gold_Price = bundle.getString("gold_price");
                Tax= bundle.getString("tax");
                Amount = bundle.getString("amount");
                Quantity = bundle.getString("quantity");*/
            }

            try {
                MainMenuActivity.mainbar.setText("Confirm Order");
                View v = view;
                v.setBackgroundResource(android.R.color.transparent);
                desccount=v.findViewById(R.id.desccount);
                desccount.setText("0/80");
                desc = (EditText) view.findViewById(R.id.desc);
                desc.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        desccount.setText(  desc.getText().toString().length() + "/80");
                    }
                });

                ImagePath = (TextView) view.findViewById(R.id.ImagePath);
                output = (ImageView) view.findViewById(R.id.output);
                img_status = view.findViewById(R.id.img_status);
                image_qr_code = (ImageView) view.findViewById(R.id.image_qr_code);
                Picasso.get().load(Payment_QR).placeholder(R.drawable.profile_image_placeholder).into(image_qr_code);
                output.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                view.findViewById(R.id.txt_download).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Functions.check_permissions(getActivity())){
                            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(Payment_QR);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            String File_Name = "QR_" + System.currentTimeMillis() + ".jpeg";
                            request.setTitle(File_Name);
                            request.setDescription(File_Name);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File_Name);
                            reference = manager.enqueue(request);
                            if(reference>0) {
                                Toaster.toast("Downloading Started");
                            }
                            getContext().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                            /* PRDownloader.initialize(getActivity().getApplicationContext());
                            DownloadRequest prDownloader= PRDownloader.download(Payment_QR, Functions.getDownloadsFolder(context), "Payment QR.png")
                                    .build()
                                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                        @Override
                                        public void onStartOrResume() {

                                        }
                                    })
                                    .setOnPauseListener(new OnPauseListener() {
                                        @Override
                                        public void onPause() {

                                        }
                                    })
                                    .setOnCancelListener(new OnCancelListener() {
                                        @Override
                                        public void onCancel() {

                                        }
                                    })
                                    .setOnProgressListener(new OnProgressListener() {
                                        @Override
                                        public void onProgress(Progress progress) {

                                            int prog=(int)((progress.currentBytes*100)/progress.totalBytes);
                                            Functions.Show_loading_progress(prog);

                                        }
                                    });

                            prDownloader.start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    Toaster.toast("QR Code Downloaded");
                                }

                                @Override
                                public void onError(Error error) {
                                    //Toast.makeText(context, "Error"  + error , Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show();
                                    Functions.cancel_determinent_loader();
                                }


                            });*/
                        }
                    }
                });
                view.findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(iswalletFullApplied) {
                            Call_api_Addorder("Wallet Applied","");
                        } else {
                            if (ImagePath.getText().toString().equalsIgnoreCase("")) {
                                Toaster.toast("Select Image");
                            } else if (desc.getText().toString().equalsIgnoreCase("")) {
                                Toaster.toast("Enter Payment Reference No");
                            } else {
                                Call_api_Addorder("Wallet Applied | " + desc.getText().toString(), ImagePath.getText().toString());
                            }
                        }


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            //broadcast_option();
        } catch (Exception e){}
        Perform_search();
        return view;
    }
    long reference;
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (reference == id) {
                Toaster.toast("Downloading Completed");
            }
        }
    };
    private void broadcast_option() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(getContext(), Variables.return_newbroadcastoption, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        JSONArray respo=response.getJSONArray("msg");
                        if(respo.length()>0){
                            JSONObject data = respo.getJSONObject(0);
                            String msg = data.optString("go_live");
                            String block = data.optString("block");
                            String is_verified = data.optString("is_verified");
                            String is_profile_updated = data.optString("is_profile_updated");
                            if (block!="") {
                                if (block.equalsIgnoreCase("1")) {
                                    Toaster.toast("Your Account is blocked, Kindly contact administration.");
                                    SharedPreferences.Editor editor= Variables.sharedPreferences.edit();
                                    editor.putString(Variables.u_id,"");
                                    editor.putString(Variables.u_name,"");
                                    editor.putString(Variables.u_pic,"");
                                    editor.putBoolean(Variables.islogin,false);
                                    editor.commit();
                                    Intent intent = new Intent(getActivity(), Blocked_A.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                }
                            }

                            if (is_verified==null)
                                is_verified="";
                            if (is_verified.trim().equalsIgnoreCase(""))
                                is_verified="0";
                            if (!is_verified.equalsIgnoreCase("2") ) {
                                if (is_verified.equalsIgnoreCase("0") || is_verified.equalsIgnoreCase("3")) {
                                    Intent intent = new Intent(getActivity(), Request_Verification_F.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                } else {
                                    Intent intent = new Intent(getActivity(), Processing_A.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return;
                                }
                            }

                            if(is_profile_updated==null) is_profile_updated="0";

                            if (!is_profile_updated.equalsIgnoreCase("1")) {

                                Edit_Profile_F edit_profile_f = new Edit_Profile_F(new Fragment_Callback() {
                                    @Override
                                    public void Responce(Bundle bundle) {
                                    }
                                });
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                                transaction.replace(R.id.nav_host_fragment, edit_profile_f).commit();
                            }

                            if (msg!="") {
                                if (msg.equalsIgnoreCase("Yes")) {
                                    Variables.GoliveStatus="Yes";
                                    Variables.sharedPreferences.getString(Variables.go_live,"No");
                                } else {
                                    Variables.GoliveStatus="No";
                                }
                            } else {
                                Variables.GoliveStatus="No";
                                //iconView.setImageResource(R.drawable.round_profile_image_placeholder);
                            }
                        }
                    } else {
                        Variables.GoliveStatus="No";
                    }
                    Variables.sharedPreferences.edit().putString(Variables.go_live,Variables.GoliveStatus);
                } catch (Exception e) {
                    //iconView.setImageResource(R.drawable.round_profile_image_placeholder);
                    Variables.GoliveStatus="No";
                    Variables.sharedPreferences.edit().putString(Variables.go_live,Variables.GoliveStatus);
                    e.printStackTrace();
                }
            }
        });
    }

    public void Call_api_Addorder(String Description,String Imagepath) {
        if(Variables.Cart_Scheme_id==null){
            img_status.setImageResource(R.drawable.error);
            txtview_msg.setText("Something went wrong.");
            view_responce.setVisibility(View.VISIBLE);
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("fb_id", Variables.user_id);
                jsonObject.put("scheme_id", Variables.Cart_Scheme_id);
                jsonObject.put("order_id", Order_id);
                jsonObject.put("trans_id", "Trans#" + System.currentTimeMillis());
                jsonObject.put("total_amount", Amount);
                jsonObject.put("amount", Double.parseDouble(Amount) - Double.parseDouble(add_to_wallet) );
                jsonObject.put("gold_price", Gold_Price);
                String GoldAmount = String.valueOf(Double.parseDouble(Gold_Quantity) * 1000);
                jsonObject.put("gold_qty", GoldAmount);
                jsonObject.put("type", myType);
                if(is_Added_to_Wallet){ Description = "Add to Wallet | " + Description; }
                jsonObject.put("remarks", Description);
                jsonObject.put("image", Imagepath);

                if(iswalletapplied){
                    jsonObject.put("is_wallet_app", "1");
                    if(iswalletFullApplied) {
                        jsonObject.put("status", "0");
                        jsonObject.put("wallet_amt", Amount);
                    } else {
                        jsonObject.put("status", "0");
                        jsonObject.put("wallet_amt", wallet);
                    }
                } else {
                    jsonObject.put("is_wallet_app", "0");
                    jsonObject.put("status", "0");
                    jsonObject.put("wallet_amt", "0");
                }
                if(is_Added_to_Wallet){
                    jsonObject.put("add_to_wallet", add_to_wallet);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Functions.Show_loader(getContext(), false, false);
            ApiRequest.Call_Api(getContext(), Variables.add_order_transactions, jsonObject, new Callback() {
                @Override
                public void Responce(String resp) {
                    output.setBackgroundResource(R.drawable.image_placeholder);
                    desc.setText("");
                    ImagePath.setText("");
                    imageFilePath="";
                    Functions.cancel_loader();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(resp);
                        String code = jsonObject.optString("code");
                        if(code.equalsIgnoreCase("200")){
                            img_status.setImageResource(R.drawable.done);
                        } else {
                            img_status.setImageResource(R.drawable.error);
                        }
                        txtview_msg.setText(jsonObject.optString("msg"));
                        view_responce.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void parse_data(String resp) {
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                //Call_Api_For_image();
                //                JSONArray msg=jsonObject.getJSONArray("msg");
                //                ArrayList<History_Get_Set> temp_list=new ArrayList<>();
                //                for (int i=0;i<msg.length();i++) {
                //                    JSONObject data = msg.getJSONObject(i);
                //
                //                    History_Get_Set item = new History_Get_Set();
                //                    item.id = data.optString("id");
                //                    item.emp_id = data.optString("emp_id");
                //                    item.machine_Status = data.optString("machine_status");
                //                }
            } else {
                Toaster.toast("Result not Updated");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Perform_search(){
        try {
            if(menu_pager!=null){
                menu_pager.removeAllViews();
            }
            //Set_tabs();
        } catch (Exception e){

        }
    }

    public void Set_tabs(){
        try {
            adapter = new ViewPagerAdapter(getChildFragmentManager());
            menu_pager = (ViewPager) view.findViewById(R.id.viewpager);
            menu_pager.setOffscreenPageLimit(5);
            tabLayout = (TabLayout) view.findViewById(R.id.tabs);

            //adapter.addFrag(new new_moment_F(),"My moments");
            menu_pager.setAdapter(adapter);
            tabLayout.setupWithViewPager(menu_pager);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (Functions.check_permissions(a))
                        openCameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    if (Functions.check_permissions(a)) {
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
        if (pictureIntent.resolveActivity(a.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), a.getPackageName() + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 1);
            }
        }
    }

    String imageFilePath;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
                //Matrix matrix = new Matrix();
//                try {
//                    ExifInterface exif = new ExifInterface(imageFilePath);
//                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                    switch (orientation) {
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            matrix.postRotate(90);
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            matrix.postRotate(180);
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_270:
//                            matrix.postRotate(270);
//                            break;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
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

    String image_bas64;

    private void beginCrop(Uri source) {
        CropImage.activity(source).start(this.a);
    }

    private void handleCrop(Uri userimageuri) {
        try {
            InputStream imageStream = null;
            try {
                imageStream = context.getContentResolver().openInputStream(userimageuri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

            String path = userimageuri.getPath();
            Matrix matrix = new Matrix();
            android.media.ExifInterface exif = null;
            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                *//*try {
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
                }*//*
            }*/

            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);

            image_bas64 = Functions.Bitmap_to_base64(a, rotatedBitmap);

            Call_Api_For_image(userimageuri);
        } catch (Exception e){
            Toaster.toast(e.toString());
            e.printStackTrace();
        }
    }

    public void Call_Api_For_image(Uri userimageuri) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
            JSONObject file_data = new JSONObject();
            file_data.put("file_data", image_bas64);
            parameters.put("image", file_data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Functions.Show_loader(context, false, false);
        ApiRequest.Call_Api(context, Variables.uploadTransactionReference, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");
                    if (code.equals("200")) {
                        output.setImageURI(userimageuri);
                        ImagePath.setText(msg);
                        Toast.makeText(getActivity(), "Image Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    public void call_Api_MyBalance(){
        Functions.Call_Api_For_WalletBalance(getActivity(),Variables.user_id,
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) { }

                    @Override
                    public void OnSuccess(String responce) {
                        try {
                            JSONObject jsonObject = new JSONObject(responce);
                            String code = jsonObject.optString("code");
                            if (code.equals("200")) {
                                JSONArray msg = jsonObject.getJSONArray("msg");
                                if (msg.length() <= 0) {
                                    wallet = "0";
                                } else {
                                    for (int i = 0; i < msg.length(); i++) {
                                        JSONObject data = msg.getJSONObject(i);
                                        wallet = String.format("%.2f",Double.parseDouble(data.getString("balance")));
                                    }
                                }
                                if(wallet.trim().equalsIgnoreCase("0.00"))
                                    apply_chk.setVisibility(View.GONE);
                                else
                                    apply_chk.setVisibility(View.VISIBLE);
                            }
                            txt_current_wallet.setText(wallet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String responce) {}

                });
    }
}
