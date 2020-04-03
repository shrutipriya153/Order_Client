package com.example.foodorder;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Item_POJO> items;
    private Context context;

    public Adapter(List<Item_POJO> listItems, Context context) {
        this.items = listItems;
        this.context = context;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v= LayoutInflater.from(context).inflate(R.layout.item_card,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Item_POJO l=items.get(i);
        viewHolder.name.setText(l.getName());
        Picasso.with(context).load(l.getImg()).into(viewHolder.i);
        //Glide.with(context).load(lg.getPic()).into(viewHolder.i);



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView price;
        // private TextView descTextView;
        private ImageView i;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_item);
            i=itemView.findViewById(R.id.item_img);
            price=itemView.findViewById(R.id.price_item);
        }
    }
}
