package com.mingjiang.android.app.memo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.mingjiang.android.app.memo.adapter.todayTaskAdapter;
import com.mingjiang.android.app.memo.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoSelectDayActivity extends AppCompatActivity {
    ListView memoDateList;
    todayTaskAdapter todayTask;
    TextView addcommit;
    Button selectdate;
    String selectedDate;
    List<Map<String,String>> taskList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_main);
        memoDateList=(ListView) findViewById(R.id.mome_date_list);
        addcommit=(TextView)findViewById(R.id.add_list) ;
        selectdate=(Button) findViewById(R.id.select_date);
        DatabaseHelper database = new DatabaseHelper(this);//这段代码放到Activity类中才用this
        SQLiteDatabase db = null;
        db = database.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        String todayDat = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) +" 月" +  calendar.get(Calendar.DAY_OF_MONTH) + "日";
        Cursor c = db.query("task",null,"taskDate=?", new String[]{todayDat},null,null,null);//查询并获得游标
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
                Intent intent = new Intent(MemoSelectDayActivity.this, MemoAddActivity.class);
                startActivity(intent);
            }
        });
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sDate = getSelectDate();

            }
        });
    }
    private String getSelectDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MemoSelectDayActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //MemoMainActivity.this.addtaskDate.setText(year + "年" + monthOfYear + "月" + dayOfMonth+ "日");
                selectedDate = year + "年" + monthOfYear + "月" + dayOfMonth+ "日";
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        return selectedDate;
    }
}
