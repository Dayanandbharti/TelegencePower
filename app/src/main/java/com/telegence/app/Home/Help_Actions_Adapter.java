package com.telegence.app.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.telegence.app.HelpfulActions_Get_Set;
import com.telegence.app.R;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Help_Actions_Adapter extends RecyclerView.Adapter<Help_Actions_Adapter.CustomViewHolder > {
    public Context context;

    String following_or_fans;
    ArrayList<HelpfulActions_Get_Set> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, HelpfulActions_Get_Set item);
    }

    public OnItemClickListener listener;

    public Help_Actions_Adapter(Context context, String following_or_fans , ArrayList<HelpfulActions_Get_Set> arrayList, OnItemClickListener listener) {
        this.context = context;
        this.following_or_fans=following_or_fans;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_help,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView user_name;
        TextView user_id;
        TextView action_txt;
        RelativeLayout mainlayout;

        public CustomViewHolder(View view) {
            super(view);

            mainlayout=view.findViewById(R.id.mainlayout);

            user_image=view.findViewById(R.id.user_image);
            user_name=view.findViewById(R.id.user_name);
            user_id=view.findViewById(R.id.user_id);
            action_txt=view.findViewById(R.id.action_txt);
        }

        public void bind(final int pos , final HelpfulActions_Get_Set item, final OnItemClickListener listener) {

            mainlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });

            action_txt.setOnClickListener(new View.OnClickListener() {
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
        HelpfulActions_Get_Set item=datalist.get(i);
        holder.user_name.setText(item.action_name);

//        if(item.schemes_res!=null && !item.schemes_res.equalsIgnoreCase("") )
//        Picasso.get()
//                .load(item.schemes_res)
//                .placeholder(R.drawable.circle_bg)
//                .into(holder.user_image);
//
//        holder.user_id.setText(item.scheme_name);
//        holder.action_txt.setText(item.scheme_value);
        holder.bind(i,datalist.get(i),listener);

}

}