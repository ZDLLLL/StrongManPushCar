package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.BluetoothLog;

import java.util.ArrayList;
import java.util.List;

import zjc.strongmanpushcar.Activity.BlueTooth.BlueToothActivity;
import zjc.strongmanpushcar.BaseTools.ClientManager;
import zjc.strongmanpushcar.Beans.DetailItem;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

public class FindBlueToothAdapter extends RecyclerView.Adapter<FindBlueToothAdapter.FindBlueToothViewHodler> {
    Context context;
    LayoutInflater linearLayout;
    List<SearchResult> list;
    private static String mac;

    BlueToothActivity blueToothActivity;
    public FindBlueToothAdapter(BlueToothActivity blueToothActivity ,Context context, List<SearchResult> list){
        this.context = context;
        this.linearLayout = LayoutInflater.from(context);
        this.list = list;
        this.blueToothActivity = blueToothActivity;
    }
    @NonNull
    @Override
    public FindBlueToothViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = linearLayout.inflate(R.layout.item_device,viewGroup,false);

        return new FindBlueToothViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindBlueToothViewHodler findBlueToothViewHodler, final int i) {
        if (list.get(i).getName().equals("NULL")){
            findBlueToothViewHodler.item_device_name.setText(list.get(i).getAddress());
        }else {
            findBlueToothViewHodler.item_device_name.setText(list.get(i).getName());
        }
        findBlueToothViewHodler.item_device_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mac = list.get(i).getAddress();
                MyApplication.setMac(mac);
                blueToothActivity.connectDevices(mac);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FindBlueToothViewHodler extends RecyclerView.ViewHolder {
        TextView  item_device_name;
        LinearLayout item_device_ll;
        public FindBlueToothViewHodler(@NonNull View itemView) {
            super(itemView);
            item_device_name = itemView.findViewById(R.id.item_device_name);
            item_device_ll = itemView.findViewById(R.id.item_device_ll);
        }
    }

}
