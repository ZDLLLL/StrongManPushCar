package zjc.strongmanpushcar.Activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorInfo;
import com.baidu.mapapi.search.poi.PoiIndoorOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.liuxin.floatmenulib.view.MenuLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zjc.strongmanpushcar.Activity.BlueTooth.ReturnCarFragment;
import zjc.strongmanpushcar.Activity.Entertainment.MovieActivity;
import zjc.strongmanpushcar.Activity.Message.MessageActivity;
import zjc.strongmanpushcar.Activity.Shopping.GuideActivity;
import zjc.strongmanpushcar.Adapter.FlightRecyclerViewAdapter;
import zjc.strongmanpushcar.Adapter.InDoorSearchAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.BaseTools.MyOrientationListener;
import zjc.strongmanpushcar.BaseTools.indoorview.BaseStripAdapter;
import zjc.strongmanpushcar.BaseTools.indoorview.StripListView;
import zjc.strongmanpushcar.BaseTools.overlayutil.IndoorPoiOverlay;
import zjc.strongmanpushcar.BaseTools.overlayutil.IndoorRouteOverlay;
import zjc.strongmanpushcar.R;

public class MainActivity extends BaseActivity implements OnGetPoiSearchResultListener,BaiduMap.OnBaseIndoorMapListener,FlightRecyclerViewAdapter.onSlidingViewClickListener{
    @BindView(R.id.bmapView)MapView mapView;
    BaiduMap mbaiduMap;
    LocationClient mLocationClient;
    private float mCurrentX;
    private double Longitude;//经度
    private double Latitude;//纬度
    /**
     * 最新一次的经纬度
     */
    public double mCurrentLantitude;
    public double mCurrentLongitude;
    /**
     * 当前的精度
     */
    private float mCurrentAccracy;
    //防止每次定位都重新设置中心点和marker
    private boolean isFirstLocation = true;
    Boolean isIndoor = true;
    IndoorRouteOverlay mIndoorRoutelineOverlay = null;
    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    RelativeLayout layout;

    private MyOrientationListener myOrientationListener;
    @BindView(R.id.mainsearch_content_et)
    EditText mainsearch_content_et;
    @BindView(R.id.mainsearch_search_tv)
    TextView mainsearch_search_tv;
    private PoiSearch mPoiSearch = null;
    static List<PoiIndoorInfo> poiIndoorInfoList = new ArrayList<>();
    @BindView(R.id.indoorsearch_rv)
    RecyclerView indoorsearch_rv;
    InDoorSearchAdapter inDoorSearchAdapter;
    @BindView(R.id.menuLayout)MenuLayout menuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbaiduMap=mapView.getMap();
        mbaiduMap.setMyLocationEnabled(true);
        stripListView = new StripListView(this);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_rl);
        layout.addView(stripListView);
        mFloorListAdapter = new BaseStripAdapter(this);


        InitLocation();
        InitIndoorMap();
        initOritationListener();
        InitIndoorSearch();
        stripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mMapBaseIndoorMapInfo == null) {
                    return;
                }
                String floor = (String) mFloorListAdapter.getItem(position);
                mbaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                mFloorListAdapter.setSelectedPostion(position);
                mFloorListAdapter.notifyDataSetInvalidated();
            }
        });
        InitFloatMenuBt();
        //肯德基
