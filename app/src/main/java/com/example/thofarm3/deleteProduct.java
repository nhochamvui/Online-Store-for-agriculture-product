package com.example.thofarm3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class deleteProduct extends AppCompatActivity {
    ListView listView;
    cuAdapter cuAdapter;
    static ArrayList<sanpham> mangsanpham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Xóa sản phẩm");
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
