package com.example.aloo.Notification;

public class Data {
    private String Title;
    private String Messenger;

    public Data(String title, String messenger) {
        Title = title;
        Messenger = messenger;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessenger() {
        return Messenger;
    }

    public void setMessenger(String messenger) {
        Messenger = messenger;
    }
}
