package com.cj.baidunavi.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.route.*;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.cj.baidunavi.R;
import com.cj.baidunavi.base.BaseVM2Activity;

import java.util.Map;

public class Plan2Activity extends BaseVM2Activity<Plan2ViewModel>
{
    private static String TAG = Plan2Activity.class.getSimpleName();
    private RoutePlanSearch mSearch;
    private static final String FROM="FROM";
    private static final String TO="TO";

    private OnGetRoutePlanResultListener getRoutePlanListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            //创建WalkingRouteOverlay实例
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(((MapView) findViewById(R.id.mapview)).getMap());
            if (walkingRouteResult.getRouteLines() != null) {
                if (walkingRouteResult.getRouteLines().size() > 0) {//获取路径规划数据,(以返回的第一条数据为例)
                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    @Override
    protected void initData() {
        viewModel.changeResult.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                LogUtils.d("exchange");
                if(viewModel.name!=null){
                    ((TextView)findViewById(R.id.btn_navi1)).setText(FROM+":"+viewModel.name);
                    findViewById(R.id.btn_navi1).invalidate();
                }
                if(viewModel.toName!=null){
                    ((TextView)findViewById(R.id.btn_navi2)).setText(TO+":"+viewModel.toName);
                    ((TextView)findViewById(R.id.btn_navi2)).invalidate();
                }
                workSearch();
            }
        });
    }

    @Override
    protected void initView() {
        viewModel.pos = getIntent().getParcelableExtra("pos");
        viewModel.name = getIntent().getStringExtra("name");
        viewModel.toName = getIntent().getStringExtra("toName");
        viewModel.toPos = getIntent().getParcelableExtra("toPos");
        viewModel.myCity = getIntent().getStringExtra("myCity");
        viewModel.changeResult.setValue(true);
        mapView=findViewById(R.id.mapview);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(getRoutePlanListener);

        ((MapView) findViewById(R.id.mapview)).showScaleControl(false);
        ((MapView) findViewById(R.id.mapview)).showZoomControls(false);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanNode stNode = PlanNode.withLocation(viewModel.pos);
                PlanNode enNode = PlanNode.withLocation(viewModel.toPos);
                mSearch.walkingSearch(
                        new WalkingRoutePlanOption()
                                .from(stNode)
                                .to(enNode)
                );
            }
        });


        workSearch();
        findViewById(R.id.btn_navi1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.myCity != null) {
                    Search2Activity.open2(Plan2Activity.this, viewModel.myCity,
                            viewModel.pos);
                }
            }
        });
        findViewById(R.id.btn_navi2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.myCity != null) {
                    Search2Activity.open3(Plan2Activity.this, viewModel.myCity,
                            viewModel.pos);
                }
            }
        });
        findViewById(R.id.navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWalkNavi(false);
            }
        });

        findViewById(R.id.ar_navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWalkNavi(true);
            }
        });
        findViewById(R.id.exchange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = viewModel.name;
                viewModel.name = viewModel.toName;
                viewModel.toName = name;
                LatLng pos = viewModel.pos;
                viewModel.pos = viewModel.toPos;
                viewModel.toPos = pos;
                viewModel.changeResult.setValue(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String city = data.getStringExtra("city") == null ? "" : data.getStringExtra("city");
            String address = data.getStringExtra("address") == null ? "" : data.getStringExtra("address");
            String district = data.getStringExtra("district");
            LatLng latLng = new LatLng(data.getStringExtra("latitude") != null ?
                    Double.parseDouble(data.getStringExtra("latitude")) : 0.0,
                    data.getStringExtra("longitude") != null ?
                            Double.parseDouble(data.getStringExtra("longitude")) : 0.0);
            String key = data.getStringExtra("key")==null?"":data.getStringExtra("key");
            if (requestCode == 444) { //from
                ((TextView)findViewById(R.id.btn_navi1)).setText( FROM+":"+key);
                findViewById(R.id.btn_navi1).invalidate();
                viewModel.name = key ==null?"":key;
                viewModel.pos = latLng;
                viewModel.changeResult.setValue(true);
                workSearch();
            } else if (requestCode == 555) { //to
                ((TextView)findViewById(R.id.btn_navi2)).setText( TO+":"+key);
                findViewById(R.id.btn_navi2).invalidate();
                viewModel.toName = key ==null?"":key;
                viewModel.toPos = latLng;
                viewModel.changeResult.setValue(true);
                workSearch();
            }
        }
    }

    private void workSearch() {
        ((MapView)findViewById(R.id.mapview)).getMap().clear();
        if(viewModel.pos!=null && viewModel.toPos!=null){
            PlanNode stNode = PlanNode.withLocation(viewModel.pos);
            PlanNode enNode = PlanNode.withLocation(viewModel.toPos);
            mSearch.walkingSearch(
                    new WalkingRoutePlanOption()
                            .from(stNode)
                            .to(enNode)
            );
        }else{
            if(viewModel.pos!=null){
                ((MapView)findViewById(R.id.mapview)).getMap().setMapStatus(
                        MapStatusUpdateFactory.newLatLng(viewModel.pos));
            }
            if(viewModel.name!=null){
                ((TextView)findViewById(R.id.btn_navi1)).setText(FROM+":"+viewModel.name);
                findViewById(R.id.btn_navi1).invalidate();
            }
        }
    }

    private void startWalkNavi(boolean isAr) {
        if(viewModel.pos==null || viewModel.toPos==null) return;
        WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
        walkStartNode.setLocation( viewModel.pos);
        WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
        walkEndNode.setLocation(viewModel.toPos);
        walkParam =new  WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);
        walkParam.extraNaviMode(isAr? 1 : 0);
        Log.d(TAG, "startWalkNavi");
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "WalkNavi engineInitSuccess");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "WalkNavi engineInitFail");
                    WalkNavigateHelper.getInstance().unInitNaviEngine();
                }
            });

        } catch ( Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    private void routePlanWithWalkParam() {
        WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "WalkNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d(TAG, "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(Plan2Activity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                Log.d(TAG, "WalkNavi onRoutePlanFail");
            }
        });
    }

    private WalkNaviLaunchParam walkParam;
    private MapView mapView;

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_plan;
    }

    @Override
    protected Class<Plan2ViewModel> provideVM() {
        return Plan2ViewModel.class;
    }

    public static void open(Context context,String name,LatLng pos,String toName,LatLng toPos,String myCity){
        Intent intent = new Intent(context, Plan2Activity.class);
        intent.putExtra("name", name);
        intent.putExtra("pos", pos);
        intent.putExtra("toName",toName);
        intent.putExtra("toPos",toPos);
        intent.putExtra("myCity",myCity);
        context.startActivity(intent);
    }

}
