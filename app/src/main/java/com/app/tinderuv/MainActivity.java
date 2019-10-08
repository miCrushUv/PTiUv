package com.app.tinderuv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mBtnRegistrar, mBtnLoggear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * Init buttons
        * */
        mBtnRegistrar= findViewById(R.id.btnRegistrar);
        mBtnLoggear = findViewById(R.id.btnIngresar);

        /*
        * Handle buttons actions
         */
        mBtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Let's start registrer activity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        mBtnLoggear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
    }
}
