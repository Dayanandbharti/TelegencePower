package com.telegence.app.TELEGENCE.TEAM.Money_Request;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

import java.util.ArrayList;
import java.util.Collections;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Team_Money_Request extends RootFragment {

    View view;
    Context context;
    RecyclerView inbox_list;
    ArrayList<Team_Money_Requests_Get_Set> inbox_arraylist;
    DatabaseReference root_ref;
    Team_Money_Request_Adapter inbox_adapter;
    ProgressBar pbar;
    AdView adView;
    ValueEventListener eventListener2;
    Query inbox_query;
    boolean isview_created=false;

    public Team_Money_Request() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Team_Money_Request(Fragment_Callback fragment_callback) {
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_team_moeny_req, container, false);
        context =getContext();
        MainMenuActivity.mainbar.setText("Money Requests");
        pbar =view.findViewById(R.id.pbar);
        inbox_list =view.findViewById(R.id.inboxlist);
        inbox_arraylist =new ArrayList<>();
        inbox_list = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inbox_list.setLayoutManager(layout);
        inbox_list.setHasFixedSize(false);
        inbox_adapter =new Team_Money_Request_Adapter(context, inbox_arraylist, new Team_Money_Request_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Team_Money_Requests_Get_Set item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                builder.setTitle("Confirmation Dialog");
                builder.setMessage("Do you want to Update?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, but close the dialog
                        Dialog redeemdialog = new Dialog(getActivity(),R.style.live_room_dialog_center_in_window) ;
                        redeemdialog.setContentView(R.layout.fragment_approve_money_req);

                        EditText mobile = redeemdialog.findViewById(R.id.txtMobileNo);
                        mobile.setText(item.type);

                        EditText name = redeemdialog.findViewById(R.id.firstname_edit);
                        name.setText(item.msg);

                        redeemdialog.findViewById(R.id.approve_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if(name.getText().toString().equalsIgnoreCase("")){
//                                    Toaster.toast("Enter Reason");
//                                    redeemdialog.show();
//                                } else
                                    if(mobile.getText().toString().equalsIgnoreCase("")){
                                    Toaster.toast("Enter Amount");
                                    redeemdialog.show();
                                } else if(Integer.parseInt(mobile.getText().toString())<=0) {
                                    Toaster.toast("Enter Amount greater than 0.");
                                    redeemdialog.show();
                                } else {
                                    Call_api_for_update(mobile.getText().toString(),item.id,name.getText().toString(),"approved");
                                    redeemdialog.dismiss();
                                }
                            }
                        });
                        redeemdialog.findViewById(R.id.disapprove).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if(name.getText().toString().equalsIgnoreCase("")){
//                                    Toaster.toast("Enter Reason");
//                                    redeemdialog.show();
//                                } else { }
                                Call_api_for_update(item.type,item.id,name.getText().toString(),"declined");
                                redeemdialog.dismiss();
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


                //Doubts_Responses_F(item.id);
                /*Doubts_Responses_F webview_f = new Doubts_Responses_F(item.id, new Fragment_Callback() {
                    @Override
                    public void Responce(Bundle bundle) {
                        Call_api();
                    }
                });
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment, webview_f).commit();*/
            }
        }, new Team_Money_Request_Adapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Team_Money_Requests_Get_Set item) {

            }
        });

        view.findViewById(R.id.txt_add_money_req).setOnClickListener(v -> {
            Add_Money_Req_F fragment = new Add_Money_Req_F(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Money Requests");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        inbox_list.setAdapter(inbox_adapter);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Functions.hideSoftKeyboard(getActivity());

            }
        });
        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });
        isview_created =true;
        //getData();
        Call_api();
        return view;
    }

    public void Call_api_for_update(String Amount,String ID, String Message,String Status) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", ID);
            jsonObject.put("amount", Amount);
            jsonObject.put("status", Status);
            jsonObject.put("details", Message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.Show_loader(getContext(), false, false);
        ApiRequest.Call_Api(getContext(), Variables.updateMoneyRequest, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(resp);
                    String Response = jsonObject.optString("msg");
                    Toaster.toast(Response);
                    Call_api();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adView = view.findViewById(R.id.bannerad);
        AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);
    }

    public void getData() {

        pbar.setVisibility(View.VISIBLE);

        inbox_query=root_ref.child("Inbox").child(Variables.user_id).orderByChild("date");
        eventListener2=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Team_Money_Requests_Get_Set model = ds.getValue(Team_Money_Requests_Get_Set.class);
                    model.setId(ds.getKey());
                    inbox_arraylist.add(model);
                }

                pbar.setVisibility(View.GONE);

                if (inbox_arraylist.isEmpty())
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                    Collections.reverse(inbox_arraylist);
                    inbox_adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener2);


    }

    public void Call_api(){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getNewTeamMoneyRequests, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                //swiperefresh.setRefreshing(false);
                pbar.setVisibility(View.GONE);
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
                ArrayList<Team_Money_Requests_Get_Set> temp_list=new ArrayList<>();
                for (int i=0;i<msg.length();i++) {
                    JSONObject data = msg.getJSONObject(i);
                    Team_Money_Requests_Get_Set item = new Team_Money_Requests_Get_Set();
                    item.id = data.optString("id");
                    item.posted_id = data.optString("fb_id");
                    item.posted_name = data.optString("referral_fb_id");
                    item.name = data.optString("scheme_name");
                    item.msg = data.optString("scheme_value");
                    item.type = data.optString("amount");
                    item.block = data.optString("scheme_id");
                    item.date = data.optString("created");
                    item.isread = data.optString("status");
                    item.status = data.optString("status");
                    temp_list.add(item);
                }

                inbox_arraylist.clear();
                inbox_arraylist.addAll(temp_list);

                /*if(datalist.size()<=0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                }*/
                inbox_adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(inbox_query!=null)
        inbox_query.removeEventListener(eventListener2);
    }

}
