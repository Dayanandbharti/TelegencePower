package com.telegence.app.Home;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.telegence.app.Attendance.Post_Your_Doubts;
import com.telegence.app.Category_Get_Set;
import com.telegence.app.HelpfulActions_Get_Set;
import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.News_Updates_Get_Set;
import com.telegence.app.Orders.Certificate.Certificate_F;
import com.telegence.app.Payment.Fragment_Cart;
import com.telegence.app.Profile.ID_Card_F;
import com.telegence.app.R;
import com.telegence.app.Scheme_Get_Set;
import com.telegence.app.See_Full_Image_F;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Home extends RootFragment {

    View view,profile_Viewer,demo_flipper_add;
    Context context;

    ImageView[] dots;
    LinearLayout linearLayout,bottomlinearlayout;
    EditText edt_search;
    String user_id;
    Home_Scheme_Adapter adapter;
    News_Updates_Adapter news_updates_adapter;
    Help_Actions_Adapter help_actions_adapter;
    Category_Adapter category_adapter;
    Home_Courses_Adapter courses_adapter;
    RecyclerView recyclerView,recycler_View_courses,recyclerView_category,recycler_view_helpful_action,recycler_view_news;

    ArrayList<Scheme_Get_Set> scheme__datalist;
    ArrayList<Category_Get_Set> category__datalist;
    ArrayList<HelpfulActions_Get_Set> helpful__datalist;
    ArrayList<News_Updates_Get_Set> news__datalist;
    ArrayList<Banner_Get_Set> top_banner__datalist;
    ArrayList<Banner_Get_Set> bottom_banner__datalist;
    ArrayList<Courses_Get_Set> courses__datalist;
    ArrayList<Courses_Get_Set> backup_courses__datalist;
    TextView txtgold_rate;
    ImageView top_banner,bottom_banner;

    RelativeLayout no_data_layout;
    ProgressBar pbar;
    String following_or_fan="Followers";
    Fragment_Callback fragment_callback;

    ViewFlipper mFlipper, mbottomFlipper;
    ArrayList<String> spinnerIDArray,spinnerBottomIDArray;

    TextView txt_news_section;

    public Fragment_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_home, container, false);
        context=getContext();
        MainMenuActivity.mainbar.setText("Home");
        Bundle bundle=getArguments();
        if(bundle!=null){
            user_id=bundle.getString("id");
            following_or_fan=bundle.getString("from_where");
        }
        spinnerIDArray = new ArrayList<>();
        spinnerBottomIDArray = new ArrayList<>();
        scheme__datalist = new ArrayList<>();
        top_banner__datalist = new ArrayList<>();
        bottom_banner__datalist = new ArrayList<>();
        courses__datalist = new ArrayList<>();
        backup_courses__datalist = new ArrayList<>();
        category__datalist = new ArrayList<>();
        helpful__datalist = new ArrayList<>();
        news__datalist = new ArrayList<>() ;
        txt_news_section = view.findViewById(R.id.txt_news_section);
        txt_news_section.setSelected(true);

        view.findViewById(R.id.post_Section).setOnClickListener(v -> {
            Post_Your_Doubts fragment_cart = new Post_Your_Doubts();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment_cart).commit();
        });


        view.findViewById(R.id.view_certificate).setOnClickListener(v -> {
            Certificate_F fragment_cart = new Certificate_F();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_home, fragment_cart).commit();
        });

        edt_search = (EditText) view.findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                filter(s.toString());
            }
        });

        linearLayout = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        mFlipper = (ViewFlipper) view.findViewById(R.id.viewflipper);
        mFlipper.addOnLayoutChangeListener(onLayoutChangeListener_viewFlipper);

        bottomlinearlayout = (LinearLayout) view.findViewById(R.id.viewBottomPagerCountDots);
        mbottomFlipper = (ViewFlipper) view.findViewById(R.id.viewflipperbottom);
        mbottomFlipper.addOnLayoutChangeListener(oBottomnLayoutChangeListener_viewFlipper);

        view.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID_Card_F id_card_f = new ID_Card_F();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.view_home, id_card_f).commit();
            }
        });
        view.findViewById(R.id.viewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                mFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                mFlipper.showNext();
            }
        });
        view.findViewById(R.id.viewprevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipper.setInAnimation(getContext(), R.anim.in_from_left); //use either the default slide animation in sdk or create your own ones
                mFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
                mFlipper.showPrevious();
                mFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                mFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
            }
        });

        view.findViewById(R.id.viewNextbottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbottomFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                mbottomFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                mbottomFlipper.showNext();
            }
        });
        view.findViewById(R.id.viewpreviousbottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbottomFlipper.setInAnimation(getContext(), R.anim.in_from_left); //use either the default slide animation in sdk or create your own ones
                mbottomFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
                mbottomFlipper.showPrevious();
                mbottomFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                mbottomFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
            }
        });


        txtgold_rate = view.findViewById(R.id.txtgold_rate);
        txtgold_rate.setText("");
        txtgold_rate.setFocusable(true);
        txtgold_rate.requestFocus();
        txtgold_rate.setSelected(true);
        //Call_Api_For_get_Gold_Price();
        top_banner = view.findViewById(R.id.top_banner);
        bottom_banner = view.findViewById(R.id.bottom_banner);

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
        recyclerView.setHasFixedSize(true);
        adapter=new Home_Scheme_Adapter(context, following_or_fan,scheme__datalist, new Home_Scheme_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Scheme_Get_Set item) {
                switch (view.getId()){
                    case R.id.mainlayout:
                        //OpenScheme(item);
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView_category = (RecyclerView) view.findViewById(R.id.recylerview_category);
        recyclerView_category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_category.setHasFixedSize(true);
        category_adapter=new Category_Adapter(context, following_or_fan,category__datalist, new Category_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Category_Get_Set item) {
                switch (view.getId()){
                    case R.id.mainlayout:
                        //OpenScheme(item);
                        break;
                }
            }
        });
        recyclerView_category.setAdapter(category_adapter);

        recycler_View_courses = (RecyclerView) view.findViewById(R.id.recycler_View_courses);
        recycler_View_courses.setLayoutManager(new GridLayoutManager(context,1));
        recycler_View_courses.setHasFixedSize(true);
        courses_adapter = new Home_Courses_Adapter(context, following_or_fan,courses__datalist, new Home_Courses_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Courses_Get_Set item) {
                switch (view.getId()){
                    case R.id.mainlayout:
                        //  OpenScheme(item);
                        break;
                }
            }
        });
        recycler_View_courses.setAdapter(courses_adapter);

        recycler_view_helpful_action = (RecyclerView) view.findViewById(R.id.recycler_view_helpful_action);
        recycler_view_helpful_action.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler_view_helpful_action.setHasFixedSize(true);
        help_actions_adapter=new Help_Actions_Adapter(context, following_or_fan,helpful__datalist, new Help_Actions_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, HelpfulActions_Get_Set item) {
                switch (view.getId()){
                    case R.id.mainlayout:
                        //OpenScheme(item);
                        break;
                }
            }
        });
        recycler_view_helpful_action.setAdapter(help_actions_adapter);


        recycler_view_news = (RecyclerView) view.findViewById(R.id.recylerview_news_updates);
        recycler_view_news.setLayoutManager(new GridLayoutManager(context,1));
        recycler_view_news.setHasFixedSize(true);
        news_updates_adapter = new News_Updates_Adapter(context, following_or_fan,news__datalist, new News_Updates_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, News_Updates_Get_Set item) {
                switch (view.getId()){
                    case R.id.mainlayout:
                        //OpenScheme(item);
                        break;
                }
            }
        });

        recycler_view_news.setAdapter(news_updates_adapter);
        no_data_layout = view.findViewById(R.id.no_data_layout);
        pbar = view.findViewById(R.id.pbar);
        Call_Api_For_get_Home_value();
        return view;
    }


    void filter(String text){

        if(text.length()>0){
            List<Courses_Get_Set> temp=new ArrayList<>();
            courses__datalist.clear();
            courses__datalist.addAll(backup_courses__datalist);
            for(Courses_Get_Set d: courses__datalist){
                if(d.banner_action.toLowerCase().contains(text.toLowerCase())){
                    temp.add(d);
                }
            }
            //update recyclerview
            courses_adapter.update(temp);
            recycler_View_courses.setVisibility(View.VISIBLE);
        } else {
            recycler_View_courses.setVisibility(View.GONE);
            //courses_adapter.update(backup_courses__datalist);
        }

    }

    View.OnLayoutChangeListener onLayoutChangeListener_viewFlipper = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            drawPageSelectionIndicators(mFlipper.getDisplayedChild(), mFlipper.getChildCount());
        }
    };
    View.OnLayoutChangeListener oBottomnLayoutChangeListener_viewFlipper = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            drawPageottomSelectionIndicators(mbottomFlipper.getDisplayedChild(), mbottomFlipper.getChildCount());
        }
    };

    private void Call_Api_For_get_Gold_Price() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id",Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_home_gold_price, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code=jsonObject.optString("code");
                    if(code.equals("200")){
                        JSONArray bannerArray = jsonObject.getJSONArray("msg");
                        pbar.setVisibility(View.GONE);
                        if(bannerArray.length()!=0){
                            JSONObject data = bannerArray.optJSONObject(0); //Top

                            String dateSample = data.optString("created");
                            String oldFormat = "yyyy/MM/dd hh:mm:ss";
                            String newFormat = "dd/MM/yy";
                            SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
                            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

                            try{
                                Double gold_price_wid_tax = Double.parseDouble(data.optString("gold_price"));
                                Double Gold_Price_without_gst = gold_price_wid_tax * (100.0/103.0);
                                Double Gold_Price_without_tax = Gold_Price_without_gst * (100.0/102.0);
                                String marquee= sdf2.format(sdf1.parse(dateSample)) + " : " + "(24K 99.9%) ₹ "+ String.format("%.2f",Gold_Price_without_tax) + "/g (without Tax)              ";
                                txtgold_rate.setText(marquee + marquee + marquee + marquee + marquee);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            no_data_layout.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Call_Api_For_get_Home_value() {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id",user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.get_home, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                pbar.setVisibility(View.GONE);
                Parse_home_data(resp);
            }
        });
    }
    public void Parse_home_data(String responce){
        scheme__datalist.clear();
        top_banner__datalist.clear();
        bottom_banner__datalist.clear();
        courses__datalist.clear();
        backup_courses__datalist.clear();
        category__datalist.clear();
        news__datalist.clear();
        helpful__datalist.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                JSONObject profile_data = msgArray.optJSONObject(0);

                //Banner
                /*JSONObject banner_data = bannerArray.optJSONObject(0); //Top
                String res = banner_data.optString("banner_image");;
                if(res!=null && !res.equalsIgnoreCase(""))
                    Picasso.get().load(res).placeholder(R.drawable.d_round_blank_border).into(top_banner);*/
                //Banner Ends

                //Donations

                JSONArray bannerArray = profile_data.getJSONArray("banner");
                for (int scheme=0;scheme<bannerArray.length();scheme++) {
                    JSONObject scheme_data = bannerArray.optJSONObject(scheme);
                    Banner_Get_Set scheme_get_set = new Banner_Get_Set();
                    scheme_get_set.banner_id = scheme_data.optString("id");
                    scheme_get_set.banner_action = scheme_data.optString("d_name");
                    scheme_get_set.banner_res = scheme_data.optString("banner_image");
                    scheme_get_set.banner_desc = scheme_data.optString("banner_offer_short_desc");
                    scheme_get_set.banner_date = scheme_data.optString("d_value");
                    scheme_get_set.banner_date = scheme_data.optString("cdate");
                    top_banner__datalist.add(scheme_get_set);
                }

                if(!top_banner__datalist.isEmpty()){
                    fliiper();
                    mFlipper.setVisibility(View.VISIBLE);
                } else {
                    mFlipper.setVisibility(View.GONE);
                }

                bannerArray = profile_data.getJSONArray("testimonial");
                for (int scheme=0;scheme<bannerArray.length();scheme++) {
                    JSONObject scheme_data = bannerArray.optJSONObject(scheme);
                    Banner_Get_Set scheme_get_set = new Banner_Get_Set();
                    scheme_get_set.banner_id = scheme_data.optString("id");
                    scheme_get_set.banner_action = scheme_data.optString("d_name");
                    scheme_get_set.banner_res = scheme_data.optString("banner_image");
                    scheme_get_set.banner_desc = scheme_data.optString("banner_offer_short_desc");
                    scheme_get_set.banner_date = scheme_data.optString("d_value");
                    scheme_get_set.banner_date = scheme_data.optString("cdate");
                    bottom_banner__datalist.add(scheme_get_set);
                }

                if(!bottom_banner__datalist.isEmpty()){
                    bottomfliiper();
                    mbottomFlipper.setVisibility(View.VISIBLE);
                } else {
                    mbottomFlipper.setVisibility(View.GONE);
                }
                bannerArray = profile_data.getJSONArray("courses");
                for (int scheme=0;scheme<bannerArray.length();scheme++) {
                    JSONObject scheme_data = bannerArray.optJSONObject(scheme);
                    Courses_Get_Set scheme_get_set = new Courses_Get_Set();
                    scheme_get_set.banner_id = scheme_data.optString("id");
                    scheme_get_set.banner_action = scheme_data.optString("banner_name");
                    scheme_get_set.banner_res = scheme_data.optString("banner_image");
                    scheme_get_set.banner_desc = scheme_data.optString("banner_offer_short_desc");
                    scheme_get_set.banner_date = scheme_data.optString("d_value");
                    scheme_get_set.banner_date = scheme_data.optString("cdate");
                    courses__datalist.add(scheme_get_set);
                }
                backup_courses__datalist.addAll(courses__datalist);
                courses_adapter.notifyDataSetChanged();

                JSONArray schemeArray=profile_data.getJSONArray("schemes");
                for (int scheme=0;scheme<schemeArray.length();scheme++) {
                    JSONObject scheme_data = schemeArray.optJSONObject(scheme);
                    Scheme_Get_Set scheme_get_set = new Scheme_Get_Set();
                    scheme_get_set.schemes_id = scheme_data.optString("id");
                    scheme_get_set.scheme_name = scheme_data.optString("d_name");
                    scheme_get_set.schemes_res = scheme_data.optString("d_image");
                    scheme_get_set.scheme_price = scheme_data.optString("d_price");
                    scheme_get_set.scheme_value = scheme_data.optString("d_value");
                    scheme_get_set.scheme_date = scheme_data.optString("cdate");
                    //scheme__datalist.add(scheme_get_set);
                    //adapter.notifyItemInserted(scheme);
                }
                adapter.notifyDataSetChanged();
                if(!scheme__datalist.isEmpty()){
                    recyclerView.setVisibility(View.VISIBLE);
                }else
                    recyclerView.setVisibility(View.GONE);

                //Donations Ends
                //Category
                msgArray = profile_data.getJSONArray("category");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject scheme_data = msgArray.optJSONObject(i);
                    Courses_Get_Set Courses_Get_Set = new Courses_Get_Set();
                    Courses_Get_Set.banner_id = scheme_data.optString("id");
                    Courses_Get_Set.banner_action = scheme_data.optString("category_name");
                    Courses_Get_Set.banner_res = scheme_data.optString("category_image");
                    //category__datalist.add(Courses_Get_Set);
                }
                category_adapter.notifyDataSetChanged();


                if(!category__datalist.isEmpty()){
                    recyclerView_category.setVisibility(View.VISIBLE);
                } else
                    recyclerView_category.setVisibility(View.GONE);
