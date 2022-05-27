package com.example.aloo.Chat;

import static android.content.ContentValues.TAG;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aloo.DBHelper;
import com.example.aloo.Notification.APIService;
import com.example.aloo.Notification.Client;
import com.example.aloo.Notification.Data;
import com.example.aloo.Notification.Response;
import com.example.aloo.Notification.Sender;
import com.example.aloo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Callback;

public class ChatFragment extends Fragment {
    ArrayList<ChatMessenger> chatMessengers;
    chatAdapter chatAdapter;
    RecyclerView recyclerView;
    FirebaseDatabase db;
    EditText contentMessage;
    ImageView send;
    DatabaseReference myref, myuser_id, youruser_id;
    DBHelper dbHelper;
    String myuser, youruser, yourtoken, myuserid, youruserid, sendMessager;
    private APIService apiService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        init(view, container);
        readMessage();
        onClick();
        return view;
    }

    void init(View view, ViewGroup container){
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = view.findViewById(R.id.chatscreen);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatMessengers = new ArrayList<>();

        contentMessage = view.findViewById(R.id.textchat);
        send = view.findViewById(R.id.sendchat);
        dbHelper = new DBHelper(getContext(), "dataAloo.sqlite", null, 1);
        Cursor dataToken =  dbHelper.getData("Select * from mytoken");
        while (dataToken.moveToNext()){
            myuser = dataToken.getString(1);
        }
        Cursor yourToken =  dbHelper.getData("Select * from yourtoken");
        while (yourToken.moveToNext()){
            youruser = yourToken.getString(1);
            yourtoken = yourToken.getString(2);
        }
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myuser_id = db.getReference("users").child(myuser).child("userid");
        myuser_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myuserid = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        youruser_id = db.getReference("users").child(youruser).child("userid");
        youruser_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                youruserid = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myref = db.getReference("chat");
    }
    void sendMessage(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(new Date());
        String key =  db.getReference("chat").push().getKey();
        ChatMessenger chatMessenger = new ChatMessenger(key,myuserid,youruserid,contentMessage.getText().toString(),strDate);
        myref.child(key).setValue(chatMessenger);
        contentMessage.setText(null);
//        Log.e("aa", strDate);
    }
   public void readMessage(){
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessengers.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatMessenger chat = dataSnapshot.getValue(ChatMessenger.class);
                    if(chat.getReceiver().equals(youruserid) && chat.getSender().equals(myuserid) ||
                    chat.getSender().equals(youruserid) && chat.getReceiver().equals(myuserid)){
                        chatMessengers.add(chat);
                    }
                    chatAdapter = new chatAdapter(chatMessengers, new chatAdapter.Listener() {
                        @Override
                        public void Listenner(ChatMessenger chatMessenger) {
                        }
                    });
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotifications(String token, String title, String content) {
        Data data = new Data(title, content);
        Sender sender = new Sender(data, token);
        apiService.sendNotification(sender).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(retrofit2.Call<Response> call, retrofit2.Response<Response> response) {
                Log.e(TAG, response.toString());
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response> call, Throwable t) {
                Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG).show();
            }
        });

    }

    void onClick(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessager = contentMessage.getText().toString();
                sendMessage();
                sendNotifications(yourtoken,"Tin nhắn mới",sendMessager);
            }
        });
    }
}