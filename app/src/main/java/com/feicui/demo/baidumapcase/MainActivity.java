package com.feicui.demo.baidumapcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main)
    RelativeLayout mainlayout;
    private MapView mMapView;
    @BindView(R.id.btn_satel)
    Button btnSatel;
    @BindView(R.id.btn_location)
    Button btnLocation;

    private BaiduMap mBaiduMap;
    private Unbinder bind;
    private LocationClient locationClient;
    private LatLng mMyloation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        /**
         * 1.找到MapView
         * 2.获取操作地图的控制器
         * 3.卫星视图和普通视图的切换
         * */

        // 地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0) // 地图俯仰的角度 -45--0
                .zoom(10) // 缩放的级别 3--21
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .zoomControlsEnabled(true) // 不显示缩放的控件
                .zoomGesturesEnabled(true) // 是否允许缩放的手势
                .compassEnabled(true) // 指南针
                // 具体方法可以查看API
                .mapStatus(mapStatus);

        /**
         * 目前来说，默认位置设置只能通过MapView的构造方法来添加，
         * 所以Demo里面是在布局中添加MapView，后面项目实施会动态创建
         * */

        // 动态创建一个MapView
        mMapView = new MapView(this, options);
        // 将MapView添加到布局上
        mainlayout.addView(mMapView,0);

        mBaiduMap = mMapView.getMap();

        // 为地图设置状态的监听
        mBaiduMap.setOnMapStatusChangeListener(mapStatusListener);
        intiView();
    }

    private void intiView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    private BaiduMap.OnMapStatusChangeListener mapStatusListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            Toast.makeText(MainActivity.this, "状态变化：维度" + mapStatus.target.latitude + "经度：" + mapStatus.target.longitude, Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick({R.id.btn_satel, R.id.btn_location})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_satel:
                // 切换卫星视图和普通视图
                if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    return;
                }
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.btn_location:
                // 定位开关
                /**
                 * 1.开启定位图层
                 * 2.初始化LocationClient
                 * 3.配置一些定位相关的参数LocationClientOption
                 * 4.设置定位的监听
                 * 5.开启定位
                 * */
                // TODO: 2016/11/3 0003 定位设置

                // 打开定位
                mBaiduMap.setMyLocationEnabled(true);
                // 初始化定位
                locationClient = new LocationClient(getApplicationContext());
                // 定位的相关配置
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);  // 打开GPS
                option.setCoorType("bd0911"); // 设置默认坐标类型，默认gcj02
                option.setIsNeedAddress(true); // 默认不需要
                option.setScanSpan(5000); // 设置扫描周期
                //添加配置到locationClient
                locationClient.setLocOption(option);

                //设置监听
                locationClient.registerLocationListener(locationListener);
                // 开启定位
                locationClient.start();
                // 针对某些机型，开启位置请求会失败
                locationClient.requestLocation();

                break;
            default:
                break;
        }
    }

    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 在定位的监听里面可以根据我们的结果来处理，显示定位的数据
            if (bdLocation == null) {
                // 没有定位信息，重新定位，重新请求定位信息
                locationClient.requestLocation(); // 请求定位
                return;
            }
            // 获取经度
            double lng = bdLocation.getLongitude();
            // 获取纬度
            double lat = bdLocation.getLatitude();

//            Toast.makeText(MainActivity.this, "经度：" + lng + "纬度：" + lat, Toast.LENGTH_SHORT).show();
            /**
             * 添加定位的标志
             * 1.拿到定位的信息
             * 2.给地图设置上定位信息
             * */
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .latitude(lat) // 纬度
                    .longitude(lng) // 经度
                    .accuracy(50f) // 定位的精度的大小
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);

            /**
             * 移动到我们的位置
             *
             * 1. 有我们定位的位置
             * 2. 移动的话，地图状态是不是发生变化了呢？要移动到定位的位置上去
             * 3. 地图状态的位置设置我们的位置
             * 4. 地图的状态变化了？我们需要对地图的状态进行更新
             */

            // 我们定位的位置
            mMyloation = new LatLng(lat,lng);
            moveToMyLocation();
        }
    };

    /**
     * 移动到我们的位置
     */
    private void moveToMyLocation() {
        // 主要是将地图的位置设置成当前位置
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mMyloation)
                .rotate(0) // 摆正地图
                .zoom(20)
                .build();

        // 更新地图的状态
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.animateMapStatus(update);
    }
}
