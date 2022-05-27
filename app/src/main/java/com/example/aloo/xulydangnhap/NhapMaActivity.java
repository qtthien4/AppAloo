package com.example.aloo.xulydangnhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aloo.DBHelper;
import com.example.aloo.MainActivity;
import com.example.aloo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NhapMaActivity extends AppCompatActivity {
    EditText yourtoken;
    Button btnConnect;
    SharedPreferences sharedPreferences;
    DBHelper dbHelper;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_ma);
        yourtoken = findViewById(R.id.nhapma);
        btnConnect = findViewById(R.id.btn_connect);


        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        dbHelper = new DBHelper(this, "dataAloo.sqlite", null, 1);
        dbHelper.QueryData("drop table if exists yourtoken");
        dbHelper.QueryData("CREATE TABLE IF NOT EXISTS yourtoken(Id INTEGER PRIMARY KEY AUTOINCREMENT,taikhoan varchar(100), token varchar(200))");

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCtoken();
            }
        });

    }

    void onClickCtoken() {
        if (yourtoken.getText().toString().isEmpty()) {
            Toast.makeText(NhapMaActivity.this, "Vui lòng nhập code", Toast.LENGTH_SHORT).show();
        }
        //kiem tra code
        myRef.child("users").child(yourtoken.getText().toString()).child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Toast.makeText(NhapMaActivity.this, "Sai code, Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.QueryData("INSERT INTO yourtoken VALUES(null, '" + yourtoken.getText().toString() + "' , '" + snapshot.getValue().toString() + "')");

                    Intent intent = new Intent(NhapMaActivity.this, AddCallActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}