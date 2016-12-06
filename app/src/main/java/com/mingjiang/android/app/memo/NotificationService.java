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

import static com.mingjiang.android.app.memo.MemoMainActivity.taskList;

public class NotificationService extends Service {

	// 获取消息线程
	private MessageThread messageThread = null;

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

		messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		messageIntent = new Intent(this, MemoMainActivity.class);
		messagePendingIntent = PendingIntent.getActivity(this, 0,
				messageIntent, 0);



		// 开启线程
		messageThread = new MessageThread();
		messageThread.isRunning = true;
		messageThread.start();
		return START_STICKY;
	}

	/**
	 * 从服务器端获取消息
	 * 
	 */
	class MessageThread extends Thread {
		// 设置是否循环推送
		public boolean isRunning = true;

		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		public void run() {
			// while (isRunning) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
				//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
				String nowTime=df.format(new Date());
				Toast.makeText(NotificationService.this,df.format(new Date()), Toast.LENGTH_SHORT).show();
				for(int i=0;i<taskList.size();i++){
					Map<String,String> taskMap= new HashMap<>();
					taskMap=taskList.get(i);

					String taskTime=taskMap.get("taskTime");
					String taskDetail=taskMap.get("taskDetail");

					if(taskTime.equalsIgnoreCase(nowTime)){
						messageNotification = new Notification.Builder(NotificationService.this)
								.setSmallIcon(R.mipmap.ic_launcher)
								.setTicker("This is ticker text")
								.setWhen(System.currentTimeMillis())
								.setContentIntent(messagePendingIntent)
								.setContentTitle("有一条备忘事项！")
								.setContentText(taskDetail)
								.build();
						// 更新通知栏
						messageNotificatioManager.notify(messageNotificationID,messageNotification);
						// 每次通知完，通知ID递增一下，避免消息覆盖掉
						messageNotificationID++;
					}
				}
				// 间隔时间
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// }
		}
	}

	@Override
	public void onDestroy() {
		// System.exit(0);
		messageThread.isRunning = false;
		super.onDestroy();
	}

	/**
	 * 模拟发送消息
	 * 
	 * @return 返回服务器要推送的消息，否则如果为空的话，不推送
	 */
	public String getServerMessage() {
		return "NEWS!";
	}
}