package com.example.foodorder;

public class POJO {
    String name;
    String quant;

    public POJO() {
    }

    public POJO(String name, String quant) {
        this.name = name;
        this.quant = quant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }
}
