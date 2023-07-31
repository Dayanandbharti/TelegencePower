package com.telegence.app.TELEGENCE.TEAM.Expenses;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.telegence.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Team_Expenses_Adapter extends RecyclerView.Adapter<Team_Expenses_Adapter.CustomViewHolder > implements Filterable {
    public Context context;
    ArrayList<Team_Expensess_Get_Set> inbox_dataList = new ArrayList<>();
    ArrayList<Team_Expensess_Get_Set> inbox_dataList_filter = new ArrayList<>();
    private OnItemClickListener listener;
    private OnLongItemClickListener longlistener;
    Integer today_day=0;

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(Team_Expensess_Get_Set item);
    }
    public interface OnLongItemClickListener{
        void onLongItemClick(Team_Expensess_Get_Set item);
    }

    public Team_Expenses_Adapter(Context context, ArrayList<Team_Expensess_Get_Set> user_dataList, OnItemClickListener listener, OnLongItemClickListener longlistener) {
        this.context = context;
        this.inbox_dataList=user_dataList;
        this.inbox_dataList_filter=user_dataList;
        this.listener = listener;
        this.longlistener=longlistener;
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_expense_list,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return inbox_dataList_filter.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView emp_id,poster_id,poster_name,username,last_message,date_created;
        ImageView user_image,status_image;

        public CustomViewHolder(View view) {
            super(view);
            emp_id=itemView.findViewById(R.id.emp_id);
            poster_id=itemView.findViewById(R.id.posted_by_id);
            poster_name=itemView.findViewById(R.id.posted_by_name);
            user_image=itemView.findViewById(R.id.user_image);
            status_image=itemView.findViewById(R.id.status_image);
            username=itemView.findViewById(R.id.username);
            last_message=itemView.findViewById(R.id.message);
            date_created=itemView.findViewById(R.id.datetxt);
        }

        public void bind(final Team_Expensess_Get_Set item, final OnItemClickListener listener, final  OnLongItemClickListener longItemClickListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });


            poster_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });


        }

    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final Team_Expensess_Get_Set item=inbox_dataList_filter.get(i);
        holder.username.setText(item.getName());
        holder.emp_id.setText(item.status);
//        holder.poster_id.setText("View");

        holder.poster_name.setText(item.posted_name);

        holder.last_message.setText(item.getMsg());
        String dateString = item.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            long date = convertedDate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nHH:mm a");
            dateString = sdf.format(date);
        } catch (ParseException e) { e.printStackTrace(); }

        holder.date_created.setText("Date: " + dateString);

        if(item.getPic()!=null && !item.getPic().equals(""))
        Picasso.get().
                load(item.getPic())
                .resize(100,100)
                .placeholder(R.drawable.profile_image_placeholder).into(holder.user_image);

        // check the status like if the message is seen by the receiver or not
        try{
            Integer status = Integer.parseInt(item.getStatus());
            if (status>0) {
                holder.status_image.setImageResource(R.drawable.check_icon);
                //holder.last_message.setTypeface(null, Typeface.BOLD);
                //holder.last_message.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                holder.status_image.setImageResource(R.drawable.cancel_icon);
                //holder.last_message.setTypeface(null, Typeface.NORMAL);
                //holder.last_message.setTextColor(context.getResources().getColor(R.color.dark_gray));
            }
        } catch (Exception e){
            holder.last_message.setTypeface(null, Typeface.NORMAL);
            holder.last_message.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }


        holder.bind(item,listener,longlistener);

   }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inbox_dataList_filter = inbox_dataList;
                } else {
                    ArrayList<Team_Expensess_Get_Set> filteredList = new ArrayList<>();
                    for (Team_Expensess_Get_Set row : inbox_dataList) {

                          if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    inbox_dataList_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inbox_dataList_filter;
                return filterResults;

            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inbox_dataList_filter = (ArrayList<Team_Expensess_Get_Set>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}