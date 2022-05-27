package com.example.aloo;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aloo.Notification.APIService;
import com.example.aloo.Notification.Client;
import com.example.aloo.Notification.Data;
import com.example.aloo.Notification.Response;
import com.example.aloo.Notification.Sender;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Callback;


public class HomeFragment extends Fragment implements ItemFoodRcvAdapter.Listener, ItemCallRcvAdapter.Listener{
//    ListView lv;
    List<Food> arrayListFood;
    List<Call> arrayListCall;
    ArrayList<String> arrayList;
    ItemFoodRcvAdapter adapterFood;
    ItemCallRcvAdapter adapterCall;
    DBHelper dbHelper;
    String myuser, youruser, yourtoken;
    Integer numR;

    ImageView btnAlo;
    RecyclerView rvFood, rvCall;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        btnAlo = view.findViewById(R.id.buttonAlo);
        //db
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

        arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        btnAlo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_home);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                TextView add1 = bottomSheetDialog.findViewById(R.id.button13);
                TextView add2 = bottomSheetDialog.findViewById(R.id.button15);
                TextView add3 = bottomSheetDialog.findViewById(R.id.button14);
                add1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.set(2, "Tui đói lắm gòi");
//                        Toast.makeText(getContext(), "asdasdasdas", Toast.LENGTH_SHORT).show();
                        add1.setBackgroundResource(R.drawable.textview_red_conner_25);
                        add1.setTextColor(Color.parseColor("#FFFFFF"));
                        add2.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                        add2.setTextColor(Color.parseColor("#FF0000"));
                        add3.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                        add3.setTextColor(Color.parseColor("#FF0000"));
                    }
                });
                add2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.set(2, "Qua chở tui nhaa");
                        add1.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                        add1.setTextColor(Color.parseColor("#FF0000"));
                        add2.setBackgroundResource(R.drawable.textview_red_conner_25);
                        add2.setTextColor(Color.parseColor("#FFFFFF"));
                        add3.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                        add3.setTextColor(Color.parseColor("#FF0000"));
                    }
                });
                add3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.set(2, "Nay tui bao");
                        add1.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                        add1.setTextColor(Color.parseColor("#FF0000"));
                        add2.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                        add2.setTextColor(Color.parseColor("#FF0000"));
                        add3.setBackgroundResource(R.drawable.textview_red_conner_25);
                        add3.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                });

                rvFood =  bottomSheetDialog.findViewById(R.id.rvFood);
                rvCall = bottomSheetDialog.findViewById(R.id.rcCall);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);

                rvFood.setLayoutManager(linearLayoutManager);
                rvCall.setLayoutManager(linearLayoutManager2);

                arrayListFood = new ArrayList<>();
                adapterFood = new ItemFoodRcvAdapter(arrayListFood, HomeFragment.this::onClick);

                arrayListCall = new ArrayList<>();
                adapterCall = new ItemCallRcvAdapter(arrayListCall, HomeFragment.this::onClick);

                rvFood.setAdapter(adapterFood);
                rvCall.setAdapter(adapterCall);
                getListFoodFromFirebase();
                getListCallFromFirebase();


                TextView randomfood = bottomSheetDialog.findViewById(R.id.random);
                randomfood.setOnClickListener(new View.OnClickListener() {
                    boolean checkrandom = false;
                    @Override
                    public void onClick(View v) {
                        if(checkrandom == false){
                            randomfood.setBackgroundResource(R.drawable.textview_red_conner_25);
                            randomfood.setTextColor(Color.parseColor("#FFFFFF"));
                            checkrandom = true;
                        }else{
                            randomfood.setBackgroundResource(R.drawable.text_bordoer_no_fix_size);
                            randomfood.setTextColor(Color.parseColor("#FF0000"));
                            checkrandom = false;
                        }
                        if( arrayListFood.get(numR).typename.contentEquals("Đồ ăn") ){
                            arrayList.set(0,  "ăn "+arrayListFood.get(numR).foodname);
                        }else if(arrayListFood.get(numR).typename.contentEquals("Đồ uống")){
                            arrayList.set(0,  "uống "+arrayListFood.get(numR).foodname);
                        }

                    }
                });

                TextView guithongbao = bottomSheetDialog.findViewById(R.id.button);
                guithongbao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = "Đi ăn nào "+arrayList.get(1).toString();
                        String content = arrayList.get(2).toString() + ". Đi " + arrayList.get(0).toString()+ " nha.";
                        String token = yourtoken;

                        sendNotifications(token, title, content);
                    }
                });
                bottomSheetDialog.show();
            }
        });
        return view;
    }

    void getListFoodFromFirebase(){

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = db.getReference("users");
        myref.child(myuser).child("food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    arrayListFood.add(food);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "get item faild", Toast.LENGTH_SHORT).show();
            }
        });

        myref.child(youruser).child("food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    arrayListFood.add(food);
                }
                Random random = new Random();
                numR = random.nextInt(arrayListFood.size());
                adapterFood.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getListCallFromFirebase(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = db.getReference("users");
        myref.child(myuser).child("call").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Call call = dataSnapshot.getValue(Call.class);
                    arrayListCall.add(call);
                }
                adapterCall.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "get item faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
//xu ly push
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
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(Food food) {
        if(food.typename.toString().contentEquals("Đồ ăn"))
        arrayList.set(0,"ăn "+food.foodname.toString());
        if(food.typename.toString().contentEquals("Đồ uống"))
            arrayList.set(0, "uống "+food.foodname.toString());
    }

    @Override
    public void onClick(Call call) {
        arrayList.set(1,call.nameCall);
    }
}