package com.telegence.app.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Refer_F extends RootFragment {
    View view;
    Context context;

    ArrayList<Refer_Get_Set> datalist;
    Refer_user_Adapter adapter;
    TextView txt_bank_details,refer_code;
    View qr_view;
    TextView txt_my_referrers;

    public Refer_F() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_refer, container, false);
        context=getContext();
        view.findViewById(R.id.refer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setDomainUriPrefix("https://learnmore.page.link/" )
                        .setLink(Uri.parse("https://learnmore.page.link/" + refer_code.getText().toString().trim()))
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.telegence.app").build())
                        .buildDynamicLink();
                //Toaster.toast(dynamicLinkUri.toString());
                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse(dynamicLink.getUri().toString()))
                        .buildShortDynamicLink()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();

                                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                                    waIntent.setType("text/plain");
                                    String text ="Earn *FREE* Gold Bit by Bit via Referring To your Friend/Relative\n*My Refer Code - " + refer_code.getText().toString() + "*\n*Click " + shortLink + "*" ;
                                    text="";
                                    waIntent.setPackage("com.whatsapp");
                                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                                    startActivity(Intent.createChooser(waIntent, "Share with"));
                                } else {
                                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                                    waIntent.setType("text/plain");
                                    String text ="Earn *FREE* Gold Bit by Bit via Referring To your Friend/Relative\n My Refer Code - " + refer_code.getText().toString() + "\n*Click " + dynamicLink.getUri().toString()  + "*" ;
                                    text="";
                                    waIntent.setPackage("com.whatsapp");
                                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                                    startActivity(Intent.createChooser(waIntent, "Share with"));
                                }
                            }
                        });


            }
       });
        String Message = "Earn <big>FREE</big> Gold Bit by Bit via Referring To your Friend/Relative";
        TextView msg=view.findViewById(R.id.message);
        txt_my_referrers=view.findViewById(R.id.txt_my_referrers);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        msg.startAnimation(anim);

        msg.setText(Html.fromHtml(Message));

        MainMenuActivity.mainbar.setText("Refer and Earn");
        txt_bank_details =  view.findViewById(R.id.txt_bank_details);
        qr_view =  view.findViewById(R.id.txt_bank_details);
        refer_code =  view.findViewById(R.id.refer_code);
        Call_Api_For_get_Gold_Price();
        GridView grdrefer =  view.findViewById(R.id.grd_refer);
        grdrefer.setAdapter(null);
        datalist = new ArrayList<>();
        adapter = new Refer_user_Adapter(context,datalist);
        grdrefer.setAdapter(adapter);
        Call_api();
        return view;
    }

    public void Call_api(){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getrefers, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                parse_data(resp);
            }
        });
    }

    private void Call_Api_For_get_Gold_Price() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_refercode, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code=jsonObject.optString("code");
                    if(code.equals("200")) {
                        refer_code.setText(jsonObject.optString("msg"));
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    public void parse_data(String resp){
        try {
            JSONObject jsonObject=new JSONObject(resp);
            String code = jsonObject.optString("code");
            if(code.equals("200")) {
                JSONArray msg=jsonObject.getJSONArray("msg");
                ArrayList<Refer_Get_Set> temp_list=new ArrayList<>();
                txt_my_referrers.setText(String.valueOf(msg.length()));
                for (int i=0;i<msg.length();i++) {
                    JSONObject data=msg.getJSONObject(i);
                    Refer_Get_Set item=new Refer_Get_Set();
                    item.fb_id=data.optString("fb_id");
                    item.first_name=data.optString("first_name");
                    item.last_name=data.optString("last_name");
                    item.profile_pic=data.optString("profile_image");
                    item.amount=data.optString("amount");
                    item.created=data.optString("created");
                    item.scheme_name=data.optString("scheme_name");
                    item.subscribed=data.optString("subscribed_scheme");
                    item.reward=data.optString("reward");
                    temp_list.add(item);
                }
                datalist.clear();
                datalist.addAll(temp_list);

                if(datalist.size()<=0) {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                }
                else {
                    view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
            e.printStackTrace();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Functions.deleteCache(context);
    }

}
