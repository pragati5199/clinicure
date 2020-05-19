package com.example.clinicure;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clinicure.user_profile.profile;
import com.example.clinicure.user_profile.symptom;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class menu_drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    FirebaseUser user;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth auth;
    TextView navName, navEmail;
    //   DatabaseReference mDatabaseReference;
    String fname, lname, email, userID;

    private static final String TAG = "menu_drawer";
    int doubleBackToExitPressedOnce = 0;

    private signup s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        navName = headerView.findViewById(R.id.name);
        navEmail = headerView.findViewById(R.id.email);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new dashboard()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference().child("Users");
        // Toast.makeText(menu_drawer.this, "Signed in"+userID, Toast.LENGTH_SHORT).show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new dashboard()).commit();
                break;
            case R.id.catgeories:
                startActivity(new Intent(this, categories.class));
                Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();
                break;
            case R.id.orders:
                Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show();
                break;
            case R.id.prog:
                Toast.makeText(this, "Program & Features", Toast.LENGTH_SHORT).show();
                break;
            case R.id.viewcart:
                startActivity(new Intent(menu_drawer.this, category_details.class));
                //        Toast.makeText(this, "View Cart", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
//                retrieveFile();
                // Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.feedback:
                Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rateus:
                Toast.makeText(this, "Rate Us!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notify:
                startActivity(new Intent(menu_drawer.this, symptom.class));
//                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.custservice:
                Toast.makeText(this, "Customer Services", Toast.LENGTH_SHORT).show();
                break;
            case R.id.offers:
                Toast.makeText(this, "Offers", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        doubleBackToExitPressedOnce = doubleBackToExitPressedOnce + 1;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (doubleBackToExitPressedOnce == 1) {
            Toast.makeText(this, "Click Again to exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
//        else{
//            super.onBackPressed();
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            updateUI();
        }

//        auth.addAuthStateListener(mAuthListener);
    }

    private void showData(DataSnapshot dataSnapshot) {
        fname = null;
        email = null;

        fname = dataSnapshot.child(userID).child("profile/fname").getValue(String.class);
        email = dataSnapshot.child(userID).child("email").getValue(String.class);
        navName.setText(fname);
        navEmail.setText(email);

    }

    private void updateUI(){
        Toast.makeText(this, "You are logged out!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(), signup.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dots_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout){
            Log.d("Firebase-Login", "Status: Logged In!");
            s = new signup();
            FirebaseAuth.getInstance().signOut();
            Log.d("Firebase-Login", "Status: Logged Out!");
            if (AccessToken.getCurrentAccessToken() != null){
                Log.d("Fb-Login", "Status: Logged In!");
                LoginManager.getInstance().logOut();
                Log.d("Fb-Login", "Status: Logged Out!");
            } else if (s.mGoogleSignInClient.isConnected()){
                Log.d("Gmail-Login", "Status: Logged In!");
                Auth.GoogleSignInApi.signOut(s.mGoogleSignInClient);
                s.mGoogleSignInClient.disconnect();
                s.mGoogleSignInClient.connect();
                Log.d("Fb-Login", "Status: Logged Out!");
            }
            Intent i = new Intent(menu_drawer.this, signup.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            Toast.makeText(this, "You are logged out!", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.users_profile){
            Intent i = new Intent(getApplicationContext(), profile.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
