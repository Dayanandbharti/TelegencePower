package com.telegence.app.Main_Menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.telegence.app.Blocked_A;
import com.telegence.app.Profile.Edit_Profile_F;
import com.telegence.app.Profile.Profile_Tab_F;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONObject;

import xdroid.toaster.Toaster;

public class MainMenuActivity extends AppCompatActivity{
    public static MainMenuActivity mainMenuActivity;
    public static TextView mainbar;
    public static TextView maintext;
    public static ImageView mainImage;
    private MainMenuFragment mainMenuFragment;
    long mBackPressed;
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    public static String token;
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        /*requestWindowFeature( Window.FEATURE_NO_TITLE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
*/
        try{
            super.onCreate(savedInstanceState);
            // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            setContentView(R.layout.activity_main_menu);
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            toolbar.setElevation(0);
            setSupportActionBar(toolbar);


            mainbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
            maintext = (TextView) toolbar.findViewById(R.id.toolbar_name);
            TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_name);
            mainImage = toolbar.findViewById(R.id.toolbar_pic);

            mainMenuActivity=this;
            intent=getIntent();
            setIntent(null);
            drawer = findViewById(R.id.drawer_layout);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Variables.screen_height= displayMetrics.heightPixels;
            Variables.screen_width= displayMetrics.widthPixels;
            Variables.sharedPreferences=getSharedPreferences(Variables.pref_name,MODE_PRIVATE);
            Variables.user_id=Variables.sharedPreferences.getString(Variables.u_id,"");
            Variables.user_group=Variables.sharedPreferences.getString(Variables.u_grp,"Student");
            Variables.GoliveStatus=Variables.sharedPreferences.getString(Variables.go_live,"No");
            Variables.Level="0";

            //updatelevel();
            textView.setText(("Hello\n" + Variables.sharedPreferences.getString(Variables.f_name,"") + " " + Variables.sharedPreferences.getString(Variables.l_name,"") + "\n" + Variables.sharedPreferences.getString(Variables.u_grp,"") + " Id " + Variables.sharedPreferences.getString(Variables.u_id,"")).toUpperCase());
            Variables.user_name=Variables.sharedPreferences.getString(Variables.u_name,"");
            Variables.user_pic=Variables.sharedPreferences.getString(Variables.u_pic,"");
            Variables.user_pic1=Variables.sharedPreferences.getString(Variables.u_pic1,"");
            Variables.user_pic2=Variables.sharedPreferences.getString(Variables.u_pic2,"");
            Variables.user_pic3=Variables.sharedPreferences.getString(Variables.u_pic3,"");
            Variables.user_pic4=Variables.sharedPreferences.getString(Variables.u_pic4,"");
            Variables.user_pic5=Variables.sharedPreferences.getString(Variables.u_pic5,"");
            Variables.user_vid=Variables.sharedPreferences.getString(Variables.u_vid,"");
            if(Variables.user_pic!=null && !Variables.user_pic.equals("")) {
                Picasso.get().load(Variables.user_pic).placeholder(R.color.transparent).into(mainImage);
            }
            NavigationView navigationView = findViewById(R.id.nav_view);
            if(Variables.user_group!=null && Variables.user_group.equalsIgnoreCase("teacher")) {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer_teacher);
            }
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_my_profile, R.id.nav_my_orders, R.id.nav_my_courses, R.id.nav_important_links, R.id.nav_mark_attendance,R.id.nav_view_attendance, R.id.nav_refer, R.id.nav_log_out)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            View headerView = navigationView.getHeaderView(0);
            ImageView imageView_header = (ImageView) headerView.findViewById(R.id.imageView_header);
            TextView navUsername = (TextView) headerView.findViewById(R.id.txtheadername);
            TextView navEmail = (TextView) headerView.findViewById(R.id.txtemail);
            /*headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Profile_Tab_F edit_profile_f = new Profile_Tab_F();
                    FragmentTransaction transaction = mainMenuActivity.getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.nav_host_fragment, edit_profile_f).commit();
                }
            });*/

