package zjc.strongmanpushcar.Activity.Map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;

import zjc.strongmanpushcar.R;

public class GaoDeNavActivity extends GaoDeBaseActivity {
    AMap aMap;
    private double endLongitude;//经度
    private double endLatitude;//纬度
    private double startLongitude;//经度
    private double startLatitude;//纬度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gao_de_nav);

        endLongitude = getIntent().getDoubleExtra("endLongitude", 0);
        endLatitude = getIntent().getDoubleExtra("endLatitude", 0);
        startLongitude = getIntent().getDoubleExtra("startLongitude", 0);
        startLatitude = getIntent().getDoubleExtra("startLatitude", 0);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);

        if (aMap == null) {
            aMap = mAMapNaviView.getMap();

        }
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.showIndoorMap(true);
                aMap.getUiSettings().setScaleControlsEnabled(true);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
            }
        });
    }
    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mAMapNavi.calculateWalkRoute(new NaviLatLng(30.0923880000, 120.5119350000), new NaviLatLng(endLatitude, endLongitude));

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAMapNaviView.onSaveInstanceState(outState);
    }
}
