package com.example.foodorder;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.ItemContract.ItemEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.foodorder.ItemContract.ItemEntry.COLUMN_AMOUNT;
import static com.example.foodorder.ItemContract.ItemEntry.COLUMN_NAME;
import static com.example.foodorder.ItemContract.ItemEntry.COLUMN_PRICE;

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
        viewHolder.price.setText(l.getPrice());
        Picasso.with(context).load(l.getImg()).into(viewHolder.i);
        //Glide.with(context).load(lg.getPic()).into(viewHolder.i);




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView price;
        private ImageButton inc;
        private ImageButton dec;
        private TextView textView;
        private Spinner spinner;
        EditText editText;
        // private TextView descTextView;
        private ImageView i;
        private ImageButton button;
        EditText text;
        sql sql;
        SQLiteDatabase mDatabase;
        ItemAdapter mAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_item);
            i=itemView.findViewById(R.id.item_img);
            price=itemView.findViewById(R.id.price_item);
            inc=itemView.findViewById(R.id.inc);
            dec=itemView.findViewById(R.id.dec);
            button=itemView.findViewById(R.id.addToCart);
            //editText=itemView.findViewById(R.id.total);
            inc.setTag(itemView);
            dec.setTag(itemView);
            sql sql=new sql(context);
            mDatabase=sql.getWritableDatabase();
           // sql=new sql(context);

            inc.setOnClickListener(this);
            dec.setOnClickListener(this);
            button.setOnClickListener(this);
            button.setEnabled(false);
            //spinner=itemView.findViewById(R.id.spinner1);
            View mView=LayoutInflater.from(context).inflate(R.layout.activity_order_page,null);
            text=mView.findViewById(R.id.total);
            /*String quant[]={"1 Kg/L","500 Gm/mL"};
            ArrayAdapter adapter1=new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,quant);
            spinner.setAdapter(adapter1);*/
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.inc){
                View tempview=(View) inc.getTag();
                textView=tempview.findViewById(R.id.quant);
                int num=Integer.parseInt(textView.getText().toString())+1;
                if(num>=1){

                    button.setEnabled(true);
                }
                textView.setText(String.valueOf(num));
                //text.setText("gfg");

            }
            if(v.getId()==R.id.dec){
                View tempview=(View) dec.getTag();
                textView=tempview.findViewById(R.id.quant);
                int num=Integer.parseInt(textView.getText().toString())-1;
                if(num<0){
                    textView.setText("0");
                    button.setEnabled(false);
                }else {
                    textView.setText(String.valueOf(num));
                }
            }
            if(v.getId()==R.id.addToCart){
               mAdapter = new ItemAdapter(context, getAllItems());
                Log.d("name",name.getText().toString());
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME,name.getText().toString());
                cv.put(COLUMN_PRICE, price.getText().toString());

                cv.put(COLUMN_AMOUNT,textView.getText().toString());
                Log.d("price",price.getText().toString());

                mDatabase.insert(ItemContract.ItemEntry.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllItems());
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        }
        private Cursor getAllItems() {
            return mDatabase.query(
                    ItemEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    ItemEntry.COLUMN_TIMESTAMP + " DESC"
            );
        }

    }
}
