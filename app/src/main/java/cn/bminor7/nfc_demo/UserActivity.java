package cn.bminor7.nfc_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private ListView mListView;
    public List<User>usersList = new ArrayList<User>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bundle = getIntent().getExtras();
        usersList = bundle.getParcelableArrayList("user");
        //初始化ListView控件
        mListView = (ListView)findViewById(R.id.userListView);
        //创建一个Adapter的实例
        MyBaseAdapter mAdapter = new MyBaseAdapter();
        //设置adapter
        mListView.setAdapter(mAdapter);
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return usersList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return usersList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            //将list_item.xml文件找出来并转换为View对象
            View view = View.inflate(UserActivity.this, R.layout.activity_user, null);
            //找到list_item.xml中创建的TextView
            TextView mTextView1 = (TextView)view.findViewById(R.id.name);
            TextView mTextView2 = (TextView)view.findViewById(R.id.carnum);
            TextView mTextView3 = (TextView)view.findViewById(R.id.money);
            mTextView1.setText(usersList.get(arg0).getName());
            mTextView2.setText(usersList.get(arg0).getCarNum());
            mTextView3.setText(Integer.toString(usersList.get(arg0).getMoney()));
            System.out.print(usersList.get(arg0).toString());
            return view;
        }
    }


}