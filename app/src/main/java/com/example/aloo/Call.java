package com.example.aloo;

public class Call {
    public String idCall;
    public String nameCall;

    public Call(){

    }

    public Call(String idCall, String nameCall) {
        this.idCall = idCall;
        this.nameCall = nameCall;
    }

    public String getIdCall() {
        return idCall;
    }

    public void setIdCall(String idCall) {
        this.idCall = idCall;
    }

    public String getNameCall() {
        return nameCall;
    }

    public void setNameCall(String nameCall) {
        this.nameCall = nameCall;
    }
}
