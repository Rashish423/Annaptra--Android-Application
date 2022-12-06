package com.example.annapatra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class receiverregister extends AppCompatActivity {
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
        setContentView(R.layout.activity_receiverregister);
        getSupportActionBar().hide();



        Button receiversignup = findViewById(R.id.receiversignup);
        final EditText receiveremail = findViewById(R.id.receiveremail);
        final EditText receiverpassword = findViewById(R.id.receiverpassword);
        final EditText receivercpassword = findViewById(R.id.receivercpassword);
        final EditText receiverfn = findViewById(R.id.receiverfn);
        final EditText receiverln = findViewById(R.id.receiverln);
        final EditText receivermob = findViewById(R.id.receivermob);
        final EditText receiveruniqueno = findViewById(R.id.receiveruniqueno);
        final EditText receiverorgname = findViewById(R.id.receiverorganizationame);
        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();


        receiversignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String rfname = receiverfn.getText().toString();
                final String rlname = receiverln.getText().toString();
                final String remail = receiveremail.getText().toString();
                final String rpassword = receiverpassword.getText().toString();
                final String rcpassword = receivercpassword.getText().toString();
                final String rmobile = receivermob.getText().toString();
                final String runiqueno = receiveruniqueno.getText().toString();
                final String rorgname = receiverorgname.getText().toString();


                if (rfname.isEmpty()) {
                    receiverfn.setError("Field cannot be left blank");
                    return;
                }
                if (rlname.isEmpty()) {
                    receiverln.setError("Field cannot be left blank");
                    return;
                }
                if (remail.isEmpty()) {
                    receiveremail.setError("Field cannot be left blank");
                    return;
                }
                if (rpassword.isEmpty()) {
                    receiverpassword.setError("Field cannot be left blank");
                    return;
                }
                if (rpassword.length() < 8) {
                    receiverpassword.setError("Password should be greater or equals to 8 Characters");
                    return;
                }
                if (!rpassword.equals(rcpassword)) {
                    Toast.makeText(receiverregister.this, "Password and Confirm Password Should be Same!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rmobile.isEmpty()) {
                    receivermob.setError("Field cannot be left blank");
                    return;
                }

                if (receivermob.length() != 10) {
                    receivermob.setError("Mobile number should be 10 digit");
                    return;
                }

                if (runiqueno.isEmpty()) {
                    receiveruniqueno.setError("Field cannot be left blank");
                    return;
                }
                if (rorgname.isEmpty()) {
                    receiverorgname.setError("Field cannot be left blank");
                    return;
                }


                if (Patterns.EMAIL_ADDRESS.matcher(remail).matches()) {
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("receiverusers");
                    databaseReference.orderByChild("mobilenumber").equalTo(rmobile).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(receiverregister.this,"User already registered",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                firebaseAuth.createUserWithEmailAndPassword(remail, rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        userID= firebaseAuth.getUid();
                                        databaseReference.child(userID).child("email").setValue(remail);
                                        databaseReference.child(userID).child("firstname").setValue(rfname);
                                        databaseReference.child(userID).child("lastname").setValue(rlname);
                                        databaseReference.child(userID).child("mobilenumber").setValue(rmobile);
                                        databaseReference.child(userID).child("password").setValue(rpassword);
                                        databaseReference.child(userID).child("orguniqueid").setValue(runiqueno);
                                        databaseReference.child(userID).child("orgname").setValue(rorgname);
                                        startActivity(new Intent(receiverregister.this,receiverdash.class));
                                        finish();
                                        Toast.makeText(receiverregister.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(receiverregister.this,"Database Error",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else receiveremail.setError("Enter Correct Email");

            }
        });












        Button receiverlog=findViewById(R.id.receiverlog);

        receiverlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(receiverregister.this,receiverlogin.class));
                finish();
            }
        });
    }
}
