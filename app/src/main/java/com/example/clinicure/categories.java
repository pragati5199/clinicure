package com.example.clinicure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.clinicure.adapters.Packages_Adapter;

public class categories extends AppCompatActivity {

    private RecyclerView packages_recyclerview;
    private ImageView cart;

//    private ArrayList<Integer> models;

    private RecyclerView.LayoutManager layoutManager;

    private int[] images = {R.drawable.card_test1,R.drawable.card_test2,R.drawable.card_test3,R.drawable.card_test4,
            R.drawable.card_test5,R.drawable.card_test6,R.drawable.card_test7,R.drawable.card_test8,R.drawable.card_test9,
            R.drawable.card_test10,R.drawable.card_test11,R.drawable.card_test12,R.drawable.card_test13,R.drawable.card_test14,
            R.drawable.card_test15,R.drawable.card_test16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), category_details.class));
            }
        });

        packages_recyclerview = findViewById(R.id.packages_recyclerview);

//        models = new ArrayList<>();
//        for (int image : images) {
//            models.add(image);
//        }

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        packages_recyclerview.setLayoutManager(layoutManager);
        Packages_Adapter adapter = new Packages_Adapter(images, this);
        packages_recyclerview.setAdapter(adapter);

    }

}
