package zjc.strongmanpushcar.Activity.Map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;

import java.math.BigDecimal;

import zjc.strongmanpushcar.R;

public class GaoDeNavActivity extends GaoDeBaseActivity {
    AMap aMap;
    private double endLongitude;//经度
    private double endLatitude;//纬度
    private double startLongitude;//经度
    private double startLatitude;//纬度
    private double [] baidu2amap;
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
        Log.v("ccc","endLongitude:"+endLongitude);
        Log.v("ccc","endLatitude:"+endLatitude);
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
    public static double[] baidu2AMap(double lat, double lon) {
        try {
            if (lat != 0 && lon != 0) {
                double var4 = 0.006401062D;
                double var6 = 0.0060424805D;
                double[] var2 = null;

                for (int var3 = 0; var3 < 2; ++var3) {
                    var2 = new double[2];
                    double var16 = lon - var4;
                    double var18 = lat - var6;
                    double[] var29 = new double[2];
                    double var24 = Math.cos(b(var16) + Math.atan2(var18, var16)) * (a(var18) + Math.sqrt(var16 * var16 + var18 * var18)) + 0.0065D;
                    double var26 = Math.sin(b(var16) + Math.atan2(var18, var16)) * (a(var18) + Math.sqrt(var16 * var16 + var18 * var18)) + 0.006D;
                    var29[1] = (c(var24));
                    var29[0] = (c(var26));
                    var2[1] = (c(lon + var16 - var29[1]));
                    var2[0] = (c(lat + var18 - var29[0]));
                    var4 = lon - var2[1];
                    var6 = lat - var2[0];
                }

                return var2;
            }
        } catch (Throwable var28) {
            // ll.a(var28, "OffsetUtil", "B2G");
        }

        return new double[]{lat, lon};
    }
    private static double a = 3.141592653589793D;

    private static double a(double var0) {
        return Math.sin(var0 * 3000.0D * (a / 180.0D)) * 2.0E-5D;
    }

    private static double b(double var0) {
        return Math.cos(var0 * 3000.0D * (a / 180.0D)) * 3.0E-6D;
    }

    private static double c(double var0) {
        return (new BigDecimal(var0)).setScale(8, 4).doubleValue();
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        baidu2amap = baidu2AMap(endLatitude,endLongitude);
        mAMapNavi.calculateWalkRoute(new NaviLatLng(30.0923880000, 120.5119350000), new NaviLatLng(baidu2amap[0],baidu2amap[1]));

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
