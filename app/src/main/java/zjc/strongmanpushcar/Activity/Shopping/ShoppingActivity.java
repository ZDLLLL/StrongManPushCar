package zjc.strongmanpushcar.Activity.Shopping;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zjc.strongmanpushcar.Adapter.FoodAdapter;
import zjc.strongmanpushcar.Adapter.FoodCategoryAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.Beans.Food;
import zjc.strongmanpushcar.Beans.FoodCatergory;
import zjc.strongmanpushcar.R;
import zjc.strongmanpushcar.Servers.Server.ShoppingServer;
import zjc.strongmanpushcar.Servers.ServerImp.ShoppingServerImp;

public class ShoppingActivity extends BaseActivity {
    @BindView(R.id.catergory_rv)
    RecyclerView catergory_rv;
    @BindView(R.id.food_rv)
    RecyclerView food_rv;
    FoodAdapter foodAdapter;
    FoodCategoryAdapter foodCategoryAdapter;

    ShoppingServer shoppingServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        shoppingServer = new ShoppingServerImp(this);
        String shopid = "2";
        shoppingServer.getClassifyListByshopId(shopid);
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

    @OnClick(R.id.shopping_back)
    public void shopping_back_OnClick(){
        finish();
    }
    @BindView(R.id.shopping_love_img)
    ImageView shopping_love_img;
    private static int i = 0;
    @OnClick(R.id.shopping_love_img)
    public void shopping_love_img_OnClick(){
        if (i == 0){
            shopping_love_img.setImageResource(R.drawable.heart_click);
            i++;
        }else {
            shopping_love_img.setImageResource(R.drawable.heart);
            i--;
        }

    }
}
