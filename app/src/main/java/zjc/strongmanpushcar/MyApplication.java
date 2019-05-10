package zjc.strongmanpushcar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.inuker.bluetooth.library.BluetoothContext;

import java.util.UUID;

public class MyApplication extends Application {
    private static Context context;//上下文 并生成get方法

    private SharedPreferences pref;//喜好设置
    private SharedPreferences.Editor editor;//让sharedPreferences处于编辑状态
    public static String IdCard ;
    public static String Name;
    private static MyApplication instance;
    //蓝牙小车的服务和特征UUID
    public static UUID Server;
    public static UUID Characteristic;
    public static String Mac;
    //商店信息
    public static String shopLocation;//地点
    public static String shopname;//商店名称
    public static String shopprice;//商店价格
    public static String shopimage;//商店图片
    public static double storeLongitude;//经度
    public static double storeLatitude;//纬度
    public static double mCurrentLongitude;//经度
    public static double mCurrentLantitude;//纬度
    public static String ShopId;
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
        //图片加载的初始化
        Fresco.initialize(this);
        MultiDex.install(this);
        //蓝牙加载的初始化
        instance = this;
        BluetoothContext.set(this);
    }

    public static String getShopLocation() {
        return shopLocation;
    }

    public static void setShopLocation(String shopLocation) {
        MyApplication.shopLocation = shopLocation;
    }

    public static String getShopname() {
        return shopname;
    }

    public static void setShopname(String shopname) {
        MyApplication.shopname = shopname;
    }

    public static String getShopprice() {
        return shopprice;
    }

    public static void setShopprice(String shopprice) {
        MyApplication.shopprice = shopprice;
    }

    public static String getShopimage() {
        return shopimage;
    }

    public static void setShopimage(String shopimage) {
        MyApplication.shopimage = shopimage;
    }

    public static String getMac() {
        return Mac;
    }

    public static void setMac(String mac) {
        Mac = mac;
    }

    public static UUID getServer() {
        return Server;
    }

    public static void setServer(UUID server) {
        Server = server;
    }

    public static UUID getCharacteristic() {
        return Characteristic;
    }

    public static void setCharacteristic(UUID characteristic) {
        Characteristic = characteristic;
    }

    public static Application getInstance() {
        return instance;
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

    public static double getStoreLongitude() {
        return storeLongitude;
    }

    public static void setStoreLongitude(double storeLongitude) {
        MyApplication.storeLongitude = storeLongitude;
    }

    public static double getStoreLatitude() {
        return storeLatitude;
    }

    public static void setStoreLatitude(double storeLatitude) {
        MyApplication.storeLatitude = storeLatitude;
    }

    public static double getmCurrentLongitude() {
        return mCurrentLongitude;
    }

    public static void setmCurrentLongitude(double mCurrentLongitude) {
        MyApplication.mCurrentLongitude = mCurrentLongitude;
    }

    public static double getmCurrentLantitude() {
        return mCurrentLantitude;
    }

    public static void setmCurrentLantitude(double mCurrentLantitude) {
        MyApplication.mCurrentLantitude = mCurrentLantitude;
    }

    public static String getShopId() {
        return ShopId;
    }

    public static void setShopId(String shopId) {
        ShopId = shopId;
    }
}
