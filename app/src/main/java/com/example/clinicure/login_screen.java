package com.example.clinicure;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clinicure.user_profile.profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login_screen extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private TextView register_textview, forgot_password;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        if (user != null && user.isEmailVerified()) {
//            startActivity(new Intent(login_screen.this, menu_drawer.class));
//        }

        inputEmail = (EditText) findViewById(R.id.autoCompleteTextView2);
        inputPassword = findViewById(R.id.autoCompleteTextView3);
        register_textview = findViewById(R.id.register_textview);
        btnLogin = findViewById(R.id.button1);
        forgot_password = findViewById(R.id.forgot_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signup.class));
                finish();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPassDialog();
            }
        });

//        forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ResetpasswordActivity cdd=new ResetpasswordActivity(MainActivity.this);
//                cdd.show();
//                Window window = cdd.getWindow();
//                window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//            }
//        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Animation shake = AnimationUtils.loadAnimation(login_screen.this, R.anim.shake);
                    inputEmail.startAnimation(shake);
                    inputEmail.setError("Enter email address");
                    inputEmail.requestFocus();

                } else if (TextUtils.isEmpty(password)) {
                    Animation shake = AnimationUtils.loadAnimation(login_screen.this, R.anim.shake);
                    inputPassword.startAnimation(shake);
                    inputPassword.setError("Enter password");
                    inputPassword.requestFocus();

                } else {
                    final LoadingActivity cddd = new LoadingActivity(login_screen.this);
                    cddd.show();

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            cddd.dismiss();
                                        }
                                    } else {
                                        if (auth.getCurrentUser().isEmailVerified()){
                                            Intent intent = new Intent(login_screen.this, profile.class);
                                            startActivity(intent);
                                            cddd.dismiss();
                                            finish();
                                            inputEmail.setText("");
                                            inputPassword.setText("");
                                        } else {
                                            Toast.makeText(login_screen.this, "Please verify your email address!", Toast.LENGTH_SHORT).show();
                                            cddd.dismiss();
                                        }

                                    }
                                }
                            });
                }
            }
        });

    }

    private void showRecoverPassDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);

        final EditText email_edittext = new EditText(this);
        email_edittext.setHint("Enter the registered email...");
        email_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        email_edittext.setMinEms(16);

        linearLayout.addView(email_edittext);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String email = email_edittext.getText().toString();
                String email = email_edittext.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();

                } else {


                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(login_screen.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
//                                        new Handler().postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//                                                Intent i=new Intent(getContext(),MainActivity.class);
//                                                c.startActivity(i);
//                                            }
//                                        }, 5000);
                                    } else {
                                        Toast.makeText(login_screen.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null && currentUser.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), menu_drawer.class));
            finish();
        }
    }
}


//        register_textview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), signup.class));
//            }
//        });
//
//        forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showRecoverPassDialog();
//            }
//        });
//    }
//
//    private void showRecoverPassDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Recover Password");
//
//        LinearLayout linearLayout = new LinearLayout(this);
//
//        final EditText email_edittext = new EditText(this);
//        email_edittext.setHint("Enter the registered email...");
//        email_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
//        email_edittext.setMinEms(16);
//
//        linearLayout.addView(email_edittext);
//        linearLayout.setPadding(10,10,10,10);
//
//        builder.setView(linearLayout);
//
//        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                String email = email_edittext.getText().toString();
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builder.create().show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

