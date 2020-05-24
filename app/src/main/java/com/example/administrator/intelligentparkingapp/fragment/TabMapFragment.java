package com.example.administrator.intelligentparkingapp.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.intelligentparkingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabMapFragment extends Fragment {
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mlocationClient;
    private MylocationListener mlistener;
    private Context context;
    private double mLatitude;
    private double mLongitude;
    private float mCurrentX;
    private Button mGetMylocationBN;
    PopupMenu popup = null;
    private Activity mParentActivity = null;
    //自定义图标
    private BitmapDescriptor mIconLocation;
    //private MyOrientationListener myOrientationListener;
    //定位图层显示方式
    private MyLocationConfiguration.LocationMode locationMode;
    public TabMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        return inflater.inflate(R.layout.fragment_tab_map, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentActivity = this .getActivity();
        Log.d("-----------------------", mParentActivity.getApplicationContext().toString());
        SDKInitializer.initialize(mParentActivity.getApplicationContext());
        //mParentActivity.setContentView(R.layout.fragment_tab_map);
        this.context =mParentActivity.getApplicationContext();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        //判断是否为Android 6.0 以上的系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showLocMap();
        } else {
            initLocation();//initLocation为定位方法
        }
        setHasOptionsMenu(true);
    }

    private void initView() {
        mMapView = (MapView) mParentActivity.findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        //根据给定增量缩放地图级别
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        MapStatus mMapStatus;//地图当前状态
        MapStatusUpdate mMapStatusUpdate;//地图将要变化成的状态
        mMapStatus = new MapStatus.Builder().overlook(-45).build();
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }


    /**

     * 定位方法

     */

    private void initLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mlocationClient = new LocationClient(context);
        mlistener = new MylocationListener();
        //注册监听器
        mlocationClient.registerLocationListener(mlistener);
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        LocationClientOption mOption = new LocationClientOption();
        //设置坐标类型
        mOption.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        mOption.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        mOption.setOpenGps(true);
        //设置扫描间隔，单位是毫秒，当<1000(1s)时，定时定位无效
        int span = 1000;
        mOption.setScanSpan(span);
        //设置 LocationClientOption
        mlocationClient.setLocOption(mOption);
        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.location);
    }



    @Override
    public void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
        }
        //myOrientationListener.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mlocationClient.stop();
        //myOrientationListener.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**

     * 获取我的定位

     */

    public void getMyLocation() {
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    /**

     * 定义请求回调接口

     */
    public class MylocationListener implements BDLocationListener {
        private boolean isFirstIn = true;
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)//设定图标方向
                    .accuracy(bdLocation.getRadius())//getRadius 获取定位精度,默认值0.0f
                    .latitude(mLatitude)//百度纬度坐标
                    .longitude(mLongitude)//百度经度坐标
                    .build();
            mBaiduMap.setMyLocationData(data);
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(configuration);
            //判断是否为第一次定位,是的话需要定位到用户当前位置
            if (isFirstIn) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                Log.d("当前经度", "显示" + bdLocation.getLatitude());
                Log.d("当前纬度", "显示" + bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.setMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(context, "您当前的位置为：" + bdLocation.getAddrStr(),Toast.LENGTH_LONG).show();
            }
        }
    }

    /**

     * Android 6.0 以上的版本的定位方法

     */
    public void showLocMap() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(mParentActivity,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
            }, BAIDU_READ_PHONE_STATE);
        }
        else {
            initLocation();
        }
    }

    //Android 6.0 以上的版本申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
