package com.telegence.app.TELEGENCE.TEAM.Expenses;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.squareup.picasso.Picasso;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.See_Full_Image_F;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.SimpleClasses.Webview_F;
import com.telegence.app.TELEGENCE.TEAM.Expenses.ADD_EXPENSES.Add_Expense;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Team_Expenses extends RootFragment {

    View view;
    Context context;
    RecyclerView inbox_list;
    ArrayList<Team_Expensess_Get_Set> inbox_arraylist;
    DatabaseReference root_ref;
    Team_Expenses_Adapter inbox_adapter;
    ProgressBar pbar;
    AdView adView;
    ValueEventListener eventListener2;
    Query inbox_query;
    boolean isview_created=false;

    public Team_Expenses() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Team_Expenses(Fragment_Callback fragment_callback) {
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
        view = inflater.inflate(R.layout.fragment_team_expense, container, false);
        context =getContext();
        MainMenuActivity.mainbar.setText("Team Expenses");
        pbar =view.findViewById(R.id.pbar);
        inbox_list =view.findViewById(R.id.inboxlist);
        inbox_arraylist =new ArrayList<>();
        inbox_list = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inbox_list.setLayoutManager(layout);
        inbox_list.setHasFixedSize(false);
        inbox_adapter =new Team_Expenses_Adapter(context, inbox_arraylist, new Team_Expenses_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Team_Expensess_Get_Set item) {

                Dialog redeemdialog = new Dialog(getActivity(),R.style.live_room_dialog_center_in_window) ;
                redeemdialog.setContentView(R.layout.fragment_approve_expense);

                TextView txt_Details = redeemdialog.findViewById(R.id.txt_Details);
                txt_Details.setText("Expense on " + item.block + " at " + item.date);

                EditText mobile = redeemdialog.findViewById(R.id.txtMobileNo);
                mobile.setText(item.msg);

                EditText name = redeemdialog.findViewById(R.id.firstname_edit);
                name.setText(item.posted_name);

                ImageView bill = redeemdialog.findViewById(R.id.bill);
                if(item.posted_id!=null && !item.posted_id.equals("")) {
                    Picasso.get().load(item.posted_id).placeholder(R.color.transparent).into(bill);
                    bill.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            See_Full_Image_F see_image_f = new See_Full_Image_F();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                            Bundle args = new Bundle();
                            args.putSerializable("image_url", item.posted_id);
                            see_image_f.setArguments(args);
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
                            redeemdialog.dismiss();
                        }
                    });
                }
                Button button = redeemdialog.findViewById(R.id.approve_btn);
                button.setVisibility(View.GONE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                /*if(name.getText().toString().equalsIgnoreCase("")){
                                    Toaster.toast("Enter Reason");
                                    redeemdialog.show();
                                } else*/
                        if(mobile.getText().toString().equalsIgnoreCase("")){
                            Toaster.toast("Enter Amount");
                            redeemdialog.show();
                        } else if(Integer.parseInt(mobile.getText().toString())<=0){
                            Toaster.toast("Enter Amount greater than 0.");
                            redeemdialog.show();
                        } else {
                            Call_api_for_update(mobile.getText().toString(),item.id,"","approved");
                            redeemdialog.dismiss();
                        }
                    }
                });

                button = redeemdialog.findViewById(R.id.disapprove);
                button.setVisibility(View.GONE);
                redeemdialog.findViewById(R.id.disapprove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(name.getText().toString().equalsIgnoreCase("")){
                            Toaster.toast("Enter Reason");
                            redeemdialog.show();
                        } else {
                            Call_api_for_update(item.msg,item.id,"","declined");
                            redeemdialog.dismiss();
                        }
                    }
                });
                redeemdialog.show();

//                AlertDialog.Builder builder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
//                builder.setTitle("Confirmation Dialog");
//                builder.setMessage("Do you want to Update?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing, but close the dialog
//                        Dialog redeemdialog = new Dialog(getActivity(),R.style.live_room_dialog_center_in_window) ;
//                        redeemdialog.setContentView(R.layout.fragment_approve_expense);
//
//                        TextView txt_Details = redeemdialog.findViewById(R.id.txt_Details);
//                        txt_Details.setText("Expense on " + item.block + " at " + item.date);
//
//                        EditText mobile = redeemdialog.findViewById(R.id.txtMobileNo);
//                        mobile.setText(item.msg);
//
//                        EditText name = redeemdialog.findViewById(R.id.firstname_edit);
//                        name.setText(item.posted_name);
//
//                        ImageView bill = redeemdialog.findViewById(R.id.bill);
//                        if(item.posted_id!=null && !item.posted_id.equals("")) {
//                            Picasso.get().load(item.posted_id).placeholder(R.color.transparent).into(bill);
//                        }
//                        Button button = redeemdialog.findViewById(R.id.approve_btn);
//                        button.setVisibility(View.GONE);
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                /*if(name.getText().toString().equalsIgnoreCase("")){
//                                    Toaster.toast("Enter Reason");
//                                    redeemdialog.show();
//                                } else*/
//                                    if(mobile.getText().toString().equalsIgnoreCase("")){
//                                    Toaster.toast("Enter Amount");
//                                    redeemdialog.show();
//                                } else if(Integer.parseInt(mobile.getText().toString())<=0){
//                                    Toaster.toast("Enter Amount greater than 0.");
//                                    redeemdialog.show();
//                                } else {
//                                    Call_api_for_update(mobile.getText().toString(),item.id,"","approved");
//                                    redeemdialog.dismiss();
//                                }
//                            }
//                        });
//                        redeemdialog.findViewById(R.id.disapprove).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if(name.getText().toString().equalsIgnoreCase("")){
//                                    Toaster.toast("Enter Reason");
//                                    redeemdialog.show();
//                                } else {
//                                    Call_api_for_update(item.msg,item.id,"","declined");
//                                    redeemdialog.dismiss();
//                                }
//                            }
//                        });
//                        redeemdialog.show();
//                    }
//                });
//
//                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();
            }
        }, new Team_Expenses_Adapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Team_Expensess_Get_Set item) {

            }
        });

        view.findViewById(R.id.txt_add_expenses).setOnClickListener(v -> {
            Add_Expense fragment = new Add_Expense(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team Expenses");
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
        ApiRequest.Call_Api(getContext(), Variables.updateExpense, jsonObject, new Callback() {
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

                    Team_Expensess_Get_Set model = ds.getValue(Team_Expensess_Get_Set.class);
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

        ApiRequest.Call_Api(context, Variables.getExpenses, jsonObject, new Callback() {
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
                ArrayList<Team_Expensess_Get_Set> temp_list=new ArrayList<>();
                for (int i=0;i<msg.length();i++) {
                    JSONObject data = msg.getJSONObject(i);
                    Team_Expensess_Get_Set item = new Team_Expensess_Get_Set();
                    item.id = data.optString("id");
                    item.posted_id = data.optString("poster_id");
                    item.posted_name = data.optString("poster_name");
                    item.name = data.optString("name");
                    item.msg = data.optString("msg");
                    item.type = data.optString("mobile");
                    item.block = data.optString("email");
                    item.date = data.optString("date");
                    item.isread = data.optString("status");
                    item.status = data.optString("type");
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

    public void chatFragment(String name,String Link){
        Webview_F webview_f = new Webview_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        Bundle bundle = new Bundle();
        bundle.putString("url", Link);
        bundle.putString("title", name);
        webview_f.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Inbox_F, webview_f).commit();
    }

    private boolean check_ReadStoragepermission(){
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Variables.permission_Read_data );
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

}
