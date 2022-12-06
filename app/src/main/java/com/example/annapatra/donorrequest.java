package com.example.annapatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class donorrequest extends AppCompatActivity {
    Button sendrqt,getloc;
    TextView dlg, dlt;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    String token="d5gvCa8ATseTTBvGPgZtF5:APA91bEjROX2C7STkyxh54-20ysMBItYcsUwhjluzD20VQM_DpSDj4u4C5ChsA5kLNBfZKdHxOcHPbf8lJOYwU_UsJdy7ZvjZ_3ZLpDnhmQBLPS7K4FFxs5LrSK1OAPbxRMCeka2nbt0";
    LocationManager locationManager;
    String latitude, longitude;
    TextView showLocation;
    FirebaseAuth firebaseAuth;
    String userID;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorrequest);
        getSupportActionBar().hide();
        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        final EditText foodname=findViewById(R.id.dfoodname);
        final EditText foodqty=findViewById(R.id.dfoodqty);
        final EditText numfeed=findViewById(R.id.numofpeople);
        final EditText dadd=findViewById(R.id.daddress);
        showLocation = findViewById(R.id.dfoodname);
        dlg = findViewById(R.id.getdlg);
        dlt = findViewById(R.id.getdlt);
        getloc = findViewById(R.id.getdlocation);
        sendrqt=findViewById(R.id.dsendrequest);
        userID= firebaseAuth.getUid();
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    //Write Function To enable gps
                    OnGPS();
                }
                else
                {
                    //GPS is already On then
                    getLocation();
                }
            }
        });


        sendrqt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String onee = dlg.getText().toString();
                final String nfood = foodname.getText().toString();
                final String qfood = foodqty.getText().toString();
                final String numpeo = numfeed.getText().toString();
                final String daddr = dadd.getText().toString();
                if(nfood.isEmpty()){
                    foodname.setError("Field cannot be left blank");
                    return;
                }
                if(qfood.isEmpty()){
                    foodqty.setError("Field cannot be left blank");
                    return;
                }
                if(numpeo.isEmpty()){
                    numfeed.setError("Field cannot be left blank");
                    return;}
                if(daddr.isEmpty()){
                    dadd.setError("Field cannot be left blank");
                    return;}
                if(onee.isEmpty()){
                    Toast.makeText(donorrequest.this, "Please share your location", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(donorrequest.this, "Sending the Request", Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,"Donote Request","Food Name : " +nfood+"\n"+"Quantity : " +qfood+"KG"+"\n"+"Address : " +daddr+"\n",getApplicationContext(),donorrequest.this);
                    notificationsSender.SendNotifications();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Requests").child("f").child("Food Name").setValue(nfood);
                    databaseReference.child("Requests").child("f").child("Quantity").setValue(qfood);
                    databaseReference.child("Requests").child("f").child("no of people").setValue(numpeo);
                    databaseReference.child("Requests").child("f").child("address").setValue(daddr);
                    databaseReference.child("Requests").child("f").child("Latitude").setValue(latitude);
                    databaseReference.child("Requests").child("f").child("Longitude").setValue(longitude);
                    databaseReference.child("Requests").child("f").child("donoruid").setValue(userID);
                    databaseReference.child("donorrequest").child(userID).child("Food Name").setValue(nfood);
                    databaseReference.child("donorrequest").child(userID).child("Quantity").setValue(qfood);
                    databaseReference.child("donorrequest").child(userID).child("no of people").setValue(numpeo);
                    databaseReference.child("donorrequest").child(userID).child("address").setValue(daddr);
                    databaseReference.child("donorrequest").child(userID).child("Latitude").setValue(latitude);
                    databaseReference.child("donorrequest").child(userID).child("Longitude").setValue(longitude);
                    databaseReference.child("requeststatus").child(userID).child("status").setValue("Inprocess");
                    startActivity(new Intent(donorrequest.this,donordash.class));
                    Toast.makeText(donorrequest.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(donorrequest.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(donorrequest.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                dlt.setText(latitude);
                dlg.setText(longitude);

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                dlt.setText(latitude);
                dlg.setText(longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                dlt.setText(latitude);
                dlg.setText(longitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Want to share location").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

}