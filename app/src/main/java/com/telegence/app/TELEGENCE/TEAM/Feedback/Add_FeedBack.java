package com.telegence.app.TELEGENCE.TEAM.Feedback;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_FeedBack extends RootFragment {

    View view;
    Context context;
    ProgressBar pbar;
    RelativeLayout no_data_layout;
    ImageView image;
    TextView txtaddress,txtphone,txtemail;
    EditText name,mob,mailid,message;
    Button btnsendquery;
    Spinner spn_project;

    public Add_FeedBack() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Add_FeedBack(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fragment_callback!=null){
            fragment_callback.Responce(new Bundle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_performance, container, false);
        context = getContext();

        spn_project =view.findViewById(R.id.spn_project);
        MainMenuActivity.mainbar.setText("Add Feedback");
        no_data_layout = view.findViewById(R.id.no_data_layout);
        image=view.findViewById(R.id.image);
        txtaddress=view.findViewById(R.id.txtphone);
        txtphone=view.findViewById(R.id.txtphone);
        txtemail=view.findViewById(R.id.txtemail);

        name = view.findViewById(R.id.edt_name);
        mob = view.findViewById(R.id.edt_phn);
        mailid = view.findViewById(R.id.edt_mail);
        message = view.findViewById(R.id.edt_message);

        view.findViewById(R.id.btnsendquery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Name");
                } else if(mob.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Mobile");
                } else if(mailid.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Email Id");
                } else if(message.getText().toString().equalsIgnoreCase("")){
                    Toaster.toast("Enter Message");
                } else {
                    Call_api();
                }
            }
        });

        pbar=view.findViewById(R.id.pbar);
//        Call_Api_For_get_Plan_value();
        return view;
    }

    public void Call_api() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("mobile", mob.getText().toString().trim());
            jsonObject.put("email", txtemail.getText().toString().trim());
            jsonObject.put("message", message.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.add_query, jsonObject, new Callback() {
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
