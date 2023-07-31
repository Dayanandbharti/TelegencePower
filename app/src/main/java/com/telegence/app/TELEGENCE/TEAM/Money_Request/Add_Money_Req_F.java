package com.telegence.app.TELEGENCE.TEAM.Money_Request;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Money_Req_F extends RootFragment implements View.OnClickListener{

    View view;
    Context context;
    Resources resources;
    View L1,L2,E1;
    TextView Balance,CoinBalance,MyCoinBalance;
    Button Exchange;
    EditText et_no_of_diamonds,edtadd_money,edtadd_remarks;
    Integer MinCoinConvert=3150,CoinConversionUnit=3,CoinConversionResult=1,MinDiamondConvert=10,DiamondConversionUnit=10,DimondConversionResult=3;
    Spinner spn_project,spn_manager;

    public static String exchange_action="Diamonds";
    public Add_Money_Req_F() {}

    Fragment_Callback fragment_callback;
    public Add_Money_Req_F(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_add_money_req, container, false);
        context=getContext();
        MainMenuActivity.mainbar.setText("Add Money Request");
        view.setClickable(true);
        edtadd_money=view.findViewById(R.id.edtadd_money);
        edtadd_remarks=view.findViewById(R.id.edtadd_remarks);
        //        edtadd_money.addTextChangedListener(new TextWatcher() {
//            Integer Amount = Integer.parseInt(edtadd_money.getText().toString().replace(",",""));
//            @Override
//            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                edtadd_money.setText(NumberFormat.getIntegerInstance().format(Amount));
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                if(edtadd_money.getText().length()==0){
//                    Amount=0;
//                } else {
//                    Amount = Integer.parseInt(edtadd_money.getText().toString().replace(",",""));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//
//            }
//        });

        spn_project =view.findViewById(R.id.spn_project);
        spn_manager =view.findViewById(R.id.spn_manager);
        et_no_of_diamonds=view.findViewById(R.id.et_no_of_diamonds);
        view.findViewById(R.id.Go_back).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        view.findViewById(R.id.txt1000).setOnClickListener(v -> {
            Integer Amount = 0;
            if(edtadd_money.getText().length()==0){
                Amount=0;
            } else {
                Amount = Integer.parseInt(edtadd_money.getText().toString().replace(",",""));
            }
            edtadd_money.setText(String.valueOf(Amount=Amount+1000));
        });
        view.findViewById(R.id.txt2000).setOnClickListener(v -> {
            Integer Amount = 0;
            if(edtadd_money.getText().length()==0){
                Amount=0;
            } else {
                Amount = Integer.parseInt(edtadd_money.getText().toString().replace(",",""));
            }
            edtadd_money.setText(String.valueOf(Amount=Amount+2000));
        });
        view.findViewById(R.id.btn_submit_Req).setOnClickListener(v -> {
            Integer Amount = 0;
            if(edtadd_money.getText().toString().equalsIgnoreCase("")){
                Toaster.toast("Please Enter Amount");
            } else if(Integer.parseInt(edtadd_money.getText().toString())<=0){
                Toaster.toast("Please Enter Amount greater than 0");
            } else if(spinnerMap==null ||  spinnerMap.size()==0){
                Toaster.toast("You are not active in any project");
            } else {
                Call_api();
            }
        });
        view.findViewById(R.id.txt5000).setOnClickListener(v -> {
            Integer Amount = 0;
            if(edtadd_money.getText().length()==0){
                Amount=0;
            } else {
                Amount = Integer.parseInt(edtadd_money.getText().toString().replace(",",""));
            }
            edtadd_money.setText(String.valueOf(Amount=Amount+5000));
        });
        Balance=view.findViewById(R.id.txtmyBalance);
        MyCoinBalance=view.findViewById(R.id.mytxtcoin_balance);
        MyCoinBalance.setText("0");
        CoinBalance=view.findViewById(R.id.txtcoin_balance);
        CoinBalance.setText("0 = 0 INR");
        Exchange=view.findViewById(R.id.btn_exchange);
        Exchange.setOnClickListener(this);
        view.findViewById(R.id.Goback).setOnClickListener(this);
        view.findViewById(R.id.wallet_btn1).setOnClickListener(this);
        view.findViewById(R.id.wallet_btn2).setOnClickListener(this);
        view.findViewById(R.id.historyLayout).setOnClickListener(this);
        view.findViewById(R.id.diamond_page2).setOnClickListener(this);

        L1=view.findViewById(R.id.diamond_page);
        E1=view.findViewById(R.id.exchange_view);
        L2=view.findViewById(R.id.coins_page);

        L1.setVisibility(View.VISIBLE);
        L2.setVisibility(View.GONE);

        /*ImageView coins_img3 = (ImageView)view.findViewById(R.id.coins_img3);
        coins_img3.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.wallet_coins_dashboard, null));*/

        ImageView coins_img1 = (ImageView)view.findViewById(R.id.coins_img1);
        coins_img1.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.exchange_diamonds, null));

        ImageView coins_img2 = (ImageView)view.findViewById(R.id.coins_img2);
        coins_img2.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.exchange_rewards, null));
        Call_api_for_projects();
        Call_api_for_managers();
        //startPayment(1,1.0);
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Goback:
                getActivity().onBackPressed();
                break;
        }
    }

    HashMap<Integer,String> spinnerMap=null;

    public void Call_api_for_projects() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getallProjects, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_ProjectData(resp);
            }
        });
    }
    public void parse_ProjectData(String resp) {
        try {
            spinnerMap = new HashMap< Integer,String>();
            JSONObject jsonObject = new JSONObject(resp);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msg = jsonObject.getJSONArray("msg");
                if (msg.length() <= 0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else  {
                    String[] spinnerArray = new String[msg.length()];

                    for (int i = 0; i < msg.length(); i++) {
                        JSONObject data = msg.getJSONObject(i);
                        spinnerMap.put(i,data.getString("project_id"));
                        spinnerArray[i]=data.getString("project_name");
                    }
                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(context,R.layout.spinner_text, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_project.setAdapter(adapter);
                }
            } else{
                Toaster.toast(jsonObject.optString("code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            jsonObject.put("fb_id", Variables.user_id);
            jsonObject.put("name", Variables.sharedPreferences.getString(Variables.f_name,"")+" "+Variables.sharedPreferences.getString(Variables.l_name,""));
            jsonObject.put("detail", edtadd_remarks.getText().toString());

            jsonObject.put("project_manager", spinner1Map.get(spn_manager.getSelectedItemPosition()));
            jsonObject.put("date", simpleDateFormat.format(new Date()));
            jsonObject.put("amount", edtadd_money.getText().toString());
            jsonObject.put("project_id", spinnerMap.get(spn_project.getSelectedItemPosition()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.add_money_request, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    edtadd_money.setText("");
                    edtadd_remarks.setText("");
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
