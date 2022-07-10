package cn.bminor7.nfc_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChargeActivity extends AppCompatActivity {

    private UserDBOpenHelper helper = null;
    private EditText cost;
    private EditText carNum;
    private int i;
    private boolean flag=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        helper=new UserDBOpenHelper(this);
        cost = (EditText)findViewById(R.id.cost1);
        carNum = (EditText)findViewById(R.id.carnum1);

    }
    public void Ready_click(View v){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user", null);
        Editable str1=carNum.getText();
        Toast.makeText(ChargeActivity.this,"充值中。。。请稍后",Toast.LENGTH_LONG).show();
        while(cursor.moveToNext()){
            if(cursor.getString(2).equals(str1.toString())){
                Editable str =cost.getText();
                i = Integer.parseInt(str.toString())+cursor.getInt(5);
                db.execSQL("update user set money=? where carNum= ? ", new Object[]{i,str1});
                Toast.makeText(ChargeActivity.this,"充值成功",Toast.LENGTH_LONG).show();
                flag=true;
                break;
            }
        }
        db.close();
        if(flag==false){
            Toast.makeText(ChargeActivity.this,"充值失败，请重新核对信息！",Toast.LENGTH_LONG).show();
        }
    }
}