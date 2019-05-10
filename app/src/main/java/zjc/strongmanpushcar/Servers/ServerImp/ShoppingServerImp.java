package zjc.strongmanpushcar.Servers.ServerImp;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjc.strongmanpushcar.Activity.MainActivity;
import zjc.strongmanpushcar.Activity.Shopping.GuideActivity;
import zjc.strongmanpushcar.Activity.Shopping.ShoppingActivity;
import zjc.strongmanpushcar.Beans.Flight;
import zjc.strongmanpushcar.Beans.Food;
import zjc.strongmanpushcar.Beans.FoodCatergory;
import zjc.strongmanpushcar.Beans.Store;
import zjc.strongmanpushcar.Beans.StoreClassification;
import zjc.strongmanpushcar.Internet.Net;
import zjc.strongmanpushcar.Internet.OKHttp;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.Servers.Server.ShoppingServer;

public class ShoppingServerImp implements ShoppingServer {
    ShoppingActivity shoppingActivity;
    GuideActivity guideActivity;
    MainActivity mainActivity;

    public ShoppingServerImp(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public ShoppingServerImp(ShoppingActivity shoppingActivity){
        this.shoppingActivity = shoppingActivity;
    }

    public ShoppingServerImp(GuideActivity guideActivity) {
        this.guideActivity = guideActivity;
    }

    @Override
    public void getClassifyListByshopId(String shopId) {
        String URL = Net.getClassifyListByshopId +"?shopId="+shopId;
        Log.v("Zjc",URL);

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
        Log.v("Zjc",URL);
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
                        food.setFoodimg(jsonObject1.getString("goodsPhoto"));
                        food.setFoodPrice(jsonObject1.getString("goodsPrice"));
                        food.setFoodName(jsonObject1.getString("goodsName"));
                        food.setFoodIntrouduce(jsonObject1.getString("goodsIntroduce"));
                        food.setShopLeftClassifyId(jsonObject1.getString("shopLeftClassifyId"));
                        list.add(food);
                    }
                    shoppingActivity.FoodCallBack(list);
                }catch (Exception e){

                }
            }
        });
    }

    @Override
    public void getAllShopList() {
        String url=Net.getAllShopList;
        OKHttp.sendOkhttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"获取商品列表失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                List<Store> storeList=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Store  store = new Store();
                        store.setLatitude(jsonObject1.getString("latitude"));
                        store.setLongitude(jsonObject1.getString("longitude"));
                        store.setShopId(jsonObject1.getString("shopId"));
                        store.setStoreImg(jsonObject1.getString("shopPhoto1"));
                        store.setStoreName(jsonObject1.getString("shopName"));
                        storeList.add(store);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void findAllShopType() {
        String url=Net.findAllShopType;
        Log.v("Zjc",url);
        OKHttp.sendOkhttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"获取商品类型失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                List<StoreClassification> storetypeList=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StoreClassification storetype = new StoreClassification();
                        storetype.setClassificationName(jsonObject1.getString("shopType"));
                        storetypeList.add(storetype);
                    }
                    guideActivity.initClassification(storetypeList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void findShopByType(int shopType) {
        String url=Net.findShopByType+"?shopType="+shopType;
        Log.v("Zjc",url);
        OKHttp.sendOkhttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"获取商店" +
                        "失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                List<Store> storeList=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Store  store = new Store();
                        store.setLatitude(jsonObject1.getString("latitude"));
                        store.setLongitude(jsonObject1.getString("longitude"));
                        store.setShopId(jsonObject1.getString("shopId"));
                        store.setStoreImg(jsonObject1.getString("shopPhoto1"));
                        store.setStoreName(jsonObject1.getString("shopName"));
                        storeList.add(store);
                    }
                    guideActivity.getStoreData(storeList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getGoodsList(String shopId) {
        String url=Net.getGoodsList+"?shopId="+shopId;
        Log.v("Zjc",url);
        OKHttp.sendOkhttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"获取商拼列表" +
                        "失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                List<Food> foodList=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Food  food = new Food();
                        food.setFoodId(jsonObject1.getString("goodsId"));
                        food.setFoodimg(jsonObject1.getString("goodsPhoto"));
                        food.setFoodPrice(jsonObject1.getString("goodsPrice"));
                        food.setFoodName(jsonObject1.getString("goodsName"));
                        food.setFoodIntrouduce(jsonObject1.getString("goodsIntroduce"));
                        food.setShopLeftClassifyId(jsonObject1.getString("shopLeftClassifyId"));
                        foodList.add(food);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void findFlightByTicket(String useIdcard) {
        String url=Net.findFlightByTicket+"userIdCard="+useIdcard;
        Log.v("zjc",url);
        OKHttp.sendOkhttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"查找航班失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    Flight flight=new Flight();
                    flight.setArrival(jsonObject1.getString("arrival"));
                    flight.setArrivalTime(jsonObject1.getString("arrivalTime"));
                    flight.setCheckPort(jsonObject1.getString("checkPort"));
                    flight.setDeparture(jsonObject1.getString("departure"));
                    flight.setDepartureTime(jsonObject1.getString("departureTime"));
                    flight.setFlightCompany(jsonObject1.getString("flightCompany"));
                    flight.setFlightDate(jsonObject1.getString("flightDate"));
                    flight.setFlightId(jsonObject1.getString("flightId"));
                    flight.setFlightNumber(jsonObject1.getString("flightNumber"));
                    mainActivity.InitFlightMessage(flight);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
