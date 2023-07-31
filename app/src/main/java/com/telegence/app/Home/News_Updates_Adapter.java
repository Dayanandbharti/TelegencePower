package com.telegence.app.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.telegence.app.R;
import com.telegence.app.News_Updates_Get_Set;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class News_Updates_Adapter extends RecyclerView.Adapter<News_Updates_Adapter.CustomViewHolder > {
    public Context context;

    String following_or_fans;

    ArrayList<News_Updates_Get_Set> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, News_Updates_Get_Set item);
    }

    public OnItemClickListener listener;

    public News_Updates_Adapter(Context context, String following_or_fans , ArrayList<News_Updates_Get_Set> arrayList, OnItemClickListener listener) {
        this.context = context;
        this.following_or_fans=following_or_fans;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
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

        public void bind(final int pos , final News_Updates_Get_Set item, final OnItemClickListener listener) {

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

        News_Updates_Get_Set item=datalist.get(i);

        holder.user_name.setText(item.news_name);

        if(item.news_res!=null && !item.news_res.equalsIgnoreCase("") )
        Picasso.get()
                .load(item.news_res)
                .placeholder(R.drawable.circle_bg)
                .into(holder.user_image);

        holder.user_id.setText(item.news_value);
        holder.action_txt.setText(item.news_details);
        holder.bind(i,datalist.get(i),listener);

}

}