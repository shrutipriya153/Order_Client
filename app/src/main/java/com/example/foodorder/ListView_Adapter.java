package com.example.foodorder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListView_Adapter extends RecyclerView.Adapter<ListView_Adapter.ViewHolder> {
    Activity activity;
    List<Food_POJO> items;
    public ListView_Adapter(Activity activity, List<Food_POJO>items){
        this.activity = activity;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.order_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food_POJO l=items.get(position);
        holder.date.setText(l.getDate());
        List<item> menu=l.getOrder();
        holder.list.setHasFixedSize(true);
        holder.list.setLayoutManager(new LinearLayoutManager(activity));
        ListAdapter adapter=new ListAdapter(activity,menu);
        holder.list.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list=itemView.findViewById(R.id.list);
            date=itemView.findViewById(R.id.date);
        }
    }
}
