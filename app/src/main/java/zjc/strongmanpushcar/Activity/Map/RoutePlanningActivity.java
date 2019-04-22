package zjc.strongmanpushcar.Activity.Map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorInfo;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorPlanNode;
import com.baidu.mapapi.search.route.IndoorRouteLine;
import com.baidu.mapapi.search.route.IndoorRoutePlanOption;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWNaviCalcRouteListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.RouteNodeType;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zjc.strongmanpushcar.Activity.MainActivity;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.BaseTools.MyOrientationListener;
import zjc.strongmanpushcar.BaseTools.NormalUtils;
import zjc.strongmanpushcar.BaseTools.indoorview.BaseStripAdapter;
import zjc.strongmanpushcar.BaseTools.indoorview.StripListView;
import zjc.strongmanpushcar.BaseTools.overlayutil.IndoorPoiOverlay;
import zjc.strongmanpushcar.BaseTools.overlayutil.IndoorRouteOverlay;
import zjc.strongmanpushcar.R;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
public class RoutePlanningActivity extends BaseActivity implements BaiduMap.OnBaseIndoorMapListener, OnGetRoutePlanResultListener {
    @BindView(R.id.MBaiduMap)
    MapView mapView;
    BaiduMap mbaiduMap;
    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    LocationClient mLocationClient;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    RelativeLayout layout;
    IndoorRouteLine mIndoorRouteline;
    int nodeIndex = -1;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private double Longitude;//经度
    private double Latitude;//纬度
    /**
     * 最新一次的经纬度
     */
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    /**
     * 当前的精度
     */
    private float mCurrentAccracy;
    //防止每次定位都重新设置中心点和marker
    private boolean isFirstLocation = true;
    RoutePlanSearch mSearch;
    String latLng;//目的地
    private double endLongitude;//经度
    private double endLatitude;//纬度
    String floor;
    @BindView(R.id.navigation_bt)Button navigation_bt;
    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "StrongManPushCar";
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION

    };
    private static final int authBaseRequestCode = 1;

    private boolean hasInitSuccess = false;
    WalkNaviLaunchParam walkParam;
    static final String ROUTE_PLAN_NODE = "routePlanNode";
    private BNRoutePlanNode mStartNode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_planning);
        endLongitude=getIntent().getDoubleExtra("endLongitude",0);
        endLatitude=getIntent().getDoubleExtra("endLatitude",0);
        floor=getIntent().getStringExtra("floor");

        mbaiduMap=mapView.getMap();
        mbaiduMap.setMyLocationEnabled(true);
        stripListView = new StripListView(this);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.routeplanning_rl);
        layout.addView(stripListView);
        mFloorListAdapter = new BaseStripAdapter(this);
        InitLocation();
        InitIndoorMap();
        initOritationListener();
        initRoutePlanning();

        if (initDirs()) {
            initNavi();
        }

    }
    @OnClick(R.id.navigation_bt)
    public void navigation_bt_Onclick(){
            Intent intent=new Intent(RoutePlanningActivity.this,GaoDeNavActivity.class);
            startActivity(intent);
    }
//        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//            if (!hasInitSuccess) {
//                Toast.makeText(RoutePlanningActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
//            }
//            BNRoutePlanNode sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", "百度大厦", CoordinateType.BD09LL);
//            BNRoutePlanNode eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", "北京天安门", CoordinateType.BD09LL);
//
//
//            mStartNode = sNode;
//
//            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
//            list.add(sNode);
//            list.add(eNode);
//
//            BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
//                    list,
//                    IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
//                    null,
//                    new Handler(Looper.getMainLooper()) {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            switch (msg.what) {
//                                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
//                                    Toast.makeText(RoutePlanningActivity.this, "算路开始", Toast.LENGTH_SHORT)
//                                            .show();
//                                    break;
//                                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
//                                    Toast.makeText(RoutePlanningActivity.this, "算路成功", Toast.LENGTH_SHORT)
//                                            .show();
//                                    break;
//                                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
//                                    Toast.makeText(RoutePlanningActivity.this, "算路失败", Toast.LENGTH_SHORT)
//                                            .show();
//                                    break;
//                                case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
//                                    Toast.makeText(RoutePlanningActivity.this, "算路成功准备进入导航", Toast.LENGTH_SHORT)
//                                            .show();
//                                    Intent intent = new Intent(RoutePlanningActivity.this,
//                                            TTSDemoGuideActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                    break;
//                                default:
//                                    // nothing
//                                    break;
//                            }
//                        }
//                    });

