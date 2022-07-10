package cn.bminor7.nfc_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserDBOpenHelper helper = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper=new UserDBOpenHelper(this);
    }


    public void regi_click(View v){
        //注册
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void read_click(View v){
        //读卡
        Intent intent = new Intent(MainActivity.this,ReadActivity.class);
        startActivity(intent);
    }

    public void charge_click(View v){
        Intent intent = new Intent(MainActivity.this,ChargeActivity.class);
        startActivity(intent);
    }

    public void user_click(View v){
        //用户表
        SQLiteDatabase db = helper.getWritableDatabase();
        // 复数数据库查询的结果集的游标
        //Cursor cursor = db.rawQuery("select * from person", null);
        Cursor cursor = db.query("user", null,null,null,null,null,null);

        List<User> usersList = new ArrayList<User>();
        while(cursor.moveToNext()){
            User user  = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setCarNum(cursor.getString(2));
            user.setUid(cursor.getString(3));
            user.setInTime(cursor.getString(4));
            user.setMoney(cursor.getInt(5));

            usersList.add(user);
            user = null;
        }
        // 记得释放游标指针
        cursor.close();
        for (User user : usersList) {
            System.out.println(user.toString());
        }

        //创建一个Intent对象，通过这个对象开启ViewList
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("user", (ArrayList<? extends Parcelable>) usersList);
        Intent intent = new Intent(MainActivity.this,UserActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        // 记得释放
        db.close();
    }
}