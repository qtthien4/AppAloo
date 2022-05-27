package com.example.aloo;

public class Food {
    public String idkey;
    public String foodname;
    public String typename;

    public Food() {

    }

    public Food( String foodname, String typename,String idkey) {
        this.idkey = idkey;
        this.foodname = foodname;
        this.typename = typename;
    }

    public String getIdkey() {
        return idkey;
    }

    public void setIdkey(String idkey) {
        this.idkey = idkey;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
