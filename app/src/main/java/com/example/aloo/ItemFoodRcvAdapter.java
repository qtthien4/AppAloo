package com.example.aloo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemFoodRcvAdapter extends RecyclerView.Adapter<ItemFoodRcvAdapter.ItemFoodviewHolder> {

    private List<Food> listFood;
    Listener listener;
    int check;


    public ItemFoodRcvAdapter(List<Food> listFood, Listener listener) {
        this.listFood = listFood;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemFoodviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent,false);

        return new ItemFoodviewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemFoodviewHolder holder, int position) {
    Food food = listFood.get(position);
        if(food == null)
            return;
    holder.itemFood.setText(food.getFoodname());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onClick(food);
            check = position;
            notifyDataSetChanged();
        }
    });
        if(check == position){
            holder.itemView.setBackgroundResource(R.drawable.border_red);
            holder.itemFood.setTextColor(Color.parseColor("#FFFFFF"));
        }else
        {
            holder.itemView.setBackgroundResource(R.drawable.dg_radius);
            holder.itemFood.setTextColor(Color.parseColor("#FFFF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }



    public class ItemFoodviewHolder extends RecyclerView.ViewHolder{
        private TextView itemFood;
        public ItemFoodviewHolder(@NonNull View itemView) {
            super(itemView);
            itemFood = itemView.findViewById(R.id.itemfoodrcv);
        }

    }

    interface Listener{
        void onClick(Food food);
    }

}
