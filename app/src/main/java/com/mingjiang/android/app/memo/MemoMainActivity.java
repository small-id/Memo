package com.mingjiang.android.app.memo;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mingjiang.android.app.memo.adapter.todayTaskAdapter;
import com.mingjiang.android.app.memo.sqlite.DatabaseHelper;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.SimpleFormatter;

public class MemoMainActivity extends AppCompatActivity {
    ListView memoDateList;
    todayTaskAdapter todayTask;
    TextView addcommit;
    Button selectdate;
    String selectedDate;
    TextView title;
    public static  List<Map<String,String>> taskList;
    int hintYear;
    int hintMonth;
    int hintDay;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_main);
        memoDateList=(ListView) findViewById(R.id.mome_date_list);
        addcommit=(TextView)findViewById(R.id.add_list) ;
        selectdate=(Button) findViewById(R.id.select_date);
        title=(TextView)findViewById(R.id.title);
        Long aaa=System.currentTimeMillis();

        //获取当前日期的年月日
        Calendar calendar = Calendar.getInstance();
        hintYear=calendar.get(Calendar.YEAR);
        hintMonth=calendar.get(Calendar.MONTH) + 1;
        hintDay=calendar.get(Calendar.DAY_OF_MONTH);

        startService(new Intent(this,NotificationService.class));

        Intent intent = getIntent();
        String addTaskDay = intent.getStringExtra("addTaskDay");
        if(addTaskDay!=null){
            String[] addTaskDayStr=addTaskDay.split("-");
            hintYear=Integer.parseInt(addTaskDayStr[0]);
            hintMonth=Integer.parseInt(addTaskDayStr[1]);
            hintDay=Integer.parseInt(addTaskDayStr[2]);
        }
        String initeDay = hintYear + "-" + hintMonth +"-" +  hintDay;
        taskList=new ArrayList<>();
        DatabaseHelper database = new DatabaseHelper(this);//这段代码放到Activity类中才用this
        SQLiteDatabase db = null;
        db = database.getReadableDatabase();
        title.setText(getTitleFormat(initeDay));
        Cursor c = db.query("task",null,"taskDate=?", new String[]{initeDay},null,null,null);//查询并获得游标
        while (c.moveToNext()){
            Map<String,String> taskMap=new HashMap<>();
            String taskDetail=c.getString(c.getColumnIndex("taskDetail"));
            String taskStatus=c.getString(c.getColumnIndex("taskStatus"));
            String taskTime=c.getString(c.getColumnIndex("taskTime"));
            int taskId=c.getInt(c.getColumnIndex("taskId"));
            taskMap.put("taskDetail",taskDetail);
            taskMap.put("taskStatus",taskStatus);
            taskMap.put("taskTime",taskTime);
            taskMap.put("taskId",taskId + "");
            taskList.add(taskMap);
        }

        todayTask=new todayTaskAdapter(this,taskList,this);
        memoDateList.setAdapter(todayTask);
        addcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addTaskDay=title.getText().toString();
                String[] addTaskDayStr=addTaskDay.split("  ");
                addTaskDay=addTaskDayStr[0];
                Intent intent = new Intent(MemoMainActivity.this, MemoAddActivity.class);
                intent.putExtra("addTaskDay",addTaskDay);
                startActivity(intent);
            }
        });
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDlg();
            }
        });
    }
    private void setTaskList(String showDate){
        taskList=new ArrayList<>();
        DatabaseHelper database = new DatabaseHelper(this);//这段代码放到Activity类中才用this
        SQLiteDatabase db = null;
        db = database.getReadableDatabase();
        title.setText(getTitleFormat(showDate));
        Cursor c = db.query("task",null,"taskDate=?", new String[]{showDate},null,null,null);//查询并获得游标
        while (c.moveToNext()){
            Map<String,String> taskMap=new HashMap<>();
            String taskDetail=c.getString(c.getColumnIndex("taskDetail"));
            String taskStatus=c.getString(c.getColumnIndex("taskStatus"));
            String taskTime=c.getString(c.getColumnIndex("taskTime"));
            int taskId=c.getInt(c.getColumnIndex("taskId"));
            taskMap.put("taskDetail",taskDetail);
            taskMap.put("taskStatus",taskStatus);
            taskMap.put("taskTime",taskTime);
            taskMap.put("taskId",taskId + "");
            taskList.add(taskMap);
        }

        todayTask=new todayTaskAdapter(this,taskList,this);
        memoDateList.setAdapter(todayTask);
    }
    private String getTitleFormat(String showDate){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        /*showDate=showDate.replace("年","-");
        showDate=showDate.replace("月","-");
        showDate=showDate.replace("日","");*/
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(showDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String wday=calendar.get(Calendar.DAY_OF_WEEK) + "";
        String weekday="";
        if("1".equals(wday)){
            weekday ="天";
        }else if("2".equals(wday)){
            weekday ="一";
        }else if("3".equals(wday)){
            weekday ="二";
        }else if("4".equals(wday)){
            weekday ="三";
        }else if("5".equals(wday)){
            weekday ="四";
        }else if("6".equals(wday)){
            weekday ="五";
        }else if("7".equals(wday)){
            weekday ="六";
        }
        String returnTitle=showDate + "  星期" + weekday;
        return returnTitle;
    }

    protected void showDatePickDlg() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MemoMainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                hintYear=year;
                hintMonth=monthOfYear +1;
                hintDay=dayOfMonth;
                selectedDate=hintYear + "-" + hintMonth + "-" + hintDay;
                setTaskList(selectedDate);
            }
        }, hintYear, hintMonth-1, hintDay);
        datePickerDialog.show();
    }
}
