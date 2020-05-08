package com.example.thofarm3;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button goShoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goShoppingButton = findViewById(R.id.button_goShopping);
        goShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goShopping();
            }
        });
        getSupportActionBar().hide();
    }

    public void goShopping() {
        Intent intent = new Intent(MainActivity.this, shoppingView.class);
        intent.putExtra("isadmin","true");
        startActivity(intent);
    }
}