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
import com.telegence.app.Scheme_Get_Set;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Home_Scheme_Adapter extends RecyclerView.Adapter<Home_Scheme_Adapter.CustomViewHolder > {
    public Context context;

    String following_or_fans;

    ArrayList<Scheme_Get_Set> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Scheme_Get_Set item);
    }

    public Home_Scheme_Adapter.OnItemClickListener listener;

    public Home_Scheme_Adapter(Context context, String following_or_fans , ArrayList<Scheme_Get_Set> arrayList, Home_Scheme_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.following_or_fans=following_or_fans;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public Home_Scheme_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Home_Scheme_Adapter.CustomViewHolder viewHolder = new Home_Scheme_Adapter.CustomViewHolder(view);
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

        public void bind(final int pos , final Scheme_Get_Set item, final Home_Scheme_Adapter.OnItemClickListener listener) {

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
    public void onBindViewHolder(final Home_Scheme_Adapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        Scheme_Get_Set item=datalist.get(i);

        holder.user_name.setText(item.scheme_name);

        if(item.schemes_res!=null && !item.schemes_res.equalsIgnoreCase("") ) {
            //Toaster.toast(item.schemes_res);
            Picasso.get()
                    .load(item.schemes_res)
                    .placeholder(R.color.gray_lightest)
                    .into(holder.user_image);
        }

        holder.user_id.setText(item.scheme_name);
        holder.action_txt.setText(item.scheme_value);
        holder.bind(i,datalist.get(i),listener);

}

}