//        }


    //导航引擎算路
    private void naviCalcRoute(int routeIndex) {
        WalkNavigateHelper.getInstance().naviCalcRoute(routeIndex, new IWNaviCalcRouteListener() {
            @Override
            public void onNaviCalcRouteSuccess() {

                Intent intent = new Intent();
                intent.setClass(RoutePlanningActivity.this, TTSDemoGuideActivity.class);
                startActivity(intent);
            }


            @Override
            public void onNaviCalcRouteFail(WalkRoutePlanError error) {
                Log.d("zjc", "WalkNavi naviCalcRoute fail");
            }
        });
    }
    public void initRoutePlanning(){
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        // 发起室内路线规划检索 30.0990130000,120.5161120000
        IndoorPlanNode startNode = new IndoorPlanNode(new LatLng(30.0983420000, 120.5183130000), "F1");
        IndoorPlanNode endNode = new IndoorPlanNode(new LatLng(endLatitude, endLongitude), floor);
        IndoorRoutePlanOption irpo = new IndoorRoutePlanOption().from(startNode).to(endNode);
        mSearch.walkingIndoorSearch(irpo);
        LatLng start=new LatLng(30.0983420000, 120.5183130000);
        LatLng end=new LatLng(endLatitude, endLongitude);
        Double a=DistanceUtil. getDistance(start, end);
        Toast.makeText(RoutePlanningActivity.this,"两点之间的距离"+a,Toast.LENGTH_LONG).show();

    }

    @OnClick(R.id.routeplanning_back_ib)
    public void routeplanning_back_ib_Onclick(){
        finish();
    }
    public void InitLocation(){

        //定位初始化
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        MyOwnLocationListener myLocationListener = new MyOwnLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }
    private void InitIndoorMap() {
        //开启室内图
        mbaiduMap.setIndoorEnable(true);
        stripListView = new StripListView(this);
        layout = (RelativeLayout) findViewById(R.id.routeplanning_rl);
        layout.addView(stripListView);
        setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(RoutePlanningActivity.this);
//        设置监听事件来监听进入和移出室内图
        mbaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b == false || mapBaseIndoorMapInfo == null) {
                    stripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                mFloorListAdapter.setmFloorList( mapBaseIndoorMapInfo.getFloors());
                stripListView.setVisibility(View.VISIBLE);
                stripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });
//        实现楼层间地图切换,展示不同楼层的室内图
        stripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mMapBaseIndoorMapInfo == null) {
                    return;
                }
                // 切换楼层信息
                //strID 通过 mMapBaseIndoorMapInfo.getID()方法获得
                String floor = (String) mFloorListAdapter.getItem(position);
                mbaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                mFloorListAdapter.setSelectedPostion(position);
                mFloorListAdapter.notifyDataSetInvalidated();
            }
        });
    }
    @Override
    public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {

        if (b) {
            stripListView.setVisibility(View.VISIBLE);
            if (mapBaseIndoorMapInfo == null) {
                return;
            }
            mFloorListAdapter.setmFloorList(mapBaseIndoorMapInfo.getFloors());
            stripListView.setStripAdapter(mFloorListAdapter);

        } else {
            stripListView.setVisibility(View.GONE);
        }
        mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

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
        if (indoorRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            IndoorRouteOverlay overlay = new IndoorRouteOverlay(mbaiduMap);
            for (int i=0;i<indoorRouteResult.getRouteLines().size();i++){
                mIndoorRouteline = indoorRouteResult.getRouteLines().get(i);
                nodeIndex = -1;
                overlay.setData(mIndoorRouteline);
                overlay.addToMap();
                overlay.zoomToSpan();
            }


//            mBtnPre.setVisibility(View.VISIBLE);
//            mBtnNext.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public class MyOwnLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            if (location.getFloor() != null) {
                // 当前支持高精度室内定位
                String buildingID = location.getBuildingID();// 百度内部建筑物ID
                String buildingName = location.getBuildingName();// 百度内部建筑物缩写
                String floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
                Log.v("aaa",buildingID);
                Log.v("bbb",buildingName);
                Log.v("ccc",floor);
                Toast.makeText(RoutePlanningActivity.this,"ID:"+buildingID+"name:"+buildingName+"楼层："+floor,Toast.LENGTH_LONG).show();
                mLocationClient.startIndoorMode();// 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
            }
            //获取当前定位
            MyLocationData locData = new MyLocationData.Builder()
                    .direction(mCurrentX)//
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            Longitude=location.getLongitude();

            Latitude=location.getLatitude();
            mCurrentAccracy = location.getRadius();
            // 设置定位数据
            mbaiduMap.setMyLocationData(locData);
            mCurrentLantitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();

            Log.v("zjc","Longitude:"+Longitude+",Latitude:"+Latitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.zoom(21.0f);
            mbaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            mBaiduMap.setMyLocationData(locData);

//            if (isFirstLocation) {
//                isFirstLocation = false;
//                //设置并显示中心点
//                setPosition2Center(mbaiduMap, location, true);
//            }
        }
    }
    /**
     * 设置中心点和添加marker
     *
     * @param map
     * @param bdLocation
     * @param isShowLoc
     */
    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);

        if (isShowLoc) {
            LatLng centerpos = new LatLng(30.0992120000,120.5168240000); // 北京南站
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(centerpos).zoom(21.0f);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(ll).zoom(19.0f);

        }
    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mbaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    /**
     * 初始化方向传感器
     */
    private void initOritationListener()
    {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setmOnOrientationListener(new MyOrientationListener.OnOrientationListener()
                {
                    @Override
                    public void onOrientationChanged(float x)
                    {
                        mCurrentX = (int) x;

                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mCurrentAccracy)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mCurrentX)
                                .latitude(mCurrentLantitude)
                                .longitude(mCurrentLongitude).build();
                        // 设置定位数据
                        mbaiduMap.setMyLocationData(locData);
                        // 设置自定义图标
//                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                                .fromResource(R.mipmap.navi_map_gps_locked);
                        MyLocationConfiguration config = new MyLocationConfiguration(
                                MyLocationConfiguration.LocationMode.NORMAL, true, null);
                        mbaiduMap.setMyLocationConfigeration(config);

                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //开启定位的允许
        mbaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
            //开启方向传感器
            myOrientationListener.start();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
        mbaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //停止方向传感器
        myOrientationListener.stop();
        //client.disconnect();
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        Log.v("zjzz",msg);
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(RoutePlanningActivity.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(RoutePlanningActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(RoutePlanningActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        String a;
                        a="aaaa";
                        Toast.makeText(RoutePlanningActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 不使用内置TTS
//         BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError"+message);
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(RoutePlanningActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        }
    }
}
