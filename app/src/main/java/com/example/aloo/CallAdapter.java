package com.example.aloo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallAdapter extends  RecyclerView.Adapter<CallAdapter.CallViewHolder>{

    private List<Call> mListCall;
    Listener listener;

    public CallAdapter(List<Call> mListCall, Listener listener) {
        this.mListCall = mListCall;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_call, parent,false);

        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        Call call = mListCall.get(position);
        if(call == null)
            return;
        holder.textNameCall.setText(call.getNameCall());
        holder.delCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Listener(call);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListCall.size();
    }

    public class CallViewHolder extends RecyclerView.ViewHolder{

        TextView textNameCall;
        Button delCall;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);

            textNameCall = itemView.findViewById(R.id.textNameCall);
            delCall = itemView.findViewById(R.id.delCall);
        }
    }

    interface Listener{
        void Listener(Call call);
    }
}
