package com.example.aloo.xulydangnhap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aloo.Call;
import com.example.aloo.Food;
import com.example.aloo.MainActivity;
import com.example.aloo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodActivity extends AppCompatActivity {
    Button themmonan, tieptuc;
    SharedPreferences sharedPreferences;
    private DatabaseReference db;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        themmonan = findViewById(R.id.btn_themdoan);
        tieptuc = findViewById(R.id.btn_themdoan2);

        themmonan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddfood();
            }
        });
        tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFoodActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void setAddfood(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_addfood);

        EditText monan = dialog.findViewById(R.id.edit_addfood);
        Button acp = dialog.findViewById(R.id.btn_dongy);
        Button huy = dialog.findViewById(R.id.btn_huy);

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monan.getText().toString().isEmpty()){
                    Toast.makeText(AddFoodActivity.this, "Bạn chưa nhập dữ liệu" , Toast.LENGTH_LONG).show();
                }else{
                    db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    key = db.child("food").push().getKey().toString();
                    Food food = new Food(monan.getText().toString() , "Đồ ăn", key);
                    writeDbFirebase(food);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }
    void writeDbFirebase(Food food){
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        db.child("users").child(sharedPreferences.getString("user", "")).child("food").child(key).setValue(food);
    }
}