package com.example.annapatra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import java.util.EventListener;

public class receiverprofile extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatabaseError databaseError;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    private TextView rremail;
    private TextView rrfn;
    private TextView rruid;
    String userID="";
    String val="trt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiverprofile);
        getSupportActionBar().hide();

        FirebaseUser duser=firebaseAuth.getInstance().getCurrentUser();
        userID=duser.getUid();
        rremail=findViewById(R.id.getremail);
        rrfn=findViewById(R.id.getrorgname);
        rruid=findViewById(R.id.getruid);
        databaseReference= firebaseDatabase.getInstance().getReference().child("receiverusers").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String val = snapshot.child("email").getValue().toString();
                    String val2 = snapshot.child("orgname").getValue().toString();
                    String val3 = snapshot.child("orguniqueid").getValue().toString();;
                    rremail.setText(val);
                    rrfn.setText(val2);
                    rruid.setText(val3);
                }
                else{
                    Toast.makeText(receiverprofile.this,"Error Fetching Data",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}