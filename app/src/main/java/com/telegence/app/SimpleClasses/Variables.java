package com.telegence.app.SimpleClasses;

import android.content.SharedPreferences;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by AQEEL on 2/15/2019.
 */


public class Variables {
    public static final String device="android";

    public static int screen_width;
    public static int screen_height;
    public static final String SelectedAudio_AAC ="SelectedAudio.aac";
    public static final String main="Telegence";
    public static final String root= Environment.getExternalStorageDirectory().toString();
    public static final String app_showing_folder =root+"/"+ main +"/";
    public static final String app_hided_folder = app_showing_folder;
    public static final String draft_app_folder= app_hided_folder +"Draft/";

    public static SharedPreferences sharedPreferences;
    public static final String pref_name="pref_name";
    public static final String u_id="u_id";
    public static final String u_name="u_name";
    public static final String u_grp="u_grp";
    public static final String u_pic="u_pic";
    public static final String u_pic1="u_pic1";
    public static final String u_pic2="u_pic2";
    public static final String u_pic3="u_pic3";
    public static final String u_pic4="u_pic4";
    public static final String u_pic5="u_pic5";
    public static final String u_vid="u_vid";
    public static final String f_name="f_name";
    public static final String l_name="l_name";
    public static final String gender="u_gender";
    public static final String islogin="is_login";
    public static final String go_live="golive";
    public static final String device_token="device_token";
    public static final String api_token="api_token";
    public static final String device_id="device_id";

    public static String user_id;
    public static String user_name;
    public static String user_group;
    public static String user_pic;
    public static String user_pic1;
    public static String user_pic2;
    public static String user_pic3;
    public static String user_pic4;
    public static String user_pic5;
    public static String user_vid;
    public static String GoliveStatus;
    public static String Level;
    public static String Cart_Scheme_id;

    public static String tag=main+"_";

    public static String price;
    public static String diamond_Unit;

    public static String Selected_sound_id = "null";

    public static  boolean Reload_my_videos = false;
    public static  boolean Reload_my_videos_inner = false;
    public static  boolean Reload_my_likes_inner = false;
    public static  boolean Reload_my_notification = false;

    // if you want a user can't share a video from your app then you have to set this value to true
    public static final boolean is_secure_info=false;

    // if you want a ads did not show into your app then set the belue valuw to true.
    public static final boolean is_remove_ads=false;

    public static final String privacy_policy = "https://www.privacypolicygenerator.info/live.php?token=";
    public static final String app_domain = "http://tpplintra.com/";
    public static final String main_domain = app_domain + "" ;
    public static final String base_url = main_domain + "API/";
    public static final String api_domain = base_url + "index.php?p=";

    public static final String update_pass = api_domain +"update_pass";
    public static final String SignUp = api_domain +"signup";
    public static final String aboutpage = main_domain +"about_app.html";
    public static final String transaction = main_domain +"transaction.html";
    public static final String contactpage = main_domain +"contact_app.html";
    public static final String get_Categories = api_domain +"all_category";
    public static final String get_home = api_domain +"get_home";
    public static final String get_plan = api_domain +"get_plan";
    public static final String get_offer = api_domain +"get_offer";
    public static final String get_help = api_domain +"get_help";
    public static final String get_profile = api_domain +"get_profile";
    public static final String get_scheme = api_domain +"get_scheme";
    public static final String get_goldprice = api_domain +"get_goldprice";
    public static final String get_home_gold_price = api_domain +"get_home_gold_price";
    public static final String get_refercode = api_domain +"get_refercode";
    public static final String get_password = api_domain +"get_password";
    public static final String uploadVideo = api_domain +"uploadVideo";
    public static final String uploadProfileVideo = api_domain +"uploadProfileVideo";
    public static final String likeDislikeVideo= api_domain +"likeDislikeVideo";
    public static final String likeDislikeUser= api_domain +"likeDislikeUser";
    public static final String updateVideoView= api_domain +"updateVideoView";
    public static final String follow_users= api_domain +"follow_users";
    public static final String edit_profile= api_domain +"new_edit_profile";
    public static final String updatepass= api_domain +"updatepass";
    public static final String delete_profile= api_domain +"Delete_User";
    public static final String get_user_data= api_domain +"get_user_data";
    public static final String uploadImage= api_domain +"uploadImage";
    public static final String uploadExpenseImage= api_domain +"uploadExpenseImage";

