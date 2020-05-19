package com.example.clinicure.user_profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.clinicure.LoadingActivity;
import com.example.clinicure.R;
import com.example.clinicure.login_screen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class profile extends AppCompatActivity implements View.OnClickListener {

    String fname, lname, address, age, contact1, contact2, gender;
    EditText et1, et2, et3, et4, et5, et6;
    RadioGroup ins;
    private FirebaseAuth mAuth;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
//    private String verficationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        et5 = (EditText) findViewById(R.id.et5);
        et6 = (EditText) findViewById(R.id.et6);
        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        ins =findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();

//        startPhoneVerification();
    }


    private void addData() {


        fname = et1.getText().toString().trim();
        lname = et2.getText().toString().trim();
        address = et3.getText().toString().trim();
        age = et4.getText().toString().trim();
        contact1 = et5.getText().toString().trim();
        contact2 = et6.getText().toString().trim();
        final int selectedId = ins.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButton1)
            gender = "Male";
        else if (selectedId == R.id.radioButton2)
            gender = "Female";
        else if (selectedId == R.id.radioButton3)
            gender = "Others";

        if (fname.isEmpty() || (fname.length() < 3)) {
            Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.shake);
            et1.startAnimation(shake);
            et1.setError("Enter atleast 3 characters");
            et1.requestFocus();
        } else if (lname.isEmpty() || (lname.length() < 3)) {
            Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.shake);
            et2.startAnimation(shake);
            et2.setError("enter atleast 3 characters");
            et2.requestFocus();

        } else if (address.isEmpty() || address.length() < 4) {
            Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.shake);
            et3.startAnimation(shake);
            et3.setError("Address required");
            et3.requestFocus();
        } else if (age.isEmpty() || age.length() <= 2) {
            Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.shake);
            et4.startAnimation(shake);
            et4.setError("Age Required");
            et4.requestFocus();
        } else if (contact1.isEmpty() || contact1.length() != 10) {
            Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.shake);
            et5.startAnimation(shake);
            et5.setError("Valid phone no required");
            et5.requestFocus();
        } else if (contact2.isEmpty() || contact2.length() != 10) {
            Animation shake = AnimationUtils.loadAnimation(profile.this, R.anim.shake);
            et6.startAnimation(shake);
            et5.setError("Valid phone no required");
            et6.requestFocus();

        } else {

            final LoadingActivity cddd = new LoadingActivity(profile.this);
            cddd.show();
            User_details user_details = new User_details(
                    fname, lname, address, age, contact1, contact2, gender
            );

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(mAuth.getCurrentUser().getUid()).child("profile")
                    .setValue(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    cddd.dismiss();
                    if (task.isSuccessful()) {

                        Toast.makeText(profile.this, "Your data have been successfully saved...", Toast.LENGTH_LONG).show();
                        et1.setText(fname);
                        et2.setText(lname);
                        et3.setText(address);
                        et4.setText(age);
                        et5.setText(contact1);
                        et6.setText(contact2);
                        Intent intent = new Intent(profile.this, profile1.class);
                        startActivity(intent);
                    } else

                        Toast.makeText(profile.this, "Unsuccessfull", Toast.LENGTH_LONG).show();

                }
            });


        }

    }

//    public void showOtpDialog() {
//
//        contact1 = et5.getText().toString().trim();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Verify Phone number");
//
//        LinearLayout linearLayout = new LinearLayout(this);
//
//        final EditText phone_edittext = new EditText(this);
//        phone_edittext.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        phone_edittext.setMinEms(16);
//        phone_edittext.setText(contact1);
//
//        final EditText otp_edittext = new EditText(this);
////        otp_edittext.setHint("Enter the registered email...");
//        otp_edittext.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        otp_edittext.setMinEms(16);
//
//        linearLayout.addView(phone_edittext);
//        linearLayout.addView(otp_edittext);
//        linearLayout.setPadding(10,10,10,10);
//
//        builder.setView(linearLayout);
//
//        builder.setNeutralButton("Generate OTP", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        phone_edittext.getText().toString().trim(),
//                        60,
//                        TimeUnit.SECONDS,
//                        profile.this,
//                        mCallback
//                );
////                dialog.dismiss();
//
//            }
//        });
//
//        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String otp = otp_edittext.getText().toString().trim();
//                if (verficationCode.equals(otp)) {
//                    Toast.makeText(profile.this, "Phone number verified!", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    if (otp.isEmpty()){
//                        Toast.makeText(profile.this, "Please enter the OTP!", Toast.LENGTH_SHORT).show();
//                    } else if (otp.length() < 6){
//                        Toast.makeText(profile.this, "Enter correct OTP!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(profile.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        builder.create().show();
//    }

//    private void startPhoneVerification() {
//        mAuth = FirebaseAuth.getInstance();
//        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                Toast.makeText(profile.this, "Verification Completed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                Toast.makeText(profile.this, "Verification Failed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//
//                verficationCode = s;
//                Toast.makeText(profile.this, "Code Sent to the mobile number:"+contact1, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeAutoRetrievalTimeOut(String s) {
//                super.onCodeAutoRetrievalTimeOut(s);
//            }
//        };
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:{

//                showOtpDialog();
                addData();
            }
            break;
            case R.id.next_btn:
                startActivity(new Intent(profile.this, profile1.class));
                break;


        }

    }
}
