package com.example.calenderproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.models.Event;

import java.util.ArrayList;
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    ArrayList<Event> data;

    public EventAdapter(ArrayList<Event> data) {
        this.data = data;
    }

    //Создается ViewHolder для каждого предмета в списке (только 1 раз)
    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new MyViewHolder(view);
    }

    //Соединяем данные с ViewHolder
    //Кладем данные во View
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    //Сколько предметов в списке
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView NameTextView;
        TextView DataTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            NameTextView = itemView.findViewById(R.id.EventNameTextView);
            DataTextView = itemView.findViewById( R.id.EventDataTextView );
        }

        public void bind(Event event) {
            NameTextView.setText( event.getText() );
            DataTextView.setText( event.getData());
        }
    }
}