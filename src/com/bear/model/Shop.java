package com.bear.model;

public class Shop {
    int shop_id;
    String shop_name;
    String shop_address;
    String shop_tel;
    String shop_other;
    int shop_type;
    String shop_img;

    public int getCollection_state() {
        return collection_state;
    }

    public void setCollection_state(int collection_state) {
        this.collection_state = collection_state;
    }

    int collection_state=0;

    public String getShop_img() {
        return shop_img;
    }

    public void setShop_img(String shop_img) {
        this.shop_img = shop_img;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public String getShop_other() {
        return shop_other;
    }

    public void setShop_other(String shop_other) {
        this.shop_other = shop_other;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }
}
