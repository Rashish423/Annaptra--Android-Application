package com.example.annapatra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class receiverdash extends AppCompatActivity {
    public static String PREFS_NAME="MyPrefsFile";
    DatabaseReference databaseReference;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    DataSnapshot dataSnapshot;
    Adapter adapter;
    String ddcounter="";
    int ddcounterint;
    String dduid;
    String dddlt;
    String dddlg;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiverdash);
        setUpToolbar();
        final TextView rfoodname=findViewById(R.id.rfoodname);
        final TextView rfoodqty=findViewById(R.id.rfoodqty);
        final TextView rnopeople=findViewById(R.id.rnopeople);
        final TextView raddress=findViewById(R.id.raddress);

        databaseReference= firebaseDatabase.getInstance().getReference().child("Requests").child("f");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Toast.makeText(receiverdash.this,"New Donate Request",Toast.LENGTH_SHORT).show();
                    String val1 = snapshot.child("Food Name").getValue().toString();
                    String val2 = snapshot.child("Quantity").getValue().toString();
                    String val3 = snapshot.child("no of people").getValue().toString();
                    String val4 = snapshot.child("address").getValue().toString();
                    String addlt = snapshot.child("Latitude").getValue().toString();
                    String addlg = snapshot.child("Longitude").getValue().toString();
                    dduid = snapshot.child("donoruid").getValue().toString();;
                    rfoodname.setText(val1);
                    rfoodqty.setText(val2+" "+"KG");
                    rnopeople.setText(val3);
                    raddress.setText(val4);
                    dddlt=addlt;
                    dddlg=addlg;
                }
                else{
                    Toast.makeText(receiverdash.this,"No Donate Request",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button accepted=findViewById(R.id.raccept);
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference= firebaseDatabase.getInstance().getReference();
                databaseReference.child("Requests").child("f").removeValue();
                databaseReference.child("requeststatus").child(dduid).child("status").setValue("Accepted");
            }
        });


        Button declined=findViewById(R.id.rdeclined);
        declined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference= firebaseDatabase.getInstance().getReference();
                databaseReference.child("Requests").child("f").removeValue();
                databaseReference.child("requeststatus").child(dduid).child("status").setValue("Declined");
            }
        });

        Button workdone=findViewById(R.id.requestdone);
        workdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference= firebaseDatabase.getInstance().getReference().child("requeststatus");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            ddcounter=snapshot.child(dduid).child("donatecounter").getValue().toString();
                            ddcounterint=Integer.parseInt(ddcounter);
                        }
                        else{
                            databaseReference.child(dduid).child("donatecounter").setValue(0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child(dduid).child("status").setValue("Successful");
                databaseReference.child(dduid).child("donatecounter").setValue(ddcounterint+1);
            }
        });

        Button getddlocation = findViewById(R.id.getddirection);
        getddlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "http://maps.google.com/maps?q=loc:" + dddlt + "," + dddlg + " (" + "Label which you want" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });



        navigationView=findViewById(R.id.dashnav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(receiverdash.this, receiverprofile.class));
                        break;
                    case R.id.logout:
                        signoutdonor();
                        break;
                    case R.id.followus:
                        Intent browserintent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/___ashish_rana___/"));
                        startActivity(browserintent2);
                        break;
                    case R.id.aboutus:
                        Intent browserintent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/ashish-rana-448713180"));
                        startActivity(browserintent3);
                        break;
                }
                return false;
            }
        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.d("Take it", "Ktya "+token);
                    }
                });






    }


    private void signoutdonor() {
        SharedPreferences sharedPreferences =getSharedPreferences(splash.PREFS_NAME,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("hasgLoggedIn",false);
        editor.commit();
        finish();
        firebaseAuth.signOut();
        Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,receiverlogin.class));
    }

    private void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


}
