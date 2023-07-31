package com.telegence.app.TELEGENCE.TEAM.Team_Details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.Functions;
import com.telegence.app.SimpleClasses.Variables;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Team_Expenses_Responses_Adapter extends RecyclerView.Adapter<Team_Expenses_Responses_Adapter.CustomViewHolder > implements Filterable {
    public Context context;
    ArrayList<Team_Expenses_Get_Set> inbox_dataList = new ArrayList<>();
    ArrayList<Team_Expenses_Get_Set> inbox_dataList_filter = new ArrayList<>();
    private OnItemClickListener listener;
    private OnLongItemClickListener longlistener;
    Integer today_day=0;

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(Team_Expenses_Get_Set item);
    }
    public interface OnLongItemClickListener{
        void onLongItemClick(Team_Expenses_Get_Set item);
    }

    public Team_Expenses_Responses_Adapter(Context context, ArrayList<Team_Expenses_Get_Set> user_dataList, OnItemClickListener listener, OnLongItemClickListener longlistener) {
        this.context = context;
        this.inbox_dataList=user_dataList;
        this.inbox_dataList_filter=user_dataList;
        this.listener = listener;
        this.longlistener=longlistener;

        // get the today as a integer number to make the dicision the chat date is today or yesterday
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_doub_resp_list,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return inbox_dataList_filter.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView username,last_message,date_created,last_message1,date_created1;
        ImageView user_image,image,image1;
        LinearLayout incoming, outgoing;

        public CustomViewHolder(View view) {
            super(view);
            user_image=itemView.findViewById(R.id.user_image);
            image=itemView.findViewById(R.id.image);
            image1=itemView.findViewById(R.id.image1);
            username=itemView.findViewById(R.id.username);
            last_message=itemView.findViewById(R.id.message);
            last_message1=itemView.findViewById(R.id.message1);
            date_created=itemView.findViewById(R.id.datetxt);
            date_created1=itemView.findViewById(R.id.datetxt1);

            incoming=itemView.findViewById(R.id.incoming);
            outgoing=itemView.findViewById(R.id.outgoing);
        }

        public void bind(final Team_Expenses_Get_Set item, final OnItemClickListener listener, final  OnLongItemClickListener longItemClickListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final Team_Expenses_Get_Set item=inbox_dataList_filter.get(i);
        holder.last_message.setText(item.getMsg());
        holder.last_message1.setText(item.getMsg());
        holder.date_created.setText(Functions.ChangeDate_to_today_or_yesterday(context,item.getDate()));
        holder.date_created1.setText(Functions.ChangeDate_to_today_or_yesterday(context,item.getDate()));
//        holder.date_created.setText(item.getDate());
        String status = "" + item.getMessage_by();
        if (status.equalsIgnoreCase("student")) {
            holder.incoming.setVisibility(View.GONE);
            holder.outgoing.setVisibility(View.VISIBLE);
        } else {
            holder.incoming.setVisibility(View.VISIBLE);
            holder.outgoing.setVisibility(View.GONE);
        }

        if(item.getMsg()!=null && !item.getMsg().equals("")) {
            Picasso.get().load(Variables.base_url + item.getMsg()).resize(200,200)
                    .placeholder(R.drawable.profile_image_placeholder).into(holder.image);
            Picasso.get().load(Variables.base_url + item.getMsg()).resize(200,200)
                    .placeholder(R.drawable.profile_image_placeholder).into(holder.image1);
        }

        if(item.type.equalsIgnoreCase("image")){
            holder.image.setVisibility(View.VISIBLE);
            holder.image1.setVisibility(View.VISIBLE);
            holder.last_message.setVisibility(View.GONE);
            holder.last_message1.setVisibility(View.GONE);
        } else {
            holder.image.setVisibility(View.GONE);
            holder.image1.setVisibility(View.GONE);
            holder.last_message.setVisibility(View.VISIBLE);
            holder.last_message1.setVisibility(View.VISIBLE);
        }

        //        if (status.equals("0")) {
//            holder.last_message.setTypeface(null, Typeface.BOLD);
//            holder.last_message.setTextColor(context.getResources().getColor(R.color.black));
//            holder.last_message1.setTypeface(null, Typeface.BOLD);
//            holder.last_message1.setTextColor(context.getResources().getColor(R.color.black));
//        } else {
//            holder.last_message.setTypeface(null, Typeface.NORMAL);
//            holder.last_message.setTextColor(context.getResources().getColor(R.color.dark_gray));
//            holder.last_message1.setTypeface(null, Typeface.NORMAL);
//            holder.last_message1.setTextColor(context.getResources().getColor(R.color.dark_gray));
//        }
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
                    ArrayList<Team_Expenses_Get_Set> filteredList = new ArrayList<>();
                    for (Team_Expenses_Get_Set row : inbox_dataList) {

                          if (row.getMsg().toLowerCase().contains(charString.toLowerCase())) {
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
                inbox_dataList_filter = (ArrayList<Team_Expenses_Get_Set>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}