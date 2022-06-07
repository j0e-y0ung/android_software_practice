package temp;


import java.util.Calendar;

import android.app.DatePickerDialog;
import android.widget.*;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import editschedule.CalActivity;


public class CalDialog {


    private EditText event_name;
    //	private EditText course_time1,course_time2;
    //private EditText course_count;


    private View view;
    private Context context;
    private LayoutInflater inflater;
    private AlertDialog.Builder builder;
    CalAdapter adapter;
    CalActivity main;
    String s1="",s6="",s7="";

    public CalDialog(Context context){
        this.context=context;
        main=(CalActivity) context;
        adapter=new CalAdapter(context);
    }
    /*
     * 点击未编辑的课程列表跳出”添加课程“对话框
     */
    public void add(final int day, final int n){
        //填装对话框的view
        inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.edit_cal,null);
        findWidgetes();//取部件
        final Button event_time=(Button)view.findViewById(R.id.event_time);
        final Button event_date=(Button)view.findViewById(R.id.event_date);
        //为两个输入时间的按钮绑定监听器
        event_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateSet_Dialog(event_date);
            }
        });
        event_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimeSet_Dialog(event_time);
            }
        });

        builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("添加日程安排信息")
                .setView(view)
                .setPositiveButton("确认",new DialogInterface.OnClickListener(){

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(!(s1=event_name.getText().toString()).equals("")) s1="日程安排: "+s1;

                        if(!(s6=event_date.getText().toString()).equals("")) s6="日期: "+s6;
                        if(!(s7=event_time.getText().toString()).equals("")) s7="时间: "+s7;
                       MainActivity.db.update(day,n+1,s1,s6,s7,Integer.toString(0));
                        main.cursor[0].requery();
                        main.list.invalidate();
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }

                });
        builder.create().show();

    }

    /*
     * 点击已编辑的课程列表跳出”修改课程信息或删除课程信息“对话框
     */
    public void modify(final int day ,final int n){
        //填装对话框的view
        inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.edit_cal,null);
        findWidgetes();//取部件
        final Button event_time=(Button)view.findViewById(R.id.event_time);
        final Button event_date=(Button)view.findViewById(R.id.event_date);
        //为两个输入时间的按钮绑定监听器
        event_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateSet_Dialog(event_date);
            }
        });
        event_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimeSet_Dialog(event_time);
            }
        });

        //从数据库取出旧数据
        main.cursor[day].moveToPosition(n);
        String [] temp=new String[8];
        for(int i=0;i<8;i++) {temp[i]=main.cursor[day].getString(i+1);}
        //将旧数据显示在编辑对话框
        if(!temp[0].equals("")) event_date.setText(temp[0].substring(temp[0].indexOf(":")+2));

        if(!temp[1].equals("")) event_date.setText(temp[1].substring(temp[1].indexOf(":")+2));
        if(!temp[1].equals("")) event_time.setText(temp[2].substring(temp[2].indexOf(":")+2));
        view.invalidate();


        builder=new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("修改日程信息")
                .setView(view)
                .setPositiveButton("确认",new DialogInterface.OnClickListener(){

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(!(s1=event_name.getText().toString()).equals("")) s1="日程安排: "+s1;

                        if(!(s6=event_date.getText().toString()).equals("")) s6="日期: "+s6;
                        if(!(s7=event_time.getText().toString()).equals("")) s7="时间: "+s7;

                        main.cursor[0].moveToPosition(n);
                        //int n1=Integer.parseInt(main.cursor[day].getString(7).trim());//课程的总节数
                        //int n2=Integer.parseInt(main.cursor[day].getString(8).trim());//选中的为该课程的第几节
                        Log.i("kkk",main.cursor[0].getString(7));
                        MainActivity.db.update(day ,n+1,s1,s6,s7,Integer.toString(0));
                        main.cursor[0].requery();
                        main.list.invalidate();

                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }

                });
        builder.create().show();

    }

    private void findWidgetes(){
        event_name=(EditText)view.findViewById(R.id.edit_event);
//		course_time1=(EditText)view.findViewById(R.id.time1);
//		course_time2=(EditText)view.findViewById(R.id.time2);
    }
    public void DateSet_Dialog(final TextView text){
        Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来。
        new DatePickerDialog(main,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        StringBuffer s_year=new StringBuffer();
                        StringBuffer s_month=new StringBuffer();
                        StringBuffer s_day=new StringBuffer();
                        s_year.append(year);
                        s_month.append(month+1);
                        s_day.append(dayOfMonth);
                        if(month<10) s_month.insert(0,"0");
                        if(dayOfMonth<10) s_day.insert(0,"0");
                        text.setText(s_year.toString()+"年"+s_month.toString()+"月"+s_day.toString()+"日");
                    }


                }
                //设置初始日期
                , c.get(Calendar.YEAR)
                , c.get(Calendar.MONTH)
                , c.get(Calendar.DAY_OF_MONTH)
                ).show();
    }
    public void TimeSet_Dialog(final TextView text){
        Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来。
        new TimePickerDialog(main,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker tp, int hourOfDay, int minute){
                        //获取完整的时间，在只有一位的数字前面加0
                        StringBuffer s_hour = new StringBuffer();
                        StringBuffer s_minute = new StringBuffer();
                        s_hour.append(hourOfDay);
                        s_minute.append(minute);
                        if(hourOfDay<10){
                            s_hour.insert(0,"0");
                        }
                        if(minute<10){
                            s_minute.insert(0,"0");
                        }
                        //将结果显示在edit中
                        text.setText(s_hour.toString() + ":" + s_minute.toString());
                    }
                }
                //设置初始时间
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE)
                //true表示采用24小时制
                , true).show();
    }
}