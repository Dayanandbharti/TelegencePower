package com.telegence.app.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.telegence.app.R;
import com.telegence.app.See_Full_Image_F;
import com.telegence.app.SimpleClasses.Variables;
import com.telegence.app.SimpleClasses.Webview_F;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Refer_user_Adapter extends BaseAdapter {

    List<Refer_Get_Set> Refer_Get_Set;
    private LayoutInflater thisInflater;
    FragmentActivity activity;

    public Refer_user_Adapter(Context con, List<Refer_Get_Set> Refer_Get_Set) {
        this.thisInflater = LayoutInflater.from(con);
        this.Refer_Get_Set = Refer_Get_Set;
    }

    @Override
    public int getCount() {
        return Refer_Get_Set.size();
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
        convertView = thisInflater.inflate(R.layout.item_refers_transactions, parent, false);
        convertView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        TextView txtserial = (TextView) convertView.findViewById(R.id.serial);
        TextView txtproduct_ID = (TextView) convertView.findViewById(R.id.product_ID);
        TextView textHeading = (TextView) convertView.findViewById(R.id.product_name);
        TextView txtsubscribed = (TextView) convertView.findViewById(R.id.subscribed_text);
        TextView left = convertView.findViewById(R.id.left);
        int val = position+1;
        left.setText(String.valueOf(val));
        SimpleDraweeView simpleDraweeView = convertView.findViewById(R.id.productimage);
        TextView txtproduct_price = (TextView) convertView.findViewById(R.id.product_price);
        TextView txtdiamond_unit = (TextView) convertView.findViewById(R.id.diamond_unit);
        View account_color_strip= (View) convertView.findViewById(R.id.account_color_strip);
        Refer_Get_Set transactions = Refer_Get_Set.get(position);
        if (transactions.profile_pic  != null && !transactions.profile_pic.equalsIgnoreCase(""))
            Picasso.get().load(transactions.profile_pic).placeholder(R.drawable.profile_image_placeholder).into(simpleDraweeView);
        txtserial.setText(String.valueOf(position + 1));
        txtproduct_price.setText(transactions.fb_id);


        String dateString = transactions.created;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            long date = convertedDate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy\n h:mm a");
            dateString = sdf.format(date);
        } catch (ParseException e) {}
        txtproduct_ID.setText(dateString);
        textHeading.setText(transactions.first_name + " " + transactions.last_name);
        if(transactions.amount.equalsIgnoreCase("0")){
            txtdiamond_unit.setText("Pending");
            if(!transactions.subscribed.equalsIgnoreCase("")) {
                txtsubscribed.setText("plan is " + transactions.subscribed);
            }
        } else {
            txtdiamond_unit.setText(transactions.amount);
            txtsubscribed.setText("plan is " + transactions.scheme_name);
        }

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