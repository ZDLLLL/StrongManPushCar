package zjc.strongmanpushcar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application {
    private static Context context;//上下文 并生成get方法

    private SharedPreferences pref;//喜好设置
    private SharedPreferences.Editor editor;//让sharedPreferences处于编辑状态
    public static String IdCard ;
    public static String Name;

    @Override
    public void onCreate() {
        super.onCreate();
        context =getApplicationContext();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        Fresco.initialize(this);
        MultiDex.install(this);
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static String getIdCard() {
        return IdCard;
    }

    public static void setIdCard(String idCard) {
        IdCard = idCard;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }
}
