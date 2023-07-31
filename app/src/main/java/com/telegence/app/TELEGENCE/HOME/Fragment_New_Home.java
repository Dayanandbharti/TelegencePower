package com.telegence.app.TELEGENCE.HOME;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.TELEGENCE.TEAM.Expenses.ADD_EXPENSES.Add_Expense;
import com.telegence.app.TELEGENCE.ATTENDANCE.Fragment_Attendance_Main;
import com.telegence.app.TELEGENCE.TEAM.Feedback.Add_FeedBack;
import com.telegence.app.TELEGENCE.TEAM.Fragment_Team_Main;
import com.telegence.app.TELEGENCE.TEAM.Wallet.Wallet_f;

import org.json.JSONObject;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_New_Home extends RootFragment {

    View view;
    Context context;
    TextView txt_name,txt_id,txt_projects,txt_project_details;
    ImageView user_image;
    LinearLayout view_team;
    public Fragment_New_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_new_home, container, false);
        view.setClickable(true);
        context=getContext();
        MainMenuActivity.mainbar.setText("Dashboard");

        view_team = view.findViewById(R.id.view_team);
        view_team.setVisibility(View.GONE);
        txt_name = view.findViewById(R.id.txt_name);
        txt_id = view.findViewById(R.id.txt_id);
        txt_projects = view.findViewById(R.id.txt_projects);
        txt_project_details = view.findViewById(R.id.txt_project_details);
        user_image = view.findViewById(R.id.user_image);

        txt_name.setText(Variables.sharedPreferences.getString(Variables.f_name,"")+" "+Variables.sharedPreferences.getString(Variables.l_name,""));
        txt_id.setText(" ID: " + Variables.user_id);
        if(Variables.user_pic!=null && !Variables.user_pic.equalsIgnoreCase(""))
            Picasso.get().load(Variables.user_pic).resize(100,100)
                    .placeholder(R.drawable.profile_image_placeholder).into(user_image);

        view.findViewById(R.id.view_attendance).setOnClickListener(v -> {
            Fragment_Attendance_Main fragment = new Fragment_Attendance_Main(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Dashboard");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_team).setOnClickListener(v -> {
            Fragment_Team_Main fragment = new Fragment_Team_Main(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Dashboard");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_wallet).setOnClickListener(v -> {
            Wallet_f fragment = new Wallet_f(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Dashboard");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_feedback).setOnClickListener(v -> {
            Add_FeedBack fragment = new Add_FeedBack(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Dashboard");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_add_exp).setOnClickListener(v -> {
            Add_Expense fragment = new Add_Expense(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Dashboard");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });
        Call_api_for_projects();
        return view;
    }

    public void Call_api_for_projects() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.am_I_a_leader, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_ProjectData(resp);
            }
        });
    }
    public void parse_ProjectData(String resp) {
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                String msg = jsonObject.optString("msg");
                if (msg.length() <= 0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                } else  {
                    if (Integer.parseInt(msg)>0) {
                        view_team.setVisibility(View.VISIBLE);
                    }
                }
            } else{
                Toaster.toast(jsonObject.optString("code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
