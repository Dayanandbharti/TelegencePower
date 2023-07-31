package com.telegence.app.Orders.Certificate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.telegence.app.R;
import com.telegence.app.Transactions_Get_Set;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order_transactions_Adapter_RecyclerView extends RecyclerView.Adapter<Order_transactions_Adapter_RecyclerView.CustomViewHolder> {

    List<Transactions_Get_Set> transactions_get_set;
    private LayoutInflater thisInflater;
    FragmentActivity activity;

    public Order_transactions_Adapter_RecyclerView(Context con, List<Transactions_Get_Set> transactions_get_set) {
        this.thisInflater = LayoutInflater.from(con);
        this.transactions_get_set = transactions_get_set;
    }

    @NonNull
    @Override
    public Order_transactions_Adapter_RecyclerView.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_request_transactions,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Order_transactions_Adapter_RecyclerView.CustomViewHolder viewHolder = new Order_transactions_Adapter_RecyclerView.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final Transactions_Get_Set transactions = transactions_get_set.get(position);

        holder.txtserial.setText(String.valueOf(position + 1));
        holder.txtproduct_rate.setText(transactions.gold_price + "/g");
        holder.txtproduct_price.setText(transactions.trans_amount + "\n(" + String.valueOf(Double.parseDouble(transactions.gold_qty)/1000)  + " g)");
        holder.txtproduct_price.setText(transactions.trans_amount);
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

        holder.txtproduct_ID.setText(dateString);
        holder.textHeading.setText(dateString);
        if(transactions.trans_status.equalsIgnoreCase("0")){
            holder.txtdiamond_unit.setText("Pending");
            //txtdiamond_unit.setTextColor(ContextCompat.getColorStateList(activity, R.color.app_sec_color));
        } else if(transactions.trans_status.equalsIgnoreCase("1")){
            holder.txtdiamond_unit.setText("Approved");
            //txtdiamond_unit.setTextColor(ContextCompat.getColorStateList(activity, R.color.tx_success_bg));
        } else if(transactions.trans_status.equalsIgnoreCase("2")){
            holder.txtdiamond_unit.setText("Rejected");
            //txtdiamond_unit.setTextColor(ContextCompat.getColorStateList(activity, R.color.redcolor));
        }

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return transactions_get_set.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = thisInflater.inflate(R.layout.item_request_transactions, parent, false);
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


    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView txtserial,txtproduct_ID,txtproduct_rate,textHeading,left,txtproduct_price,txtdiamond_unit;
        View account_color_strip;

        public CustomViewHolder(View convertView) {
            super(convertView);
            txtserial = (TextView) convertView.findViewById(R.id.serial);
            txtproduct_ID = (TextView) convertView.findViewById(R.id.product_ID);
            txtproduct_rate = (TextView) convertView.findViewById(R.id.product_rate);
            textHeading = (TextView) convertView.findViewById(R.id.product_name);
            left = (TextView) convertView.findViewById(R.id.left);
            txtproduct_price = (TextView) convertView.findViewById(R.id.product_price);
            txtdiamond_unit = (TextView) convertView.findViewById(R.id.diamond_unit);
            account_color_strip= (View) convertView.findViewById(R.id.account_color_strip);
        }

    }
}