package com.example.clinicure.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clinicure.R;

import java.util.Arrays;

public class Packages_Adapter extends RecyclerView.Adapter<Packages_Adapter.ViewHolder>{

    private static final String TAG = "RecyclerView";

    private int[] packages;
    private Context context;

    public Packages_Adapter(int[] packages, Context context) {
        this.packages = packages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.packages_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: Called");
        final int img_id = packages[i];
        viewHolder.package_images.setImageResource(img_id);
        viewHolder.package_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, img_id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return packages.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView package_images;

        public ViewHolder(View items){
            super(items);
            package_images = items.findViewById(R.id.package_images);
        }
    }
}
