package com.mingjiang.android.app.memo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.mingjiang.android.app.memo.MemoMainActivity.publicTaskId;
import static com.mingjiang.android.app.memo.MemoMainActivity.taskList;

public class NotificationService extends Service {

	// 点击查看
	private Intent messageIntent = null;
	private PendingIntent messagePendingIntent = null;

	// 通知栏消息
	private int messageNotificationID = 1000;
	private Notification messageNotification = null;
	private NotificationManager messageNotificatioManager = null;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent,flags,startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


		Timer timer=new Timer();
		timer.schedule(new noficationshow(),1,1000*60);
	}

	public class noficationshow extends TimerTask{
		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		public void run(){
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
			//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
			String nowTime=df.format(new Date());
			for(int i=0;i<taskList.size();i++){
				Map<String,String> taskMap= new HashMap<>();
				taskMap=taskList.get(i);

				String taskTime=taskMap.get("taskTime");

				if(taskTime.equalsIgnoreCase(nowTime)){
					publicTaskId=taskMap.get("taskId");
					messageIntent = new Intent(NotificationService.this, MemoDetailActivity.class);
					//messageIntent.putExtra("taskId",taskId);
					messagePendingIntent = PendingIntent.getActivity(NotificationService.this, 0, messageIntent, 0);

					String taskDetail=taskMap.get("taskDetail");
					messageNotification = new Notification.Builder(NotificationService.this)
							.setSmallIcon(R.mipmap.ic_launcher)
							.setTicker("有一条备忘事项！")
							.setWhen(System.currentTimeMillis())
							.setContentIntent(messagePendingIntent)
							.setContentTitle("有一条备忘事项！")
							.setContentText(taskDetail)
							.setAutoCancel(true)
							.build();
					// 更新通知栏
					//startForeground(1, messageNotification);
					messageNotificatioManager.notify(messageNotificationID,messageNotification);
					// 每次通知完，通知ID递增一下，避免消息覆盖掉
					messageNotificationID++;
				}
			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}