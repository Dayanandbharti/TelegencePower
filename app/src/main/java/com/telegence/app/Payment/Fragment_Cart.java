package com.telegence.app.Payment;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.Scheme_Get_Set;
import com.telegence.app.SimpleClasses.API_CallBack;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.SimpleClasses.Webview_F;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Cart extends RootFragment {

    View view;
    Context context;
    ProgressBar pbar;
    RelativeLayout no_data_layout,service_not_available;
    ImageView image;
    TextView gold_amount,txt_gold_price,txtname,txtvalue,txtproduct_details,txtproduct_description,txtterms;
    EditText edt_price;
    CheckBox terms ;
    String Gold_Price;
    String Tax = "3";
    String Order_id="";
    String Max_qty="0";
    String type = "open";
    String WalletBal = "0";

    public Fragment_Cart() {
        // Required empty public constructor
    }

    public Fragment_Cart(String SchemeId ) {
        Variables.Cart_Scheme_id = SchemeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_cart, container, false);
        context=getContext();
        MainMenuActivity.mainbar.setText("My Cart");
        Call_Api_For_get_Gold_Price();
        Call_Api_For_get_Plan_value();
        no_data_layout = view.findViewById(R.id.no_data_layout);
        service_not_available = view.findViewById(R.id.service_not_available);
        terms = view.findViewById(R.id.terms);
        Order_id = "Order#" + System.currentTimeMillis();
        type = "open";
        Max_qty = "0";
        Bundle bundle = getArguments();
        if(bundle!=null) {
            Order_id = bundle.getString("order_id");
            type = bundle.getString("type");
            Max_qty = bundle.getString("max_qty");
        }

        image=view.findViewById(R.id.coin);
        if(Variables.Cart_Scheme_id!=null && !Variables.Cart_Scheme_id.trim().equalsIgnoreCase(""))
            no_data_layout.setVisibility(View.GONE);
        else
            no_data_layout.setVisibility(View.VISIBLE);

        service_not_available.setVisibility(View.GONE);
        txt_gold_price =view.findViewById(R.id.txt_gold_price);
        gold_amount =view.findViewById(R.id.gold_amount);
        txtname = view.findViewById(R.id.txtname);
        edt_price = view.findViewById(R.id.edt_price);
        edt_price.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    Double amount = Double.parseDouble(edt_price.getText().toString().trim());
                    Double GoldAmount = amount/Double.parseDouble(Gold_Price);
                    if (Double.parseDouble(Max_qty.trim()) < (GoldAmount * 1000)) {
                        Double required = (Double.parseDouble(Max_qty.trim()) / 1000) * Double.parseDouble(Gold_Price);
                        gold_amount.setText(String.format("Physical Quantity of Gold : %.3f g(mg) \n₹ %.2f is adjust to your wallet", Double.parseDouble(Max_qty.trim())/1000,amount-required));
                    } else {
                        gold_amount.setText(String.format("Physical Quantity of Gold : %.3f g(mg)", GoldAmount));
                    }
                } else {
                    gold_amount.setText("Physical Quantity of Gold : 0.000 g(mg)");
                }
            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,int before, int count) { }
        });
        txtvalue = view.findViewById(R.id.txtvalue);
        txtproduct_details = view.findViewById(R.id.txtproduct_details);
        txtproduct_description = view.findViewById(R.id.txtproduct_description);
        txtterms = view.findViewById(R.id.txtterms);
        view.findViewById(R.id.paynow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isvalid()){
                    Double amount = Double.parseDouble(edt_price.getText().toString().trim());
                    Double GoldAmount = (Double) (amount * ((Double) 100.0 / (Integer.parseInt(Tax) + 100.0))) /  Double.parseDouble(Gold_Price);
                    do_transaction(GoldAmount,false,0.0);
                    /* if (Double.parseDouble(Max_qty.trim()) < (GoldAmount * 1000)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                        builder.setTitle("Confirmation Dialog");
                        builder.setMessage(String.format("Entered Amount is greater than the required(₹ %.2f g(mg)).\nDo you want to proceed? ", (Double.parseDouble(Max_qty.trim()) / 1000) * Double.parseDouble(Gold_Price)));
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Double required = (Double.parseDouble(Max_qty.trim()) / 1000) * Double.parseDouble(Gold_Price);
                                do_transaction(Double.parseDouble(Max_qty.trim())/1000,true,amount-required);
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
                    } else {

                    }*/

                }
            }
        });
        txt_gold_price.setText("Today's Gold Price\n" + "₹ "+ Gold_Price + "/gm");

        txtterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Webview_F webview_f = new Webview_F();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                Bundle bundle = new Bundle();
                bundle.putString("url", Variables.main_domain + "Terms_nd_conditions.htm");
                bundle.putString("title", "Terms and Conditions");
                webview_f.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment, webview_f).commit();
            }
        });
        pbar=view.findViewById(R.id.pbar);
        if(Variables.Cart_Scheme_id!=null && !Variables.Cart_Scheme_id.equalsIgnoreCase(""))
            Call_Api_For_get_Plan_value();
        else
            no_data_layout.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        terms.setChecked(false);
        edt_price.setText("");
    }

    private void do_transaction(Double GoldAmount,boolean is_wallet_applied,Double add_to_wallet){
        try{
            gold_amount.setText(String.format("Physical Quantity of Gold : %.3f g(mg)", GoldAmount));
            Confirm_Order confirm_order = new Confirm_Order();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            Bundle bundle = new Bundle();
            bundle.putString("amount", edt_price.getText().toString().trim());
            bundle.putString("gold_price", Gold_Price);
            bundle.putString("tax", Tax);
            bundle.putString("add_to_wallet", String.valueOf(add_to_wallet));
            bundle.putString("quantity", GoldAmount.toString());
            bundle.putString("order_id", Order_id);
            bundle.putString("type", type);
            confirm_order.setArguments(bundle);
            transaction.addToBackStack(null);
            transaction.replace(R.id.nav_host_fragment, confirm_order).commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Boolean isvalid(){
//        if(!terms.isChecked()){
//            Toaster.toast("Please Accept Terms and Conditions.");
//            return false;
//        } else
        if(edt_price.getText().toString().trim().equalsIgnoreCase("")){
            Toaster.toast("Please Enter Amount.");
            return false;
        } else {
            return true;
//            if(type.equalsIgnoreCase("open") && Integer.parseInt(edt_price.getText().toString().trim()) < Integer.parseInt(txtvalue.getText().toString().trim()) * 100){
//                Toaster.toast("Please Enter Amount greater than ₹ " + String.format("%.2f",Integer.parseInt(txtvalue.getText().toString().trim()) * 100.0));
//                return false;
//            } else if(type.equalsIgnoreCase("add") && Integer.parseInt(edt_price.getText().toString().trim()) < 100){
//                Toaster.toast("Please Enter Amount greater than ₹100." );
//                return false;
//            } else {
//                return true;
//            }
        }
    }

    public void call_Api_MyBalance(){
        Functions.Call_Api_For_MyBalance(getActivity(),Variables.user_id,
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
                                    WalletBal = "0";
                                } else {
                                    for (int i = 0; i < msg.length(); i++) {
                                        JSONObject data = msg.getJSONObject(i);
                                        WalletBal = data.getString("balance");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnFail(String responce) {}

                });
    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Gold_Price() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_goldprice, parameters, new Callback() {
            @Override
            public void Responce(String resp){
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code=jsonObject.optString("code");
                    if(code.equals("200")){
                        JSONArray bannerArray=jsonObject.getJSONArray("msg");
                        pbar.setVisibility(View.GONE);
                        if(bannerArray.length()==0){
                            service_not_available.setVisibility(View.VISIBLE);
                        } else {
                            JSONObject data = bannerArray.optJSONObject(0); //Top
                            Gold_Price = data.optString("gold_price");
                            Double gold_price_wid_tax = Double.parseDouble(data.optString("gold_price"));
                            Double Gold_Price_without_gst = gold_price_wid_tax * (100.0/103.0);
                            Double Gold_Price_without_tax = Gold_Price_without_gst * (100.0/102.0);
                            txt_gold_price.setText("Today's Gold Price\n" + "₹ "+ String.format("%.2f",Gold_Price_without_tax) + "/g");
                            Tax = data.optString("tax");
                            service_not_available.setVisibility(View.GONE);
                            no_data_layout.setVisibility(View.GONE);
                        }
                    } else {
                        service_not_available.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    service_not_available.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }
    private void Call_Api_For_get_Plan_value() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("scheme_id",Variables.Cart_Scheme_id);
            parameters.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_scheme, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_plan_data(resp);
            }
        });
    }

    public void Parse_plan_data(String responce) {
        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray bannerArray=jsonObject.getJSONArray("msg");
                pbar.setVisibility(View.GONE);
                if(bannerArray.length()==0) {
                    no_data_layout.setVisibility(View.VISIBLE);
                } else {
                    JSONObject data = bannerArray.optJSONObject(0); //Top
                    txtname.setText(data.optString("d_name"));
                    txtvalue.setText(data.optString("d_value"));
                    if(type.equalsIgnoreCase("open"))
                        Max_qty = String.valueOf(Double.parseDouble(data.optString("d_value")) * 1000);
                    txtproduct_details.setText(data.optString("product_details"));
                    txtproduct_description.setText(data.optString("product_description"));
                    String res= data.optString("d_image");;
                    if(res!=null && !res.equalsIgnoreCase(""))
                            Picasso.get().load(data.optString("d_image")).placeholder(R.color.gray).into(image);

                    no_data_layout.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                no_data_layout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            service_not_available.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    private void OpenProfile(final Scheme_Get_Set item) {
        /*Profile_F profile_f = new Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

            }
        });

        View view=getActivity().findViewById(R.id.MainMenuFragment);
        if(view!=null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            Bundle args = new Bundle();
            args.putString("user_id",item.fb_id);
            args.putString("user_name",item.first_name+" "+item.last_name);
            args.putString("user_pic",item.profile_pic);
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, profile_f).commit();
        }
        else {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            Bundle args = new Bundle();
            args.putString("user_id",item.fb_id);
            args.putString("user_name",item.first_name+" "+item.last_name);
            args.putString("user_pic",item.profile_pic);
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.following_layout, profile_f).commit();
        }*/


    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
