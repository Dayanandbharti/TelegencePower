package com.telegence.app.Main_Menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.telegence.app.Authentication.Login_A;
import com.telegence.app.TELEGENCE.HOME.Fragment_New_Home;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.OnBackPressListener;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.Variables;


public class MainMenuFragment extends RootFragment implements View.OnClickListener {

    public static TabLayout tabLayout;
    String Qr_Code_Action="";
    String Qr_Code_Data="";
    protected Custom_ViewPager pager;
    private ViewPagerAdapter adapter;
    Context context;


    public MainMenuFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        context=getContext();
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(1);
        pager.setPagingEnabled(false);
        view.setOnClickListener(this);

        /*tabLayout.setScrollPosition(1,0f,true);
        pager.setCurrentItem(1);*/
        /*try {
            Bundle bundle = this.getArguments();
            if(bundle != null){
                // handle your code here.
                String ActionType=bundle.getString("Request","").toString();
                if(ActionType!=null){
                    if(ActionType!=""){
                        if(ActionType=="QR_Code"){


                            pager.setAdapter(new ViewPagerAdapter(getContext().getSupportFragmentManager()));

                            tabLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    tabLayout.setupWithViewPager(viewPager);
                                }
                            });

                            selectTabIndex(3);

                        }
                    }
                }
            }
        } catch (Exception e){

        }*/
        return view;
    }

    private void selectTabIndex(final int index){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tabLayout.setScrollPosition(index, 0, true);
                pager.setCurrentItem(index);
                // or
                // tabLayout.getTabAt(index).select();
            }
        },100);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();
        Qr_Code_Action="";
        Qr_Code_Data="";
        try {
            Bundle bundle = this.getArguments();
            if(bundle != null){
                // handle your code here.
                String ActionType=bundle.getString("Request","").toString();
                if(ActionType!=null){
                    if(ActionType!=""){
                        if(ActionType=="QR_Code"){
                            Qr_Code_Action="QRCODE";
                            Qr_Code_Data=bundle.getString("UserID","").toString();

                            TabLayout.Tab tab = tabLayout.getTabAt(1);
                            tab.select();
                        }
                    }
                }
            }
        } catch (Exception e){

        }
    }

    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

    // this function will set all the icon and text in
    // Bottom tabs when we open an activity
    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView1= view1.findViewById(R.id.image);
        TextView  title1=view1.findViewById(R.id.text);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_white));
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
        title1.setText("HOME");
        title1.setTextColor(context.getResources().getColor(R.color.blue));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.bigitem_tablayout, null);
        TextView  title2=view2.findViewById(R.id.text);
        title2.setText("PROFILE");
        title2.setTextColor(context.getResources().getColor(R.color.black));
        tabLayout.getTabAt(1).setCustomView(view2);

        View view3 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView3= view3.findViewById(R.id.image);
        TextView title3=view3.findViewById(R.id.text);
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.exchange_diamonds));
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        title3.setText("CHAT");
        title3.setTextColor(context.getResources().getColor(R.color.black));
        tabLayout.getTabAt(2).setCustomView(view3);

        /*View view4 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView4= view4.findViewById(R.id.image);
        TextView  title4=view4.findViewById(R.id.text);
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.smallest_user));
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        title4.setText("Profile");
        title4.setTextColor(context.getResources().getColor(R.color.black));
        tabLayout.getTabAt(3).setCustomView (view4);

        View view5 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView5 = view5.findViewById(R.id.image);
        TextView title5=view5.findViewById(R.id.text);
        imageView5.setImageDrawable(getResources().getDrawable(R.drawable.order));
        imageView5.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        title5.setText("Donations");
        title5.setTextColor(context.getResources().getColor(R.color.black));
        tabLayout.getTabAt(4).setCustomView(view5);*/
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v=tab.getCustomView();
                ImageView image=v.findViewById(R.id.image);
                TextView  title=v.findViewById(R.id.text);
                switch (tab.getPosition()){
                    case 0:
                        Onother_Tab_Click(false);
                        image.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.blue));
                        MainMenuActivity.mainbar.setText("HOME");
                        break;

                    case 1:
                        Onother_Tab_Click(false);
                        //image.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.blue));
                        MainMenuActivity.mainbar.setText("PROFILE");
                        break;

                    case 2:
                        Onother_Tab_Click(false);
                        image.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.blue));
                        MainMenuActivity.mainbar.setText("CHAT");
                        break;

                    /*case 3:
                        Onother_Tab_Click(false);
                        image.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.blue));
                        MainMenuActivity.mainbar.setText("Profile");
                        break;
                    case 4:
                        Onother_Tab_Click(false);
                        image.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.blue));
                        MainMenuActivity.mainbar.setText("Donations");
                        break;*/
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v=tab.getCustomView();
                ImageView image=v.findViewById(R.id.image);
                TextView  title=v.findViewById(R.id.text);

                switch (tab.getPosition()){
                    case 0:
                        image.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 1:
                        image.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 2:
                        image.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    /*case 3:
                        image.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 4:
                        image.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;*/
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        final LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);

        tabStrip.getChildAt(2).setClickable(false);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){
                    TabLayout.Tab tab=tabLayout.getTabAt(2);
                    tab.select();
                } else {
                    Intent intent = new Intent(getActivity(), Login_A.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }




                    /*Functions.make_directry(Variables.app_hided_folder);
                    Functions.make_directry(Variables.app_showing_folder);
                    Functions.make_directry(Variables.draft_app_folder);
                    OnEcplore_Tab_Click();

                    if(!Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                        Toast.makeText(context, "You have to login First", Toast.LENGTH_SHORT).show();
                    } else if (Functions.isMyServiceRunning(getActivity(),new Upload_Service().getClass())) {
                        Toast.makeText(getActivity(), "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Sorry, Not Available Right Now!", Toast.LENGTH_SHORT).show();
                    }*/

            }
        });

        //tabStrip.getChildAt(3).setClickable(false);
        /*view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                    TabLayout.Tab tab=tabLayout.getTabAt(3);
                    tab.select();
                }
                else {
                    Intent intent = new Intent(getActivity(), UserGroup_A.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }
            }
        });

        tabStrip.getChildAt(4).setClickable(false);
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)){

                    TabLayout.Tab tab=tabLayout.getTabAt(4);
                    tab.select();

                }else {

                    Intent intent = new Intent(getActivity(), UserGroup_A.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }

            }
        });*/

        Onother_Tab_Click(true);

        if(MainMenuActivity.intent!=null){

            if(MainMenuActivity.intent.hasExtra("action_type")) {


                if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)) {
                    String action_type=MainMenuActivity.intent.getExtras().getString("action_type");

                    if(action_type.equals("message")){

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TabLayout.Tab  tab=tabLayout.getTabAt(3);
                                tab.select();
                            }
                        },1500);


                        String id=MainMenuActivity.intent.getExtras().getString("senderid");
                        String name=MainMenuActivity.intent.getExtras().getString("title");
                        String icon=MainMenuActivity.intent.getExtras().getString("icon");

                        //chatFragment(id,name,icon);
                    }
                }
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new Fragment_New_Home();
                    break;

                case 1:
                    result = new BlankFragment();
                    break;

                case 2:
                    result = new BlankFragment();
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 3;
        }




        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            registeredFragments.remove(position);

            super.destroyItem(container, position, object);

        }


        /**
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {

            return registeredFragments.get(position);

        }
    }

    public void Onother_Tab_Click(boolean isHome){

        TabLayout.Tab tab0=tabLayout.getTabAt(0);
        View view0=tab0.getCustomView();
        TextView tex0=view0.findViewById(R.id.text);
        ImageView imageView0= view0.findViewById(R.id.image);
        if(isHome){
            imageView0.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
            tex0.setTextColor(context.getResources().getColor(R.color.blue));
        } else{
            imageView0.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
            tex0.setTextColor(context.getResources().getColor(R.color.darkgray));
        }
        tab0.setCustomView(view0);

        TabLayout.Tab tab1=tabLayout.getTabAt(1);
        View view1=tab1.getCustomView();
        TextView tex1=view1.findViewById(R.id.text);
        tex1.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2=tabLayout.getTabAt(2);
        View view2=tab2.getCustomView();
        TextView tex2=view2.findViewById(R.id.text);
        ImageView image= view2.findViewById(R.id.image);
        image.setImageResource(R.drawable.chat_icon);
        image.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex2.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab2.setCustomView(view2);

        /*TabLayout.Tab tab3=tabLayout.getTabAt(3);
        View view3=tab3.getCustomView();
        ImageView imageView3= view3.findViewById(R.id.image);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex3=view3.findViewById(R.id.text);
        tex3.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab3.setCustomView(view3);

        TabLayout.Tab tab4=tabLayout.getTabAt(4);
        View view4=tab4.getCustomView();
        ImageView imageView4= view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4=view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab4.setCustomView(view4);*/

