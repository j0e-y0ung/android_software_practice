package editschedule;

import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.*;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import temp.*;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;


public class CalActivity extends Activity {
    
    private TextView backButton = null;
    private TextView setButton = null;
    public SimpleCursorAdapter adapter;
    public ListView list=null;
    public  Cursor[] cursor=new Cursor[1];
    private TabHost tabs   = null;

    //定义手势检测器实例
    private GestureDetector detector = null;
    //定义手势动作两点之间的最小距离
    private final int FLIP_DISTANCE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);
        //将该activity加入到MyApplication对象实例容器中
        MyApplication.getInstance().addActivity(this);



        //View view= this.getLayoutInflater().inflate((R.layout.activity_cal), null);
        list = (ListView) findViewById(R.id.list );
        backButton =(TextView)  findViewById(R.id.backButton2);
        setButton = (TextView) findViewById(R.id.setButton);
       // tabs  = (TabHost)view.findViewById(R.id.tab);

/*
        //在配置任何的TabSpec之前，必须在TabHost上调用该方法
        tabs.setup();

        TabHost.TabSpec  spec = null;
        addCard(spec,"tag1",R.id.list,"预定事件");
        //修改tabHost选项卡中的字体的颜色
        TabWidget tabWidget = tabs.getTabWidget();
        for(int i=0;i<tabWidget.getChildCount();i++){
            TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(0xff004499);
        }

        //设置打开时默认的选项卡是当天的选项卡
        tabs.setCurrentTab(1);
*/

        //声明一个获取系统音频服务的类的对象
        final AudioManager audioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
        //获取手机之前设置好的铃声模式,该数据将用来传递给activity_set
        final int orgRingerMode = audioManager.getRingerMode();



        cursor[0]=MainActivity.db.select(7);
        list.setAdapter(adapter());
        //为退出按钮绑定监听器
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               finish();
            }
        });

        //为set按钮绑定监听器
        setButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalActivity.this, SetActivity.class);
                //将orgRingerMode数据传给activity_set
                intent.putExtra("mode_ringer", orgRingerMode);
                startActivity(intent);
            }
        });
    /*
        //创建手势检测器
        detector = new GestureDetector(this, new DetectorGestureListener());
        list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event)   {
                return detector.onTouchEvent(event);
            }
        });
        */
            //为ListView的每个item绑定监听器，点击则弹出由AlertDialog创建的列表对话框进行选择
            list.setOnItemClickListener(new OnItemClickListener() {

                @SuppressLint("ResourceType")
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        final int id, long arg3) {
                    final int n=id;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CalActivity.this);
                    builder.setIcon(R.drawable.ic_launcher);
                    builder.setTitle("选择");
                    TextView tv=(TextView)arg1.findViewById(R.id.ltext0);
                    Log.i("Test",(tv.getText().toString().equals(""))+"");
                    //如果课程栏目为空就启动添加对话框
                    if((tv.getText()).toString().equals("")){
                        //通过数组资源为对话框中的列表添加选项内容，这里只有一个选项
                        builder.setItems(R.array.edit_event_options1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //如果单击了该列表项，则跳转到编辑课程信息的界面
                                if(which == 0){
                                    new CalDialog(CalActivity.this).add(7,n);
                                }
                            }
                        });
                        builder.create().show();
                    }
                    //否则启动修改对话框，或直接删除数据
                    else{
                        builder.setItems(R.array.edit_date_options2, new DialogInterface.OnClickListener() {

                            @SuppressWarnings("deprecation")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //如果单击了该列表项，则跳转到编辑课程信息的界面
                                if(which == 0){
                                    cursor[0].moveToPosition(n);
                                    MainActivity.db.deleteData(7,n+1);
                                    cursor[0].requery();
                                    list.invalidate();
                                    new CalDialog(CalActivity.this).add(7,n);
                                }
                                if(which == 1){
                                    cursor[0].moveToPosition(n);
                                    MainActivity.db.deleteData(7,n+1);
                                    cursor[0].requery();
                                    list.invalidate();
                                }
                            }
                        });
                        builder.create().show();
                    }
                }
            });



    }
    /*
    //子 方法:为主界面添加选项卡
    public void addCard(TabHost.TabSpec spec,String tag,int id,String name){
        spec = tabs.newTabSpec(tag);
        spec.setContent(id);
        spec.setIndicator(name);
        tabs.addTab(spec);
    }
    //内部类，实现GestureDetector.OnGestureListener接口
    class DetectorGestureListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        //当用户在触屏上“滑过”时触发此方法
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            int i = tabs.getCurrentTab();
            //第一个触点事件的X坐标值减去第二个触点事件的X坐标值超过FLIP_DISTANCE，也就是手势从右向左滑动
            if(e1.getX() - e2.getX() > FLIP_DISTANCE){
                if(i<6)
                    tabs.setCurrentTab(i+1);
                //	float currentX = e2.getX();
                //	list[i].setRight((int) (inialX - currentX));
                return true;
            }

            //第二个触点事件的X坐标值减去第一个触点事件的X坐标值超过FLIP_DISTANCE，也就是手势从左向右滑动
            else if(e2.getX() - e1.getX() > FLIP_DISTANCE){
                if(i>0)
                    tabs.setCurrentTab(i-1);
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

    }



    //覆写Activity中的onTouchEvent方法，将该Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }
*/
    public SimpleCursorAdapter adapter(){
        return new SimpleCursorAdapter(this, R.layout.list_v3,cursor[0],new String[]{"_id","name","date","time"},new int[]{R.id.number,R.id.ltext0,R.id.ltext6,R.id.ltext7} );
    }
    
}