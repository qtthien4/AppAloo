package com.example.aloo.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAhMMS7Ak:APA91bGO1h_AeJPGdt_13V6R1uvP15pVKFzOpq6tyiEojt1YymyvOoMAJ6Ouc8eoXalWXIC-KuP6wmbbn-4FZFXNuiZaMcFqvzofJ2q2s34nF5zPqYKBhntRiJXTR21vzO6YXvy0hSJM"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);


}
