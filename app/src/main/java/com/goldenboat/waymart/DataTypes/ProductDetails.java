package com.goldenboat.waymart.DataTypes;


public class ProductDetails {
    String id="",pic_url="",product_title="";
    int price=0,count=1;

    public void setCount(int count) {
        this.count = count;
    }

    //public ProductDetails(String id, String pic_url, String product_title, int price) {
    //    this.id = id;
    //    this.pic_url = pic_url;
    //    this.product_title = product_title;
    //    this.price = price;
    //}

    public void setId(String id) {
        this.id = id;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public String getId() {
        return id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public int getPrice() {
        return price;
    }

    public void countplus() {
        count++;
    }
}
