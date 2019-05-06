package zjc.strongmanpushcar.Servers.Server;

public interface ShoppingServer {
    public void getClassifyListByshopId(String shopId);
    public void getGoodsByClassifyId(String classifyId);
    public void getAllShopList();
    public void findAllShopType();
    public void findShopByType(int shopType);
    public void getGoodsList(String shopId);
}
