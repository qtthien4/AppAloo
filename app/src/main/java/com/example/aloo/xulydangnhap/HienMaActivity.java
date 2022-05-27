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
import android.widget.TextView;

import com.example.aloo.DBHelper;
import com.example.aloo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class HienMaActivity extends AppCompatActivity {
    TextView tokencuatoi;
    Button btnInput;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference();
    SharedPreferences sharedPreferences;
    DBHelper dbHelper;
    FirebaseUser fbuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hien_ma);
       dbHelper = new DBHelper(this, "dataAloo.sqlite", null, 1);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        dbHelper.QueryData("drop table if exists mytoken");
        dbHelper.QueryData("CREATE TABLE IF NOT EXISTS mytoken(Id INTEGER PRIMARY KEY AUTOINCREMENT,taikhoan varchar(100))");

       tokencuatoi = findViewById(R.id.macuatoi);
        btnInput = findViewById(R.id.btn_input);
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HienMaActivity.this, NhapMaActivity.class);
                startActivity(intent);
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
                        tokencuatoi.setText(myRef.child("users").child(user).getKey());
                        fbuser = FirebaseAuth.getInstance().getCurrentUser();
                        myRef.child("users").child(user).child("userid").setValue(fbuser.getUid());
                    }
                });
    }
}