//        tabLayout.setBackgroundResource(R.drawable.d_main_menu_background);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.white));
        }

    }

    public void OnEcplore_Tab_Click(){

        TabLayout.Tab tab0=tabLayout.getTabAt(0);
        View view0=tab0.getCustomView();
        TextView tex0=view0.findViewById(R.id.text);
        ImageView imageView0= view0.findViewById(R.id.image);
        imageView0.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex0.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab0.setCustomView(view0);

        TabLayout.Tab tab1=tabLayout.getTabAt(1);
        View view1=tab1.getCustomView();
        TextView tex1=view1.findViewById(R.id.text);
        ImageView imageView1= view1.findViewById(R.id.image);
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex1.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2=tabLayout.getTabAt(2);
        View view2=tab2.getCustomView();
        ImageView image= view2.findViewById(R.id.image);
        image.setImageDrawable(context.getResources().getDrawable(R.drawable.chat_icon));
        TextView tex2=view2.findViewById(R.id.text);
        tex2.setTextColor(context.getResources().getColor(R.color.blue));
        tab2.setCustomView(view2);

        /*TabLayout.Tab tab3=tabLayout.getTabAt(3);
        View view3=tab3.getCustomView();
        ImageView imageView3= view3.findViewById(R.id.image);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex3=view3.findViewById(R.id.text);
        tex3.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab3.setCustomView(view3);


        TabLayout.Tab tab4=tabLayout.getTabAt(4);
        View view4=tab4.getCustomView();
        ImageView imageView4= view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4=view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab4.setCustomView(view4);*/

//        tabLayout.setBackgroundResource(R.drawable.d_main_menu_background);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.white));
        }
    }

}