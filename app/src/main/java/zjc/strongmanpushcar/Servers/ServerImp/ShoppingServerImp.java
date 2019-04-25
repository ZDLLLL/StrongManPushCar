package zjc.strongmanpushcar.Servers.ServerImp;

import android.os.Looper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjc.strongmanpushcar.Activity.Shopping.ShoppingActivity;
import zjc.strongmanpushcar.Beans.Food;
import zjc.strongmanpushcar.Beans.FoodCatergory;
import zjc.strongmanpushcar.Internet.Net;
import zjc.strongmanpushcar.Internet.OKHttp;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.Servers.Server.ShoppingServer;

public class ShoppingServerImp implements ShoppingServer {
    ShoppingActivity shoppingActivity;
    public ShoppingServerImp(ShoppingActivity shoppingActivity){
        this.shoppingActivity = shoppingActivity;
    };

    @Override
    public void getClassifyListByshopId(String shopId) {
        String URL = Net.getClassifyListByshopId +"?shopId="+shopId;
        OKHttp.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"得到商品信息分类失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                try {
                    List<FoodCatergory>  list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        FoodCatergory  foodCatergory = new FoodCatergory();
                        foodCatergory.setCatergryName(jsonObject1.getString("shopLeftClassifyName"));
                        foodCatergory.setCatergryId(jsonObject1.getString("shopLeftClassifyId"));
                        list.add(foodCatergory);
                    }
                    shoppingActivity.CatergoryCallBack(list);
                }catch (Exception e){

                }
            }
        });
    }

    @Override
    public void getGoodsByClassifyId(String classifyId) {
        String URL = Net.getGoodsByClassifyId +"?classifyId="+classifyId;
        OKHttp.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"得到商品失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                try {
                    List<Food>  list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Food  food = new Food();
                        food.setFoodId(jsonObject1.getString("goodsId"));
                        food.setFoodName(jsonObject1.getString("goodsName"));
                        food.setFoodIntrouduce(jsonObject1.getString("shopIntroduce"));
                        food.setFoodPrice(jsonObject1.getString("shopPrice"));
                        list.add(food);
                    }
                    shoppingActivity.FoodCallBack(list);
                }catch (Exception e){

                }
            }
        });
    }

}
