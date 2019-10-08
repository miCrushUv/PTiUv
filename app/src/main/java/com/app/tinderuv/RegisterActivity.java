package com.app.tinderuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button mRegistrerBtn;
    EditText mEmailTxt, mPsswdTxt;
    ProgressBar progressBar;
    TextView tengoCuentaBtn;

    //Firebase user aut
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        progressBar = new ProgressBar(this);

        //Init firebase aut
        mAuth = FirebaseAuth.getInstance();

        //Init Views
        tengoCuentaBtn = findViewById(R.id.tengoCuentaBtn);
        mRegistrerBtn = findViewById(R.id.btnRegistrar);
        mEmailTxt = findViewById(R.id.emailTxt);
        mPsswdTxt = findViewById(R.id.psswdTxt);

        //Handle register btn click
        mRegistrerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Take the text from the edit view
                String email = mEmailTxt.getText().toString().trim();
                String psswd = mPsswdTxt.getText().toString().trim();

                //check if email matches whit the correct patern
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailTxt.setError("Email invalido");
                    mEmailTxt.setFocusable(true);
                }else{
                    if(psswd.length()<6){
                        mPsswdTxt.setError("Contraseña demasiado corta");
                        mPsswdTxt.setFocusable(true);
                    }
                    else{
                        registerUser(email,psswd);
                    }
                }
            }
        });

        //Handle tengoCuentaBtn
        tengoCuentaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser(String email, String psswd) {
        progressBar.setVisibility(View.VISIBLE);

        //email and psswd is correct, lets register user and show progress bar
        mAuth.createUserWithEmailAndPassword(email, psswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Create user in success, close progressBar
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Get user data (email, uid) from auth
                            String email = user.getEmail();
                            String uid= user.getUid();

                            //When the user is registred store data into firebase realtime database

                            //use HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();

                            //Put info into hashMap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("nombre","");
                            hashMap.put("carrera","");
                            hashMap.put("Genero","");
                            hashMap.put("Orientacion","");
                            hashMap.put("universidad","");
                            hashMap.put("hijos","");
                            hashMap.put("facultad","");
                            hashMap.put("ejercicio","");
                            hashMap.put("complexion","");
                            hashMap.put("estatura","");
                            hashMap.put("Estado","");
                            hashMap.put("frase","");
                            hashMap.put("foto","");

                            //Get firebase instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            //Path to store user data named "Users"
                            DatabaseReference reference = database.getReference("Users");

                            //Put data within hashMap in database
                            reference.child(uid).setValue(hashMap);

                            //Dispose progressBar while loading
                            progressBar.setVisibility(View.GONE);

                            //Show status
                            Toast.makeText(RegisterActivity.this,"¡Registrado!+\n"+user.getEmail(),Toast.LENGTH_SHORT);

                            //start a new activity
                            startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));

                            //finish the actual activity
                            finish();
                        } else {
                            // If create user fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
