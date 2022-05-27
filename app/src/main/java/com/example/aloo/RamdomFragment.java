package com.example.aloo;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RamdomFragment extends Fragment {
    RecyclerView rcFood;
    RandomAdapter mFoodAdapter;
    List<Food> mListFood;
    Button random;
    Context context;
    String myuser,youruser;
    DBHelper dbHelper;


    void initUi(View view, ViewGroup container){
        rcFood = view.findViewById(R.id.rcFood);
        random = view.findViewById(R.id.btnrandom);
        dbHelper = new DBHelper(getContext(), "dataAloo.sqlite", null, 1);
        Cursor dataToken =  dbHelper.getData("Select * from mytoken");
        while (dataToken.moveToNext()){
            myuser = dataToken.getString(1);
        }
        Cursor yourToken =  dbHelper.getData("Select * from yourtoken");
        while (yourToken.moveToNext()){
            youruser = yourToken.getString(1);
        }

        //them vaof rc
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        rcFood.setLayoutManager(linearLayoutManager);

//phan cach item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL);
        rcFood.addItemDecoration(dividerItemDecoration);

        mListFood = new ArrayList<>();


        mFoodAdapter = new RandomAdapter(mListFood);

        rcFood.setAdapter(mFoodAdapter);
    }

    void getListFoodFromFirebase(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://aloo-9d752-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = db.getReference("users");
        myref.child(myuser).child("food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    mListFood.add(food);
                }
                myref.child(youruser).child("food").addValueEventListener(new ValueEventListener() {
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
                    }
                });

                random.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_random);

                        TextView txtrandom = dialog.findViewById(R.id.txtrandom);
                        Button cancell = dialog.findViewById(R.id.cancellRandom);

                        Random random = new Random();
                        int numR = random.nextInt(mListFood.size());

                        txtrandom.setText("App đã giúp bạn chọn món " + mListFood.get(numR).foodname );
                        dialog.show();

                        cancell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                mFoodAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "get item faild", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ramdom, container, false);
        initUi(view, container);
        getListFoodFromFirebase();


        return view;
    }

}