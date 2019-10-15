package com.app.tinderuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.nio.file.Files;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;

    FirebaseUser currentUser;

    DrawerLayout drawerLayout;

    NavController navController;

    NavigationView navigationView;

    FirebaseAuth auth;

    FloatingActionButton fab;

    DatabaseReference databaseReference;

    FirebaseDatabase database;

    //Views



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        auth = FirebaseAuth.getInstance();

        checkUserStatus();

        setupNavigation();

    }

    private void setupNavigation() {

        //Do not move this, it's magic
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawerLayout);

        fab = findViewById(R.id.floatingEditProfile);

        navigationView = findViewById(R.id.navigationView);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        //Ok, you can move whatever you want from now

    }


    @Override
    public boolean onSupportNavigateUp() {
        //Load user information to the menu

        if(currentUser != null) {//check if the user is not null
            AppCompatTextView emailMenu = findViewById(R.id.mail_navigation_menu);
            emailMenu.setText(currentUser.getEmail());

            AppCompatTextView nameMenu = findViewById(R.id.name_navigation_menu);
            nameMenu.setText(currentUser.getEmail());
        }

        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.nav_host_fragment),drawerLayout);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {
            case R.id.perfil_item:
                navController.navigate(R.id.fragmento_perfil);
                break;
            case R.id.match_item:
                navController.navigate(R.id.fragmento_match);
                break;
            case R.id.chat_item:
                navController.navigate(R.id.fragmento_chat);
                break;
            case R.id.buscar_item:
                navController.navigate(R.id.fragmento_buscar);
                break;
            case R.id.configuracion_item:
                navController.navigate(R.id.fragmento_configuracion);
                break;
            case R.id.salir_item:
                logout();
                break;
        }
        return true;
    }

    private void logout() {
        auth.signOut();
        checkUserStatus();
    }

    private void checkUserStatus(){
        currentUser = auth.getCurrentUser();
        if(currentUser!=null){

        }else{
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

}
