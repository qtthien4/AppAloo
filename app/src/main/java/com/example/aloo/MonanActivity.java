package com.example.aloo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MonanActivity extends AppCompatActivity {

    RecyclerView rcFood;
    FoodAdapter mFoodAdapter;
    List<Food> mListFood;
    String keyput;
    DBHelper dbHelper;
    SharedPreferences sharedPreferences;
    Button btnAddFood;
    Context context;
    String yourtoken, youruser;

    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monan);
        context = this;
        initUi();
        getListFoodFromFirebase();
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddFood();
            }
        });
    }



    void initUi(){
        rcFood = findViewById(R.id.rcFood);
        btnAddFood = findViewById(R.id.btnAddFood);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        //them vaof rc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcFood.setLayoutManager(linearLayoutManager);
        //phan cach item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcFood.addItemDecoration(dividerItemDecoration);

        mListFood = new ArrayList<>();
        mFoodAdapter = new FoodAdapter(mListFood, new FoodAdapter.IClickListener() {
            @Override
            public void IClickDeleteFood(Food food) {
                deleteDbFirebase(food);
            }
        });
        rcFood.setAdapter(mFoodAdapter);

    }

    void getListFoodFromFirebase(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = db.getReference("users");
        myref.child(sharedPreferences.getString("user","")).child("food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    mListFood.add(food);
                }
                mFoodAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonanActivity.this, "get item faild", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void writeDbFirebase(Food food){
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        db.child("users").child(sharedPreferences.getString("user", "")).child("food").child(keyput).setValue(food);
    }

    void deleteDbFirebase(Food food){
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
        db.child(sharedPreferences.getString("user", "")).child("food").child(String.valueOf(food.getIdkey())).removeValue();
        mListFood.clear();
//        getListFoodFromFirebase();
    }

    void DialogAddFood(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_type_food);
        Button pickFood = dialog.findViewById(R.id.food);
        Button pickDrink = dialog.findViewById(R.id.drink);

        pickFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.dialog_addfood);
                Button acpAddFood = dialog.findViewById(R.id.acpAddFood);
                EditText textAddFood = dialog.findViewById(R.id.textAddFood);

                acpAddFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //them firebase
                        mListFood.clear();
                        String item  = textAddFood.getText().toString().trim();
                        if(TextUtils.isEmpty(item)){
                            Toast.makeText(context, "Bạn chưa nhập dữ liệu" , Toast.LENGTH_LONG).show();
                        }else {
                            db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                            keyput = db.child("food").push().getKey().toString();
                            Food food = new Food(item , "Đồ ăn", keyput);
                            writeDbFirebase(food);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        pickDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.dialog_addfood);
                Button acpAddFood = dialog.findViewById(R.id.acpAddFood);
                EditText textAddFood = dialog.findViewById(R.id.textAddFood);

                acpAddFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //them firebase
                        mListFood.clear();
                        String item  = textAddFood.getText().toString().trim();
                        if(TextUtils.isEmpty(item)){
                            Toast.makeText(context, "Bạn chưa nhập dữ liệu" , Toast.LENGTH_LONG).show();
                        }else {
                            keyput = db.child("users").push().getKey().toString();
                            Food food = new Food(item , "Đồ uống",keyput);
                            writeDbFirebase(food);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        dialog.show();
    }



}
