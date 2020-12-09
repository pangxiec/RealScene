package com.cj.baidunavi.ui;

import androidx.lifecycle.MutableLiveData;
import com.baidu.mapapi.model.LatLng;
import com.cj.baidunavi.base.Base2ViewModel;

/**
 * Created by 大头 on 2020/5/6.
 */
public class Main2ViewModel extends Base2ViewModel
{
    MutableLiveData<Integer> typeResult=new MutableLiveData<>();
    LatLng latLng;
    String name="";
    LatLng myLatLng;
    String myName="我的位置";
    String myCity="";
}
