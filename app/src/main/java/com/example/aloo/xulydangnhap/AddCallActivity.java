package com.example.aloo.xulydangnhap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aloo.Call;
import com.example.aloo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCallActivity extends AppCompatActivity {
    Button add, tieptuc;
    SharedPreferences sharedPreferences;
    private DatabaseReference db;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_call);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        add = findViewById(R.id.btn_themmoi);
        tieptuc = findViewById(R.id.btn_tieptuc);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickadd();
            }
        });

        tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCallActivity.this, AddFoodActivity.class);
                startActivity(intent);
            }
        });
    }
    void clickadd(){
    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.layout_dialog_call);

        EditText bietdanh = dialog.findViewById(R.id.txt_bietdanh);
        Button acp = dialog.findViewById(R.id.btn_okdanhxung);
        Button huy = dialog.findViewById(R.id.btn_huydx);

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bietdanh.getText().toString().isEmpty()){
                    Toast.makeText(AddCallActivity.this, "Bạn chưa nhập dữ liệu" , Toast.LENGTH_LONG).show();
                }else{
                    db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    key = db.child("users").push().getKey().toString();
                    Call call = new Call(key, bietdanh.getText().toString());
                    writeDbFirebase(call);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    void writeDbFirebase(Call call){
        db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        db.child("users").child(sharedPreferences.getString("user", "")).child("call").child(key).setValue(call);
    }
}