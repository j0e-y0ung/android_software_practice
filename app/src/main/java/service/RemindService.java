package service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import editschedule.RemindActivity;
import editschedule.SetActivity;
import temp.DataBase;
import temp.ShareMethod;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.IBinder;

import static android.media.AudioManager.*;

public class RemindService extends Service {
	private Vibrator vibrator;
	//定义保存每张表数据的cursor集合
	Cursor[] cursor = new Cursor[1];
	//保存时间，temp[day][row][hm]表示第day+1个tab选项卡中的第row+1个行中用户输入的第一个（即课程开始）时间拆分为时和分
	//hm为0时表示时，1表示分，2时代表时和分的组合，即未拆分前的字符串
	//将temp数组中的字符串转化为相应的正数，这里去掉了时和分的组合
	int[][][] start_time = new int[7][12][2];

	String[][] date =new String[12][4];//年，月，日 ，年月日
	String[][] time=new String[12][3];
	int [][] sdate=new int[12][3];
	int [][] stime=new int[12][2];
	private static final String Test="Test";

	private int advance_time;

	TimerTask t=new TimerTask() {
		@Override
		public void run() {
			Log.d(Test,"+++++++");
			//取得数据库
			DataBase db = new DataBase(RemindService.this);
			//取出数据库中每日的数据，保存在cursor数组中
			cursor[0] = db.select(7);
			//从数据库取出用户输入的上课的时和分，用来设置课前提醒
			for (int row = 0; row < 12; row++) {
				cursor[0].moveToPosition(row);
				date[row][3] = cursor[0].getString(cursor[0].getColumnIndex("date"));
				if (!date[row][3].equals("")) {
					date[row][2] = date[row][3].substring(12, 14);
					date[row][1] = date[row][3].substring(9, 11);
					date[row][0] = date[row][3].substring(4, 8);
				} else {
					date[row][0] = date[row][1] = date[row][2] = "0";
				}

				for (int hm = 0; hm < 3; hm++) {
					sdate[row][hm] = Integer.parseInt(date[row][hm]);


				}
			}
			for (int row = 0; row < 12; row++) {
				cursor[0].moveToPosition(row);
				time[row][2] = cursor[0].getString(cursor[0].getColumnIndex("time"));
				if (!time[row][2].equals("")) {
					time[row][1] = time[row][2].substring(7);//分
					time[row][0] = time[row][2].substring(4, 6);//时
				} else {
					time[row][0] = time[row][1] = "0";
				}
				for (int hm = 0; hm < 2; hm++) {
					stime[row][hm] = Integer.parseInt(time[row][hm]);
				}
			}

			//从该context中启动提醒的activity，根据SDK文档的说明，需要加上addFlags()一句
			Intent remind_intent = new Intent(RemindService.this, RemindActivity.class);
			remind_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			//获取提前提醒的时间值,如果没有获取到则取默认值30分钟
			//这里模式一定要设置为MODE_MULTI_PROCESS，否则即使相应的xml文件中数据有更新，RemindReceiver中也不能获取更新后的数据，而是一直获取上次的数据， 除非清空缓存
			SharedPreferences pre = RemindService.this.getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
			advance_time = pre.getInt("time_choice", 30);
//		System.out.println(advance_time);

			Calendar c = Calendar.getInstance();
			//获取当前的时和分
			int current_year = c.get(Calendar.YEAR);
			int current_month = c.get(Calendar.MONTH);
			int current_day = c.get(Calendar.DAY_OF_MONTH);
			int current_hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			int current_minute = c.get(Calendar.MINUTE);

			//定义一个标志位，用来排除掉重复的提醒
			boolean flag = true;
			//循环判断当天的课前提醒
			for (int i = 0; i < 12; i++) {
				if (sdate[i][0] != 0 && sdate[i][1] != 0) {
					//将calendar的时和分设置为提醒时候的时和分
					c.set(Calendar.YEAR, sdate[i][0]);
					c.set(Calendar.MONTH, sdate[i][1]);
					c.set(Calendar.DAY_OF_MONTH, sdate[i][2]);
					c.set(Calendar.HOUR_OF_DAY, stime[i][0]);
					c.set(Calendar.MINUTE, stime[i][1]);
					c.add(Calendar.MINUTE,-1*advance_time);

					//获取设置的提醒的时和分
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);
						/*
						String h=new String();
						h=String.valueOf(year);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(month);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(day);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(hourOfDay);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(minute);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(current_year);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(current_month);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(current_day);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(current_hourOfDay);
						Log.d(Test,"+++++++++"+h);
						h=String.valueOf(current_minute);
						Log.d(Test,"+++++++++"+h);
						*/
					//如果到了设定的提醒时间，就启动提醒的activity
					if (year == current_year && month == current_month+1 && day == current_day && hourOfDay == current_hourOfDay && minute == current_minute) {
						if (flag) {
							Log.d(Test,"-----------go");
							RemindService.this.startActivity(remind_intent);
							flag = false;
						}
					} else {
						flag = true;
					}
				}
			}
		}
	};
	Timer tim=new Timer();
	@Override
	public IBinder onBind(Intent arg0){
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public  void onDestroy(){
		super.onDestroy();
		tim.cancel();
		this.stopSelf();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//if(SetActivity.time_choice==0) return super.onStartCommand(intent, flags, startId);
		//每隔一分钟从数据库中取以此数据，获得一次当前的时间，并与用用户输入的上下课时间比较，如果相等，则执行相应的静音或者恢复铃声操作
		tim.schedule(t, 0, 10000);
		return super.onStartCommand(intent, flags, startId);
	}

}