package zjc.strongmanpushcar.Internet;

public class Net {
    public final static String Head = "http://www.mcartoria.com:8080/cart/";//服务器地址
    public final static String getShopList = Head + "shop/getShopList";//根据类型查询商店信息
    public final static String getClassifyListByshopId = Head + "classify/getClassifyListByshopId";//查询商店下的商品分类栏
    public final static String getGoodsByClassifyId = Head + "goods/getGoodsByClassifyId";//根据商品分类寻找商品
    public final static String getAllShopList=Head+"shop/getAllShopList";//查找所有的商店
    public final static String findAllShopType=Head+"shop/findAllShopType";//查找商店的类型
    public final static String findShopByType=Head+"shop/findShopByTypeId";//根据类型查找商店列表
    public final static String getGoodsList=Head+"shop/getGoodsList";//查找商店的类型
    public final static String findAllFlight=Head+"flight/findAllFlight";//查找商店的类型
    public final static String findFlightByTicket=Head+"shop/findFlightByTicket";//根据身份证查找航班

}
