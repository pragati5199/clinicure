package com.example.clinicure;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Phone_Auth_Test extends AppCompatActivity {
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationCode,OTP_no;
    private static final String TAG = "PhoneAuthActivity";

    private EditText phone_no,otp;
    private Button generate,verify,save_fruit,ret_fruit;

    private CheckBox mango,apple,orange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone__auth__test);

        phone_no = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        generate = findViewById(R.id.generate);
        verify = findViewById(R.id.verify);

        mango = findViewById(R.id.mango);
        apple = findViewById(R.id.apple);
        orange = findViewById(R.id.orange);
        save_fruit = findViewById(R.id.save_fruit);
        ret_fruit = findViewById(R.id.retreive_fruit);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("", "onVerificationCompleted:" + credential);
                Toast.makeText(Phone_Auth_Test.this,"verification completed", Toast.LENGTH_SHORT).show();
//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("", "onVerificationFailed", e);
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    fieldPhoneNumber.setError("Invalid phone number.");
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//
//                }
                Toast.makeText(Phone_Auth_Test.this,"verification failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(Phone_Auth_Test.this,"Code sent\n"+s,Toast.LENGTH_SHORT).show();
            }
        };

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP_no=otp.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, OTP_no);
                String n = credential.getSmsCode();
                if (OTP_no.equals(n)){
                    Toast.makeText(Phone_Auth_Test.this, "Verified user\n"+OTP_no+"\n"+n, Toast.LENGTH_SHORT).show();
                    otp.setText(" ");
                } else {
                    Toast.makeText(Phone_Auth_Test.this, "Verified Not user", Toast.LENGTH_SHORT).show();
                }

            }
        });

        save_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (mango.isChecked()) {FirebaseDatabase.getInstance().getReference("Test").child("fruits").setValue("mango");}
                if (apple.isChecked()) {FirebaseDatabase.getInstance().getReference("Test").child("fruits").setValue("apple");}
                if (orange.isChecked()) {FirebaseDatabase.getInstance().getReference("Test").child("fruits").setValue("orange");}

            }
        });

        ret_fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Test").child("fruits")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Toast.makeText(Phone_Auth_Test.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

    }

    private void getOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_no.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
}
