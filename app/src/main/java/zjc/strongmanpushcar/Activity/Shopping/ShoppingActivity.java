package zjc.strongmanpushcar.Activity.Shopping;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    public void CatergoryCallBack(List<FoodCatergory> list){
        catergory_rv.setLayoutManager(new LinearLayoutManager(this));
        foodCategoryAdapter = new FoodCategoryAdapter(this, list,this);
        catergory_rv.setAdapter(foodCategoryAdapter);
    }
    public void getFood(int i){
        if (i==0){
            food_rv.setLayoutManager(new LinearLayoutManager(this));
            foodAdapter = new FoodAdapter(this,getFoodDate());
            food_rv.setAdapter(foodAdapter);
        }else {
            food_rv.setLayoutManager(new LinearLayoutManager(this));
            foodAdapter = new FoodAdapter(this,getFoodDate2());
            food_rv.setAdapter(foodAdapter);
        }

    }

    public List<Food> getFoodDate(){
        List<Food>  foodlist = new ArrayList<>();
        Food food = new Food();
        food.setFoodName("渣渣涛");
        foodlist.add(food);
        food = new Food();
        food.setFoodName("渣涛");
        foodlist.add(food);
        food = new Food();
        food.setFoodName("涛");
        foodlist.add(food);
        return foodlist;
    }
    public List<Food> getFoodDate2(){
        List<Food>  foodlist = new ArrayList<>();
        Food food = new Food();
        food.setFoodName("周董涛");
        foodlist.add(food);
        food = new Food();
        food.setFoodName("董涛");
        foodlist.add(food);
        food = new Food();
        food.setFoodName("涛");
        foodlist.add(food);
        return foodlist;
    }
    @OnClick(R.id.shopping_back)
    public void shopping_back_OnClick(){
        finish();
    }
}
