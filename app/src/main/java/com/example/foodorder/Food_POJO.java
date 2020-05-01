package com.example.foodorder;

import java.util.List;

public class Food_POJO {
    String date;
    List<item> order;

    public Food_POJO(String date, List<item> order) {
        this.date = date;
        this.order = order;
    }

    public Food_POJO() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<item> getOrder() {
        return order;
    }

    public void setOrder(List<item> order) {
        this.order = order;
    }
}
