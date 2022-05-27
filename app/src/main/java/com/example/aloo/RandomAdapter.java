package com.example.aloo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class RandomAdapter extends  RecyclerView.Adapter<RandomAdapter.RandomViewHolder>{

    private List<Food> mListFood;

    public RandomAdapter(List<Food> mListFood) {
        this.mListFood = mListFood;
    }

    @NonNull
    @Override
    public RandomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_random, parent,false);

        return new RandomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomViewHolder holder, int position) {
        Food food = mListFood.get(position);
        if(food == null)
            return;
        holder.NameFood.setText(food.getFoodname());
        holder.typeFood.setText(food.getTypename());
    }

    @Override
    public int getItemCount() {
        return mListFood.size();
    }

    public class RandomViewHolder extends RecyclerView.ViewHolder{

        TextView NameFood, typeFood;

        public RandomViewHolder(@NonNull View itemView) {
            super(itemView);

            NameFood = itemView.findViewById(R.id.NameFoodRandom);
            typeFood= itemView.findViewById(R.id.texttypeFood);

        }
    }
}
