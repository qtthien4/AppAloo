package com.example.aloo.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aloo.DBHelper;
import com.example.aloo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MessageViewHolder>{
    List<ChatMessenger> chatMessengers;
    int VIEW_TYPE_LEFT = 0;
    int VIEW_TYPE_RIGHT = 1;
    FirebaseUser fbuser;
    Listener listener;
    boolean checkClickItem = false;

    public chatAdapter(List<ChatMessenger> chatMessengers, Listener listener) {
        this.chatMessengers = chatMessengers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender, parent,false);
            return new MessageViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_reciever, parent,false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessenger chat = chatMessengers.get(position);
        holder.chatsend.setText(chat.message);
        holder.chattimesend.setText(chat.time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Listenner(chat);
                if(checkClickItem==false){
                    checkClickItem = true;
                    holder.chattimesend.setVisibility(View.VISIBLE);
                }else {
                    checkClickItem = false;
                    holder.chattimesend.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatMessengers.size();
    }

    public class  MessageViewHolder extends RecyclerView.ViewHolder{
     TextView chatsend, chattimesend;
     public MessageViewHolder(@NonNull View itemView) {
         super(itemView);

         chatsend = itemView.findViewById(R.id.text_message);
         chattimesend = itemView.findViewById(R.id.time_message);
         
     }
 }
    @Override
    public int getItemViewType(int position) {
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatMessengers.get(position).getSender().equals(fbuser.getUid())){
            return VIEW_TYPE_RIGHT;
        }else {
            return VIEW_TYPE_LEFT;
        }
    }

    interface Listener{
        void Listenner(ChatMessenger chatMessenger);
    }
}
