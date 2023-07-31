package com.telegence.app.TELEGENCE.ATTENDANCE.CALENDER;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.telegence.app.Main_Menu.MainMenuActivity;
import com.telegence.app.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.telegence.app.R;
import com.telegence.app.SimpleClasses.ApiRequest;
import com.telegence.app.SimpleClasses.Callback;
import com.telegence.app.SimpleClasses.Fragment_Callback;
import com.telegence.app.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import xdroid.toaster.Toaster;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Calender extends RootFragment {

    View view;
    Context context;
    TextView txt_present,txt_absent,txt_late,txt_date,txt_check_in,txt_check_out;
    CalendarView simpleCalendarView;
    long selectedDate;
    public Fragment_Calender() {
        // Required empty public constructor
    }
    Fragment_Callback fragment_callback;
    public Fragment_Calender(Fragment_Callback fragment_callback) {
        this.fragment_callback  = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_attendance, container, false);
        view.setClickable(true);
        context=getContext();
        MainMenuActivity.mainbar.setText("Attendance");

        txt_present = view.findViewById(R.id.txt_present);
        txt_absent = view.findViewById(R.id.txt_absent);
        txt_late = view.findViewById(R.id.txt_late);
        txt_date = view.findViewById(R.id.txt_date);
        txt_check_in = view.findViewById(R.id.txt_check_in);
        txt_check_out = view.findViewById(R.id.txt_check_out);

        simpleCalendarView = view.findViewById(R.id.view_calender);
        simpleCalendarView.setMaxDate(System.currentTimeMillis());
        simpleCalendarView.setWeekSeparatorLineColor(Color.GREEN);
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                txt_check_in.setText("00:00:00");
                txt_check_out.setText("00:00:00");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date parse = null;
                try {
                    parse = sdf.parse(dayOfMonth + "/" + (month + 1)  +"/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(parse);

                Date d = new Date(c.getTimeInMillis());
                SimpleDateFormat df = new SimpleDateFormat("dd MMMM YYYY", Locale.US);
                String time = df.format(d);
                String i="";
                int dayofWeek = c.get(Calendar.DAY_OF_WEEK);
                if (dayofWeek == 1) {
                    i = "Sunday";
                } else if (dayofWeek == 2) {
                    i = "Monday";
                } else if (dayofWeek == 3) {
                    i = "Tuesday";
                } else if (dayofWeek == 4) {
                    i = "Wednesday";
                } else if (dayofWeek == 5) {
                    i = "Thursday";
                } else if (dayofWeek == 6) {
                    i = "Friday";
                } else if (dayofWeek == 7) {
                    i = "Saturday";
                }
                txt_date.setText(i + ", " +time);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/");
                SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd");
                LocalDate date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int days = 0;

                    if(simpleDateFormat3.format(new Date()).equalsIgnoreCase(simpleDateFormat3.format(d))
                        && simpleDateFormat2.format(new Date()).equalsIgnoreCase(simpleDateFormat2.format(d))) {
                        days = Integer.parseInt(simpleDateFormat3.format(new Date()));
                    } else{
                        date = LocalDate.of(Integer.parseInt(simpleDateFormat4.format(d)),
                                Integer.parseInt(simpleDateFormat2.format(d)),
                                Integer.parseInt(simpleDateFormat3.format(d)));
                        days = date.lengthOfMonth();
                    }
                    Call_api(simpleDateFormat.format(d),String.valueOf(days),simpleDateFormat1.format(d),simpleDateFormat2.format(d));
                }

            }
        });
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE, dd MMMM yyyy");
        txt_date.setText(simpleDateFormat1.format(new Date()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy/MM/");

        Call_api(simpleDateFormat.format(new Date()), simpleDateFormat4.format(new Date()),simpleDateFormat3.format(new Date()),simpleDateFormat2.format(new Date()));
        // get selected date in milliseconds
        return view;
    }


    public void Call_api(String Date,String Date1,String Date2,String Date3){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("fb_id", Variables.user_id);
            jsonObject.put("att_date", Date);
            jsonObject.put("att_date1", Date1);
            jsonObject.put("att_date2", Date2);
            jsonObject.put("att_date3", Date3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.view_att_time, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    JSONObject jsonObject=new JSONObject(resp);
                    String code = jsonObject.optString("code");
                    if(code.equals("200")) {
                        JSONArray msg=jsonObject.getJSONArray("msg");
                        JSONObject data = msg.getJSONObject(0);
                        txt_present.setText(data.optString("present"));
                        txt_absent.setText(data.optString("absent"));
                        txt_late.setText(data.optString("late"));
                        txt_check_in.setText(data.optString("check_in_time"));
                        txt_check_out.setText(data.optString("check_out_time"));
                        if(txt_check_out.getText().toString().equalsIgnoreCase(txt_check_in.getText().toString())){
                            txt_check_out.setText("Not Marked");
                        }
                    } else if(code.equals("202")) {
                        JSONArray msg=jsonObject.getJSONArray("msg");
                        JSONObject data = msg.getJSONObject(0);
                        txt_present.setText(data.optString("present"));
                        txt_absent.setText(data.optString("absent"));
                        txt_late.setText(data.optString("late"));
                        txt_check_in.setText(data.optString("check_in_time"));
                        txt_check_out.setText(data.optString("check_out_time"));
                        txt_date.setText("Absent");
                    } else {
                        Toaster.toast(jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fragment_callback!=null){
            fragment_callback.Responce(new Bundle());
        }
    }
}

