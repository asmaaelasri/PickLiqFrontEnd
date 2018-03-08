package com.example.elasri.pickliq;

import java.util.Comparator;

/**
 * Created by elasri on 05/03/2018.
 */

public class Alcohol {
    public String name;
    public String brand;
    public String subcategory;
    public String range;
    public Integer price;
    public String category;

    public Alcohol(String name, String brand, String subcategory, String range, Integer price, String category) {
        this.name = name;
        this.brand = brand;
        this.subcategory = subcategory;
        this.range = range;
        this.price = price;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getRange() {
        return range;
    }

    public Integer getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Alcohol(String name, String brand, String subcategory, String range, Integer price) {
        this.name = name;
        this.brand = brand;
        this.subcategory = subcategory;
        this.range = range;
        this.price = price;
    }


}
