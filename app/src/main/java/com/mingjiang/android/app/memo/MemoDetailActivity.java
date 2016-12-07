package com.mingjiang.android.app.memo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mingjiang.android.app.memo.adapter.todayTaskAdapter;
import com.mingjiang.android.app.memo.sqlite.DatabaseHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.mingjiang.android.app.memo.MemoMainActivity.publicTaskId;

/**
 * Created by hasee on 2016/11/29.
 */

public class MemoDetailActivity extends AppCompatActivity {
    EditText taskContent;
    Button tback;
    Button tagain;
    Button tupdate;
    Button tdetete;
    Button adddate;
    TextView addtaskTime;
    Button addtime;
    TextView addtaskDate;
    String taskDetail="";
    String taskDate="";
    String taskTime="";
    String taskId="";
    int hintYear;
    int hintMonth;
    int hintDay;
    int hintHour;
    int hintMinute;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        taskContent=(EditText)findViewById(R.id.task_content);
        adddate=(Button)findViewById(R.id.add_task_date_btn);
        addtime=(Button)findViewById(R.id.add_task_time_btn);
        addtaskTime=(TextView)findViewById(R.id.add_task_time);
        addtaskDate=(TextView)findViewById(R.id.add_task_date);
        tback=(Button)findViewById(R.id.today_list_back);
        tagain=(Button)findViewById(R.id.edit_task_status);
        tupdate=(Button)findViewById(R.id.edit_commit);
        tdetete=(Button)findViewById(R.id.delete_task);
        Intent intent = getIntent();
        //taskId = intent.getStringExtra("taskId");
        taskId=publicTaskId;
        DatabaseHelper database = new DatabaseHelper(this);//这段代码放到Activity类中才用this
        SQLiteDatabase db = null;
        db = database.getReadableDatabase();
        Cursor c = db.query("task",null,"taskId=?", new String[]{taskId},null,null,null);//查询并获得游标
        while (c.moveToNext()){
            Map<String,String> taskMap=new HashMap<>();
            taskDetail=c.getString(c.getColumnIndex("taskDetail"));
            taskDate=c.getString(c.getColumnIndex("taskDate"));
            taskTime=c.getString(c.getColumnIndex("taskTime"));
            break;
        }
        addtaskDate.setText(taskDate);
        addtaskTime.setText(taskTime);
        taskContent.setText(taskDetail);
        //获取已保存日期
        String[] taskDateStr=taskDate.split("-");
        hintYear=Integer.parseInt(taskDateStr[0]);
        hintMonth=Integer.parseInt(taskDateStr[1]);
        hintDay=Integer.parseInt(taskDateStr[2]);
        //获取已保存时间
        String[] taskTimeStr=taskTime.split(":");
        hintHour=Integer.parseInt(taskTimeStr[0]);
        hintMinute=Integer.parseInt(taskTimeStr[1]);
        tback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newcontent=taskContent.getText().toString();
                String newtaskDate=addtaskDate.getText().toString();
                String newtaskTime=addtaskTime.getText().toString();
                DatabaseHelper database = new DatabaseHelper(MemoDetailActivity.this);//这段代码放到Activity类中才用this
                SQLiteDatabase db = null;
                db = database.getReadableDatabase();
                ContentValues cv = new ContentValues();//实例化ContentValues
                cv.put("taskDetail",newcontent);//添加要更改的字段及内容
                cv.put("taskDate",newtaskDate);//添加要更改的字段及内容
                cv.put("taskTime",newtaskTime);//添加要更改的字段及内容
                String whereClause = "taskId=?";//修改条件
                String[] whereArgs = {taskId};//修改条件的参数
                db.update("task",cv,whereClause,whereArgs);//执行修改
                startActivity(new Intent(MemoDetailActivity.this,MemoMainActivity.class));
            }
        });
        tdetete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper database = new DatabaseHelper(MemoDetailActivity.this);//这段代码放到Activity类中才用this
                SQLiteDatabase db = null;
                db = database.getReadableDatabase();
                String whereClause = "taskId=?";//修改条件
                String[] whereArgs = {taskId};//修改条件的参数
                db.delete("task",whereClause,whereArgs);//执行修改
                startActivity(new Intent(MemoDetailActivity.this,MemoMainActivity.class));
            }
        });
        adddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
        addtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickDlg();
            }
        });
    }
    protected void showDatePickDlg() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MemoDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int trueMonth=monthOfYear+1;
                MemoDetailActivity.this.addtaskDate.setText(year + "-" + trueMonth + "-" + dayOfMonth);
            }
        }, hintYear, hintMonth-1, hintDay);
        datePickerDialog.show();

    }
    protected void showTimePickDlg() {
        TimePickerDialog time = new TimePickerDialog(MemoDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                addtaskTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                //Toast.makeText(MemoAddActivity.this, hourOfDay + "hour " + minute + "minute", Toast.LENGTH_SHORT).show();
            }
        }, hintHour, hintMinute, true);
        time.show();
    }
}
