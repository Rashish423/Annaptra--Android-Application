package com.example.annapatra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import javax.security.auth.login.LoginException;

public class splash extends AppCompatActivity {
    public static final String PREFS_NAME ="MyPrefsFile";
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences= getSharedPreferences(donordash.PREFS_NAME,0);
                boolean hasLoggedIn=sharedPreferences.getBoolean("hasLoggedIn",false);
                boolean hasgLoggedIn=sharedPreferences.getBoolean("hasgLoggedIn",false);
                if(hasLoggedIn){
                    startActivity(new Intent(splash.this, donordash.class));
                    finish();
                }
                else if(hasgLoggedIn){
                    startActivity(new Intent(splash.this, receiverdash.class));
                    finish();
                }
                else{
                    startActivity(new Intent(splash.this,userselection.class));
                finish();
            }
            }
        },1000);


        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1000);
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(splash.this, userselection.class);
                    startActivity(intent);
                    finish();
                }
            }

        };thread.start();
    }

}