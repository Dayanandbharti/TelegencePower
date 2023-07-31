package com.telegence.app.TELEGENCE.TEAM.Attendance;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
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
import com.telegence.app.SimpleClasses.Webview_F;
import com.telegence.app.TELEGENCE.TEAM.Expenses.ADD_EXPENSES.Add_Expense;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Team_Attendance extends RootFragment {

    View view;
    TextView txtdate;
    Context context;
    RecyclerView inbox_list;
    ArrayList<Team_Atetndaces_Get_Set> inbox_arraylist;
    DatabaseReference root_ref;
    Team_Attendance_Adapter inbox_adapter;
    ProgressBar pbar;
    ValueEventListener eventListener2;
    Query inbox_query;
    boolean isview_created=false;

    public Team_Attendance() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Team_Attendance(Fragment_Callback fragment_callback) {
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
        view = inflater.inflate(R.layout.fragment_team_attendance, container, false);
        context =getContext();
        MainMenuActivity.mainbar.setText("Team Attendance");
        txtdate =view.findViewById(R.id.txtdate);
        pbar =view.findViewById(R.id.pbar);
        inbox_list =view.findViewById(R.id.inboxlist);
        inbox_arraylist =new ArrayList<>();
        inbox_list = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inbox_list.setLayoutManager(layout);
        inbox_list.setHasFixedSize(false);
        inbox_adapter =new Team_Attendance_Adapter(context, inbox_arraylist, new Team_Attendance_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Team_Atetndaces_Get_Set item) {
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
        }, new Team_Attendance_Adapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Team_Atetndaces_Get_Set item) {

            }
        });

        view.findViewById(R.id.txt_add_expenses).setOnClickListener(v -> {
            Add_Expense fragment = new Add_Expense(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team Attendance");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_Select_Date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.DialogTheme,fromdate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");

        txtdate.setText(simpleDateFormat1.format(new Date()));
        Call_api(simpleDateFormat.format(new Date()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        adView = view.findViewById(R.id.bannerad);
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

                    Team_Atetndaces_Get_Set model = ds.getValue(Team_Atetndaces_Get_Set.class);
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
            txtdate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            Call_api(year + "-" + monthOfYear + "-" + dayOfMonth);
        }
    };


    public void Call_api(String date){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fb_id",Variables.user_id);
            jsonObject.put("date",date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.emp_att_list, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
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
                ArrayList<Team_Atetndaces_Get_Set> temp_list=new ArrayList<>();
                for (int i=0;i<msg.length();i++) {
                    JSONObject data = msg.getJSONObject(i);
                    Team_Atetndaces_Get_Set item = new Team_Atetndaces_Get_Set();
                    item.id = data.optString("fb_id");
                    item.posted_id = data.optString("fb_id");
                    item.posted_name = data.optString("username");
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
