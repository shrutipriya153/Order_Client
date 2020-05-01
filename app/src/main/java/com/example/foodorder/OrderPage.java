package com.example.foodorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class OrderPage extends AppCompatActivity {
    EditText edit1,edit2;
   ImageButton im;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ImageButton imageButton,ima;
    List<Item_POJO> lt;
    sql sql;
    static boolean calledAlready = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        if(!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        Log.d("called",String.valueOf(calledAlready));

        mAuth=FirebaseAuth.getInstance();
        im=findViewById(R.id.menubar);

        imageButton=findViewById(R.id.checkout);
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(OrderPage.this,MainActivity.class));
        }
        ima=findViewById(R.id.order_show);
        ima.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OrderPage.this,OrderView.class));
                    }
                }
        );
        //edit1=findViewById(R.id.total);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lt=new ArrayList<Item_POJO>();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderPage.this,Cart.class));
                OrderPage.this.finish();
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(OrderPage.this,im);
                popupMenu.getMenuInflater().inflate(R.menu.signout,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(OrderPage.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        /*progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading.....");
        progressDialog.show();*/


//        uid=mAuth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("Items");
        // loadRecyclerViewData();
        Intent service=new Intent(OrderPage.this,ListenOrder.class);
        startService(service);
    }
    int i=1;
    @Override
    protected void onStart() {
        super.onStart();
        mRef= FirebaseDatabase.getInstance().getReference().child("Items");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //              lt.clear();
               //progressDialog.dismiss();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                    if(i<=5) {
                        mRef.child("Item" + i);
                        String name = ds.child("name").getValue(String.class);
                        String pic = ds.child("img").getValue(String.class);
                        String price = ds.child("price").getValue(String.class);
                        Log.d("name", name);
                        Log.d("pic", pic);
                        Log.d("price",price);

                        Item_POJO hp = new Item_POJO(name, pic,price);
                        lt.add(hp);
                        i++;
                    }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter=new Adapter(lt,getApplicationContext());
        recyclerView.setAdapter(adapter);
        /*Intent intent=getIntent();
        String total=intent.getStringExtra("total");
        edit1.setText("Total: "+total);*/
        /*sql=new sql(this);
        sqLiteDatabase=sql.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+ItemContract.ItemEntry.TABLE_NAME);*/


    }



}
