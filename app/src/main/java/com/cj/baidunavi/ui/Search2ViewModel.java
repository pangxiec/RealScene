package com.cj.baidunavi.ui;

import androidx.lifecycle.MutableLiveData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.*;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cj.baidunavi.base.Base2ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 大头 on 2020/5/6.
 */
public class Search2ViewModel extends Base2ViewModel implements OnGetSuggestionResultListener, OnGetPoiSearchResultListener {
    String myCity = "重庆";
    LatLng myLng;
    MutableLiveData<ArrayList<HashMap<String,String>>> mSuggestionResult= new MutableLiveData<>();
    MutableLiveData<List<PoiInfo>> mPoiResult= new MutableLiveData<>();
    SuggestionSearch mSuggestionSearch;
    PoiSearch poiSearch;

    public Search2ViewModel() {
        mSuggestionSearch= SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        poiSearch= PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if(suggestionResult==null || suggestionResult.getAllSuggestions()==null) return;
        ArrayList<HashMap<String,String>> sugResult=new ArrayList<HashMap<String,String>>();
        for(SuggestionResult.SuggestionInfo i : suggestionResult.getAllSuggestions()){
            if(i.key!=null && i.address!=null && i.city!=null && i.district!=null &&
                    i.getPt()!=null ){
                HashMap<String,String> iR=new HashMap<String,String>();
                iR.put("key",i.key);
                iR.put("address",i.address);
                iR.put("city",i.city);
                iR.put("district",i.district);
                iR.put("latitude",i.getPt().latitude+"");
                iR.put("longitude",i.getPt().longitude+"");
                sugResult.add(iR);
            }
        }

        mSuggestionResult.setValue(sugResult);
    }

    public void searchForSuggestions(String cs){
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        mSuggestionSearch.requestSuggestion(
                new SuggestionSearchOption()
                        .keyword(cs==null?"":cs) // 关键字
                        .city(myCity)
        );// 城市
    }

    public void searchPoi(){
        if (myLng != null) {
            poiSearch.searchNearby(
                    new PoiNearbySearchOption()
                            .location(myLng)
                            .radius(1000)
                            .pageCapacity(4)
                            .keyword("美食")
            );
        }
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult!=null){
            mPoiResult.setValue(poiResult.getAllPoi());
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        poiSearch.destroy();
        mSuggestionSearch.destroy();
    }
}
