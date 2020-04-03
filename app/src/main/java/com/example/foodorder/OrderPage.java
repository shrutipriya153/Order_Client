package com.example.foodorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String uid;
    ChildEventListener childEventListener;
    private List<Item_POJO> lt;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(OrderPage.this,MainActivity.class));
        }

        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lt=new ArrayList<Item_POJO>();

        /*progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading.....");
        progressDialog.show();*/


//        uid=mAuth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("Items");
        // loadRecyclerViewData();

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


    }


}