//            if(Variables.user_group!=null && !Variables.user_group.equalsIgnoreCase("student"))
//                imageView_header.setImageResource(R.drawable.student_boy);
//
//            if(Variables.user_group!=null && !Variables.user_group.equalsIgnoreCase("teacher"))
//                imageView_header.setImageResource(R.drawable.male_teacher);

            navUsername.setText(Variables.sharedPreferences.getString(Variables.f_name,"")+" "+Variables.sharedPreferences.getString(Variables.l_name,""));
            navEmail.setText(Variables.user_name);

            if(token==null || (token.equals("")||token.equals("null"))) {
                //token= FirebaseInstanceId.getInstance().getToken();
                token= Variables.sharedPreferences.getString(Variables.device_token,"null");
            }

            if (savedInstanceState == null) {
                navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
            } else {
                mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().getFragments().get(0);
            }
            //broadcast_option(this);
            Functions.make_directry(Variables.app_hided_folder);
            Functions.make_directry(Variables.app_showing_folder);
            Functions.make_directry(Variables.draft_app_folder);

            Intent bundle = intent;
            if(bundle!=null) {
                Uri appLinkData = bundle.getData();
                if (appLinkData != null) {
                    String link = appLinkData.toString();
//                    if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)){
//                        Intent intent = new Intent(this, MainActivity.class);
//                        intent.putExtra("LinkLiveValue", link);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(),"You are not logged in",Toast.LENGTH_LONG).show();
//                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void updatelevel(){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(this, Variables.updatelevel, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    String msg = response.optString("msg");
                    if (code.equals("200")) {
                        if (msg!="") {
                            Variables.Level=msg.toString();
                        } else {
                            Variables.Level="0";
                        }
                    } else {
                        Variables.Level="0";
                    }
                } catch (Exception e) {
                    Variables.Level="0";
                    e.printStackTrace();
                }
            }
        });
    }

    private void broadcast_option(MainMenuActivity activity) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(this, Variables.return_newbroadcastoption, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        JSONArray respo=response.getJSONArray("msg");
                        if(respo.length()>0){
                            JSONObject data = respo.getJSONObject(0);
                            String msg = data.optString("go_live");
                            String block = data.optString("block");
                            String is_verified = data.optString("is_verified");
                            String is_profile_updated = data.optString("is_profile_updated");
                            if (block!="") {
                                if (block.equalsIgnoreCase("1")) {
                                    Toaster.toast("Your Account is blocked, Kindly contact administration.");
                                    SharedPreferences.Editor editor= Variables.sharedPreferences.edit();
                                    editor.putString(Variables.u_id,"");
                                    editor.putString(Variables.u_name,"");
                                    editor.putString(Variables.u_pic,"");
                                    editor.putBoolean(Variables.islogin,false);
                                    editor.commit();
                                    Intent intent = new Intent(activity, Blocked_A.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }

                            /*if (is_verified==null)
                                is_verified="";
                            if (is_verified.trim().equalsIgnoreCase(""))
                                is_verified="0";
                            if (!is_verified.equalsIgnoreCase("2") ) {
                                if (is_verified.equalsIgnoreCase("0") || is_verified.equalsIgnoreCase("3")) {
                                    Intent intent = new Intent(activity, Request_Verification_F.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else {
                                    Intent intent = new Intent(activity, Processing_A.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                            }*/
                            if(is_profile_updated==null) is_profile_updated="0";
                            if (!is_profile_updated.equalsIgnoreCase("1")) {

                                Edit_Profile_F edit_profile_f = new Edit_Profile_F(new Fragment_Callback() {
                                    @Override
                                    public void Responce(Bundle bundle) {
                                    }
                                });
                                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                                transaction.replace(R.id.nav_host_fragment, edit_profile_f).commit();
                            }

                            if (msg!="") {
                                if (msg.equalsIgnoreCase("Yes")) {
                                    Variables.GoliveStatus="Yes";
                                    Variables.sharedPreferences.getString(Variables.go_live,"No");
                                } else {
                                    Variables.GoliveStatus="No";
                                }
                            } else {
                                Variables.GoliveStatus="No";
                                //iconView.setImageResource(R.drawable.round_profile_image_placeholder);
                            }
                        }

                    } else {
                        Variables.GoliveStatus="No";
                    }
                    Variables.sharedPreferences.edit().putString(Variables.go_live,Variables.GoliveStatus);
                } catch (Exception e) {
                    //iconView.setImageResource(R.drawable.round_profile_image_placeholder);
                    Variables.GoliveStatus="No";
                    Variables.sharedPreferences.edit().putString(Variables.go_live,Variables.GoliveStatus);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null){
            String type=intent.getStringExtra("type");
            /*if(type!=null && type.equalsIgnoreCase("message")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Chat_Activity chat_activity = new Chat_Activity(new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {

                            }
                        });
                        FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

                        Bundle args = new Bundle();
                        args.putString("user_id", intent.getStringExtra("user_id"));
                        args.putString("user_name", intent.getStringExtra("user_name"));
                        args.putString("user_pic", intent.getStringExtra("user_pic"));
                        onPause();
                        chat_activity.setArguments(args);
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.MainMenuFragment, chat_activity).commit();
                    }
                },10);

            }
            else*/
            /*if(type!=null && type.equalsIgnoreCase("wallet")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Dimaonds_F wallet_main_f = new Dimaonds_F();
                        FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                        onPause();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.MainMenuFragment, wallet_main_f).commit();
                    }
                },10);
            }
            else if(type!=null && type.equalsIgnoreCase("moments")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Moment_Nav_Main_F momentNavMainF = new Moment_Nav_Main_F();
                        FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                        onPause();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.MainMenuFragment, momentNavMainF).commit();
                    }
                },10);
            }
            else if(type!=null && type.equalsIgnoreCase("Live_Search")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Live_Search_Main_F wallet_main_f = new Live_Search_Main_F();
                        FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                        onPause();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.MainMenuFragment, wallet_main_f).commit();
                    }
                },100);

            }
            else if(type!=null && type.equalsIgnoreCase("exchange")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Wallet_Main_F wallet_main_f = new Wallet_Main_F();
                        FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                        onPause();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.MainMenuFragment, wallet_main_f).commit();
                    }
                },10);

            }
            else if(type!=null && type.equalsIgnoreCase("profile")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Profile_F profile_f = new Profile_F(new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {}
                        });
                        FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

                        Bundle args = new Bundle();
                        args.putString("user_id", intent.getStringExtra("user_id"));
                        args.putString("user_name", intent.getStringExtra("user_name"));
                        args.putString("user_pic", intent.getStringExtra("user_pic"));
                        onPause();
                        profile_f.setArguments(args);
                        transaction.addToBackStack(null);

                        transaction.replace(R.id.MainMenuFragment, profile_f).commit();

                    }
                },10);

            }
            else */
            if(type!=null && type.equalsIgnoreCase("Myprofile")){
                Profile_Tab_F wallet_main_f = new Profile_Tab_F();
                FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction transaction = MainMenuActivity.mainMenuActivity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
                onPause();
                transaction.addToBackStack(null);
                transaction.replace(R.id.MainMenuFragment, wallet_main_f).commit();
                /*
                TabLayout.Tab profile= MainMenuFragment.tabLayout.getTabAt(4);
                profile.select();*/
            }
            else{
                if(token==null || (token.equals("")||token.equals("null"))) {
                    //token = FirebaseInstanceId.getInstance().getToken();
                    token= Variables.sharedPreferences.getString(Variables.device_token,"null");
                }
                initScreen();
            }
        }
    }

    private void initScreen() {
        mainMenuFragment = new MainMenuFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainMenuFragment)
                .commit();

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (mainMenuFragment!=null && !mainMenuFragment.onBackPressed()) {
//            int count = this.getSupportFragmentManager().getBackStackEntryCount();
//            if (count == 0) {
//                if (mBackPressed + 2000 > System.currentTimeMillis()) {
//                    super.onBackPressed();
//                    return;
//                } else {
//                    Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
//                    mBackPressed = System.currentTimeMillis();
//
//                }
//            } else {
//
//            }
//        }

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else{
//            if (mainMenuFragment!=null && !mainMenuFragment.onBackPressed()) {
            if (1==1) {
                int count = this.getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    if (mBackPressed + 2000 > System.currentTimeMillis()) {
                        super.onBackPressed();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
                        mBackPressed = System.currentTimeMillis();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Functions.deleteCache(this);
    }

    public void reach(String Code){
        try {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.payment_response,
                    (ViewGroup) findViewById(R.id.payment_resp_layout));
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
            TextView txtmsg=view.findViewById(R.id.txtmsg);
            ImageView imageView=view.findViewById(R.id.imgback);
            if(Code=="1"){
                imageView.setImageResource(R.drawable.payment_success);
                txtmsg.setText("Payment Successful");
                //new Login_A.MyTask().execute();
            } else {
                if(Code=="Pay but Failed"){
                    imageView.setImageResource(R.drawable.payment_success);
                    txtmsg.setText("Something went wrong, we'll revert your payment within 3 days.");
                } else {
                    imageView.setImageResource(R.drawable.payment_error);
                    txtmsg.setText("Payment Unsuccessful");
                }
            }
            Toast custToast = new Toast(this);
            custToast.setView(view);
            custToast.setDuration(Toast.LENGTH_LONG);
            custToast.show();

            Profile_Tab_F wallet_f=new Profile_Tab_F();
//            FragmentManager fragmentManager = MainMenuActivity.mainMenuActivity.getSupportFragmentManager();
//            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            /*Bundle args = new Bundle();
            args.putString("code",Code);
            payment_resp.setArguments(args);*/
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment,wallet_f).commit();
        } catch ( Exception e){}
    }

    public void createTransaction(String PaymentID) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.user_id);
            parameters.put("amount",Variables.price);
            Variables.price=null;
            parameters.put("senderfb_id","");
            parameters.put("diamond_quantity", Variables.diamond_Unit);
            Variables.diamond_Unit=null;
            parameters.put("order_id", PaymentID);
            parameters.put("status", "1");
            parameters.put("source", "Purchased");

        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(getApplicationContext(), Variables.createTransaction, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    JSONArray msg = response.optJSONArray("msg");
                    if (code.equals("200")) {
                        reach("1");
                    } else {
                        reach("Pay but Failed");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
