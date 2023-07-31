package com.telegence.app.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Tab_F extends RootFragment implements View.OnClickListener {
    View view;
    Context context;
    SwipeRefreshLayout swiperefresh;
    TextView txtname1,txtname2,txtmob,txtemail,txtpassword,txtaadharcardno,txtpancardno;
    ImageView image,addhar,pancard;
    EditText edt_pass;

    public Profile_Tab_F() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_profile, container, false);
        context=getContext();
        MainMenuActivity.mainbar.setText("My Profile");
        return init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_btn:
                Open_Edit_profile();
//                Dialog dialog = new Dialog(getActivity(),R.style.live_room_dialog_center_in_window) ;
//                dialog.setContentView(R.layout.confirm_pass);
//
//                edt_pass = dialog.findViewById(R.id.edt_pass);
//                dialog.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String password = edt_pass.getText().toString();
//                        if(password.equalsIgnoreCase("")){
//                            Toaster.toast("Enter Password");
//                            edt_pass.requestFocus();
//                            dialog.show();
//                        } else {
//                            Call_Api_For_confirmpass(password);
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
                break;
        }
    }

    private void Call_Api_For_confirmpass(String password) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_password, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code=jsonObject.optString("code");
                    if(code.equals("200"))
                    {
                        if(jsonObject.optString("msg").equalsIgnoreCase(password))
                        {
                            Open_Edit_profile();
                        } else {
                            Toaster.toast("Password not Verified");
                        }
                    } else {
                        Toaster.toast("Something went wrong");
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    public View init(){
        view.findViewById(R.id.edit_btn).setOnClickListener(this);

        addhar=view.findViewById(R.id.Aadhar_image);
        pancard=view.findViewById(R.id.pan_image);
        image=view.findViewById(R.id.profile_image);

        txtname1=view.findViewById(R.id.txtname1);
        txtname2=view.findViewById(R.id.txtname2);
        txtmob=view.findViewById(R.id.txtmob);
        txtemail=view.findViewById(R.id.txtmail);
        txtpassword=view.findViewById(R.id.txtpassword);
        txtaadharcardno=view.findViewById(R.id.txtaadharcardno);
        txtpancardno=view.findViewById(R.id.txtpancardno);

        Call_Api_For_Get_Profile_Details();
        return view;
    }

    public void update_profile(){
        txtname1.setText(Variables.sharedPreferences.getString(Variables.u_name,""));
        txtname1.setText(Variables.sharedPreferences.getString(Variables.f_name, "") + " " + Variables.sharedPreferences.getString(Variables.l_name, ""));
    }

    private void Call_Api_For_Get_Profile_Details() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_profile, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });
    }

    public void Parse_data(String responce){
        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                JSONObject user_info=msgArray.getJSONObject(0);
                txtname1.setText(user_info.optString("user_name"));
                txtname2.setText(user_info.optString("user_name"));
                txtmob.setText(user_info.optString("fb_id"));
                txtemail.setText(user_info.optString("bio"));

                String pass = user_info.optString("password");
                for(int i =0;i<=9;i++){
                    pass = pass.replace(String.valueOf(i),"*");
                }
                txtpassword.setText(pass);

                txtaadharcardno.setText(user_info.optString("aadhar_no"));
                txtpancardno.setText(user_info.optString("pan_no"));

                Variables.user_pic = user_info.optString("profile_pic");
                Variables.user_pic1 = user_info.optString("aadhar_pic");
                Variables.user_pic2 = user_info.optString("pan_pic");
                
                if(Variables.user_pic!=null && !Variables.user_pic.equals(""))
                    Picasso.get()
                            .load(Variables.user_pic)
                            .centerCrop()
                            .placeholder(context.getResources().getDrawable(R.drawable.round_profile_image_placeholder)).resize(1000,1000)
                            .into(image);

                if(Variables.user_pic1!=null && !Variables.user_pic1.equals(""))
                    Picasso.get()
                            .load(Variables.user_pic1)
                            .centerCrop()
                            .placeholder(context.getResources().getDrawable(R.drawable.d_round_white_background)).resize(1000,1000)
                            .into(addhar);

                if(Variables.user_pic2!=null && !Variables.user_pic2.equals(""))
                    Picasso.get()
                            .load(Variables.user_pic2)
                            .centerCrop()
                            .placeholder(context.getResources().getDrawable(R.drawable.d_round_white_background)).resize(1000,1000)
                            .into(pancard);

            } else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Open_Edit_profile(){
        Edit_Profile_F edit_profile_f = new Edit_Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {
                Call_Api_For_Get_Profile_Details();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.nav_host_fragment, edit_profile_f).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Functions.deleteCache(context);
    }

}
