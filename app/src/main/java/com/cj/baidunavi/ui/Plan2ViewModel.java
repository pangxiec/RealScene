package com.cj.baidunavi.ui;

import androidx.lifecycle.MutableLiveData;
import com.baidu.mapapi.model.LatLng;
import com.cj.baidunavi.base.Base2ViewModel;


/**
 * Created by 大头 on 2020/5/6.
 */
public class Plan2ViewModel extends Base2ViewModel {
    MutableLiveData<Boolean> changeResult=new MutableLiveData<>();
    LatLng pos;
    String name="";
    LatLng toPos;
    String toName="";
    String myCity="";
}
