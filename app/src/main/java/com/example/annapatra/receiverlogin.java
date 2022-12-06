package com.example.annapatra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class receiverlogin extends AppCompatActivity {
FirebaseAuth firebaseAuth;
DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://annapatrabo-4e59d-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiverlogin);
        getSupportActionBar().hide();
        Button donorpage,btncreatereceiver;
        donorpage=findViewById(R.id.btndonorloginpage);
        donorpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(receiverlogin.this, userselection.class);
                startActivity(intent);
                finish();
            }
        });

        Button receiverlogin = findViewById(R.id.btnreceiverlogin);
        EditText receiveremail = findViewById(R.id.receivermail);
        EditText receiverpassword = findViewById(R.id.receiverpassword);
        firebaseAuth= FirebaseAuth.getInstance();

        receiverlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String remail = receiveremail.getText().toString();
                String rpassword = receiverpassword.getText().toString();

                if(remail.isEmpty()){
                    receiveremail.setError("Please Enter Email");
                    return;
                }
                if(rpassword.isEmpty()){
                    receiverpassword.setError("Please Enter Password");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(remail,rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences sharedPreferences =getSharedPreferences(splash.PREFS_NAME,0);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("hasgLoggedIn",true);
                            editor.commit();
                            Toast.makeText(receiverlogin.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(receiverlogin.this, receiverdash.class));
                            finish();
                        }
                        else {
                            Toast.makeText(receiverlogin.this,"Wrong Email and Password",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });



        btncreatereceiver=findViewById(R.id.btnreceivercreateaccount);
        btncreatereceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(receiverlogin.this, receiverregister.class);
                startActivity(intent);
            }
        });
    }
}