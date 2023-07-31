package com.telegence.app.Authentication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.telegence.app.R;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.SimpleClasses.Variables;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Logout extends RootFragment {

    View view;
    Context context;


    public Fragment_Logout() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.blank, container, false);
        context=getContext();
        SharedPreferences.Editor editor= Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id,"");
        editor.putString(Variables.u_name,"");
        editor.putString(Variables.u_pic,"");
        editor.putBoolean(Variables.islogin,false);
        editor.commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), Login_A.class));

        return view;
    }

}
