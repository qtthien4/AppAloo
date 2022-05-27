package com.example.aloo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemCallRcvAdapter extends RecyclerView.Adapter<ItemCallRcvAdapter.ItemCallviewHolder> {

    private List<Call> listCall;
    Listener listener;
    int check;

    public ItemCallRcvAdapter(List<Call> listCall, Listener listener) {
        this.listCall = listCall;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemCallviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent,false);

        return new ItemCallviewHolder(view);
    }


    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ItemCallviewHolder holder, int position) {
        Call call = listCall.get(position);
        if(call == null)
            return;
        holder.itemcall.setText(call.getNameCall());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(call);
                check = position;
                notifyDataSetChanged();
            }
        });
        if(check == position){
            holder.itemView.setBackgroundResource(R.drawable.border_red);
            holder.itemcall.setTextColor(Color.parseColor("#FFFFFF"));
        }else
        {
            holder.itemView.setBackgroundResource(R.drawable.dg_radius);
            holder.itemcall.setTextColor(Color.parseColor("#FFFF0000"));
        }
    }

    @Override
    public int getItemCount() {
        if(listCall == null) return 0;
        return listCall.size();
    }


    public class ItemCallviewHolder extends RecyclerView.ViewHolder{
        private TextView itemcall;
        public ItemCallviewHolder(@NonNull View itemView) {
            super(itemView);
            itemcall = itemView.findViewById(R.id.itemfoodrcv);

        }
    }

    interface Listener{
        void onClick(Call call);
    }

}
