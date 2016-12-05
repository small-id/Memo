package com.mingjiang.android.app.memo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mingjiang.android.app.memo.sqlite.DatabaseHelper;

import java.util.Calendar;

/**
 * Created by hasee on 2016/11/29.
 */

public class MemoAddActivity extends AppCompatActivity {
    EditText addtaskContent;
    Button giveupadd;
    Button addcommit;
    Button adddate;
    TextView addtaskTime;
    Button addtime;
    TextView addtaskDate;
    int hintYear;
    int hintMonth;
    int hintDay;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);
        addtaskContent=(EditText)findViewById(R.id.add_task_content);
        addtaskContent.setFocusable(true);
        giveupadd=(Button)findViewById(R.id.giveup_add);
        addcommit=(Button)findViewById(R.id.add_commit);
        adddate=(Button)findViewById(R.id.add_task_date_btn);
        addtime=(Button)findViewById(R.id.add_task_time_btn);
        addtaskTime=(TextView)findViewById(R.id.add_task_time);
        addtaskDate=(TextView)findViewById(R.id.add_task_date);
        Intent intent = getIntent();
        final String addTaskDay = intent.getStringExtra("addTaskDay");
        //Calendar calendar = Calendar.getInstance();
        //String todayDat = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) +" 月" +  calendar.get(Calendar.DAY_OF_MONTH) + "日";
        addtaskDate.setText(addTaskDay);
        addtaskTime.setText("08:00");
        String[] addTaskDayStr=addTaskDay.split("-");
        hintYear=Integer.parseInt(addTaskDayStr[0]);
        hintMonth=Integer.parseInt(addTaskDayStr[1]);
        hintDay=Integer.parseInt(addTaskDayStr[2]);
        giveupadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addtask=addtaskContent.getText().toString();
                DatabaseHelper database = new DatabaseHelper(MemoAddActivity.this);//这段代码放到Activity类中才用this
                SQLiteDatabase db = null;
                db = database.getReadableDatabase();
                ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据
                String taskDate= addtaskDate.getText().toString();
                String taskTime= addtaskTime.getText().toString();

                cv.put("taskDate",taskDate);
                cv.put("taskTime",taskTime);
                cv.put("taskDetail",addtask);
                cv.put("taskStatus","VALID");
                db.insert("task",null,cv);//执行插入操作
                Intent intent = new Intent(MemoAddActivity.this, MemoMainActivity.class);
                intent.putExtra("addTaskDay", addTaskDay);
                startActivity(intent);
                //startActivity(new Intent(MemoAddActivity.this,MemoMainActivity.class));

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
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MemoAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int trueMonth=monthOfYear+1;
                MemoAddActivity.this.addtaskDate.setText(year + "-" + trueMonth + "-" + dayOfMonth);
            }
        }, hintYear, hintMonth-1, hintDay);
        datePickerDialog.show();

    }
    protected void showTimePickDlg() {
        TimePickerDialog time = new TimePickerDialog(MemoAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                addtaskTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                //Toast.makeText(MemoAddActivity.this, hourOfDay + "hour " + minute + "minute", Toast.LENGTH_SHORT).show();
            }
        }, 8, 00, true);
        time.show();
    }
}
