package com.cj.baidunavi.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.cj.baidunavi.R;


import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity
{
    final String TAG = "测试MainActivity";

    // 抽屉
    DrawerLayout drawerLayout;

    // 抽屉菜单里的ListView
    MyDrawerListAdapter drawerListAdapter;
    ArrayList<MyDrawerListItem> drawerListData = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // 初始化DrawerLayout
        drawerLayout = findViewById(R.id.id_drawer_layout);
        drawerLayout.setScrimColor(0xAACCCCCC); // 抽屉拉开时ContentView的灰化颜色

        // 设置DrawerLayout里的菜单ListView
        // 初始化ListView
        drawerListAdapter = new MyDrawerListAdapter();
        ListView drawerListView = (ListView) findViewById(R.id.id_drawer_menu);
        drawerListView.setAdapter(drawerListAdapter);
        // 点击ListView
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch (i)
                {
                    case 0:
                        Intent intent = new Intent(LibraryActivity.this, LibraryOverviewActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent2 = new Intent(LibraryActivity.this, FloorActivity.class);
                        startActivity(intent2);
                        break;

                    case 2:
                        Intent intent3 = new Intent(LibraryActivity.this,LibraryRulesActivity.class);
                        startActivity(intent3);
                        break;

                    case 3:
                        Intent intent4 = new Intent(LibraryActivity.this, LappointmentActivity.class);
                        startActivity(intent4);
                        break;

                    case 4:
                        Intent intent5 = new Intent(LibraryActivity.this, LappointmentActivity.class);
                        startActivity(intent5);
                        break;

                    case 5:
                        onListItemClicked(i);
                        break;
                }
            }
        });

        // 初始化抽屉里的菜单项
        drawerListData.add(new MyDrawerListItem("本馆概况", "cmd1", null));
        drawerListData.add(new MyDrawerListItem("楼层分布", "cmd2", null));
        drawerListData.add(new MyDrawerListItem("规章制度", "cmd3", null));
        drawerListData.add(new MyDrawerListItem("预约借书", "cmd4", null));
        drawerListData.add(new MyDrawerListItem("全景看图书馆", "cmd5", null));
        drawerListData.add(new MyDrawerListItem("咨询台", "cmd6", null));

    }

    // 抽屉菜单里的菜单项被点击
    private void onDrawerItemClicked(View view, MyDrawerListItem it)
    {
        Log.w(TAG, "点击了菜单项: " + it.cmd);
//        drawerLayout.closeDrawer(Gravity.START, true);
    }

    private void onListItemClicked(int position)
    {

        Toast.makeText(this, "电话咨询：023-65022227", Toast.LENGTH_SHORT).show();
    }

    ///////////// 每一条记录的数据 /////////
    static class MyDrawerListItem
    {
        public String title; // 菜单项显示文本
        public String cmd; // 菜单项对应的命令
        public Drawable icon; // 菜单项左侧显示的图标(本例忽略)

        public MyDrawerListItem(){}
        public MyDrawerListItem(String title, String cmd, Drawable icon)
        {
            this.title = title;
            this.cmd = cmd;
            this.icon = icon;
        }
    }

    //////////// 适配器 //////////////////
    private class MyDrawerListAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return drawerListData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return drawerListData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // 创建控件
            if (convertView == null)
            {
                convertView = getLayoutInflater()
                        .inflate(R.layout.activity_library_drawer_listitem, parent, false);
            }

            // 获取/显示数据
            MyDrawerListItem it = (MyDrawerListItem) getItem(position);
            ((TextView) convertView.findViewById(R.id.id_item_title)).setText(it.title);
            return convertView;
        }
    }

}
