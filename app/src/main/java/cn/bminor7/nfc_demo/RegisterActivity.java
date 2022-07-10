package cn.bminor7.nfc_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private UserDBOpenHelper helper = null;
    private EditText et_userName;
    private EditText et_carNum;
    private String str="0";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        helper=new UserDBOpenHelper(this);
        et_userName=(EditText)findViewById(R.id.regi_username);
        et_carNum=(EditText)findViewById(R.id.regi_carnum);

        //获取NfcAdapter实例
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //获取通知
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        if (nfcAdapter == null) {
            Toast.makeText(RegisterActivity.this,"设备不支持NFC",Toast.LENGTH_LONG).show();
            return;
        }
        if (nfcAdapter!=null&&!nfcAdapter.isEnabled()) {
            Toast.makeText(RegisterActivity.this,"请在系统设置中先启用NFC功能",Toast.LENGTH_LONG).show();
            return;
        }
        //因为启动模式是singleTop，于是会调用onNewIntent方法
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    void resolveIntent(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            processTag(intent);
        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private String flipHexStr(String s){
        StringBuilder  result = new StringBuilder();
        for (int i = 0; i <=s.length()-2; i=i+2) {
            result.append(new StringBuilder(s.substring(i,i+2)));
        }
        return result.toString();
    }


    public void processTag(Intent intent) {//处理tag
        //获取到卡对象
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取卡id这里即uid
        byte[] aa = tagFromIntent.getId();
        str = ByteArrayToHexString(aa);
        str = flipHexStr(str);

    }


    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            //设置程序不优先处理
            nfcAdapter.disableForegroundDispatch(this);
    }


    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            //设置程序优先处理
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    null, null);
    }
    public void readyButton(View v){
        //读卡
        if(str.equals("0")){
            Toast.makeText(RegisterActivity.this,"读取错误，未识别到卡片",Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            SQLiteDatabase db = helper.getWritableDatabase();
            // 执行sql语句
            db.execSQL("insert into user (name,carNum,uid,inTime,money) values (?, ?, ?, ?, ?)", new Object[] { et_userName.getText(), et_carNum.getText(),str,"0",0});
            db.close();
            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

    }
}