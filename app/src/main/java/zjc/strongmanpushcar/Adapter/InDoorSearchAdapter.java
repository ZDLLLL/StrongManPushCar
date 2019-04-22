package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.PoiIndoorInfo;


import java.util.List;

import zjc.strongmanpushcar.Activity.MainActivity;
import zjc.strongmanpushcar.Activity.Map.RoutePlanningActivity;
import zjc.strongmanpushcar.R;


public class InDoorSearchAdapter extends RecyclerView.Adapter<InDoorSearchAdapter.InDoorSearchViewHolder> {
    List<PoiIndoorInfo> poiIndoorInfoList;
    MainActivity mainActivity;
    private Context mContext;

    public InDoorSearchAdapter(List<PoiIndoorInfo> poiIndoorInfoList, MainActivity mainActivity, Context mContext) {
        this.poiIndoorInfoList = poiIndoorInfoList;
        this.mainActivity = mainActivity;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public InDoorSearchAdapter.InDoorSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.indoorsearch_item,viewGroup,false);
        return new InDoorSearchAdapter.InDoorSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InDoorSearchAdapter.InDoorSearchViewHolder inDoorSearchViewHolder, final int i) {
        inDoorSearchViewHolder.indoorsearch_item_name_tv.setText(poiIndoorInfoList.get(i).name);
        inDoorSearchViewHolder.indoorsearch_item_address_tv.setText(poiIndoorInfoList.get(i).address);
        inDoorSearchViewHolder.goon_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainActivity, RoutePlanningActivity.class);
                intent.putExtra("endLongitude",poiIndoorInfoList.get(i).latLng.longitude);
                intent.putExtra("endLatitude",poiIndoorInfoList.get(i).latLng.latitude);
                intent.putExtra("floor",poiIndoorInfoList.get(i).floor);
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poiIndoorInfoList.size();
    }

    public class InDoorSearchViewHolder extends RecyclerView.ViewHolder {
        private TextView indoorsearch_item_name_tv;
        private TextView indoorsearch_item_address_tv;
        private Button goon_bt;
        public InDoorSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            indoorsearch_item_name_tv=itemView.findViewById(R.id.indoorsearch_item_name_tv);
            indoorsearch_item_address_tv=itemView.findViewById(R.id.indoorsearch_item_address_tv);
            goon_bt=itemView.findViewById(R.id.goon_bt);
        }
    }
}
