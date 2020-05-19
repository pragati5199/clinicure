package com.example.clinicure;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clinicure.user_profile.FirebaseApp;
import com.example.clinicure.user_profile.profile;
import com.example.clinicure.user_profile.symptom;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class signup extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup radioGroup;
//    private ImageView imageP,imageD,imageL;
    private TextView tvP,tvD,tvL,login_textview;
    private Button signup_button;
    private EditText uname,pwd,cnfpwd;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    private FirebaseApp fireObj;
    private FirebaseUser user;
    private long maxId=0;

    private ImageView fb_button,gmail_button;

    private static final String TAG = "EmailPassword";

    private CallbackManager callbackManager;
    GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mref = database.getReference().child("Users");

        fireObj = new FirebaseApp();

        callbackManager = CallbackManager.Factory.create();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(signup.this, symptom.class));
                }
            }
        };

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            boolean visible;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.imageP){
                    visible = !visible;
                    tvP.setVisibility(visible ? View.VISIBLE: View.VISIBLE);
                    tvD.setVisibility(visible ? View.INVISIBLE: View.INVISIBLE);
                    tvL.setVisibility(visible ? View.INVISIBLE: View.INVISIBLE);
                }
                else if(checkedId == R.id.imageD){
                    visible = !visible;
                    tvD.setVisibility(visible ? View.VISIBLE: View.VISIBLE);
                    tvP.setVisibility(visible ? View.INVISIBLE: View.INVISIBLE);
                    tvL.setVisibility(visible ? View.INVISIBLE: View.INVISIBLE);
                }
                else if(checkedId == R.id.imageL){
                    visible = !visible;
                    tvL.setVisibility(visible ? View.VISIBLE: View.VISIBLE);
                    tvD.setVisibility(visible ? View.INVISIBLE: View.INVISIBLE);
                    tvP.setVisibility(visible ? View.INVISIBLE: View.INVISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_textview.setOnClickListener(this);
        signup_button.setOnClickListener(this);
        fb_button.setOnClickListener(this);
        gmail_button.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
                        .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Toast.makeText(signup.this, "You got an error!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

        login_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginScreen();
            }
        });

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxId = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        fb_button.setPermissions("email", "public_profile");
//        fb_button.registerCallback();

    }

    private void init(){
//        imageP = findViewById(R.id.imageP);
//        imageD = findViewById(R.id.imageD);
//        imageL = findViewById(R.id.imageL);

        radioGroup = findViewById(R.id.radioGroup);

        tvP = findViewById(R.id.tvP);
        tvD = findViewById(R.id.tvD);
        tvL = findViewById(R.id.tvL);

        uname = findViewById(R.id.uname);
        pwd = findViewById(R.id.pwd);
        cnfpwd = findViewById(R.id.cnfpwd);

        login_textview = findViewById(R.id.login_textview);
        signup_button = findViewById(R.id.signup_button);

        fb_button = findViewById(R.id.fb_button);
        gmail_button = findViewById(R.id.gmail_button);
    }

    private boolean validateForm() {

        boolean valid = true;

        String email = uname.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            uname.setError("Required.");
            valid = false;
        } else {
            uname.setError(null);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            uname.setError("Invalid Email!");
        }

        String password = pwd.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            pwd.setError("Required.");
            valid = false;
        } else if (password.length()<6){
            pwd.setError("Password too short!");
            valid = false;
        }
        else {
            pwd.setError(null);
        }

        if(radioGroup.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(getApplicationContext(), "Please select a category!", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private void loginScreen() {
//        if (user != null && user.isEmailVerified()){
//            startActivity(new Intent(getApplicationContext(), symptom.class));
//            finish();
//        }else {
//            startActivity(new Intent(getApplicationContext(), login_screen.class));
//        }
        startActivity(new Intent(getApplicationContext(), login_screen.class));
        finish();
    }

    private void signUpScreen() {
        if (!validateForm()){
            return;
        }

        final String email = uname.getText().toString().trim();
        final String pass = pwd.getText().toString().trim();
        String cnfpass = cnfpwd.getText().toString().trim();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        String category = null;
        if(selectedId==R.id.imageP)
            category = "patient";
        else if(selectedId==R.id.imageD)
            category = "doctor";
        else if(selectedId==R.id.imageL)
            category = "lab";

        if (pass.equals(cnfpass)){
            final String finalCategory = category;
            final LoadingActivity cddd = new LoadingActivity(signup.this);
            cddd.show();
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            fireObj.setEmail(email);
                                            fireObj.setPassword(pass);
                                            fireObj.setCategory(finalCategory);
//                                                    mref.child(String.valueOf(maxId+1)).setValue(fireObj);
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            Toast.makeText(getApplicationContext(), "Registered Successfully! Please check your email for verification!", Toast.LENGTH_LONG).show();
                                            user = mAuth.getCurrentUser();
                                            mref.child(user.getUid()).setValue(fireObj);
                                            cddd.dismiss();
                                            Intent intent = new Intent(signup.this, login_screen.class);
                                            startActivity(intent);
                                            finish();
//                                                    uname.setText("");
//                                                    pwd.setText("");
//                                                    cnfpwd.setText("");
                                        } else {
                                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Password did not match!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fbScreen() {
        LoginManager.getInstance().logInWithReadPermissions(signup.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    private void gmailScreen(){
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null && currentUser.isEmailVerified()){
            Toast.makeText(this, "You are logged in!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), menu_drawer.class));
            finish();
        }

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void updateUI() {
        Toast.makeText(this, "You are logged in!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(), profile.class));
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            finish();
        }
//        else{
//            super.onBackPressed();
//        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void handleFacebookAccessToken(AccessToken token) {

        int selectedId = radioGroup.getCheckedRadioButtonId();
        String category = null;
        if(selectedId==R.id.imageP)
            category = "patient";
        else if(selectedId==R.id.imageD)
            category = "doctor";
        else if(selectedId==R.id.imageL)
            category = "lab";

        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        final String finalCategory = category;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            fireObj.setEmail(user.getEmail());
                            fireObj.setCategory(finalCategory);
                            mref.child(user.getUid()).setValue(fireObj);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        int selectedId = radioGroup.getCheckedRadioButtonId();
        String category = null;
        if(selectedId==R.id.imageP)
            category = "patient";
        else if(selectedId==R.id.imageD)
            category = "doctor";
        else if(selectedId==R.id.imageL)
            category = "lab";

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        final String finalCategory = category;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            fireObj.setEmail(user.getEmail());
                            fireObj.setCategory(finalCategory);
                            mref.child(user.getUid()).setValue(fireObj);
                            updateUI();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_textview) {
            loginScreen();
        }
        if(i == R.id.signup_button){
            signUpScreen();
        } else if (i == R.id.fb_button) {
            fbScreen();
        } else if (i == R.id.gmail_button) {
            gmailScreen();
        }
    }
}
