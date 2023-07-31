package com.telegence.app.ImportantLinks;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.SimpleClasses.Webview_F;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportantLinks_F extends RootFragment {

    View view;
    Context context;
    RecyclerView inbox_list;

    ArrayList<ImportantLink_Get_Set> inbox_arraylist;
    DatabaseReference root_ref;

    ImportantLinks_Adapter inbox_adapter;

    ProgressBar pbar;

    boolean isview_created=false;

    public ImportantLinks_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_importantlinks, container, false);
        context=getContext();
        pbar=view.findViewById(R.id.pbar);
        inbox_list=view.findViewById(R.id.inboxlist);
        inbox_arraylist=new ArrayList<>();
        inbox_list = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inbox_list.setLayoutManager(layout);
        inbox_list.setHasFixedSize(false);
        inbox_adapter=new ImportantLinks_Adapter(context, inbox_arraylist, new ImportantLinks_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImportantLink_Get_Set item) {
                chatFragment(item.getName(),item.msg);
            }
        }, new ImportantLinks_Adapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(ImportantLink_Get_Set item) {

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
        isview_created=true;
        //getData();
        Call_api();
        return view;
    }


    AdView adView;
    @Override
    public void onStart() {
        super.onStart();
        adView = view.findViewById(R.id.bannerad);
        AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);
    }

    // on start we will get the Inbox Message of user  which is show in bottom list of third tab
    ValueEventListener eventListener2;
    Query inbox_query;
    public void getData() {

        pbar.setVisibility(View.VISIBLE);

        inbox_query=root_ref.child("Inbox").child(Variables.user_id).orderByChild("date");
        eventListener2=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inbox_arraylist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ImportantLink_Get_Set model = ds.getValue(ImportantLink_Get_Set.class);
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

        ApiRequest.Call_Api(context, Variables.getimportantlink, jsonObject, new Callback() {
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
                ArrayList<ImportantLink_Get_Set> temp_list=new ArrayList<>();
                for (int i=0;i<msg.length();i++) {
                    JSONObject data = msg.getJSONObject(i);
                    ImportantLink_Get_Set item = new ImportantLink_Get_Set();
                    item.id = data.optString("id");
                    item.msg = data.optString("link");
                    item.date = data.optString("date");
                    item.type = data.optString("name");
                    item.name = data.optString("name");
                    item.block = data.optString("name");
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

    // on stop we will remove the listener
    @Override
    public void onStop() {
        super.onStop();
        if(inbox_query!=null)
        inbox_query.removeEventListener(eventListener2);
    }



    //open the chat fragment and on item click and pass your id and the other person id in which
    //you want to chat with them and this parameter is that is we move from match list or inbox list
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



    //this method will check there is a storage permission given or not
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
