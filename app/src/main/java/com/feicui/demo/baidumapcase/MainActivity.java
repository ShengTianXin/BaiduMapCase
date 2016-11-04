package com.feicui.demo.baidumapcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.btn_satel)
    Button btnSatel;
    @BindView(R.id.btn_location)
    Button btnLocation;

    private BaiduMap mBaiduMap;
    private Unbinder bind;

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
        mBaiduMap = mMapView.getMap();

        // 地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0) // 地图俯仰的角度 -45--0
                .zoom(15) // 缩放的级别 3--21
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .zoomControlsEnabled(false) // 不显示缩放的控件
                .zoomGesturesEnabled(true) // 是否允许缩放的手势
                // 具体方法可以查看API
                .mapStatus(mapStatus);

        /**
         * 目前来说，默认位置设置只能通过MapView的构造方法来添加，
         * 所以Demo里面是在布局中添加MapView，后面项目实施会动态创建
         * */
//        MapView mapView = new MapView(this,options);

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

    @OnClick({R.id.btn_satel,R.id.btn_location})
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
//                mBaiduMap.setMyLocationEnabled(true);
//                LocationClient locationClient = new LocationClient(getApplicationContext());
//                LocationClientOption option = new LocationClientOption();

                break;
            default:
                break;
        }
    }
}
