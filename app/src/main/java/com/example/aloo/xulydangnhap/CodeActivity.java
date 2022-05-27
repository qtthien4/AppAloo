package com.example.aloo.xulydangnhap;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aloo.DBHelper;
import com.example.aloo.MainActivity;
import com.example.aloo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Random;

public class CodeActivity extends AppCompatActivity {
    EditText mytoken, yourtoken;
    Button Ctoken;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference();
    private static Random generator = new Random();
    SharedPreferences sharedPreferences;
    DBHelper dbHelper;

    void unit(){
        mytoken = findViewById(R.id.mytoken);
        yourtoken = findViewById(R.id.yourtoken);
        Ctoken = findViewById(R.id.comfirmtoken);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
    dbHelper = new DBHelper(this, "dataAloo.sqlite", null, 1);
    // create table
        dbHelper.QueryData("drop table if exists tokens");
        dbHelper.QueryData("drop table if exists mytoken");
        dbHelper.QueryData("CREATE TABLE IF NOT EXISTS tokens(Id INTEGER PRIMARY KEY AUTOINCREMENT,taikhoan varchar(100), token varchar(200))");
        dbHelper.QueryData("CREATE TABLE IF NOT EXISTS mytoken(Id INTEGER PRIMARY KEY AUTOINCREMENT,taikhoan varchar(100))");
    }


    void onClickCtoken(){
        if(yourtoken.getText().toString().isEmpty()){
            Toast.makeText(CodeActivity.this, "Vui lòng nhập code", Toast.LENGTH_SHORT).show();
        }
        //kiem tra code
        myRef.child("users").child(yourtoken.getText().toString()).child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() ==null){
                    Toast.makeText(CodeActivity.this, "Sai code, Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.QueryData("INSERT INTO tokens VALUES(null, '"+yourtoken.getText().toString()+"' , '"+snapshot.getValue().toString()+"')");

                    Intent intent = new Intent(CodeActivity.this, MainActivity.class);
                    startActivity(intent);

                    //hien thi
//                    Cursor dataToken =  dbHelper.getData("Select * from tokens");
////                   Log.e(TAG, dataToken.getString(1)) ;
//                    while (dataToken.moveToNext()){
//                        String user = dataToken.getString(1);
//                        Log.e(TAG, user) ;
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        unit();
        Ctoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCtoken();
            }
        });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, String.valueOf(task.getException()));
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        String email = sharedPreferences.getString("email", "");
                        String[] tach = email.split("@");
                        String user = tach[0];
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user",user);
                        editor.commit();

                        dbHelper.QueryData("INSERT INTO mytoken VALUES(null, '"+user+"')");

                        myRef.child("users").child(user).child("token").setValue(token);
                        mytoken.setText(myRef.child("tokens").child(user).getKey());
                    }
                });
    }
}