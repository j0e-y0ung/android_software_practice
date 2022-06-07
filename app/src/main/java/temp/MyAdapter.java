package temp;
import android.widget.SimpleCursorAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import com.example.myapplication.MainActivity;

public class MyAdapter {

	private Context context;
	private MainActivity main;
	private Cursor[] cursor=new Cursor[8];
	private SimpleCursorAdapter[] adapter;
	
	private SharedPreferences preferences;
	
	public MyAdapter(Context context){
		this.context=context;
		main=(MainActivity) context;
	}
	public void test(){
	
	
			
	}
	
}
