package com.example.foodorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderView extends AppCompatActivity implements View.OnClickListener {
FirebaseDatabase mdatabase;
FirebaseAuth mAuth;
DatabaseReference mRef;
String uid;
int i;
    List<Food_POJO> or;
    List<item> items;
RecyclerView recyclerView;
ImageButton back,menu;
//OrderAdapter orderAdapter;
    ListView_Adapter listView_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        recyclerView=findViewById(R.id.orderdisplay);
        back=findViewById(R.id.back);
        menu=findViewById(R.id.menubar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        or=new ArrayList<>();
        mdatabase=FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("Request").child(uid);
        i=0;
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        Request_POJO request_pojo=ds.getValue(Request_POJO.class);
                        //String name = ds.child("name").getValue(String.class);
                        //String pic = ds.child("img").getValue(String.class);
                        //Log.d("hss", String.valueOf(or.get(0)));
                        items=request_pojo.getList();

                            or.add(new Food_POJO(request_pojo.getDate(),items));



                    }

                    listView_adapter = new ListView_Adapter(OrderView.this,or);
                   recyclerView.setAdapter(listView_adapter);
                }
                else{
                    Toast.makeText(OrderView.this, "No New Orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            startActivity(new Intent(OrderView.this,OrderPage.class));
            OrderView.this.finish();

        }else if(v.getId()==R.id.menubar){
            PopupMenu popupMenu=new PopupMenu(OrderView.this,menu);
            popupMenu.getMenuInflater().inflate(R.menu.signout,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(OrderView.this,MainActivity.class);
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
