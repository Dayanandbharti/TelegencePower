package com.telegence.app.Attendance.Mark_Attendance;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.Orders.Redeem_get_set;
import com.telegence.app.Payment.Fragment_Cart;
import com.telegence.app.R;
import com.telegence.app.Scheme_Get_Set;
import com.telegence.app.SimpleClasses.API_CallBack;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.Transactions_Get_Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Mark_Attendance extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    Attendance_Adapter adapter;
    RecyclerView recyclerView;

    ArrayList<Attendance_Get_Set> datalist;

    SwipeRefreshLayout swiperefresh;
    LinearLayout address_details;
    RadioGroup rgshipmenttype;
    RadioButton rdbcoin,rdbjewellery,rdbdrop,rdbpickup;
    EditText name,mobile,address,city,state,pincode;
    String follow_status="0";
    AdView adView;
    ArrayList mode_of_payment;
    Spinner spn_mode_of_payment;

    int mode_of_payment_opt=0;
    public Fragment_Mark_Attendance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_markattendance, container, false);
        context=getContext();
        if(MainMenuActivity.mainbar!=null){
            MainMenuActivity.mainbar.setText("Orders");
        }

        spn_mode_of_payment = view.findViewById(R.id.spn_courses);

        datalist = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter=new Attendance_Adapter(context, datalist, new Attendance_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Attendance_Get_Set item) {

                switch (view.getId()) {
                    case R.id.txt_add:
                        if(item.remaining.equalsIgnoreCase("0") && !item.order_status.equalsIgnoreCase("0")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                            builder.setTitle("Confirmation Dialog");
                            builder.setMessage("Do you want to edit?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, but close the dialog
                                    get_redeem_details(item);
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
//                            Variables.Cart_Scheme_id = item.scheme.schemes_id;
//                            Fragment_Cart order_f = new Fragment_Cart();
//                            Bundle bundle = new Bundle();
//
//                            bundle.putString("order_id", item.Order_id);
//                            bundle.putString("type", "add");
//                            bundle.putString("max_amt", item.remaining);
//                            order_f.setArguments(bundle);
//                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
//                            transaction.addToBackStack(null);
//                            transaction.replace(R.id.nav_host_fragment, order_f).commit();
                        }
                        else if(item.remaining.equalsIgnoreCase("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                            builder.setTitle("Confirmation Dialog");
                            builder.setMessage("Do you want to redeem?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, but close the dialog
                                    Dialog redeemdialog = new Dialog(getActivity(),R.style.live_room_dialog_center_in_window) ;
                                    redeemdialog.setContentView(R.layout.fragment_redeem);

                                    address_details = redeemdialog.findViewById(R.id.address_details);
                                    rgshipmenttype = redeemdialog.findViewById(R.id.rgshipmenttype);
                                    rdbcoin = redeemdialog.findViewById(R.id.rdb_coin);
                                    rdbjewellery = redeemdialog.findViewById(R.id.rdb_jewellery);

                                    rdbdrop = redeemdialog.findViewById(R.id.rdb_deliver);
                                    rdbpickup = redeemdialog.findViewById(R.id.rdb_pickup);


                                    name = redeemdialog.findViewById(R.id.firstname_edit);
                                    mobile = redeemdialog.findViewById(R.id.txtMobileNo);
                                    address = redeemdialog.findViewById(R.id.address_edit);
                                    city = redeemdialog.findViewById(R.id.city_edit);
                                    state = redeemdialog.findViewById(R.id.state_edit);
                                    pincode = redeemdialog.findViewById(R.id.pincode_edit);

                                    rgshipmenttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                                    {
                                        public void onCheckedChanged(RadioGroup group, int checkedId)
                                        {
                                            // This will get the radiobutton that has changed in its check state
                                            address.setText("");
                                            city.setText("");
                                            pincode.setText("");
                                            state.setText("");
                                            if (rdbdrop.isChecked())
                                            {
                                                address_details.setVisibility(View.VISIBLE);
                                            } else {
                                                address_details.setVisibility(View.GONE);
                                            }
                                        }
                                    });





                                    redeemdialog.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            boolean isvalid = false;
                                            if(name.getText().toString().equalsIgnoreCase("")){
                                                Toaster.toast("Enter Name");
                                                redeemdialog.show();
                                            } else if(mobile.getText().toString().equalsIgnoreCase("")){
                                                Toaster.toast("Enter Mobile");
                                                redeemdialog.show();
                                            } else {
                                                if(rdbdrop.isChecked()) {
                                                    isvalid = false;
                                                    if(address.getText().toString().equalsIgnoreCase("")){
                                                        Toaster.toast("Enter Address");
                                                        redeemdialog.show();
                                                    } else if(city.getText().toString().equalsIgnoreCase("")){
                                                        Toaster.toast("Enter City");
                                                        redeemdialog.show();
                                                    } else if(state.getText().toString().equalsIgnoreCase("")){
                                                        Toaster.toast("Enter State");
                                                        redeemdialog.show();
                                                    } else if(pincode.getText().toString().equalsIgnoreCase("")){
                                                        Toaster.toast("Enter Pincode");
                                                        redeemdialog.show();
                                                    } else {
                                                        isvalid = true;
                                                    }
                                                } else{
                                                    isvalid = true;
                                                }
                                            }

                                           if(isvalid) {
                                               Redeem_get_set redeem_get_set = new Redeem_get_set();
                                               redeem_get_set.redeem_name = name.getText().toString().trim();
                                               redeem_get_set.redeem_mobile = mobile.getText().toString().trim();
                                               if(rdbdrop.isChecked()){
                                                   redeem_get_set.redeem_address = address.getText().toString().trim();
                                                   redeem_get_set.redeem_city = city.getText().toString().trim();
                                                   redeem_get_set.redeem_pincode = pincode.getText().toString().trim();
                                                   redeem_get_set.redeem_state = state.getText().toString().trim();
                                               } else {
                                                   redeem_get_set.redeem_address = "";
                                                   redeem_get_set.redeem_city = "";
                                                   redeem_get_set.redeem_pincode = "";
                                                   redeem_get_set.redeem_state = "";
                                               }

                                               if(rdbpickup.isChecked()) redeem_get_set.redeem_type = "Pickup";
                                               else redeem_get_set.redeem_type = "Drop";
                                               if(rdbcoin.isChecked()) redeem_get_set.redeem_wish = "Coin";
                                               else redeem_get_set.redeem_wish = "Jewellery";

                                                /*redeem_get_set.redeem_address = address.getText(
                                                ).toString().trim();
                                                redeem_get_set.redeem_address = address.getText().toString().trim();*/
                                               Call_api_for_update(item,"redeem",redeem_get_set);
                                               redeemdialog.dismiss();
                                           }
                                        }
                                    });
                                    redeemdialog.show();
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

//                            Variables.Cart_Scheme_id = item.scheme.schemes_id;
//                            Fragment_Cart order_f = new Fragment_Cart();
//                            Bundle bundle = new Bundle();
//
//                            bundle.putString("order_id", item.Order_id);
//                            bundle.putString("type", "add");
//                            bundle.putString("max_amt", item.remaining);
//                            order_f.setArguments(bundle);
//                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
//                            transaction.addToBackStack(null);
//                            transaction.replace(R.id.nav_host_fragment, order_f).commit();
                        }
                        else {
                            Variables.Cart_Scheme_id = item.scheme.schemes_id;
                            Fragment_Cart order_f = new Fragment_Cart();
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", item.Order_id);
                            bundle.putString("type", "add");
                            bundle.putString("max_qty", item.remaining);
                            order_f.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.nav_host_fragment, order_f).commit();
                        }
                    break;
