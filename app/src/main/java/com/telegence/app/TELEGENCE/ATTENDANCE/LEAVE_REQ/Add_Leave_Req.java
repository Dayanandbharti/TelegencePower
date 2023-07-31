package com.telegence.app.TELEGENCE.ATTENDANCE.LEAVE_REQ;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Leave_Req extends RootFragment {

    View view;
    Context context;
    ProgressBar pbar;
    RelativeLayout no_data_layout;
    ImageView image;
    TextView txtaddress,name,mob;
    EditText message;
    Button btnsendquery;

    public Add_Leave_Req() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Add_Leave_Req(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_leave_req, container, false);
        context = getContext();
        MainMenuActivity.mainbar.setText("Add Leave Request");
        view.setClickable(true);
        no_data_layout = view.findViewById(R.id.no_data_layout);
        image=view.findViewById(R.id.image);
        txtaddress=view.findViewById(R.id.txtphone);

        name = view.findViewById(R.id.edt_name);
        mob = view.findViewById(R.id.edt_phn);
        message = view.findViewById(R.id.edt_message);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.DialogTheme,fromdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.DialogTheme,todate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        view.findViewById(R.id.btnsendquery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Name");
                } else if(mob.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Mobile");
                } else if(message.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Message");
                } else {
                    Call_api();
                }
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


    DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear = monthOfYear + 1;
            mob.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
        }
    };


    public void Call_api() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("mobile", mob.getText().toString().trim());
            jsonObject.put("email", "");
            jsonObject.put("message", message.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.add_leave_Req, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    name.setText("");
                    mob.setText("");
                    //mailid.setText("");
                    message.setText("");
                    Toaster.toast(jsonObject.optString("msg"));
                } catch (JSONException e) {
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
