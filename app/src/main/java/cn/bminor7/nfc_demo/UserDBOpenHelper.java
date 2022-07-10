package cn.bminor7.nfc_demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBOpenHelper extends SQLiteOpenHelper {

    public UserDBOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }


    public UserDBOpenHelper(Context context) {
        super(context, "user.db", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL("create table user (_id integer primary key autoincrement,name varchar(20),carNum varchar(20),uid varchar(20),inTime varchar(20),money int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}