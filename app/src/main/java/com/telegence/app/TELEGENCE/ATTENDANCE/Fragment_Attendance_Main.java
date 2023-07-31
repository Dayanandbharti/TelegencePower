package com.telegence.app.TELEGENCE.ATTENDANCE;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.TELEGENCE.ATTENDANCE.CALENDER.Fragment_Calender;
import com.telegence.app.TELEGENCE.ATTENDANCE.LEAVE_REQ.Add_Leave_Req;

import org.json.JSONException;
import org.json.JSONObject;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Attendance_Main extends RootFragment {

    View view;
    Context context;
    TextView txt_name,txt_id,txt_project_details;
    ImageView user_image;

    public Fragment_Attendance_Main() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Fragment_Attendance_Main(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_attendance_main, container, false);
        view.setClickable(true);
        context=getContext();
        MainMenuActivity.mainbar.setText("Attendance");

        txt_name = view.findViewById(R.id.txt_name);
        txt_id = view.findViewById(R.id.txt_id);


        txt_project_details = view.findViewById(R.id.txt_project_details);
        user_image = view.findViewById(R.id.user_image);

        txt_name.setText(Variables.sharedPreferences.getString(Variables.f_name,"")+" "+Variables.sharedPreferences.getString(Variables.l_name,""));
        txt_id.setText(" ID: " + Variables.user_id);
        if(Variables.user_pic!=null && !Variables.user_pic.equalsIgnoreCase(""))
            Picasso.get().load(Variables.user_pic).resize(100,100)
                    .placeholder(R.drawable.profile_image_placeholder).into(user_image);

        view.findViewById(R.id.view_check_in).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
            builder.setTitle("Confirmation Dialog");
            builder.setMessage("Are you sure you want to Check In?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Call_api();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        view.findViewById(R.id.view_check_out).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
            builder.setTitle("Confirmation Dialog");
            builder.setMessage("Are you sure you want to Check Out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Call_api_check_out();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        });

        view.findViewById(R.id.view_leave_req).setOnClickListener(v -> {
            Add_Leave_Req fragment_cart = new Add_Leave_Req(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Attendance");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment_cart).commit();
        });

        view.findViewById(R.id.view_attendance).setOnClickListener(v -> {
            Fragment_Calender fragment_cart = new Fragment_Calender(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Attendance");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment_cart).commit();
        });

        return view;
    }

    public void Call_api() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.check_in, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    Toaster.toast(jsonObject.optString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Call_api_check_out() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.check_out, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    Toaster.toast(jsonObject.optString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fragment_callback!=null){
            fragment_callback.Responce(new Bundle());
        }
    }
}

