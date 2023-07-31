package com.telegence.app.Orders.Certificate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.telegence.app.R;
import com.telegence.app.See_Full_Image_F;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.SimpleClasses.Webview_F;
import com.telegence.app.Transactions_Get_Set;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order_transactions_Adapter extends BaseAdapter {

    List<Transactions_Get_Set> transactions_get_set;
    private LayoutInflater thisInflater;
    FragmentActivity activity;

    public Order_transactions_Adapter(Context con, List<Transactions_Get_Set> transactions_get_set) {
        this.thisInflater = LayoutInflater.from(con);
        this.transactions_get_set = transactions_get_set;
    }

    @Override
    public int getCount() {
        return transactions_get_set.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = thisInflater.inflate(R.layout.item_request_transactions, parent, false);
        }
        TextView txtserial = (TextView) convertView.findViewById(R.id.serial);
        TextView txtproduct_ID = (TextView) convertView.findViewById(R.id.product_ID);
        TextView txtproduct_rate = (TextView) convertView.findViewById(R.id.product_rate);
        TextView textHeading = (TextView) convertView.findViewById(R.id.product_name);
        TextView left = (TextView) convertView.findViewById(R.id.left);

        TextView txtproduct_price = (TextView) convertView.findViewById(R.id.product_price);
        TextView txtdiamond_unit = (TextView) convertView.findViewById(R.id.diamond_unit);
        View account_color_strip= (View) convertView.findViewById(R.id.account_color_strip);

        Transactions_Get_Set transactions = transactions_get_set.get(position);
        txtserial.setText(String.valueOf(position + 1   ));
        txtproduct_rate.setText(transactions.gold_price + "/g");
        //txtproduct_price.setText(transactions.trans_amount + "\n(" + String.valueOf(Double.parseDouble(transactions.gold_qty)/1000)  + " g)");
        txtproduct_price.setText(transactions.trans_amount);
        String dateString = transactions.trans_created;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            long date = convertedDate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy\n h:mm a");
            dateString = sdf.format(date);
        } catch (ParseException e) {
        }

        txtproduct_ID.setText(dateString);
        textHeading.setText(dateString);
        if(transactions.trans_status.equalsIgnoreCase("0")){
            txtdiamond_unit.setText("Pending");
            //txtdiamond_unit.setTextColor(ContextCompat.getColorStateList(activity, R.color.app_sec_color));
        } else if(transactions.trans_status.equalsIgnoreCase("1")){
            txtdiamond_unit.setText("Approved");
            //txtdiamond_unit.setTextColor(ContextCompat.getColorStateList(activity, R.color.tx_success_bg));
        } else if(transactions.trans_status.equalsIgnoreCase("2")){
            txtdiamond_unit.setText("Rejected");
            //txtdiamond_unit.setTextColor(ContextCompat.getColorStateList(activity, R.color.redcolor));
        }
     try{
         /*if(spinnerNameArray[position].equalsIgnoreCase("Purchased")){
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
             account_color_strip.setBackgroundResource(R.color.tx_success_bg);
             left.setText("P");
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.tx_success_bg));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.light_g));
         }else if(spinnerNameArray[position].equalsIgnoreCase("Admin") )  {
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
             left.setText("AD");
             account_color_strip.setBackgroundResource(R.color.maincolor);
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.maincolor));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.light_m));
         } else if(spinnerNameArray[position].equalsIgnoreCase("Gift Received") ){
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);
             left.setText("GR");
             account_color_strip.setBackgroundResource(R.color.tx_success_bg);
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.tx_success_bg));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.light_g));
         }else if(spinnerNameArray[position].equalsIgnoreCase("Sent") ){
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
             left.setText("S");
             account_color_strip.setBackgroundResource(R.color.redcolor);
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.redcolor));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.light_pink));
         }else if(spinnerNameArray[position].trim().contains("Exchanged to") ){
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
             left.setText("ED");
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.purple_dark));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.purple));
             account_color_strip.setBackgroundResource(R.color.purple_dark);
         }else if(spinnerNameArray[position].trim().contains("Exchange to") ){
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
             left.setText("ED");
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.purple_dark));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.purple));
             account_color_strip.setBackgroundResource(R.color.purple_dark);
         } else if(spinnerNameArray[position].contains("salary") ) {
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);
             left.setText("S");
             account_color_strip.setBackgroundResource(R.color.blue);
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.blue));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.blue_alpha));
         } else {
             if(spinnerNameArray[position].length()>1){
                 left.setText(spinnerNameArray[position].substring(0,1));
             }else{
                 left.setText("");
             }
             //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
             account_color_strip.setBackgroundResource(R.color.gray_darkest);
             left.setTextColor(ContextCompat.getColorStateList(activity, R.color.gray_darkest));
             left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.black_half_transparent));
         }*/
//        if(spinnerNameArray[position].equalsIgnoreCase("Purchased") ){
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupee, 0, 0, 0);
//            txtproduct_price.setText(spinnerPriceArray[position]);
//            account_color_strip.setBackgroundResource(R.color.tx_success_bg);
//        }else if(spinnerNameArray[position].equalsIgnoreCase("Admin") )  {
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gift, 0, 0, 0);
//            txtproduct_price.setText("");
//            txtproduct_price.setVisibility(View.GONE);
//            account_color_strip.setBackgroundResource(R.color.maincolor);
//        } else if(spinnerNameArray[position].equalsIgnoreCase("Gift Received") ){
//            //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            txtproduct_price.setText(spinnerPriceArray[position]);
//            txtproduct_price.setVisibility(View.GONE);
//            account_color_strip.setBackgroundResource(R.color.tx_success_bg);
//        }else if(spinnerNameArray[position].equalsIgnoreCase("Sent") ){
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gift, 0, 0, 0);
//            txtproduct_price.setText("");
//            account_color_strip.setBackgroundResource(R.color.redcolor);
//        }else if(spinnerNameArray[position].equalsIgnoreCase("Exchange to Diamond") ){
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dimond, 0, 0, 0);
//            txtproduct_price.setVisibility(View.GONE);
//            txtdiamond_unit.setText("");
//            account_color_strip.setBackgroundResource(R.color.redcolor);
//        }else if(spinnerNameArray[position].equalsIgnoreCase("Exchange to Rupee") ){
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            //txtdiamond_unit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);
//            txtproduct_price.setText("");
//            txtproduct_price.setVisibility(View.GONE);
//            account_color_strip.setBackgroundResource(R.color.redcolor);
//        }else{
//            txtproduct_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            txtproduct_price.setText("");
//            account_color_strip.setBackgroundResource(R.color.redcolor);
//        }
     } catch (Exception e){}
        return convertView;
    }

    public void OpenfullsizeImage(String url) {
        See_Full_Image_F see_image_f = new See_Full_Image_F();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        Bundle args = new Bundle();
        args.putSerializable("image_url", url);
        see_image_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
    }

    public void Open_Invoice(String OrderID) {
        Webview_F webview_f = new Webview_F();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        Bundle bundle = new Bundle();
        bundle.putString("url",  Variables.app_domain +  "cp/thankyou/" + OrderID);
        bundle.putString("title", "Invoice");
        webview_f.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Setting_F, webview_f).commit();
    }

}