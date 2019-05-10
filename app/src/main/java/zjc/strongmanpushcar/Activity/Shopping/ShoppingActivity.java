package zjc.strongmanpushcar.Activity.Shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zjc.strongmanpushcar.Activity.Map.GaoDeNavActivity;
import zjc.strongmanpushcar.Adapter.FoodAdapter;
import zjc.strongmanpushcar.Adapter.FoodCategoryAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.Beans.Food;
import zjc.strongmanpushcar.Beans.FoodCatergory;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;
import zjc.strongmanpushcar.Servers.Server.ShoppingServer;
import zjc.strongmanpushcar.Servers.ServerImp.ShoppingServerImp;

public class ShoppingActivity extends BaseActivity {
    @BindView(R.id.catergory_rv)
    RecyclerView catergory_rv;
    @BindView(R.id.food_rv)
    RecyclerView food_rv;
    RoundedImageView shopping_img;
    @BindView(R.id.shopping_bg)
    ImageView shopping_bg;
    @BindView(R.id.shopping_name)
    TextView shopping_name;
    FoodAdapter foodAdapter;
    FoodCategoryAdapter foodCategoryAdapter;

    ShoppingServer shoppingServer;
    static String ShopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        shoppingServer = new ShoppingServerImp(this);
        shopping_img = findViewById(R.id.shopping_img);
        ShopId=getIntent().getStringExtra("shopId");
        shoppingServer.getClassifyListByshopId(ShopId);
        Glide.with(getApplicationContext())
                .load(MyApplication.getShopimage())
                .asBitmap()
                .error(R.drawable.kfc)
                .into(shopping_bg);
        Glide.with(getApplicationContext())
                .load(MyApplication.getShopimage())
                .asBitmap()
                .error(R.drawable.kfc)
                .into(shopping_img);
        shopping_name.setText(MyApplication.getShopname());

    }
    public void CatergoryCallBack(final List<FoodCatergory> list){
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 catergory_rv.setLayoutManager(new LinearLayoutManager(ShoppingActivity.this));
                 foodCategoryAdapter = new FoodCategoryAdapter(ShoppingActivity.this, list,ShoppingActivity.this);
                 catergory_rv.setAdapter(foodCategoryAdapter);
             }
         });

    }
    public void getFood(String i){
        shoppingServer.getGoodsByClassifyId(i);

    }
    public void FoodCallBack(final List<Food> list){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                food_rv.setLayoutManager(new LinearLayoutManager(ShoppingActivity.this));
                foodAdapter = new FoodAdapter(ShoppingActivity.this,list);
                food_rv.setAdapter(foodAdapter);
            }
        });

    }
    @OnClick(R.id.tohere_ll)
    public void tohere_ll_Onlick(){
        Intent intent=new Intent(ShoppingActivity.this, GaoDeNavActivity.class);
        intent.putExtra("endLongitude", MyApplication.getStoreLongitude());
        intent.putExtra("endLatitude",MyApplication.getStoreLatitude());
        intent.putExtra("startLatitude",MyApplication.getmCurrentLantitude());
        intent.putExtra("startLongitude",MyApplication.getmCurrentLongitude());
        startActivity(intent);
    }

    @OnClick(R.id.shopping_back)
    public void shopping_back_OnClick(){
        finish();
    }
//    @BindView(R.id.shopping_love_img)
//    ImageView shopping_love_img;
//    private static int i = 0;
//    @OnClick(R.id.shopping_love_img)
//    public void shopping_love_img_OnClick(){
//        if (i == 0){
//            shopping_love_img.setImageResource(R.drawable.heart_click);
//            i++;
//        }else {
//            shopping_love_img.setImageResource(R.drawable.heart);
//            i--;
//        }
//
//    }
}
