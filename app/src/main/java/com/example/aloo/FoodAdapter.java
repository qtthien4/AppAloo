package com.example.aloo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends  RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{

    private List<Food> mListFood;
    private IClickListener iClickListener;



    public FoodAdapter(List<Food> mListFood, IClickListener listener) {
        this.mListFood = mListFood;
        this.iClickListener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent,false);

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = mListFood.get(position);
        if(food == null)
            return;
        holder.textNameFood.setText(food.getFoodname());
        holder.textTypeName.setText(food.getTypename());

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickListener.IClickDeleteFood(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFood.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{

        TextView textNameFood, textTypeName;
        Button btnDel;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameFood = itemView.findViewById(R.id.textNameFood);
            textTypeName = itemView.findViewById(R.id.texttypeFood);
            btnDel = itemView.findViewById(R.id.delFood);
        }
    }

    public interface IClickListener{
        void IClickDeleteFood(Food food);
    }
}

