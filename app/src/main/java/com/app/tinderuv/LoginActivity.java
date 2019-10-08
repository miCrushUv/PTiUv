package com.app.tinderuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private EditText mPsswdField, mEmailField;
    private TextView haveAcount, mRecoverPasswd;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //InitComponents
        mLoginBtn = findViewById(R.id.ingresarBtn);
        mEmailField = findViewById(R.id.emailTxt);
        mRecoverPasswd = findViewById(R.id.recuperarContraseña);
        mPsswdField = findViewById(R.id.psswdTxt);
        haveAcount = findViewById(R.id.NotengoCuentaBtn);
        mAuth = FirebaseAuth.getInstance();

        //Handle Login btn actions
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String psswd = mPsswdField.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //Invalid email
                    mEmailField.setError("Email invalido");
                    mEmailField.setFocusable(true);
                }else{
                    loginUser(email,psswd);
                }
            }
        });

        //Handle don't have accound actions
        haveAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        //Handle recover lost passwd

        mRecoverPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverDialog();
            }
        });

        //ProgressBar init
        progressBar = new ProgressBar(LoginActivity.this);

    }

    private void showRecoverDialog() {
        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar mi contraseña");

        //Set linear layout
        LinearLayout linearLayout = new LinearLayout(this);

        //Views to the linearLayout
        final EditText emailTx = new EditText(this);
        emailTx.setHint("Email");
        emailTx.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailTx.setMinEms(16);

        //Inflate Linear layout
        linearLayout.addView(emailTx);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Recuperame", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                beginRecovery(emailTx.getText().toString().trim());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss
                dialog.dismiss();
            }
        });

        //now let's create it
        builder.create().show();
    }

    private void beginRecovery(String email) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Email enviado", Toast.LENGTH_SHORT);
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Ups, algo salio mal!", Toast.LENGTH_SHORT);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void loginUser(String email, String psswd) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, psswd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
