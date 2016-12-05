package com.mingjiang.android.app.memo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mingjiang.android.app.memo.MemoAddActivity;
import com.mingjiang.android.app.memo.MemoDetailActivity;
import com.mingjiang.android.app.memo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hasee on 2016/11/28.
 */

public class todayTaskAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public Context context;
    public List<Map<String,String>> taskList =new ArrayList<>();

    private Activity activity;
    public todayTaskAdapter(Context context, List<Map<String,String>> taskList,Activity activity){
        inflater = LayoutInflater.from(context);
        this.taskList=taskList;
        this.context=context;
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int i) {
        return taskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Map<String,String> taskMap=new HashMap<>();
        ViewHolder viewHolder=null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_task, null);

            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }

        taskMap=taskList.get(i);
        String taskDetail=taskMap.get("taskDetail");
        String taskStatus=taskMap.get("taskStatus");
        String taskTime=taskMap.get("taskTime");
        if(taskStatus.equalsIgnoreCase("VALID")){
            viewHolder.txtContent.setTextColor(Color.GRAY);
        }
        viewHolder.txtContent.setText(taskDetail);
        viewHolder.txttime.setText(taskTime);
        final Map<String, String> finalTaskMap = taskMap;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MemoDetailActivity.class);
                intent.putExtra("taskId", finalTaskMap.get("taskId"));
                intent.putExtra("taskDetail", finalTaskMap.get("taskDetail"));
                intent.putExtra("taskStatus", finalTaskMap.get("taskStatus"));
                activity.startActivity(intent);
                //Toast.makeText(context, finalTaskMap.get("taskDetail"),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    static class ViewHolder {
        TextView txtContent;
        TextView txttime;
        ViewHolder(View view) {
            txtContent=(TextView) view.findViewById(R.id.task_item);
            txttime=(TextView) view.findViewById(R.id.task_list_time);
        }
    }
}
