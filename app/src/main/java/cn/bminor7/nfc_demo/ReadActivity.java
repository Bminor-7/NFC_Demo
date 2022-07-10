package cn.bminor7.nfc_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private UserDBOpenHelper helper = null;
    private TextView inTime;
    private TextView outTime;
    private TextView money;
    private TextView cost;
    private TextView carType;
    private String in = "0";
    private String out= "0";
    private String str= null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        helper=new UserDBOpenHelper(this);
        inTime = (TextView)findViewById(R.id.in_time);
        outTime = (TextView)findViewById(R.id.out_time);
        money = (TextView)findViewById(R.id.read_money);
        cost = (TextView)findViewById(R.id.cost);
        carType = (TextView)findViewById(R.id.car_type);


        //获取NfcAdapter实例
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //获取通知
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        if (nfcAdapter == null) {
            Toast.makeText(ReadActivity.this,"设备不支持NFC",Toast.LENGTH_LONG).show();
            return;
        }
        if (nfcAdapter!=null&&!nfcAdapter.isEnabled()) {
            Toast.makeText(ReadActivity.this,"请在系统设置中先启用NFC功能",Toast.LENGTH_LONG).show();
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

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user", null);
        while(cursor.moveToNext()){
            if(cursor.getString(3).equals(str)){
                carType.setText(cursor.getString(2));
                if(cursor.getString(4).equals("0")){//判断数据库里入库时间是否为空
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    in=formatter.format(curDate);
                    inTime.setText(in);
                    db.execSQL("update user set inTime=? where uid= ? ", new Object[]{in,str});
                    break;
                }
                else
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    out=formatter.format(curDate);
                    inTime.setText(cursor.getString(4));
                    outTime.setText(out);
                    long delta_time=daysBetween2(cursor.getString(4),out);
                    cost.setText(String.valueOf(delta_time));
                    String temp= String.valueOf(delta_time);
                    int i=cursor.getInt(5)-Integer.parseInt(temp);
                    money.setText(Integer.toString(i));
                    db.execSQL("update user set inTime=? where uid= ? ", new Object[]{"0",str});
                    db.execSQL("update user set money=? where uid= ? ", new Object[]{i,str});
                    break;
                }
            }
        }
        db.close();
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

    public static long daysBetween2(String startTime, String endTime) {
        //时间格式，自己可以随便定义
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //一天的毫秒数
        long nd = 1000 * 60 * 60 * 24;
        //一小时的毫秒数
        long nh = 1000 * 60 * 60;
        //一分钟的毫秒数
        long nm = 1000 * 60;
        //一秒钟的毫秒数
        long ns = 1000;

        long diff = 0;
        try {
            //获取两个时间的毫秒时间差
            diff = format.parse(endTime).getTime() - format.parse(startTime).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算相差的天数
        long day = diff / nd;
        //计算相差的小时
        long hour = diff % nd / nh;
        //计算相差的分钟
        long min = diff % nd % nh / nm;
        //计算相差的秒2
        long sec = diff % nd % nh % nm / ns;

        return sec;
    }

}