package zjc.strongmanpushcar.Activity.BlueTooth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zjc.strongmanpushcar.Adapter.FindBlueToothAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.BaseTools.ClientManager;
import zjc.strongmanpushcar.Beans.DetailItem;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

public class BlueToothActivity extends BaseActivity {
    @BindView(R.id.bluetooth_rv)
    RecyclerView bluetooth_rv;
    @BindView(R.id.bluetooth_connect_tv)
    TextView bluetooth_connect_tv;
    List<SearchResult> list;
    FindBlueToothAdapter adapter;
    private static String mac;
    String mac1 = "00:15:80:90:76:22";
    private boolean mConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        list = new ArrayList<>();
        bluetooth_rv.setLayoutManager(new LinearLayoutManager(this));
        serachDevice();

    }
    //扫描设备
    public void serachDevice(){
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();

        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (!list.contains(device)){
                    list.add(device);
                    adapter = new FindBlueToothAdapter(BlueToothActivity.this,BlueToothActivity.this,list);

                    bluetooth_rv.setAdapter(adapter);

                }

            }

            @Override
            public void onSearchStopped() {
            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }


    //刷新按钮的点击事件
    @OnClick(R.id.bluetooth_refrash)
    public void bluetooth_refrash_OnClick(){
        list.clear();
        adapter = new FindBlueToothAdapter(BlueToothActivity.this,BlueToothActivity.this,list);
        bluetooth_rv.setAdapter(adapter);

        serachDevice();
    }

    public void connectDevices(String mac){
        this.mac = mac;
        connectDevice();
//        //注册连接监听
//        ClientManager.getClient().registerConnectStatusListener(mac, new BleConnectStatusListener() {
//            @Override
//            public void onConnectStatusChanged(String mac, int status) {
//                BluetoothLog.v(String.format("DeviceDetailActivity onConnectStatusChanged %d in %s",
//                        status, Thread.currentThread().getName()));
//
//                mConnected = (status == STATUS_CONNECTED);
//                connectDeviceIfNeeded();
//            }
//        });
    }
    //连接监听
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            BluetoothLog.v(String.format("DeviceDetailActivity onConnectStatusChanged %d in %s",
                    status, Thread.currentThread().getName()));

            mConnected = (status == STATUS_CONNECTED);
            connectDeviceIfNeeded();
        }
    };
    private void connectDeviceIfNeeded() {
        if (!mConnected) {
            connectDevice();
        }
    }
    //设置连接方式
    private void connectDevice() {
        bluetooth_connect_tv.setVisibility(View.VISIBLE);
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
        //发送连接
        ClientManager.getClient().connect(mac, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    setGattProfile(profile);
                }
                bluetooth_connect_tv.setVisibility(View.GONE);
                Toast.makeText(BlueToothActivity.this,"设备连接成功",Toast.LENGTH_LONG).show();
            }
        });
    }
    //得到最后一个Service和Characteristic用于传递两个UUID
    public void setGattProfile(BleGattProfile profile) {
        List<DetailItem> items = new ArrayList<DetailItem>();

        List<BleGattService> services = profile.getServices();

        for (BleGattService service : services) {
            items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
            List<BleGattCharacter> characters = service.getCharacters();
            for (BleGattCharacter character : characters) {
                items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
                MyApplication.setServer(service.getUUID());
                MyApplication.setCharacteristic(character.getUuid());
            }
        }
        String s = MyApplication.getServer().toString();
        String c = MyApplication.getCharacteristic().toString();
        Log.d("Server",MyApplication.getServer().toString());
        Log.d("Characteristic",MyApplication.getCharacteristic().toString());
    }
}
