package com.app.tinderuv;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        checkUserStatus();
    }

    private void checkUserStatus(){
        currentUser = auth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(SplashScreen.this, ProfileActivity.class));
            finish();
        }else{
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }
}
