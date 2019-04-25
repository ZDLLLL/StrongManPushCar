package zjc.strongmanpushcar.Activity.Shopping;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import zjc.strongmanpushcar.Adapter.ClassificationAdapter;
import zjc.strongmanpushcar.Adapter.StoreAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.Beans.Store;
import zjc.strongmanpushcar.Beans.StoreClassification;
import zjc.strongmanpushcar.R;

public class GuideActivity extends BaseActivity {
    BGABanner guide_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        guide_banner = findViewById(R.id.guide_banner);
        initBanner();
        initClassification();
    }
    //左侧分类栏布局
    @BindView(R.id.guide_claai_rv)
    RecyclerView guide_claai_rv;
    ClassificationAdapter classificationAdapter;
    public void initClassification(){
        guide_claai_rv.setLayoutManager(new LinearLayoutManager(this));
        classificationAdapter = new ClassificationAdapter(this,getClassiData(),this);
        guide_claai_rv.setAdapter(classificationAdapter);
    }
    public List<StoreClassification> getClassiData(){
        List<StoreClassification> list = new ArrayList<>();
        StoreClassification store = new StoreClassification();
        store.setClassificationName("推荐");
        list.add(store);
        store = new StoreClassification();
        store.setClassificationName("热门");
        list.add(store);
        store = new StoreClassification();
        store.setClassificationName("吃货");
        list.add(store);
        store = new StoreClassification();
        store.setClassificationName("纪念品");
        list.add(store);
        return list;
    }
    @BindView(R.id.guide_store_rv)
    RecyclerView guide_store_rv;
    StoreAdapter storeAdapter;
    //点击分类布局出现的商店布局
    public void  getStoreData(int i){
        if (i!=0){
            guide_banner.setVisibility(View.GONE);
        }else {
            guide_banner.setVisibility(View.VISIBLE);
        }
        guide_store_rv.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        storeAdapter = new StoreAdapter(this,getStoreData(),this);
        guide_store_rv.setAdapter(storeAdapter);

    }
    public List<Store> getStoreData(){
        List<Store> list = new ArrayList<>();
        Store store = new Store();
        store.setStoreName("肯德基");
        list.add(store);
        store = new Store();
        store.setStoreName("德克士");
        list.add(store);
        store = new Store();
        store.setStoreName("必胜客");
        list.add(store);
        store = new Store();
        store.setStoreName("肯德基");
        list.add(store);
        store = new Store();
        store.setStoreName("肯德基");
        list.add(store);
        store = new Store();
        store.setStoreName("肯德基");
        list.add(store);
        store = new Store();
        store.setStoreName("肯德基");
        list.add(store);
        store = new Store();
        store.setStoreName("肯德基");
        list.add(store);
        store = new Store();
        store.setStoreName("肯德基");
        list.add(store);

        return list;
    }
    //轮播图布局
    public void initBanner(){
     guide_banner.setAutoPlayAble(true);
     guide_banner.setAdapter(new BGABanner.Adapter<CardView, String>() {
         @Override
         public void fillBannerItem(BGABanner banner, CardView itemView, String model, int position) {
             //图片布局
             SimpleDraweeView simpleDraweeView = itemView.findViewById(R.id.sdv_item_fresco_content);
             simpleDraweeView.setImageURI(Uri.parse(model));
         }
     });
        List<String> img = new ArrayList<String>();
        img.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556098253671&di=548fab2d687e61c058b22bdb5a04b0af&imgtype=0&src=http%3A%2F%2Fwx3.sinaimg.cn%2Flarge%2F4ad81cefly1fedyjf7vjij20g4092wg2.jpg");
        img.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556098321959&di=5c5472bd0de75ee1b95573fbd8b2f058&imgtype=0&src=http%3A%2F%2Fimage1.juanlaoda.com%2FQMMImg%2F2014%2F12%2F23%2F161231330.jpg");
        //设置Banner的布局，图片和文字
        guide_banner.setData(R.layout.item_fresco,img,null);
        //设置Banner的点击事件
        guide_banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

            }
        });
    }
}