    public static final String add_order_transactions = api_domain +"add_order_transactions";
    public static final String updateMoneyRequest = api_domain +"updateMoneyRequest";
    public static final String updateExpense = api_domain +"updateExpense";
    public static final String add_query = api_domain +"add_query";
    public static final String add_money_request = api_domain +"add_money_request";
    public static final String send_money = api_domain +"send_money";

    public static final String check_in = api_domain +"check_in";
    public static final String check_out = api_domain +"check_out";
    public static final String view_att_time = api_domain +"view_att_time";

    public static final String add_expense = api_domain +"add_expense";
    public static final String add_leave_Req = api_domain +"add_leave_Req";
    public static final String add_response = api_domain +"add_response";
    public static final String uploadTransactionReference = api_domain +"uploadTransactionReference";
    public static final String DeleteVideo = api_domain +"DeleteVideo";
    public static final String search = api_domain +"search";

    public static final String getOrders = api_domain +"getall_order";
    public static final String getCertificate = api_domain +"getCertificate";
    public static final String getMyCourse = api_domain +"getMyCourse";
    public static final String getrefers = api_domain +"getrefers";
    public static final String getVerified = api_domain +"getVerified";
    public static final String getallbatch = api_domain +"all_batch";
    public static final String updateinterest = api_domain +"updateInterest";
    public static final String getallinterest = api_domain +"all_interest";
    public static final String getallproduct = api_domain +"all_diamond";
    public static final String getallbanner = api_domain +"all_banner";
    public static final String is_valid_signup = api_domain +"is_valid_signup";
    public static final String getMyBalance = api_domain +"MyDiamond_balance";
    public static final String getwalletAmt = api_domain +"getwalletAmt";
    public static final String getwallettransaction = api_domain +"getwallettransaction";
    public static final String getMoneyRequest= api_domain +"getMoneyRequest";
    public static final String getNewTeamMoneyRequests= api_domain +"getNewTeamMoneyRequests";
    public static final String getimportantlink= api_domain +"getimportantlink";
    public static final String getDoubts = api_domain +"getDoubts";
    public static final String emp_att_list = api_domain +"emp_att_list";
    public static final String emp_list = api_domain +"emp_list";
    public static final String getExpenses = api_domain +"getExpenses";
    public static final String getDoubt_responses = api_domain +"getDoubt_responses";
    public static final String getMyCoinBalance= api_domain +"MyCoin_balance";
    public static final String getmy_inr_balance= api_domain +"my_inr_balance";
    public static final String getallOrder= api_domain +"all_order";
    public static final String getall_redeemtransaction= api_domain +"getall_redeemtransaction";
    public static final String getall_order= api_domain +"getall_order";
    public static final String getallTransaction= api_domain +"all_transactions";
    public static final String getall_Cointransactions= api_domain +"all_cointransactions";
    public static final String createTransaction= api_domain +"createTransaction";
    public static final String getallcountry= api_domain +"all_country";
    public static final String get_redeem_details= api_domain +"get_redeem_details";
    public static final String getallindianstate= api_domain +"getallindianstate";
    public static final String am_I_a_leader= api_domain +"am_I_a_leader";
    public static final String getallProjects= api_domain +"getallProjects";
    public static final String getallEmployee= api_domain +"getallEmployee";
    public static final String getallManager= api_domain +"getallManager";
    public static final String return_newbroadcastoption= api_domain +"return_newbroadcastoption";
    public static final String updatelevel= api_domain +"updatelevel";
    public static final String return_version= api_domain +"returnVersion";
    public static final String returnhasPassword= api_domain +"returnhasPassword";

    public static final String sendOTP= api_domain +"sendOTP";
    public static final String getUserList= api_domain +"return_channel_data";

    public static final String MyStreams = api_domain +"MyStreams";

    public final static int permission_Read_data=789;

    public static final String gif_firstpart="https://media.giphy.com/media/";
    public static final String gif_secondpart="/100w.gif";

    public static final String gif_firstpart_chat="https://media.giphy.com/media/";
    public static final String gif_secondpart_chat="/200w.gif";


    public static final SimpleDateFormat df =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);

    public static final SimpleDateFormat df2 =
            new SimpleDateFormat("dd-MM-yyyy HH:mmZZ", Locale.ENGLISH);

    public final static int permission_camera_code=786;
    public final static int permission_write_data=788;
    public final static int permission_Recording_audio=790;
    public final static int Pick_video_from_gallery=791;
    public static final String sendPushNotification= api_domain +"sendPushNotification";
    public static String gif_api_key1="giphy_api_key_here";
}
