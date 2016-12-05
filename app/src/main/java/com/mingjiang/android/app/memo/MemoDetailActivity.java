package com.mingjiang.android.app.memo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mingjiang.android.app.memo.adapter.todayTaskAdapter;
import com.mingjiang.android.app.memo.sqlite.DatabaseHelper;

/**
 * Created by hasee on 2016/11/29.
 */

public class MemoDetailActivity extends AppCompatActivity {
    EditText taskContent;
    Button tback;
    Button tagain;
    Button tupdate;
    Button tdetete;
    String taskId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        taskContent=(EditText)findViewById(R.id.task_content);

        tback=(Button)findViewById(R.id.today_list_back);
        tagain=(Button)findViewById(R.id.edit_task_status);
        tupdate=(Button)findViewById(R.id.edit_commit);
        tdetete=(Button)findViewById(R.id.delete_task);
        Intent intent = getIntent();
        String taskDetail = intent.getStringExtra("taskDetail");
        String taskStatus = intent.getStringExtra("taskStatus");
        taskId = intent.getStringExtra("taskId");
        taskContent.setText(taskDetail);
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
                DatabaseHelper database = new DatabaseHelper(MemoDetailActivity.this);//这段代码放到Activity类中才用this
                SQLiteDatabase db = null;
                db = database.getReadableDatabase();
                ContentValues cv = new ContentValues();//实例化ContentValues
                cv.put("taskDetail",newcontent);//添加要更改的字段及内容
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
    }
}