//Category Ends
//Help Full Actions
                helpful__datalist.clear();
                msgArray = profile_data.getJSONArray("help");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject scheme_data = msgArray.optJSONObject(i);
                    HelpfulActions_Get_Set helpfulActions_get_set = new HelpfulActions_Get_Set();
                    helpfulActions_get_set.action_id = scheme_data.optString("id");
                    helpfulActions_get_set.action_name = scheme_data.optString("action_details");
                    //helpful__datalist.add(helpfulActions_get_set);
                    txt_news_section.setText(txt_news_section.getText() + " | " + scheme_data.optString("action_details"));
                }
                help_actions_adapter.notifyDataSetChanged();
                if(!helpful__datalist.isEmpty()){
                    recycler_view_helpful_action.setVisibility(View.VISIBLE);
                } else
                    recycler_view_helpful_action.setVisibility(View.GONE);
                //News

                //Help Full Actions
                news__datalist.clear();
                msgArray = profile_data.getJSONArray("news");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject scheme_data = msgArray.optJSONObject(i);
                    News_Updates_Get_Set helpfulActions_get_set = new News_Updates_Get_Set();
                    helpfulActions_get_set.news_id = scheme_data.optString("id");
                    helpfulActions_get_set.news_name = scheme_data.optString("d_name");
                    helpfulActions_get_set.news_res = scheme_data.optString("d_image");
                    helpfulActions_get_set.news_price = scheme_data.optString("d_price");
                    helpfulActions_get_set.news_value = scheme_data.optString("d_value");
                    helpfulActions_get_set.news_date = scheme_data.optString("cdate");
                    //news__datalist.add(helpfulActions_get_set);
                }
                news_updates_adapter.notifyDataSetChanged();
                if(!news__datalist.isEmpty()){
                    recycler_view_news.setVisibility(View.VISIBLE);
                } else
                    recycler_view_news.setVisibility(View.GONE);
                //Help Full Actions


                /*banner_data = bannerArray.optJSONObject(1); //bottom
                res= banner_data.optString("banner_image");;
                if(res!=null && !res.equalsIgnoreCase(""))
                    Picasso.get().load(res).placeholder(R.drawable.d_round_white_background).into(bottom_banner);*/


            } else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                no_data_layout.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    public void Parse_home_data(String responce){

        home_get_set = new Home_Get_Set();

        datalist.clear();
        scheme__datalist.clear();
        top_banner__datalist.clear();
        bottom_banner__datalist.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject profile_data = msgArray.optJSONObject(i);
                    JSONArray schemeArray=profile_data.getJSONArray("schemes");
                    for (int scheme=0;scheme<schemeArray.length();scheme++) {
                        JSONObject scheme_data = schemeArray.optJSONObject(scheme);
                        Scheme_Get_Set scheme_get_set = new Scheme_Get_Set();
                        scheme_get_set.schemes_id = scheme_data.optString("id");
                        scheme_get_set.scheme_name = scheme_data.optString("d_name");
                        scheme_get_set.schemes_res = scheme_data.optString("d_image");
                        scheme_get_set.scheme_price = scheme_data.optString("d_price");
                        scheme_get_set.scheme_value = scheme_data.optString("d_value");
                        scheme_get_set.scheme_date = scheme_data.optString("cdate");
                        scheme__datalist.add(scheme_get_set);
                    }

                    JSONArray bannerArray = jsonObject.getJSONArray("banner");

                    for (int banner=0;banner<schemeArray.length();banner++) {
                        JSONObject banner_data = bannerArray.optJSONObject(i);
                        Banner_Get_Set banner_get_set = new Banner_Get_Set();
                        banner_get_set.banner_id = banner_data.optString("id");
                        banner_get_set.banner_res = banner_data.optString("banner_image");
                        banner_get_set.banner_action = "";
                        banner_get_set.banner_date = banner_data.optString("createddate");
                        if(banner_data.optString("banner_type").equalsIgnoreCase("Home_top_Banner")){
                            top_banner__datalist.add(banner_get_set);
                        } else if(banner_data.optString("banner_type").equalsIgnoreCase("Home_bottom_Banner")){
                            bottom_banner__datalist.add(banner_get_set);
                        }
                    }

                    */
    /*JSONArray bannerArray=jsonObject.getJSONArray("banner");

                    for (int banner=0;banner<schemeArray.length();banner++) {
                        JSONObject banner_data = bannerArray.optJSONObject(i);
                        Banner_Get_Set banner_get_set = new Banner_Get_Set();
                        banner_get_set.banner_id = banner_data.optString("id");
                        banner_get_set.banner_res = banner_data.optString("banner_image");
                        banner_get_set.banner_action = "";
                        banner_get_set.banner_date = banner_data.optString("createddate");
                        if(banner_data.optString("banner_type").equalsIgnoreCase("Home_top_Banner")){
                            top_banner__datalist.add(banner_get_set);
                        } else if(banner_data.optString("banner_type").equalsIgnoreCase("Home_bottom_Banner")){
                            bottom_banner__datalist.add(banner_get_set);
                        }
                    }*/
    /*



                    home_get_set.scheme_get_sets = scheme__datalist;
                    home_get_set.home_top_banner_get_sets=top_banner__datalist;
                    home_get_set.home_bottom_banner_get_sets=bottom_banner__datalist;

                    datalist.add(item);
                    adapter.notifyItemInserted(i);
                }

                adapter.notifyDataSetChanged();

                pbar.setVisibility(View.GONE);

                if(datalist.isEmpty()){
                    no_data_layout.setVisibility(View.VISIBLE);
                }else
                    no_data_layout.setVisibility(View.GONE);

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/

    private void OpenScheme(final Courses_Get_Set item) {
        Variables.Cart_Scheme_id = item.banner_id;
        Fragment_Cart fragment_cart = new Fragment_Cart(Variables.Cart_Scheme_id);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.nav_host_fragment, fragment_cart).commit();
        /*TabLayout.Tab profile= MainMenuFragment.tabLayout.getTabAt(4);
        profile.select();*/
    }


    @Override
    public void onDetach() {

        if(fragment_callback!=null)
            fragment_callback.Responce(new Bundle());

        super.onDetach();
    }

    private void fliiper(){
        try{
            mFlipper.removeAllViews();

            for(int i=0;i<top_banner__datalist.size();i++) {
                SimpleDraweeView imageView = new SimpleDraweeView(getContext());
                Picasso.get().load(top_banner__datalist.get(i).banner_res).placeholder(R.color.gray_darkest).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                spinnerIDArray.add(top_banner__datalist.get(i).banner_res);
                mFlipper.addView(imageView);
            }

            if(top_banner__datalist.size()>1) {
                mFlipper.setFlipInterval(8000);

                mFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                mFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mFlipper.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            Toaster.toast(scrollX + ":" + scrollY + ":" + oldScrollX + ":" + oldScrollY);
                        }
                    });
                }
                mFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {

                    public void onAnimationStart(Animation animation) {}

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {

                        int displayedChild = mFlipper.getDisplayedChild();
                        int childCount = mFlipper.getChildCount();

                        if (displayedChild == childCount - 1) {
                            displayedChild = 0;
                        }
                    }
                });
                mFlipper.startFlipping();
                profile_Viewer.setOnTouchListener(new View.OnTouchListener() {
                    private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            super.onFling(e1, e2, velocityX, velocityY);
                            float deltaX = e1.getX() - e2.getX();
                            float deltaXAbs = Math.abs(deltaX);
                            // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                            if((deltaXAbs > 50) && (deltaXAbs < 1500)) {
                                if(deltaX > 0){
                                    mFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                                    mFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                                    mFlipper.showNext();
                                }
                                else if(deltaX < 0) {
                                    mFlipper.setInAnimation(getContext(), R.anim.in_from_left); //use either the default slide animation in sdk or create your own ones
                                    mFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
                                    mFlipper.showPrevious();
                                }
                            }
                            return true;
                        }

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            super.onSingleTapUp(e);

                            if(spinnerIDArray!=null){
                                if(spinnerIDArray.size()-1 >=mFlipper.getDisplayedChild()){
                                    See_Full_Image_F see_image_f = new See_Full_Image_F();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                    Bundle args = new Bundle();
                                    args.putSerializable("image_url",spinnerIDArray.get(mFlipper.getDisplayedChild()));
                                    see_image_f.setArguments(args);
                                    transaction.addToBackStack(null);
                                    transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
                                }
                            }
                    /*if(!player.getPlayWhenReady()){
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }else{
                        is_user_stop_video=true;
                        privious_player.setPlayWhenReady(false);
                    }*/
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {

                    /*if(!player.getPlayWhenReady()){
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }


                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        Show_heart_on_DoubleTap(item, mainlayout, e);
                        Like_Video(currentPage, item);
                    }else {
                        Toast.makeText(context, "Please Login into app", Toast.LENGTH_SHORT).show();
                    }*/
                            return super.onDoubleTap(e);

                        }
                    });

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        gestureDetector.onTouchEvent(event);
                        return true;
                    }
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void bottomfliiper(){
        try{
            mbottomFlipper.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(context);
            for(int i=0;i<bottom_banner__datalist.size();i++) {

                RelativeLayout relview = (RelativeLayout) inflater.inflate(R.layout.achiever, null, false);
                relview.setVisibility(View.VISIBLE);
                CircleImageView imageView = relview.findViewById(R.id.testimonial_profile);
                TextView testimonial_name = relview.findViewById(R.id.testimonial_name);
                TextView testimonial_details = relview.findViewById(R.id.testimonial_details);
                Picasso.get().load(bottom_banner__datalist.get(i).banner_res).placeholder(R.color.gray_darkest).into(imageView);
                testimonial_name.setText(bottom_banner__datalist.get(i).banner_action);
                testimonial_details.setText(bottom_banner__datalist.get(i).banner_desc);
                spinnerBottomIDArray.add(bottom_banner__datalist.get(i).banner_res);
                if (relview != null) {
                    ViewGroup parent = (ViewGroup) relview.getParent();
                    if (parent != null) {
                        parent.removeView(relview);
                    }
                }
                mbottomFlipper.addView(relview);
                relview = null;
            }

            if(bottom_banner__datalist.size()>1) {
                mbottomFlipper.setFlipInterval(8000);
                mbottomFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                mbottomFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mbottomFlipper.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            Toaster.toast(scrollX + ":" + scrollY + ":" + oldScrollX + ":" + oldScrollY);
                        }
                    });
                }
                mbottomFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {

                    public void onAnimationStart(Animation animation) {}

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {

                        int displayedChild = mbottomFlipper.getDisplayedChild();
                        int childCount = mbottomFlipper.getChildCount();

                        if (displayedChild == childCount - 1) {
                            displayedChild = 0;
                        }
                    }
                });
                mbottomFlipper.startFlipping();
                profile_Viewer.setOnTouchListener(new View.OnTouchListener() {
                    private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            super.onFling(e1, e2, velocityX, velocityY);
                            float deltaX = e1.getX() - e2.getX();
                            float deltaXAbs = Math.abs(deltaX);
                            // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                            if((deltaXAbs > 50) && (deltaXAbs < 1500)) {
                                if(deltaX > 0){
                                    mbottomFlipper.setInAnimation(getContext(), R.anim.in_from_right); //use either the default slide animation in sdk or create your own ones
                                    mbottomFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
                                    mbottomFlipper.showNext();
                                }
                                else if(deltaX < 0) {
                                    mbottomFlipper.setInAnimation(getContext(), R.anim.in_from_left); //use either the default slide animation in sdk or create your own ones
                                    mbottomFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
                                    mbottomFlipper.showPrevious();
                                }
                            }
                            return true;
                        }

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            super.onSingleTapUp(e);

                            if(spinnerBottomIDArray!=null){
                                if(spinnerBottomIDArray.size()-1 >=mbottomFlipper.getDisplayedChild()){
                                    See_Full_Image_F see_image_f = new See_Full_Image_F();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                    Bundle args = new Bundle();
                                    args.putSerializable("image_url",spinnerBottomIDArray.get(mbottomFlipper.getDisplayedChild()));
                                    see_image_f.setArguments(args);
                                    transaction.addToBackStack(null);
                                    transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
                                }
                            }
                    /*if(!player.getPlayWhenReady()){
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }else{
                        is_user_stop_video=true;
                        privious_player.setPlayWhenReady(false);
                    }*/
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                        }

                        @Override
                        public boolean onDoubleTap(MotionEvent e) {

                    /*if(!player.getPlayWhenReady()){
                        is_user_stop_video=false;
                        privious_player.setPlayWhenReady(true);
                    }


                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        Show_heart_on_DoubleTap(item, mainlayout, e);
                        Like_Video(currentPage, item);
                    }else {
                        Toast.makeText(context, "Please Login into app", Toast.LENGTH_SHORT).show();
                    }*/
                            return super.onDoubleTap(e);

                        }
                    });

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        gestureDetector.onTouchEvent(event);
                        return true;
                    }
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawPageSelectionIndicators(int mPosition,int dotsCount){
        if(dotsCount>1){
            if(linearLayout!=null) {
                linearLayout.removeAllViews();
            }

            dots = new ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(context);
                if(i==mPosition)
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
                else
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_unselected));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);
                linearLayout.addView(dots[i], params);
            }
        }
    }
    private void drawPageottomSelectionIndicators(int mPosition,int dotsCount){
        if(dotsCount>1){
            if(bottomlinearlayout!=null) {
                bottomlinearlayout.removeAllViews();
            }

            dots = new ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(context);
                if(i==mPosition)
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
                else
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_unselected));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);
                bottomlinearlayout.addView(dots[i], params);
            }
        }
    }

}