//                    default:
//                        Open_productDescription(item);
//                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.inbox_btn).setOnClickListener(this);

        swiperefresh=view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call_api();
            }
        });

        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false))
            Call_api();
        return view;
    }

    public void Follow_unFollow_User(String effectedID){

        final String send_status;
        if(follow_status.equals("0")){
            send_status="1";
        }else {
            send_status="0";
        }

        Functions.Call_Api_For_Follow_or_unFollow(getActivity(),
                Variables.sharedPreferences.getString(Variables.u_id,""),
                effectedID,
                send_status,
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {


                    }

                    @Override
                    public void OnSuccess(String responce) {

                        if(send_status.equals("1")){
                            //follow_unfollow_btn.setBackgroundResource(R.drawable.ic_friend);
                            //follow_unfollow_btn.setText("");
                            //follow_unfollow_btn.setBackgroundResource(R.drawable.unfollow_icon);
                            follow_status="1";
                        }
                        else if(send_status.equals("0")){
                            //follow_unfollow_btn.setBackgroundResource(R.drawable.ic_follow);
                            //follow_unfollow_btn.setText("");
                            /*follow_unfollow_btn.setText("Follow");*/
                            //follow_unfollow_btn.setBackgroundResource(R.drawable.follow_icon);
                            follow_status="0";
                        }
                        Call_api();
                    }

                    @Override
                    public void OnFail(String responce) {

                    }

                });
    }

    @Override
    public void onStart() {
        super.onStart();
        adView = view.findViewById(R.id.bannerad);
        if(!Variables.is_remove_ads) {
            AdRequest adRequest = new AdRequest.Builder().build();
            //adView.loadAd(adRequest);
        }else {
            adView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(view!=null && Variables.Reload_my_notification){
            Variables.Reload_my_notification=false;
            Call_api();
        }
    }
    public void Call_api_for_update(Attendance_Get_Set item,String type,Redeem_get_set redeem_get_set) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
            jsonObject.put("scheme_id",item.scheme.schemes_id);
            jsonObject.put("order_id", item.Order_id);
            jsonObject.put("trans_id", "Trans#" + System.currentTimeMillis());
            jsonObject.put("total_amount", String.valueOf(-((Double.parseDouble(item.coompleted)/1000) * Double.parseDouble(item.gold_price))));
            jsonObject.put("amount", String.valueOf(-((Double.parseDouble(item.coompleted)/1000) * Double.parseDouble(item.gold_price))));
            jsonObject.put("gold_price", item.gold_price);
            jsonObject.put("gold_qty", String.valueOf(-Double.parseDouble(item.coompleted)));
            jsonObject.put("type", type);
            jsonObject.put("remarks", "User want to " + type );
            jsonObject.put("image", "");
            jsonObject.put("status", "0");

            jsonObject.put("is_wallet_app", "0");
            jsonObject.put("wallet_amt", "0");

            jsonObject.put("redeem_wish", redeem_get_set.redeem_wish);
            jsonObject.put("redeem_type", redeem_get_set.redeem_type);
            jsonObject.put("redeem_name", redeem_get_set.redeem_name);
            jsonObject.put("redeem_mobile", redeem_get_set.redeem_mobile);
            jsonObject.put("redeem_address", redeem_get_set.redeem_address);
            jsonObject.put("redeem_state", redeem_get_set.redeem_state);
            jsonObject.put("redeem_city", redeem_get_set.redeem_city);
            jsonObject.put("redeem_pincode", redeem_get_set.redeem_pincode);

            jsonObject.put("add_to_wallet", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.add_order_transactions, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    String Response = jsonObject.optString("msg");
                    show_resp(Response);
                    Call_api();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void show_resp( String Response){
        AlertDialog.Builder builder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        builder.setTitle("Information Dialog");
        builder.setMessage(Response);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Call_api() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getMyCourse, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                swiperefresh.setRefreshing(false);
                parse_data(resp);
            }
        });

    }

    private void get_redeem_details(Attendance_Get_Set item){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("order_id", item.Order_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(context, Variables.get_redeem_details, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    JSONArray msg = response.optJSONArray("msg");

                    Dialog redeemdialog = new Dialog(getActivity(),R.style.live_room_dialog_center_in_window) ;
                    redeemdialog.setContentView(R.layout.fragment_redeem);

                    address_details = redeemdialog.findViewById(R.id.address_details);
                    rgshipmenttype = redeemdialog.findViewById(R.id.rgshipmenttype);
                    rdbcoin = redeemdialog.findViewById(R.id.rdb_coin);
                    rdbjewellery = redeemdialog.findViewById(R.id.rdb_jewellery);

                    rdbdrop = redeemdialog.findViewById(R.id.rdb_deliver);
                    rdbpickup = redeemdialog.findViewById(R.id.rdb_pickup);



                    name = redeemdialog.findViewById(R.id.firstname_edit);
                    mobile = redeemdialog.findViewById(R.id.txtMobileNo);
                    address = redeemdialog.findViewById(R.id.address_edit);
                    city = redeemdialog.findViewById(R.id.city_edit);
                    state = redeemdialog.findViewById(R.id.state_edit);
                    pincode = redeemdialog.findViewById(R.id.pincode_edit);


                    for(int i=0;i<msg.length();i++){
                        JSONObject jsonObject = msg.getJSONObject(i);
                        name.setText(jsonObject.optString("name"));
                        mobile.setText(jsonObject.optString("mobile"));
                        address.setText(jsonObject.optString("address"));
                        city.setText(jsonObject.optString("state"));
                        state.setText(jsonObject.optString("city"));
                        pincode.setText(jsonObject.optString("pincode"));
                        if(jsonObject.optString("wish").equalsIgnoreCase("Coin")){
                            rdbcoin.setChecked(true);
                        } else{
                            rdbjewellery.setChecked(true);
                        }

                        if(jsonObject.optString("type").equalsIgnoreCase("Drop")){
                            rdbdrop.setChecked(true);
                        } else{
                            rdbpickup.setChecked(true);
                        }

                        if (rdbdrop.isChecked()) {
                            address_details.setVisibility(View.VISIBLE);
                        } else {
                            address_details.setVisibility(View.GONE);
                        }
                    }

                    rgshipmenttype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId)
                        {
                            // This will get the radiobutton that has changed in its check state
                            address.setText("");
                            city.setText("");
                            pincode.setText("");
                            state.setText("");
                            if (rdbdrop.isChecked())
                            {
                                address_details.setVisibility(View.VISIBLE);
                            } else {
                                address_details.setVisibility(View.GONE);
                            }
                        }
                    });

                    redeemdialog.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isvalid = false;
                            if(name.getText().toString().equalsIgnoreCase("")){
                                Toaster.toast("Enter Name");
                                redeemdialog.show();
                            } else if(mobile.getText().toString().equalsIgnoreCase("")){
                                Toaster.toast("Enter Mobile");
                                redeemdialog.show();
                            } else {
                                if(rdbdrop.isChecked()) {
                                    isvalid = false;
                                    if(address.getText().toString().equalsIgnoreCase("")){
                                        Toaster.toast("Enter Address");
                                        redeemdialog.show();
                                    } else if(city.getText().toString().equalsIgnoreCase("")){
                                        Toaster.toast("Enter City");
                                        redeemdialog.show();
                                    } else if(state.getText().toString().equalsIgnoreCase("")){
                                        Toaster.toast("Enter State");
                                        redeemdialog.show();
                                    } else if(pincode.getText().toString().equalsIgnoreCase("")){
                                        Toaster.toast("Enter Pincode");
                                        redeemdialog.show();
                                    } else {
                                        isvalid = true;
                                    }
                                } else{
                                    isvalid = true;
                                }
                            }

                            if(isvalid) {
                                Redeem_get_set redeem_get_set = new Redeem_get_set();
                                redeem_get_set.redeem_name = name.getText().toString().trim();
                                redeem_get_set.redeem_mobile = mobile.getText().toString().trim();
                                if(rdbdrop.isChecked()){
                                    redeem_get_set.redeem_address = address.getText().toString().trim();
                                    redeem_get_set.redeem_city = city.getText().toString().trim();
                                    redeem_get_set.redeem_pincode = pincode.getText().toString().trim();
                                    redeem_get_set.redeem_state = state.getText().toString().trim();
                                } else {
                                    redeem_get_set.redeem_address = "";
                                    redeem_get_set.redeem_city = "";
                                    redeem_get_set.redeem_pincode = "";
                                    redeem_get_set.redeem_state = "";
                                }

                                if(rdbpickup.isChecked()) redeem_get_set.redeem_type = "Pickup";
                                else redeem_get_set.redeem_type = "Drop";
                                if(rdbcoin.isChecked()) redeem_get_set.redeem_wish = "Coin";
                                else redeem_get_set.redeem_wish = "Jewellery";

                                                /*redeem_get_set.redeem_address = address.getText(
                                                ).toString().trim();
                                                redeem_get_set.redeem_address = address.getText().toString().trim();*/
                                Call_api_for_update(item,"redeem_update",redeem_get_set);
                                redeemdialog.dismiss();
                            }
                        }
                    });
                    redeemdialog.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parse_data(String resp){
        try {
            JSONObject jsonObject=new JSONObject(resp);
            String code = jsonObject.optString("code");
            if(code.equals("200")) {
                JSONArray msg=jsonObject.getJSONArray("msg");
                ArrayList<Attendance_Get_Set> temp_list=new ArrayList<>();
                mode_of_payment =new ArrayList<>();


                for (int i=0;i<msg.length();i++) {
                    JSONObject data=msg.getJSONObject(i);
                    JSONObject scheme_details=data.optJSONObject("scheme");

                    Attendance_Get_Set item=new Attendance_Get_Set();
                    item.scheme = new Scheme_Get_Set();
                    item.transactions = new ArrayList<>();
                    item.gold_price=data.optString("gold_price");
                    item.Order_id=data.optString("id");
                    item.scheme.schemes_id=scheme_details.optString("id");
                    item.scheme.scheme_name=scheme_details.optString("d_name");
                    mode_of_payment.add(scheme_details.optString("d_name"));
                    item.scheme.schemes_res=scheme_details.optString("d_image");
                    item.scheme.product_details=scheme_details.optString("product_details");
                    item.scheme.product_description=scheme_details.optString("product_description");
                    JSONArray value_data=data.optJSONArray("transactions");
                    for (int j=0;j<value_data.length();j++) {
                        Transactions_Get_Set transactions_get_set = new Transactions_Get_Set();
                        JSONObject transactions = value_data.getJSONObject(j);
                        transactions_get_set.id = transactions.optString("id");
                        transactions_get_set.trans_id = transactions.optString("trans_id");
                        transactions_get_set.trans_amount = transactions.optString("amount");
                        transactions_get_set.gold_price = transactions.optString("gold_price");
                        transactions_get_set.gold_qty = transactions.optString("gold_qty");
                        transactions_get_set.trans_status = transactions.optString("status");
                        transactions_get_set.trans_created = transactions.optString("trans_created");
                        item.transactions.add(transactions_get_set);
                    }
                    item.target=data.optString("target");
                    item.coompleted=data.optString("completed");
                    item.Invested_Amt=data.optString("invested_amt");
                    item.remaining=data.optString("remaining");
                    item.created=data.optString("created");
                    item.order_status=data.optString("status");
                    item.mainstatus=data.optString("mainstatus");
                    temp_list.add(item);
                }
                ArrayAdapter<String> courseadapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_text,mode_of_payment);
                courseadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_mode_of_payment.setAdapter(courseadapter);
                spn_mode_of_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mode_of_payment_opt = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                datalist.clear();
                datalist.addAll(temp_list);

                if(datalist.size()<=0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                }
                else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inbox_btn:
                Open_inbox_F();
                break;
        }
    }

    private void Open_inbox_F() {
        /*Inbox_F inbox_f = new Inbox_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, inbox_f).commit();*/
    }

    private void OpenWatchVideo(Attendance_Get_Set item) {
        /*Intent intent=new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("video_id", item.id);
        startActivity(intent);*/
    }

    public void Open_productDescription(Attendance_Get_Set item){
        /*Profile_F profile_f = new Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        Bundle args = new Bundle();
        args.putString("order_id", item.Order_id);
        profile_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, profile_f).commit();*/
    }



}
