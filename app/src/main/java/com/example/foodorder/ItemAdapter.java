package com.example.foodorder;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private Context mContext;
    private Cursor mCursor;

    public ItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME));
        int amount = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_AMOUNT));
        String price=mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRICE));

        Log.d("amount",String.valueOf(amount));
        Log.d("amount_p",String.valueOf(price));
        holder.nameText.setText(name);
        holder.price.setText(String.valueOf(amount*Integer.parseInt(price.substring(3))));
        holder.countText.setText(String.valueOf(amount));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView countText;
        public TextView price;

        public ItemViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.card_name);
            price = itemView.findViewById(R.id.card_price);
            countText=itemView.findViewById(R.id.card_quant);
        }
    }
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
