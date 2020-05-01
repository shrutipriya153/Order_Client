package com.example.foodorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.R.layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Payment extends AppCompatActivity {
    EditText address;
    ImageButton imageButton, im, gpay;
    FirebaseDatabase database;
    DatabaseReference request;
    FirebaseAuth auth;
    SQLiteDatabase db;
    int t;
    int counter=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        address = findViewById(R.id.address);
        imageButton = findViewById(R.id.menubar);
        sql sql=new sql(getApplicationContext());
        db=sql.getReadableDatabase();
        Intent i=getIntent();
        t=i.getIntExtra("total",0);
        SQLiteQueryBuilder builder=new SQLiteQueryBuilder();
        im = findViewById(R.id.back);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment.this, OrderPage.class));
                Payment.this.finish();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);


                View view = getLayoutInflater().inflate(R.layout.menu_dialoge, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                Window window=dialog.getWindow();
                window.setGravity(Gravity.TOP);
                window.setGravity(Gravity.RIGHT);
                dialog.show();*/
                PopupMenu popupMenu = new PopupMenu(Payment.this, imageButton);
                popupMenu.getMenuInflater().inflate(R.menu.signout, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Payment.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);


                View view = getLayoutInflater().inflate(R.layout.address_dialog, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText address1 = view.findViewById(R.id.add1);
                final EditText address2 = view.findViewById(R.id.add2);
                final EditText pin = view.findViewById(R.id.pin1);
                ImageButton ok = view.findViewById(R.id.ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String add1 = address1.getText().toString();
                        String add2 = address2.getText().toString();
                        String pincode = pin.getText().toString();
                        if (add1.isEmpty()) {
                            address1.setError("Requied");
                            address1.requestFocus();
                            return;
                        }
                        if (add2.isEmpty()) {
                            address2.setError("Requied");
                            address2.requestFocus();
                            return;
                        }
                        if (pincode.isEmpty()) {
                            pin.setError("Requied");
                            pin.requestFocus();
                            return;
                        }
                        if (pincode.matches("[A-Za-z]*")) {
                            pin.setError("Invalid Pin");
                            pin.requestFocus();
                            return;
                        }
                        address.setText(add1 + " , " + add2 + " , " + pincode);
                        dialog.cancel();
                        importDatabase();
                        deleteCart();
                    }
                });


            }

        });


    }

    String name,phone,uid,total;
    List<item> list=new ArrayList<>();
    private void importDatabase() {
        database=FirebaseDatabase.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        request= FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        Intent intent = getIntent();
        getItem();
        total=intent.getStringExtra("total");
        auth=FirebaseAuth.getInstance();
        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());


        request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //user user=dataSnapshot.getValue(com.example.foodorder.user.class);
                user user=dataSnapshot.getValue(com.example.foodorder.user.class);

                //Log.d("User",user.getName());
                 name =dataSnapshot.child("name").getValue(String.class);
                 phone =dataSnapshot.child("phone").getValue(String.class);
                 Log.d("jbcjs",name);
                request=FirebaseDatabase.getInstance().getReference("Request").child(uid);
                Request_POJO request_pojo=new Request_POJO(
                        name,phone,address.getText().toString(),total,currentDate,list,0
                );
                int i=0;

                //Log.d("ksjdswww", String.valueOf(counter));
               request.child(String.valueOf(System.currentTimeMillis())).setValue(request_pojo);
               // request.child(String.valueOf(ItemContract.ItemEntry.COLUMN_TIMESTAMP)).setValue(request_pojo);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void payclick(View view) {
        if (view.getId() == R.id.upi) {
            final String id = "vimaltripathi1999@okhdfcbank";
            String note = "Order", name = "Vimalu";
            int amount = t;


            payUsingUpi(name, id, note, String.valueOf(amount));

        }
    }

    void getItem(){

        Cart c=new Cart();

        Cursor cursor=db.query(ItemContract.ItemEntry.TABLE_NAME,null,null,null,null,null,ItemContract.ItemEntry.COLUMN_TIMESTAMP + " DESC");
        if(cursor.moveToFirst()){
            do{
                list.add(new item(cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_AMOUNT))));
            }while (cursor.moveToNext());
        }
    }
    void deleteCart(){
        db.delete(ItemContract.ItemEntry.TABLE_NAME,null,null);
    }

    final int UPI_PAYMENT = 0;

    void payUsingUpi(String name, String upiId, String note, String amount) {

        Log.e("main ", "name " + name + "--up--" + upiId + "--" + note + "--" + amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Payment.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }


            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */

                switch (requestCode) {
                    case UPI_PAYMENT:
                        if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                            if (data != null) {
                                String trxt = data.getStringExtra("response");
                                Log.e("UPI", "onActivityResult: " + trxt);
                                ArrayList<String> dataList = new ArrayList<>();
                                dataList.add(trxt);
                                upiPaymentDataOperation(dataList);
                            } else {
                                Log.e("UPI", "onActivityResult: " + "Return data is null");
                                ArrayList<String> dataList = new ArrayList<>();
                                dataList.add("nothing");
                                upiPaymentDataOperation(dataList);
                            }
                        } else {
                            //when user simply back without payment
                            Log.e("UPI", "onActivityResult: " + "Return data is null");
                            ArrayList<String> dataList = new ArrayList<>();
                            dataList.add("nothing");
                            upiPaymentDataOperation(dataList);
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + requestCode);
                }
            }
            private void upiPaymentDataOperation(ArrayList<String> data) {
                if (isConnectionAvailable(Payment.this)) {
                    String str = data.get(0);
                    Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
                    String paymentCancel = "";
                    if(str == null) str = "discard";
                    String status = "";
                    String approvalRefNo = "";
                    String response[] = str.split("&");
                    for (int i = 0; i < response.length; i++) {
                        String equalStr[] = response[i].split("=");
                        if(equalStr.length >= 2) {
                            if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                                status = equalStr[1].toLowerCase();
                            }
                            else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                                approvalRefNo = equalStr[1];
                            }
                        }
                        else {
                            paymentCancel = "Payment cancelled by user.";
                        }
                    }
                    if (status.equals("success")) {
                        //Code to handle successful transaction here.
                        Toast.makeText(Payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                        Log.e("UPI", "payment successfull: "+approvalRefNo);
                    }
                    else if("Payment cancelled by user.".equals(paymentCancel)) {
                        Toast.makeText(Payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                        Log.e("UPI", "Cancelled by user: "+approvalRefNo);
                    }
                    else {
                        Toast.makeText(Payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                        Log.e("UPI", "failed payment: "+approvalRefNo);
                    }
                } else {
                    Log.e("UPI", "Internet issue: ");
                    Toast.makeText(Payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
                }
            }
            public static boolean isConnectionAvailable(Payment context) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnected()
                            && netInfo.isConnectedOrConnecting()
                            && netInfo.isAvailable()) {
                        return true;
                    }
                }
                return false;
            }
        }




