package com.cj.baidunavi.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cj.baidunavi.R;

import java.util.ArrayList;

public class Explore2Activity extends AppCompatActivity
{
    Bitmap[] images = new Bitmap[4];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore2);

        /*----轮播图板块-----*/
        //加载图片
        images[0] = BitmapFactory.decodeResource(getResources(),R.drawable.im_ck1);
        images[1] = BitmapFactory.decodeResource(getResources(),R.drawable.im_ck2);
        images[2] = BitmapFactory.decodeResource(getResources(),R.drawable.im_ck3);
        images[3] = BitmapFactory.decodeResource(getResources(),R.drawable.im_ck4);

        AfCarousel afCarousel = (AfCarousel) findViewById(R.id.id_carousel);
        afCarousel.setImages(images);

        /*----列表板块-----*/
        //取出列表 采用Adapter模式来给ListView数据
        ListView listView = (ListView) findViewById(R.id.id_listView);
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        //点击事件,点击不同行跳转到不同页面
        //点击事件,点击不同行跳转到不同页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    //新生攻略
                    case 0:
                        Intent intent = new Intent(Explore2Activity.this,NewstuActivity.class);
                        startActivity(intent);
                        break;

                    //关于校园
                    case 1:
                        Intent intent1 = new Intent(Explore2Activity.this,AboutSActivity.class);
                        startActivity(intent1);
                        break;

                    //趣味分享
                    case 2:
                        Intent intent2 = new Intent(Explore2Activity.this,InterestActivity.class);
                        startActivity(intent2);
                        break;

                    //神秘组织
                    case 3:
                        Intent intent3 = new Intent(Explore2Activity.this,MysteryActivity.class);
                        startActivity(intent3);
                        break;

                    case 4:
                        onListItemClicked(position);
                        break;
                }
            }
        });
    }

    private void onListItemClicked(int position)
    {

        Toast.makeText(this, "暂无更新", Toast.LENGTH_SHORT).show();
    }

    //给ListView提供数据
    private class MyAdapter extends BaseAdapter
    {
        ArrayList<SchoolData> listData = new ArrayList<>();

        public MyAdapter()
        {
            SchoolData data1 = new SchoolData("新生攻略",1);
            SchoolData data2 = new SchoolData("关于校园",2);
            SchoolData data3 = new SchoolData("全景重科",3);
            SchoolData data4 = new SchoolData("神秘组织",4);
            SchoolData data5 = new SchoolData("检查更新",5);

            listData.add(data1);
            listData.add(data2);
            listData.add(data3);
            listData.add(data4);
            listData.add(data5);
        }

        //获取一共有多少行数据
        @Override
        public int getCount()
        {
            return listData.size();
        }

        //获取某行数据
        @Override
        public Object getItem(int position)
        {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        //获取某行的显示
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                //inflate()将xml转为View控件
                convertView = getLayoutInflater().inflate(R.layout.layout_schooldata,parent,false);

            }

            TextView textView = (TextView) convertView.findViewById(R.id.id_item_text);
            if(textView == null)
                textView = new TextView(Explore2Activity.this);

            //将数据显示到View
            SchoolData schoolData = (SchoolData) getItem(position);
            textView.setText(schoolData.getListName());

            //设置图标的显示
            ImageView imageView = (ImageView) convertView.findViewById(R.id.id_item_icon);
            if(schoolData.getId() == 1)
                imageView.setImageDrawable(getDrawable(R.drawable.ic_newstu));
            else if (schoolData.getId() == 2)
                imageView.setImageDrawable(getDrawable(R.drawable.ic_school));
            else if (schoolData.getId() == 3)
                imageView.setImageDrawable(getDrawable(R.drawable.ic_sharing));
            else if (schoolData.getId() == 4)
                imageView.setImageDrawable(getDrawable(R.drawable.ic_mystery));
            else if (schoolData.getId() == 5)
                imageView.setImageDrawable(getDrawable(R.drawable.ic_updata));

            //因为convertView代表一整行的显示所以要返回convertView
            return convertView;
        }
    }



}
