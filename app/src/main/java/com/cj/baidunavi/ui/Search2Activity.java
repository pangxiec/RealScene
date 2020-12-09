package com.cj.baidunavi.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.lifecycle.Observer;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.cj.baidunavi.BaseApplication;
import com.cj.baidunavi.R;
import com.cj.baidunavi.base.BaseVM2Activity;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search2Activity extends BaseVM2Activity<Search2ViewModel> {
    private ListView sug_list;
    private ListView history_list;
    private TextView tv_history;
    private LinearLayout ll_poi;

    @Override
    protected Class<Search2ViewModel> provideVM() {
        return Search2ViewModel.class;
    }

    @Override
    protected void initView() {
        viewModel.myCity=getIntent().getStringExtra("myCity")==null?"重庆":
                getIntent().getStringExtra("myCity");
        viewModel.myLng=getIntent().getParcelableExtra("myLng");
        ((AutoCompleteTextView)findViewById(R.id.searchkey)).setThreshold(1);
        sug_list=findViewById(R.id.sug_list);
        history_list=findViewById(R.id.history_list);
        tv_history=findViewById(R.id.tv_history);
        ll_poi=findViewById(R.id.ll_poi);
        doHistory();
        ((AutoCompleteTextView)findViewById(R.id.searchkey)).addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable e) {

            }

            @Override
            public void beforeTextChanged(CharSequence c, int p, int p2, int p3) {

            }


            @Override
            public void onTextChanged(CharSequence p0,int p1, int p2, int p3) {
                if (p0 == null ) {
                    return;
                }
                if(TextUtils.isEmpty(p0.toString())){
                    sug_list.setVisibility( View.GONE);
                    history_list.setVisibility(View.VISIBLE);
                    tv_history.setVisibility(View.VISIBLE);
                    ll_poi.setVisibility(View.VISIBLE);
                    doHistory();
                    return;
                }else{
                    history_list.setVisibility(View.GONE);
                    ll_poi.setVisibility(View.GONE);
                    tv_history.setVisibility(View.GONE);
                    sug_list.setVisibility(View.VISIBLE);
                }
                viewModel.searchForSuggestions(p0.toString());
            }

        });

        findViewById(R.id.tv_clear_history).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ArrayList<HashMap<String,String>> dd= Hawk.get(BaseApplication.SEARCH_KEY);
               dd.clear();
               findViewById(R.id.tv_clear_history).setVisibility(View.INVISIBLE);
               Hawk.put(BaseApplication.SEARCH_KEY,dd);
               doHistory();
           }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.ll1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.mPoiResult.getValue()!=null &&
                    viewModel.mPoiResult.getValue().get(0)!=null){
                    PoiInfo info=viewModel.mPoiResult.getValue().get(0);
                    Intent intent=new Intent();
                    intent.putExtra("city",info.city);
                    intent.putExtra("address",info.address);
                    intent.putExtra("district",info.direction);
                    intent.putExtra("key",info.name);
                    intent.putExtra("latitude",info.location.latitude+"");
                    intent.putExtra("longitude",info.location.longitude+"");
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
        findViewById(R.id.ll2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.mPoiResult.getValue()==null) return;
                PoiInfo info=viewModel.mPoiResult.getValue().get(1);
                if(info!=null){
                    Intent intent=new Intent();
                    intent.putExtra("city",info.city);
                    intent.putExtra("address",info.address);
                    intent.putExtra("district",info.direction);
                    intent.putExtra("key",info.name);
                    intent.putExtra("latitude",info.location.latitude+"");
                    intent.putExtra("longitude",info.location.longitude+"");
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });

        findViewById(R.id.ll3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.mPoiResult.getValue()==null) return;
                PoiInfo info=viewModel.mPoiResult.getValue().get(2);
                if(info!=null){
                    Intent intent=new Intent();
                    intent.putExtra("city",info.city);
                    intent.putExtra("address",info.address);
                    intent.putExtra("district",info.direction);
                    intent.putExtra("key",info.name);
                    intent.putExtra("latitude",info.location.latitude+"");
                    intent.putExtra("longitude",info.location.longitude+"");
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });

        findViewById(R.id.ll4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.mPoiResult.getValue()==null) return;
                PoiInfo info=viewModel.mPoiResult.getValue().get(3);
                if(info!=null){
                    Intent intent=new Intent();
                    intent.putExtra("city",info.city);
                    intent.putExtra("address",info.address);
                    intent.putExtra("district",info.direction);
                    intent.putExtra("key",info.name);
                    intent.putExtra("latitude",info.location.latitude+"");
                    intent.putExtra("longitude",info.location.longitude+"");
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void doHistory(){
        if(!Hawk.contains(BaseApplication.SEARCH_KEY)){
            findViewById(R.id.tv_clear_history).setVisibility(View.INVISIBLE);
            return;
        }
        final ArrayList<HashMap<String,String>> data =Hawk.get(BaseApplication.SEARCH_KEY);
        if(data.size()==0) findViewById(R.id.tv_clear_history).setVisibility(View.INVISIBLE);
        if(data!=null){
            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    getApplicationContext(),
                    data,
                    R.layout.item_layout,
                    new String[]{"key", "city", "district"},
                    new int[]{R.id.sug_key, R.id.sug_city, R.id.sug_dis}
            );
            history_list.setAdapter(simpleAdapter);
            history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    try{
                        Intent intent=new Intent();
                        HashMap<String,String> r= data.get(pos);
                        intent.putExtra("city",r.get("city"));
                        intent.putExtra("address",r.get("address"));
                        intent.putExtra("district",r.get("district"));
                        intent.putExtra("key",r.get("key"));
                        intent.putExtra("latitude",r.get("latitude"));
                        intent.putExtra("longitude",r.get("longitude"));
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }catch (Throwable t){
                        t.printStackTrace();
                    }
                }
            });
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
     public void initData() {
        viewModel.searchPoi();
        viewModel.mSuggestionResult.observe(this, new Observer<ArrayList<HashMap<String, String>>>() {
            @Override
            public void onChanged(ArrayList<HashMap<String, String>> it) {
                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        getApplicationContext(),
                        it,
                        R.layout.item_layout,
                        new String[]{"key", "city", "district"},
                        new int[]{R.id.sug_key, R.id.sug_city, R.id.sug_dis}
                );
                sug_list.setAdapter(simpleAdapter);
                sug_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        try{
                            Intent intent=new Intent();
                            HashMap<String,String> r= viewModel.mSuggestionResult.getValue().get(pos);
                            if(r!=null){
                                ArrayList<HashMap<String,String>> dd=Hawk.get(BaseApplication.SEARCH_KEY);
                                if(dd==null) dd=new ArrayList();
                                dd.add(0,r);
                                if(dd.size()>5){
                                    int t=dd.size()-6;
                                    while(t>=0){
                                        dd.remove(t);
                                        t--;
                                    }
                                }
                                LogUtils.d(dd);
                                Hawk.put(BaseApplication.SEARCH_KEY,dd);
                            }
                            intent.putExtra("city",r.get("city"));
                            intent.putExtra("address",r.get("address"));
                            intent.putExtra("district",r.get("district"));
                            intent.putExtra("key",r.get("key"));
                            intent.putExtra("latitude",r.get("latitude"));
                            intent.putExtra("longitude",r.get("longitude"));
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }catch (Throwable t){
                            t.printStackTrace();
                        }
                    }
                });
                simpleAdapter.notifyDataSetChanged();
            }
        } );
        viewModel.mPoiResult.observe(this, new Observer<List<PoiInfo>>() {
            @Override
            public void onChanged(List<PoiInfo> poiInfos) {
                if(poiInfos==null) return;
                if(poiInfos.size()==0) return;
                int size=poiInfos.size();
                if(size>=1){
                    if(poiInfos.get(0)!=null){
                        ((TextView)findViewById(R.id.tv1)).setText(poiInfos.get(0).name);
                    }
                }
                if(size>=2){
                    if(poiInfos.get(1)!=null){
                        ((TextView)findViewById(R.id.tv2)).setText(poiInfos.get(1).name);
                    }
                }
                if(size>=3){
                    if(poiInfos.get(2)!=null){
                        ((TextView)findViewById(R.id.tv3)).setText(poiInfos.get(2).name);
                    }
                }
                if(size>=4){
                    if(poiInfos.get(3)!=null){
                        ((TextView)findViewById(R.id.tv4)).setText(poiInfos.get(3).name);
                    }
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.mSuggestionSearch.destroy();
    }

    public static void open(Activity context, String myCity, LatLng myLng){
        Intent intent= new Intent(context,Search2Activity.class);
        intent.putExtra("myCity",myCity);
        intent.putExtra("myLng",myLng);
        context.startActivityForResult(intent,333);
    }

    public static void open2(Activity context, String myCity, LatLng myLng){
        Intent intent= new Intent(context,Search2Activity.class);
        intent.putExtra("myCity",myCity);
        intent.putExtra("myLng",myLng);
        context.startActivityForResult(intent,444);
    }

    public static void  open3(Activity context, String myCity, LatLng myLng){
        Intent intent= new Intent(context,Search2Activity.class);
        intent.putExtra("myCity",myCity);
        intent.putExtra("myLng",myLng);
        context.startActivityForResult(intent,555);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_search;
    }
}
