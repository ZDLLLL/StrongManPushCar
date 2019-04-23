package zjc.strongmanpushcar.Internet;

public class Net {
    public final static String Head = "http://www.mcartoria.com:8080/cart/";//服务器地址
    public final static String getShopList = Head + "shop/getShopList";//根据类型查询商店信息
    public final static String getClassifyListByshopId = Head + "classify/getClassifyListByshopId";//查询商店下的商品分类栏
}
