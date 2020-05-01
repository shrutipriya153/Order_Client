package com.example.foodorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    TextView t;
    List<item> list;
    ImageButton back;
    Cursor mCursor;
   // private SQLiteDatabase mDatabase;
    private ItemAdapter mAdapter;
    SQLiteDatabase mDatabase;
    ImageButton pay,back1,menubar;

    List<item> cart=new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        back1=findViewById(R.id.back);
        menubar=findViewById(R.id.menubar);
        back1.setOnClickListener(this);
        menubar.setOnClickListener(this);
        sql dbHelper = new sql(this);
        mDatabase = dbHelper.getWritableDatabase();
        mAdapter = new ItemAdapter(this, getAllItems());
        back=findViewById(R.id.back);
        list=new ArrayList<>();
        pay=findViewById(R.id.checkout);
        recyclerView=findViewById(R.id.cartdisplay);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        t=findViewById(R.id.total);
        mCursor=getAllItems();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Cart.this,OrderPage.class);
                i.putExtra("n",1);
                startActivity(i);
                Cart.this.finish();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPayment();
            }
        });
        loadCart();

    }
    int total;
    private void gotoPayment() {
        Intent intent = new Intent(Cart.this, Payment.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)list);
//        intent.putExtra("BUNDLE",args);
        intent.putExtra("total",total);
        startActivity(intent);
    }


    private void loadCart() {
        mAdapter = new ItemAdapter(getApplicationContext(), getAllItems());
        int i=0;
        if (!mCursor.moveToPosition(i)) {
            Toast.makeText(this, "Your Cart Looks Empty!!", Toast.LENGTH_LONG).show();
            return;
        }
        total=0;
        if(mCursor.moveToFirst()){
            do {
                String price = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRICE));
                int amount = mCursor.getInt(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_AMOUNT));
                Log.d("price", price.substring(3));
                Log.d("amount", String.valueOf(amount));
                total += Integer.parseInt(price.substring(3)) * amount;
                String name = mCursor.getString(mCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME));
                t.setText(String.valueOf(total));
                item im = new item(name, price, String.valueOf(amount));
            }while (mCursor.moveToNext());

        }

    }
    public Cursor getAllItems() {
        return mDatabase.query(
                ItemContract.ItemEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ItemContract.ItemEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            startActivity(new Intent(Cart.this,OrderPage.class));
            Cart.this.finish();

        }else if(v.getId()==R.id.menubar){
            PopupMenu popupMenu=new PopupMenu(Cart.this,menubar);
            popupMenu.getMenuInflater().inflate(R.menu.signout,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(Cart.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                }
            });
            popupMenu.show();
        }
    }
}
