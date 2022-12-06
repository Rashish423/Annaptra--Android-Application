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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userselection extends AppCompatActivity {
    String url="https://annapatrabo-4e59d-default-rtdb.firebaseio.com/";
DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl(url);
   FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userselection);
        getSupportActionBar().hide();
        Button receiverpage;
        Button btncreatedonor;
        receiverpage=findViewById(R.id.btnreceiverloginpage);
        receiverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userselection.this, receiverlogin.class);
                startActivity(intent);
                finish();
            }
        });

        btncreatedonor=findViewById(R.id.btndonorcreateaccount);
        btncreatedonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userselection.this, donorregister.class);
                startActivity(intent);
            }
        });

        Button donorlogin = findViewById(R.id.btndonorlogin);
        EditText donoremail = findViewById(R.id.donoremail);
        EditText donorpassword = findViewById(R.id.donorpassword);
        firebaseAuth= FirebaseAuth.getInstance();

        donorlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String demail = donoremail.getText().toString();
                String dpassword = donorpassword.getText().toString();

                if(demail.isEmpty()){
                    donoremail.setError("Please Enter Email");
                    return;
                }
                if(dpassword.isEmpty()){
                    donorpassword.setError("Please Enter Password");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(demail,dpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences sharedPreferences =getSharedPreferences(splash.PREFS_NAME,0);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn",true);
                            editor.commit();
                            Toast.makeText(userselection.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(userselection.this, donordash.class));
                            finish();
                        }
                        else {
                            Toast.makeText(userselection.this,"Wrong Email and Password",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });

    }
}