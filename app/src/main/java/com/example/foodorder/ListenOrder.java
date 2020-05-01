package com.example.foodorder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase database;
    DatabaseReference mref;
    String uid;
    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();
        mref=database.getReference().child("Request").child(uid);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mref.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Request_POJO request_pojo=dataSnapshot.getValue(Request_POJO.class);
        showNotification(dataSnapshot.getKey(),request_pojo);
    }

    private void showNotification(String key, Request_POJO request_pojo) {
        Intent intent=new Intent(getBaseContext(),OrderView.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Order")
                .setContentTitle("Picked Up")
                .setContentText("Your Order is Picked Up")
                .setContentIntent(pendingIntent)

        .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Request_POJO request_pojo=dataSnapshot.getValue(Request_POJO.class);
        show(dataSnapshot.getKey(),request_pojo);
    }

    private void show(String key, Request_POJO request_pojo) {
        Intent intent=new Intent(getBaseContext(),OrderView.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Order")
                .setContentTitle("Deilvered")
                .setContentText("Your order had been Delivered")
                .setContentIntent(pendingIntent)

                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
