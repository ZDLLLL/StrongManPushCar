package zjc.strongmanpushcar.Beans;

public class Store {
    public String StoreName;//商店名
    public String StoreImg;//商店图片
    public String ShopId;//商店ID
    public String longitude;//经度
    public String latitude;//纬度
    public String averconsumption;//人均消费
    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getStoreImg() {
        return StoreImg;
    }

    public void setStoreImg(String storeImg) {
        StoreImg = storeImg;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAverconsumption() {
        return averconsumption;
    }

    public void setAverconsumption(String averconsumption) {
        this.averconsumption = averconsumption;
    }
}
