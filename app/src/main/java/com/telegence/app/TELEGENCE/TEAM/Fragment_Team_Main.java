package com.telegence.app.TELEGENCE.TEAM;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.TELEGENCE.TEAM.Attendance.Team_Attendance;
import com.telegence.app.TELEGENCE.TEAM.Expenses.Team_Expenses;
import com.telegence.app.TELEGENCE.TEAM.Feedback.Team_Feedback;
import com.telegence.app.TELEGENCE.TEAM.Money_Request.Team_Money_Request;
import com.telegence.app.TELEGENCE.TEAM.Team_Details.Team_Details;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Team_Main extends RootFragment {

    View view;
    Context context;
    TextView txt_name,txt_id,txt_projects,txt_project_details;
    ImageView user_image;

    public Fragment_Team_Main() {
        // Required empty public constructor
    }

    Fragment_Callback fragment_callback;
    public Fragment_Team_Main(Fragment_Callback fragment_callback) {
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
        view= inflater.inflate(R.layout.frag_team_main, container, false);
        view.setClickable(true);
        context=getContext();
            MainMenuActivity.mainbar.setText("Team");

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
            Team_Attendance fragment = new Team_Attendance(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_team).setOnClickListener(v -> {
            Team_Details fragment = new Team_Details(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_expenses).setOnClickListener(v -> {
            Team_Expenses fragment = new Team_Expenses(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_feedback).setOnClickListener(v -> {
            Team_Feedback fragment = new Team_Feedback(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });

        view.findViewById(R.id.view_money_req).setOnClickListener(v -> {
            Team_Money_Request fragment = new Team_Money_Request(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    MainMenuActivity.mainbar.setText("Team");
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment).commit();
        });


        return view;
    }

}