//        30.0983420000,120.5183130000
        //30.0983093423,120.5184390625
        //星巴克
            //30.0987473423,120.5183270625
        //老凤祥
    //        30.0992423423,120.5158610625


    }
    public void InitFloatMenuBt(){
        menuLayout.mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (main_message.getVisibility() == View.VISIBLE){
                        animateClose(main_message);
                        animationIvClose(main_left);
                    }
                menuLayout.popupMenu();
            }
        });

        menuLayout.setMainButtonColorAndIcon(R.color.colorWhite,R.mipmap.ic_add_black_36dp)
                .setListImageResource(R.drawable.flight32,R.drawable.entertainment32,R.drawable.toilet32,R.drawable.shop32,R.drawable.repay32)
                .setListText("所有航班","娱乐","厕所","导购","还车")
                .setOnItemClickListener(new MenuLayout.OnItemClickListener() {
                    @Override
                    public void onTextItemClickListener(int position, String str) {

                      //  Toast.makeText(MainActivity.this,"positiion"+position+":"+str,Toast.LENGTH_SHORT).show();
                        if(position==0){//显示所有航班
                            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                            startActivity(intent);
                        }else if(position==1){//娱乐
                            Intent intent = new Intent(MainActivity.this,MovieActivity.class);
                            startActivity(intent);
                        }else if(position==2){//厕所
                            MapBaseIndoorMapInfo indoorInfo = mbaiduMap.getFocusedBaseIndoorMapInfo();
                            if (indoorInfo == null) {
                                Toast.makeText(MainActivity.this, "当前无室内图，无法搜索", Toast.LENGTH_LONG).show();
                                return;
                            }
                            PoiIndoorOption option = new PoiIndoorOption().poiIndoorBid(
                                    indoorInfo.getID()).poiIndoorWd("厕所");
                            mPoiSearch.searchPoiIndoor(option);
                        }else if(position==3){//导购
                            Intent intent = new Intent(MainActivity.this,GuideActivity.class);
                            startActivity(intent);
                        }else {//还车

                        }
                        menuLayout.popupMenu();
                    }

                    @Override
                    public void onImageItemClickListener(int position, int resId) {

                       // Toast.makeText(MainActivity.this,"positiion"+position+":"+resId,Toast.LENGTH_SHORT).show();
                        if(position==0){//显示所有航班
                            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                            startActivity(intent);
                        }else if(position==1){//娱乐
                            Intent intent = new Intent(MainActivity.this,MovieActivity.class);
                            startActivity(intent);
                        }else if(position==2){//厕所
                            MapBaseIndoorMapInfo indoorInfo = mbaiduMap.getFocusedBaseIndoorMapInfo();
                            if (indoorInfo == null) {
                                Toast.makeText(MainActivity.this, "当前无室内图，无法搜索", Toast.LENGTH_LONG).show();
                                return;
                            }
                            PoiIndoorOption option = new PoiIndoorOption().poiIndoorBid(
                                    indoorInfo.getID()).poiIndoorWd("厕所");
                            mPoiSearch.searchPoiIndoor(option);
                        }else if(position==3){//导购
                            Intent intent = new Intent(MainActivity.this,GuideActivity.class);
                            startActivity(intent);
                        }else {//还车
                            ReturnCarFragment returnCarFragment = new ReturnCarFragment();
                            returnCarFragment.show(MainActivity.this.getSupportFragmentManager(),"ReturnCarFragment");
                        }
                        menuLayout.popupMenu();
                    }
                }).createMenu();

    }

    private void InitIndoorSearch() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void InitIndoorMap() {
        //开启室内图
        mbaiduMap.setIndoorEnable(true);
//        isIndoorBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isIndoor) {
//                    EnableIndoorMap();
//                } else {
//                    DisableIndoorMap();
//                }
//                isIndoor = !isIndoor;
//            }
//        });
        layout = findViewById(R.id.main_rl);
        stripListView = new StripListView(this);
        layout.addView(stripListView);
        setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(MainActivity.this);
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
    @OnClick(R.id.mainsearch_search_tv)
    public void mainsearch_search_tv_Onclick(){
        View showview = getWindow().peekDecorView();

        if (showview != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(showview.getWindowToken(), 0);
        }
        MapBaseIndoorMapInfo indoorInfo = mbaiduMap.getFocusedBaseIndoorMapInfo();
        if (indoorInfo == null) {
            Toast.makeText(MainActivity.this, "当前无室内图，无法搜索", Toast.LENGTH_LONG).show();
            return;
        }
        PoiIndoorOption option = new PoiIndoorOption().poiIndoorBid(
                indoorInfo.getID()).poiIndoorWd(mainsearch_content_et.getText().toString());
        mPoiSearch.searchPoiIndoor(option);
    }
    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult result) {
        mbaiduMap.clear();
        poiIndoorInfoList.clear();
        if (result == null || result.error == PoiIndoorResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MainActivity.this, "无结果", Toast.LENGTH_LONG).show();
            return;
        }
        if (result.error == PoiIndoorResult.ERRORNO.NO_ERROR) {
            IndoorPoiOverlay overlay = new MyIndoorPoiOverlay(mbaiduMap);
            poiIndoorInfoList = result.getmArrayPoiInfo();
            indoorsearch_rv.setVisibility(View.VISIBLE);
//            indoorsearch_path_bt.setVisibility(View.GONE);
            inDoorSearchAdapter = new InDoorSearchAdapter(poiIndoorInfoList, this, this);
            indoorsearch_rv.setLayoutManager(new LinearLayoutManager(this));
            indoorsearch_rv.setAdapter(inDoorSearchAdapter);
//            Log.d("zjccc", poiIndoorInfoList.get(1).address);
            mbaiduMap.setOnMarkerClickListener(overlay);
            //address
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
        }
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
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {

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
                Toast.makeText(MainActivity.this,"ID:"+buildingID+"name:"+buildingName+"楼层："+floor,Toast.LENGTH_LONG).show();
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
//            mBaiduMap.setMyLocationData(locData);

            if (isFirstLocation) {
                isFirstLocation = false;
                //设置并显示中心点
                setPosition2Center(mbaiduMap, location, true);
            }
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
//            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(ll).zoom(19.0f);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }
//    private void EnableIndoorMap() {
//        mbaiduMap.setIndoorEnable(true);
//        isIndoorBtn.setText("关闭室内图");
//        Toast.makeText(MainActivity.this, "室内图已打开", Toast.LENGTH_SHORT).show();
//    }
//    private void DisableIndoorMap() {
//        if (null != mIndoorRoutelineOverlay) {
//            mIndoorRoutelineOverlay.removeFromMap();
//            mIndoorRoutelineOverlay = null;
//        }
//        mbaiduMap.clear();
//        mbaiduMap.setIndoorEnable(false);
//        isIndoorBtn.setText("打开室内图");
//
//        Toast.makeText(MainActivity.this, "室内图已关闭", Toast.LENGTH_SHORT).show();
//    }

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

    private class MyIndoorPoiOverlay extends IndoorPoiOverlay {

        /**
         * 构造函数
         *
         * @param baiduMap 该 IndoorPoiOverlay 引用的 BaiduMap 对象
         */
        public MyIndoorPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        /**
         * 响应点击室内POI点事件
         * @param i
         *            被点击的poi在
         *            {@link com.baidu.mapapi.search.poi.PoiIndoorResult#getmArrayPoiInfo()} } 中的索引
         * @return
         */
        public boolean onPoiClick(int i) {
            PoiIndoorInfo info = getIndoorPoiResult().getmArrayPoiInfo().get(i);
            Log.v("zjc", String.valueOf(info.latLng.longitude));
            Log.v("zjc", String.valueOf(info.latLng.latitude));
            Toast.makeText(MainActivity.this, info.name + ",在" + info.floor + "层", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }
    @OnClick(R.id.main_left)
    public void main_left_OnClick(){
        if (main_message.getVisibility() == View.GONE){

            animateOpen(main_message);
            animationIvOpen(main_left);
            if(menuLayout.isStart) {
                menuLayout.popupMenu();
            }
        }else {
            animateClose(main_message);
            animationIvClose(main_left);
        }
    }
    @BindView(R.id.main_message)
    LinearLayout main_message;
    @BindView(R.id.main_left)
    ImageView main_left;
    //动画
    private float mDensity;
    private int mHiddenViewMeasuredHeight;
    private void animateOpen(View v) {
        v.setVisibility(View.VISIBLE);
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        v.measure(w, h);
        // 隐藏部分布局的高度
        mDensity = getResources().getDisplayMetrics().density;
        mHiddenViewMeasuredHeight = (int) (mDensity * 210 + 0.5);
//        mHiddenViewMeasuredHeight = (int) v.getMeasuredHeight();
        ValueAnimator animator = createDropAnimator(v, 0,
                mHiddenViewMeasuredHeight);
        animator.start();
    }

    private void animationIvOpen(ImageView view) {
        //旋转动画
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        view.startAnimation(animation);
    }

    private void animationIvClose(ImageView view) {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        view.startAnimation(animation);
    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }
}
