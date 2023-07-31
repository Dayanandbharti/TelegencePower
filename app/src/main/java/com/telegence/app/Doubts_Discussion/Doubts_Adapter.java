package com.telegence.app.Doubts_Discussion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.telegence.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Doubts_Adapter extends RecyclerView.Adapter<Doubts_Adapter.CustomViewHolder > {
    public Context context;



    ArrayList<Doubt_Get_Set> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Doubt_Get_Set item);
    }

    public OnItemClickListener listener;

    public Doubts_Adapter(Context context, ArrayList<Doubt_Get_Set> arrayList, OnItemClickListener listener) {
        this.context = context;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_doubts,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {


        SimpleDraweeView product_image;
        GridView grdbatch;
        RecyclerView recylerview_transactions;
        TextView _name,message,watch_btn,txt_add;
        TextView txtinvested,txtpending,txtttl;
        View view_main,view_Details;
        public CustomViewHolder(View view) {
            super(view);
            this.setIsRecyclable(false);
            product_image = view.findViewById(R.id.productimage);
            grdbatch = view.findViewById(R.id.grdbatch);
            recylerview_transactions =view.findViewById(R.id.recylerview_transactions);
            txt_add = view.findViewById(R.id.txt_add);
            _name = view.findViewById(R.id.product_name);
            message = view.findViewById(R.id.product_price);
            watch_btn = view.findViewById(R.id.diamond_unit);
            txtinvested = view.findViewById(R.id.txtinvested);
            txtpending = view.findViewById(R.id.txtpending);
            view_main = view.findViewById(R.id.view_main);
            view_Details = view.findViewById(R.id.view_Details);
            txtttl = view.findViewById(R.id.txtttl);
        }

        public void bind(final int pos , final Doubt_Get_Set item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });

            txt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        final Doubt_Get_Set item=datalist.get(i);
        holder._name.setText(item.scheme.scheme_name);
        if(item.scheme.schemes_res!=null && !item.scheme.schemes_res.equals("")) {
            Picasso.get().load(item.scheme.schemes_res).placeholder(R.color.transparent).into(holder.product_image);
        }
        holder.watch_btn.setText(item.created);
        if(!item.order_status.equalsIgnoreCase("0") || !item.mainstatus.equalsIgnoreCase("0")){
            holder.txt_add.setVisibility(View.GONE);
        }
        if(item.remaining.equalsIgnoreCase("0")){
            holder.txt_add.setText("Redeem");
        }

        if(!item.mainstatus.equalsIgnoreCase("0")){
            holder.txt_add.setVisibility(View.GONE);
        }
        if(item.remaining.equalsIgnoreCase("0") && !item.order_status.equalsIgnoreCase("0")){
            holder.txt_add.setVisibility(View.VISIBLE);
            holder.txt_add.setText("Edit");
        }

        Double amount = Double.parseDouble(item.target)/1000.0;
        Double remainingamount = Double.parseDouble(item.remaining)/1000.0;
        Double completed = Double.parseDouble(item.completed)/1000.0;
        Double perg = Double.parseDouble(item.gold_price);
        holder.message.setText(completed + "/");
        holder.txtinvested.setText(item.Invested_Amt + "(" + completed +  " g)");
        holder.txtinvested.setText(item.Invested_Amt);
        holder.txtttl.setText((amount * perg) + "(" + amount +  " g)");
        holder.txtttl.setText(String.valueOf(amount * perg));
        holder.txtpending.setText(String.valueOf(remainingamount * perg));
        holder.txtpending.setText(String.valueOf(remainingamount * perg));

//        holder.grdbatch.setAdapter(null);
//        Order_transactions_Adapter myAdapter = new Order_transactions_Adapter(context,item.transactions);
//        holder.grdbatch.setAdapter(myAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recylerview_transactions.setLayoutManager(layoutManager);
        holder.recylerview_transactions.setHasFixedSize(true);
        Doubt_transactions_Adapter_RecyclerView order_transactions_adapter_recyclerView = new Doubt_transactions_Adapter_RecyclerView(context,item.transactions);
        holder.recylerview_transactions.setAdapter(order_transactions_adapter_recyclerView);
//
//        if(item.remaining.equalsIgnoreCase("0") && !item.order_status.equalsIgnoreCase("0")){
//            holder.view_main.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_yellow));
//            /*left.setText("P");
//            left.setTextColor(ContextCompat.getColorStateList(activity, R.color.tx_success_bg));
//            left.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.light_g));*/
//        }
//        if(!item.mainstatus.equalsIgnoreCase("0")){
//            holder.view_main.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_g));
//            holder.view_Details.setVisibility(View.GONE);
//        }

        //        if(item.type.equalsIgnoreCase("comment_video")){
        //            holder.message.setText(item.first_name+" have comment on your video");
        //            if(item.follow_status.equals("0"))
        //                holder.watch_btn.setText("+ Follow");
        //            else
        //                holder.watch_btn.setText("Friend");
        //
        //            holder.watch_btn.setVisibility(View.VISIBLE);
        //        }
        //        else if(item.type.equalsIgnoreCase("video_like")) {
        //            holder.message.setText(item.first_name + " liked your video.");
        //            if(item.follow_status.equals("0"))
        //                holder.watch_btn.setText("+ Follow");
        //            else
        //                holder.watch_btn.setText("Friend");
        //
        //            holder.watch_btn.setVisibility(View.VISIBLE);
        //        } else if(item.type.equalsIgnoreCase("comment_moment")){
        //            holder.message.setText(item.first_name+" have comment on your moment");
        //            if(item.follow_status.equals("0"))
        //                holder.watch_btn.setText("+ Follow");
        //            else
        //                holder.watch_btn.setText("Friend");
        //
        //            holder.watch_btn.setVisibility(View.VISIBLE);
        //        }
        //        else if(item.type.equalsIgnoreCase("moment_like")) {
        //            holder.message.setText(item.first_name + " liked your moment.");
        //            if(item.follow_status.equals("0"))
        //                holder.watch_btn.setText("+ Follow");
        //            else
        //                holder.watch_btn.setText("Friend");
        //
        //            holder.watch_btn.setVisibility(View.VISIBLE);
        //        }
        //        else if(item.type.equalsIgnoreCase("following_you")) {
        //
        //            /*
        //            if(item.follow_status.equals("0"))
        //                holder.message.setText(item.first_name + " following you.");
        //            else
        //                holder.message.setText(item.first_name + " followed back.");
        //            */
        //            holder.message.setText(item.first_name + " following you.");
        //            //holder.watch_btn.setVisibility(View.GONE);
        //            if(item.follow_status.equals("0"))
        //                holder.watch_btn.setText("+ Follow");
        //            else
        //                holder.watch_btn.setText("Friend");
        //
        //            holder.watch_btn.setVisibility(View.VISIBLE);
        //
        //        }

        holder.bind(i,datalist.get(i),listener);

}

}