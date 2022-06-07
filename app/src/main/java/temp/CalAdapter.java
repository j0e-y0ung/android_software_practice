package temp;
import android.widget.SimpleCursorAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import editschedule.CalActivity;

public class CalAdapter {

    private Context context;
    private CalActivity main;
    private Cursor[] cursor=new Cursor[1];
    private SimpleCursorAdapter[] adapter;

    private SharedPreferences preferences;

    public CalAdapter(Context context){
        this.context=context;
        main=(CalActivity) context;
    }
    public void test(){



    }

}
