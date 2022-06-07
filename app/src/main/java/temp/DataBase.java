package temp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper{
	
	private final static String DB_NAME="myBase";
	private final static String[] TB_NAME={"Mon","Tue","Wed","Thur","Fri","Sat","Sun","Event_t"};
	public  final static String ID="_id";
	public final static String CLASS="classes";


	public final static String TIME1="time1";
	public final static String TIME2="time2";
	public final static String WHICH="which";

	public final static String NAME="name";
	public final static String DATE="date";

	public final static String TIME="time";
	
	public DataBase(Context context){
		super(context,DB_NAME,null,1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		for(int i=0;i<7;i++){
		String sql="CREATE TABLE  "+TB_NAME[i]+" (_id INTEGER primary key autoincrement,classes varchar(70)," +
				"time1 varchar(70),time2 varchar(70),which varchar(70));";
		db.execSQL(sql);	
		}
		String sql="CREATE TABLE  "+TB_NAME[7]+" (_id INTEGER primary key autoincrement,name varchar(70)," +
				"date varchar(70),time varchar(70),which varchar(70));";
		db.execSQL(sql);

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oleVersion, int newVersion) {
		for(int i=0;i<8;i++){
			String sql="DROP TABLE IF EXISTS "+TB_NAME[i];
			db.execSQL(sql);
		}
		onCreate(db);
		
	}
	public Cursor select(int i){
		
		SQLiteDatabase db=DataBase.this.getReadableDatabase();
		Cursor cursor=db.query(TB_NAME[i],null,null,null,null,null,null);
		return cursor;
	}
	public  long insert(int i,String cla,String time1,String time2,String which){
		SQLiteDatabase db=DataBase.this.getWritableDatabase();
		ContentValues cv=new ContentValues();		
		if(i<7){
			cv.put(CLASS,cla);
			cv.put(TIME1,time1);
			cv.put(TIME2,time2);
			cv.put(WHICH,which);
		}
		else {
			cv.put(NAME,cla);
			cv.put(DATE,time1);
			cv.put(TIME,time2);
			cv.put(WHICH,which);
		}
		long row=db.insert(TB_NAME[i],null,cv);
		return row;
	}
	public void update(int i,int _id,String cla,String time1,String time2,String which){
		SQLiteDatabase db=DataBase.this.getWritableDatabase();
		String where="_id = ?";
		String[] whereValues={Integer.toString(_id)};
		ContentValues cv=new ContentValues();
		if(i<7){
			if(!cla.equals("")) cv.put(CLASS,cla);
			if(!time1.equals("")) cv.put(TIME1,time1);
			if(!time2.equals("")) cv.put(TIME2,time2);
			if(!which.equals("")) cv.put(WHICH,which);
		}
		else {
			if(!cla.equals("")) cv.put(NAME,cla);
			if(!time1.equals("")) cv.put(DATE,time1);
			if(!time2.equals("")) cv.put(TIME,time2);
			if(!which.equals("")) cv.put(WHICH,which);
		}
		db.update(TB_NAME[i], cv, where, whereValues);
	}
	public void deleteData(int i,int _id){
		SQLiteDatabase db=DataBase.this.getWritableDatabase();
		String where="_id = ?";
		String[] whereValues={Integer.toString(_id)};
		ContentValues cv=new ContentValues();
		if(i<7){
			cv.put("classes","");
			cv.put("time1","");
			cv.put("time2","");
			cv.put("which","");
		}
		else {
			cv.put("name","");
			cv.put("date","");
			cv.put("time","");
			cv.put("which","");
		}
		db.update(TB_NAME[i], cv, where, whereValues);
	}
	public void delete(int i,int _id){
		SQLiteDatabase db=this.getWritableDatabase();
		String where="_id = ?";
		String[] whereValues={Integer.toString(_id)};
		db.delete(TB_NAME[i], where, whereValues);
	}
	public  void createTable(int j){

				for(int i=1;i<=12;i++)
					insert(j,"", "", "","");

	}
	
}
