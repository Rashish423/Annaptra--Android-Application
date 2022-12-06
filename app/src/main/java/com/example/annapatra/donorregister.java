package com.example.annapatra;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.util.Patterns;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class donorregister extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DataSnapshot dataSnapshot;
    DatabaseError databaseError;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser duser;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://annapatrabo-4e59d-default-rtdb.firebaseio.com/");
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorregister);
        getSupportActionBar().hide();

        Button donorlogin = findViewById(R.id.donorloginpage);
        donorlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(donorregister.this, userselection.class);
                startActivity(intent);
            }
        });

        Button donorsignup = findViewById(R.id.donorsignup);
        final EditText donoremail = findViewById(R.id.donoremail);
        final EditText donorpassword = findViewById(R.id.donorpassword);
        final EditText donorcpassword = findViewById(R.id.donorcpassword);
        final EditText donorfn = findViewById(R.id.donorfn);
        final EditText donorln = findViewById(R.id.donorln);
        final EditText donormob = findViewById(R.id.donormob);
        final EditText donoraddress = findViewById(R.id.donoraddress);
        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();


        donorsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dfname = donorfn.getText().toString();
                final String dlname = donorln.getText().toString();
                final String demail = donoremail.getText().toString();
                final String dpassword = donorpassword.getText().toString();
                final String dcpassword = donorcpassword.getText().toString();
                final String dmobile = donormob.getText().toString();
                final String daddress = donoraddress.getText().toString();


                if (dfname.isEmpty()) {
                    donorfn.setError("Field cannot be left blank");
                    return;
                }
                if (dlname.isEmpty()) {
                    donorln.setError("Field cannot be left blank");
                    return;
                }
                if (demail.isEmpty()) {
                    donoremail.setError("Field cannot be left blank");
                    return;
                }
                if (dpassword.isEmpty()) {
                    donorpassword.setError("Field cannot be left blank");
                    return;
                }
                if (dpassword.length() < 8) {
                    donorpassword.setError("Password should be greater or equals to 8 Characters");
                    return;
                }
                if (!dpassword.equals(dcpassword)) {
                    Toast.makeText(donorregister.this, "Password and Confirm Password Should be Same!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dmobile.isEmpty()) {
                    donormob.setError("Field cannot be left blank");
                    return;
                }

                if (donormob.length() != 10) {
                    donormob.setError("Mobile number should be 10 digit");
                    return;
                }

                if (daddress.isEmpty()) {
                    donoraddress.setError("Field cannot be left blank");
                    return;
                }


                if (Patterns.EMAIL_ADDRESS.matcher(demail).matches()) {
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("donorusers");
                    databaseReference.orderByChild("mobilenumber").equalTo(dmobile).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(donorregister.this,"User already registered",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                firebaseAuth.createUserWithEmailAndPassword(demail, dpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        userID= firebaseAuth.getUid();
                                        databaseReference.child(userID).child("email").setValue(demail);
                                        databaseReference.child(userID).child("firstname").setValue(dfname);
                                        databaseReference.child(userID).child("lastname").setValue(dlname);
                                        databaseReference.child(userID).child("mobilenumber").setValue(dmobile);
                                        databaseReference.child(userID).child("password").setValue(dpassword);
                                        databaseReference.child(userID).child("address").setValue(daddress);
                                        startActivity(new Intent(donorregister.this,donordash.class));
                                        finish();
                                        Toast.makeText(donorregister.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(donorregister.this,"Database Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else donoremail.setError("Enter Correct Email");

            }
        });

    }
}