package com.example.companionapp;



import android.os.Bundle;

import android.widget.ArrayAdapter;

import android.widget.GridView;



import androidx.appcompat.app.AppCompatActivity;



public class GridViewHolder extends AppCompatActivity {

    GridView gv;

    String[] testItems = {"item1","item2","item3","item4"};



    @Override



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        gv = (GridView) findViewById(R.id.gv);



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testItems);

        gv.setAdapter(adapter);



    }

}