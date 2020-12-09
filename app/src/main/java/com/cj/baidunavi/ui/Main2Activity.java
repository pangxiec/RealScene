package com.cj.baidunavi.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import com.apkfuns.logutils.LogUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.cj.baidunavi.PoiUp2Dialog;
import com.cj.baidunavi.R;
import com.cj.baidunavi.base.BaseVM2Activity;

public class Main2Activity extends BaseVM2Activity<Main2ViewModel>
{
    private BDAbstractLocationListener myLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            if (location.getCity() != null) {
                viewModel.myCity = location.getCity();
            }
            viewModel.myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            ((MapView) findViewById(R.id.mapview)).getMap().setMyLocationData(locData);
            MapStatus.Builder statusBuilder = new MapStatus.Builder();
            statusBuilder.target(new LatLng(location.getLatitude(), location.getLongitude()));
            statusBuilder.zoom(17f);
            ((MapView) findViewById(R.id.mapview)).getMap()
                    .setMapStatus(MapStatusUpdateFactory.newMapStatus(statusBuilder.build()));
            stopLocation();
        }
    };

    private BaiduMap.OnMapClickListener mapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {

        }

        @Override
        public void onMapPoiClick(MapPoi mapPoi) {
            if (mapPoi != null && mapPoi.getPosition() != null) {
                viewModel.latLng = new LatLng(mapPoi.getPosition().latitude, mapPoi.getPosition().longitude);
                viewModel.name = mapPoi.getName() == null ? "" : mapPoi.getName();
                viewModel.typeResult.setValue(1);
            }
        }
    };

    private MapView mapview;

    @Override
    protected void initView()
    {
        mapview = findViewById(R.id.mapview);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search2Activity.open(Main2Activity.this,
                                      viewModel.myCity, viewModel.myLatLng);
            }
        });

        findViewById(R.id.btn_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan2Activity.open(Main2Activity.this,
                        viewModel.myName, viewModel.myLatLng, null, null,
                        viewModel.myCity);
            }
        });

        findViewById(R.id.btn_ex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,Explore2Activity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_identify).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,IdentifyActivity.class);
                startActivity(intent);
            }
        });

        mapview.showScaleControl(false);
        mapview.showZoomControls(false);
        mapview.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                viewModel.latLng = marker.getPosition();
                viewModel.name = marker.getExtraInfo().getString("key", "");
                viewModel.typeResult.setValue(2);
                LogUtils.d("marker clicked");
                return false;
            }
        });
        //定位
        mapview.getMap().setMyLocationEnabled(true); //开启地图的定位图层
        initLocation();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 333);
        } else {
            startLocation();
        }
        findViewById(R.id.btn_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocation();
            }
        });

        //点击地图poi
        mapview.getMap().setOnMapClickListener(mapClickListener);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);
        option.setIsNeedAltitude(true);
        option.setIsNeedLocationPoiList(true);
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener(myLocationListener);
    }

    private void startLocation() {
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    private LocationClient mLocationClient;
    private PoiUp2Dialog poiUpDialog;

    @Override
    protected void initData() {
        viewModel.typeResult.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer it) {
                if (it == null) return;
                if (it == 0) {
                    if (poiUpDialog != null) poiUpDialog.dismiss();
                } else if (it == 1) {
                    poiUpDialog = new PoiUp2Dialog(Main2Activity.this, viewModel.name, viewModel.latLng);
                    poiUpDialog.setPoiUpListener(new PoiUp2Dialog.PoiUpListener() {
                        @Override
                        public void clicked(String name, LatLng pos) {
                            Plan2Activity.open(Main2Activity.this,
                                    viewModel.myName, viewModel.myLatLng, name, pos, viewModel.myCity);
                            poiUpDialog.dismiss();
                        }
                    });
                    poiUpDialog.show();
                } else if (it == 2) {
                    poiUpDialog = new PoiUp2Dialog(Main2Activity.this, viewModel.name, viewModel.latLng);
                    poiUpDialog.setPoiUpListener(new PoiUp2Dialog.PoiUpListener() {
                        @Override
                        public void clicked(String name, LatLng pos) {
                            Plan2Activity.open(Main2Activity.this,
                                    viewModel.myName, viewModel.myLatLng, name, pos, viewModel.myCity);
                            poiUpDialog.dismiss();
                        }
                    });
                    poiUpDialog.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 333) {
            mapview.getMap().clear();
            String city = data.getStringExtra("city") == null ? "" : data.getStringExtra("city");
            String address = data.getStringExtra("address") == null ? "" : data.getStringExtra("address");
            String district = data.getStringExtra("district");
            LatLng latLng = new LatLng(data.getStringExtra("latitude") == null ? 0.0 :
                    Double.parseDouble(data.getStringExtra("latitude")),
                    data.getStringExtra("longitude") == null ? 0.0 :
                            Double.parseDouble(data.getStringExtra("longitude")));
            //定义Maker坐标点
            LatLng point = new LatLng(latLng.latitude, latLng.longitude);
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
            //构建MarkerOption，用于在地图上添加Marker
            Bundle carryInfo = new Bundle();
            carryInfo.putString("city", city);
            carryInfo.putString("address", address);
            carryInfo.putString("district", district);
            carryInfo.putString("key", data.getStringExtra("key"));
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .extraInfo(carryInfo)
                    .clickable(true)
                    .icon(bitmap);
            mapview.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            //在地图上添加Marker，并显示
            mapview.getMap().addOverlay(option);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333) {
            if (grantResults.length!=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopLocation();
        mapview.getMap().setMyLocationEnabled(false);
        mapview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<Main2ViewModel> provideVM() {
        return Main2ViewModel.class;
    }
}
