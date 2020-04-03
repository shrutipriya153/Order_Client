package com.example.foodorder;

public class Item_POJO {
    private String name,img,price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Item_POJO(String name, String img, String price) {
        this.name = name;
        this.img = img;
        this.price = price;
    }
}
