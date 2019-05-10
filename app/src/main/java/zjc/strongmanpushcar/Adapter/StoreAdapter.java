package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zjc.strongmanpushcar.Activity.Shopping.GuideActivity;
import zjc.strongmanpushcar.Activity.Shopping.View.StoreMessageFragment;
import zjc.strongmanpushcar.Beans.Store;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHodler> {
     public Context context;
     public LayoutInflater linearLayout;
     public List<Store> list;
     public GuideActivity guideActivity;
     public StoreAdapter(Context context, List<Store> list, GuideActivity guideActivity){
         this.context =context;
         this.list = list;
         this.linearLayout = LayoutInflater.from(context);
         this.guideActivity = guideActivity;
     }
    @NonNull
    @Override
    public StoreViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View view = linearLayout.inflate(R.layout.item_store,viewGroup,false);
        return new StoreViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHodler storeViewHodler, final int i) {
        storeViewHodler.store_name.setText(list.get(i).getStoreName());
        Glide.with(context)
                .load(list.get(i).getStoreImg())
                .asBitmap()
                .error(R.drawable.kfc)
                .into(storeViewHodler.store_img);
        storeViewHodler.store_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.setShopname(list.get(i).getStoreName());
                MyApplication.setShopimage(list.get(i).getStoreImg());
                MyApplication.setShopprice(list.get(i).getAverconsumption());
                MyApplication.setShopLocation(list.get(i).getShopLocation());
                MyApplication.setStoreLongitude(Double.parseDouble(list.get(i).getLongitude()));
                MyApplication.setStoreLatitude(Double.parseDouble(list.get(i).getLatitude()));
                MyApplication.setShopId(list.get(i).getShopId());
                StoreMessageFragment storeMessageFragment = new StoreMessageFragment();
                storeMessageFragment.show(guideActivity.getSupportFragmentManager(),"storeMessageFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StoreViewHodler extends RecyclerView.ViewHolder {
         ImageView store_img;
         TextView  store_name;
         FrameLayout store_fl;
        public StoreViewHodler(@NonNull View itemView) {
            super(itemView);
            store_img = itemView.findViewById(R.id.store_img);
            store_name = itemView.findViewById(R.id.store_name);
            store_fl = itemView.findViewById(R.id.store_fl);
        }
    }
}
