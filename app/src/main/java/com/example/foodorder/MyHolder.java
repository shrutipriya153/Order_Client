package com.example.foodorder;

import android.view.View;
import android.widget.TextView;

public class MyHolder {
    TextView name,quant;
    public MyHolder(View view){
        name=view.findViewById(R.id.order);
        quant=view.findViewById(R.id.order_num);
    }
}
