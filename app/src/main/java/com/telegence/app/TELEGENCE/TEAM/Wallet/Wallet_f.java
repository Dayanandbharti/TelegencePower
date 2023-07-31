package com.telegence.app.TELEGENCE.TEAM.Wallet;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.API_CallBack;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.TELEGENCE.TEAM.Money_Request.Add_Money_Req_F;
import com.telegence.app.TELEGENCE.TEAM.Money_Request.Send_Money_F;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet_f extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    Wallet_transactions_Adapter adapter;
    RecyclerView recyclerView;
    TextView txt_current_wallet;

    ArrayList<Wallet_Get_Set> datalist;

    public Wallet_f() {
        // Required empty public constructor
    }
    Fragment_Callback fragment_callback;
    public Wallet_f(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_wallet, container, false);
        context=getContext();
        MainMenuActivity.mainbar.setText("Wallet");
        datalist=new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        txt_current_wallet = view.findViewById(R.id.txt_current_wallet);
        view.findViewById(R.id.txtamount).setVisibility(View.GONE);
        TextView ratehead =  view.findViewById(R.id.ratehead);
        ratehead.setText("Amount\n(₹)");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter=new Wallet_transactions_Adapter(context, datalist);
        recyclerView.setAdapter(adapter);

        //view.findViewById(R.id.inbox_btn).setOnClickListener(this);

/*
        swiperefresh=view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call_api();
            }
        });
*/

        if(Variables.sharedPreferences.getBoolean(Variables.islogin,false))
        {
            Call_api();
            call_Api_MyBalance();
        }

        view.findViewById(R.id.txt_add_money_req).setOnClickListener(v -> {
            Add_Money_Req_F fragment = new Add_Money_Req_F(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Wallet");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });
        view.findViewById(R.id.txt_send_req).setOnClickListener(v -> {
            Send_Money_F fragment = new Send_Money_F(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Wallet");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       /* adView = view.findViewById(R.id.bannerad);
        if(!Variables.is_remove_ads) {
            AdRequest adRequest = new AdRequest.Builder().build();
            //adView.loadAd(adRequest);
        }else {
            adView.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inbox_btn:
                Open_inbox_F();
                break;
        }
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
                                    txt_current_wallet.setText("0");
                                } else {
                                    for (int i = 0; i < msg.length(); i++) {
                                        JSONObject data = msg.getJSONObject(i);
                                        txt_current_wallet.setText(String.format("%.2f",Double.parseDouble(data.getString("balance"))));
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
    private void Open_inbox_F() {
        /*Inbox_F inbox_f = new Inbox_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, inbox_f).commit();*/
    }

    private void OpenWatchVideo(Wallet_Get_Set item) {
        /*Intent intent=new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("video_id", item.id);
        startActivity(intent);*/
    }

    public void Open_productDescription(Wallet_Get_Set item){
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

    public void Call_api(){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getwallettransaction, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                //swiperefresh.setRefreshing(false);
                parse_data(resp);
            }
        });
    }

    public void parse_data(String resp){
        try {
            JSONObject jsonObject=new JSONObject(resp);
            String code = jsonObject.optString("code");
            if(code.equals("200")) {
                JSONArray msg=jsonObject.getJSONArray("msg");
                ArrayList<Wallet_Get_Set> temp_list=new ArrayList<>();
                for (int i=0;i<msg.length();i++) {
                    JSONObject data=msg.getJSONObject(i);
                    Wallet_Get_Set item=new Wallet_Get_Set();
                    item.id=data.optString("id");
                    item.fb_id=data.optString("fb_id");
                    item.referral_fb_id=data.optString("referral_fb_id");
                    item.amount=data.optString("amount");
                    item.scheme_id=data.optString("scheme_id");
                    item.scheme_name=data.optString("scheme_name");
                    item.scheme_value=data.optString("scheme_value");
                    item.created=data.optString("created");
                    item.status=data.optString("status");
                    temp_list.add(item);
                }
                datalist.clear();
                datalist.addAll(temp_list);

                /*if(datalist.size()<=0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                }*/
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fragment_callback!=null){
            fragment_callback.Responce(new Bundle());
        }
    }

}
