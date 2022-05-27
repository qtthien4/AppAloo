package com.example.aloo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CallActivity extends AppCompatActivity {
    RecyclerView rcCall;
    List<Call> mListCall;
    CallAdapter mCallAdapter;
    SharedPreferences sharedPreferences;
    String key;
    Context context;
    Button btnAddCall;
    private DatabaseReference db;

    void initUi(){
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        rcCall = findViewById(R.id.rcCall);
        btnAddCall = findViewById(R.id.btnAddCall);
        //them vaof rc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcCall.setLayoutManager(linearLayoutManager);
        //phan cach item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcCall.addItemDecoration(dividerItemDecoration);

        mListCall = new ArrayList<>();
        mCallAdapter = new CallAdapter(mListCall, new CallAdapter.Listener() {
            @Override
            public void Listener(Call call) {
                deleteDbFirebase(call);
                rcCall.setAdapter(mCallAdapter);
            }
        });

        rcCall.setAdapter(mCallAdapter);
    }

    void getListCallFromFirebase(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = db.getReference("users");
        myref.child(sharedPreferences.getString("user","")).child("call").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Call call = dataSnapshot.getValue(Call.class);
                    mListCall.add(call);
//                    Log.e("TAG", dataSnapshot.);
//                    Toast.makeText(CallActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                }
                mCallAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CallActivity.this, "get item faild", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void writeDbFirebase(Call call){
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        db.child("users").child(sharedPreferences.getString("user", "")).child("call").child(key).setValue(call);
    }

    void deleteDbFirebase(Call call){
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
        db.child(sharedPreferences.getString("user", "")).child("call").child(String.valueOf(call.getIdCall())).removeValue();
        mCallAdapter.notifyDataSetChanged();
        mListCall.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        context = this;
        initUi();

        getListCallFromFirebase();

        btnAddCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddCall();
            }
        });


    }


    void DialogAddCall(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addcall);

        // anhs xaj
        Button acpAddCall = dialog.findViewById(R.id.acpAddCall);
        EditText textAddCall = dialog.findViewById(R.id.textAddCall);
        acpAddCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListCall.clear();
                String item  = textAddCall.getText().toString().trim();
                if(TextUtils.isEmpty(item)){
                    Toast.makeText(context, "Bạn chưa nhập dữ liệu" , Toast.LENGTH_LONG).show();
                }else{
                    db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    key = db.child("users").push().getKey().toString();
                    Call call = new Call(key, item);
                    writeDbFirebase(call);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}