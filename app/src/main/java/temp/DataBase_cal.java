package temp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.jar.Attributes;

public class DataBase_cal extends SQLiteOpenHelper{

    private final static String DB_NAME="myBase_cal";
    private final static String T_NAME="Event";
    public  final static String ID="_id";
    public final static String NAME="name";
    public final static String DATE="date";

    public final static String TIME="time";
    public final static String WHICH="which";

    public DataBase_cal(Context context){
        super(context,DB_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql="CREATE TABLE "+T_NAME+" (_id INTEGER primary key autoincrement,name varchar(70)," +
                "date varchar(70),time varchar(70),which varchar(70))";
        db.execSQL(sql);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oleVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS "+T_NAME;
        db.execSQL(sql);
        onCreate(db);

    }
    public Cursor select(){

        SQLiteDatabase db=DataBase_cal.this.getReadableDatabase();
        Cursor cursor=db.query(T_NAME,null,null,null,null,null,null);
        return cursor;
    }
    public  long insert(String name,String date, String time,String which){
        SQLiteDatabase db=DataBase_cal.this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(NAME,name);
        cv.put(DATE,date);
        cv.put(TIME,time);
        cv.put(WHICH,which);
        long row=db.insert(T_NAME,null,cv);
        return row;
    }
    public void update(int _id,String name, String date,String time,String which){
        SQLiteDatabase db=DataBase_cal.this.getWritableDatabase();
        String where="_id = ?";
        String[] whereValues={Integer.toString(_id)};
        ContentValues cv=new ContentValues();
        if(!name.equals("")) cv.put(NAME,name);
        if(!date.equals("")) cv.put(DATE,date);

        if(!time.equals("")) cv.put(TIME,time);
        if(!which.equals("")) cv.put(WHICH,which);
        db.update(T_NAME, cv, where, whereValues);
    }
    public void deleteData(int _id){
        SQLiteDatabase db=DataBase_cal.this.getWritableDatabase();
        String where="_id = ?";
        String[] whereValues={Integer.toString(_id)};
        ContentValues cv=new ContentValues();
        cv.put("name","");
        cv.put("date","");
        cv.put("time","");
        cv.put("which","");
        db.update(T_NAME, cv, where, whereValues);
    }
    public void delete(int _id){
        SQLiteDatabase db=this.getWritableDatabase();
        String where="_id = ?";
        String[] whereValues={Integer.toString(_id)};
        db.delete(T_NAME, where, whereValues);
    }
    public  void createTable(){

        for(int i=1;i<=12;i++)
            insert("", "","","");

    }

}
