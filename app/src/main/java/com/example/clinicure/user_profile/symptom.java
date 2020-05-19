package com.example.clinicure.user_profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clinicure.LoadingActivity;
import com.example.clinicure.R;
import com.example.clinicure.login_screen;
import com.example.clinicure.menu_drawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class symptom extends AppCompatActivity implements View.OnClickListener {
    private CheckBox rb1,rb2,rb3,rb4,rb5,rb6,rb7,rb8,rb9,rb10,rb11,rb12,rb13;
    private EditText et1,et2;
    String[] symptom;
    private FirebaseAuth mAuth;
    String fname, lname, email, userID, address, dob, contact, gender,past_med,current_ill,blood_grp,drug_his,allergy,insurance,
            s1, s2, s3, s4, s5, s6, s7,s8,s9,s10,s11,s12,s13,days,plzspecify,pl_specify,dayssince;
    int weight;
    private static final int PICK_PDF_CODE = 234;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;
    Bitmap imglogo, scaledimglogo, fb, scalefb, insta, scaleinsta, tweet, scaletweet,image,scaledimage;
    private Uri filePath;
    private DatabaseReference myRef;
    private TextView textViewStatus;
    private ProgressBar progressBar;
    private Intent chooseFile;
    //the firebase objects for storage and database
    private StorageReference mStorageReference;
    private FirebaseStorage storage;
    private DatabaseReference mDatabaseReference;
    int p=0,count=0,a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);
        rb6 = findViewById(R.id.rb6);
        rb7 = findViewById(R.id.rb7);
        rb8 = findViewById(R.id.rb8);
        rb9 = findViewById(R.id.rb9);
        rb10 = findViewById(R.id.rb10);
        rb11 = findViewById(R.id.rb11);
        rb12 = findViewById(R.id.rb12);
        rb13 = findViewById(R.id.rb13);
        et1 = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        ActivityCompat.requestPermissions(symptom.this, new String[]{
                        Manifest.permission.INTERNET},
                PackageManager.PERMISSION_GRANTED);
        mAuth = FirebaseAuth.getInstance();

        textViewStatus = (TextView) findViewById(R.id.file);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        //getting firebase objects
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
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
        //  gs://clinicure-diagnostic.appspot.com/4HGt3cKMpsO1bGip7FU0aCyxyd23/1
        //   gs://clinicure-diagnostic.appspot.com/4HGt3cKMpsO1bGip7FU0aCyxyd23



    }
    private void showData(DataSnapshot dataSnapshot) {

        fname = dataSnapshot.child(userID).child("profile/fname").getValue(String.class);
        address = dataSnapshot.child(userID).child("profile/address").getValue(String.class);
        dob = dataSnapshot.child(userID).child("profile/dob").getValue(String.class);
        contact = dataSnapshot.child(userID).child("profile/contact1").getValue(String.class);
        gender = dataSnapshot.child(userID).child("profile/gender").getValue(String.class);
        past_med = dataSnapshot.child(userID).child("medical_history/past_med").getValue(String.class);
        current_ill = dataSnapshot.child(userID).child("medical_history/current_ill").getValue(String.class);
        blood_grp = dataSnapshot.child(userID).child("medical_history/blood_grp").getValue(String.class);
        drug_his = dataSnapshot.child(userID).child("medical_history/drug_his").getValue(String.class);
        allergy = dataSnapshot.child(userID).child("medical_history/allergy").getValue(String.class);
        insurance = dataSnapshot.child(userID).child("medical_history/insurance").getValue(String.class);
        //weight = Integer.valueOf(dataSnapshot.child(userID).child("medical_history/weight").getValue(Integer.class));
        plzspecify= dataSnapshot.child(userID).child("Symptoms/please_specify").getValue(String.class);
        dayssince= dataSnapshot.child(userID).child("Symptoms/days").getValue(String.class);


    }

    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }*/

        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);

        //creating an intent for file chooser
        //  Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        //       chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICK_PDF_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                filePath=data.getData();
                String filePaths = filePath.getPath();
                textViewStatus.setText(filePaths);
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {
        progressBar.setVisibility(View.VISIBLE);
        p++;
        StorageReference sRef = mStorageReference.child(userID+"/"+p);
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        textViewStatus.setText("File Uploaded Successfully");
                        Upload upload = new Upload("hello", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                        count++;
                        final LoadingActivity cddd = new LoadingActivity(symptom.this);
                        cddd.show();
                        storage = FirebaseStorage.getInstance("gs://clinicure-diagnostic.appspot.com/");
                        mStorageReference = storage.getReference();
                        StorageReference mstorage = mStorageReference.getRoot().child(userID).child("/1");

                        try {
                            final File localFile = File.createTempFile("image", "jpeg");
                            mstorage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    Toast.makeText(symptom.this, ""+localFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                    cddd.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(symptom.this, "Failed to create"+exception, Toast.LENGTH_SHORT).show();
                                    cddd.dismiss();
                                }
                            });
                        } catch (IOException e ) {}


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addSymptoms() {

        pl_specify = et1.getText().toString().trim();
        days = et2.getText().toString().trim();

        s1 = "Fever";
        s2 = "Pain";
        s3 = "Cold";
        s4 = "Nausea";
        s5 = "Bleeding";
        s6 = "Infection";
        s7 = "Iching";
        s8 = "Drying";
        s9 = "Others";
        s10 = "Vommiting";
        s11 = "Headache";
        s12 = "Swelling";
        s13 = "Disenty";

        final LoadingActivity cddd = new LoadingActivity(symptom.this);
        cddd.show();
        user_symptoms user_symptoms = new user_symptoms();
        if (rb1.isChecked()){
            user_symptoms.setS1(s1); }
        if (rb2.isChecked()){
            user_symptoms.setS2(s2); }
        if (rb3.isChecked()){
            user_symptoms.setS3(s3); }
        if (rb4.isChecked()){
            user_symptoms.setS4(s4); }
        if (rb5.isChecked()){
            user_symptoms.setS5(s5); }
        if (rb6.isChecked()){
            user_symptoms.setS6(s6); }
        if (rb7.isChecked()){
            user_symptoms.setS7(s7); }
        if (rb8.isChecked()){
            user_symptoms.setS8(s8); }
        if (rb9.isChecked()){
            user_symptoms.setS9(s9); }
        if (rb10.isChecked()){
            user_symptoms.setS10(s10); }
        if (rb11.isChecked()){
            user_symptoms.setS11(s11); }
        if (rb12.isChecked()){
            user_symptoms.setS12(s12); }
        if (rb13.isSelected()){
            user_symptoms.setS13(s13); }




        user_symptoms.setPlease_specify(pl_specify);

        user_symptoms.setDays(days);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Symptoms")
                .setValue(user_symptoms).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                cddd.dismiss();
                if (task.isSuccessful()) {

                    Toast.makeText(symptom.this, "Your data have been successfully saved...", Toast.LENGTH_LONG).show();
                    //  Intent intent = new Intent(SymptomsActivity.this, menu_drawer.class);
                    //   startActivity(intent);
                } else

                    Toast.makeText(symptom.this, "Unsuccessfull", Toast.LENGTH_LONG).show();

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createMyPDF() {
        int myblue = ContextCompat.getColor(this, R.color.myblue);
        int mygreen = ContextCompat.getColor(this, R.color.mygreen);
        int mypurple = ContextCompat.getColor(this, R.color.mypurple);
        int myblack = ContextCompat.getColor(this, R.color.black);
        //       for (int i=0;i<=mUpload.size();i++) {
        //          Upload uploadCurrent = mUpload.get(i);
        //      }
        imglogo = BitmapFactory.decodeResource(getResources(), R.mipmap.logopdf);
        scaledimglogo = Bitmap.createScaledBitmap(imglogo, 35, 15, false);
        fb = BitmapFactory.decodeResource(getResources(), R.mipmap.facebook);
        scalefb = Bitmap.createScaledBitmap(fb, 5, 5, false);
        insta = BitmapFactory.decodeResource(getResources(), R.mipmap.instagram);
        scaleinsta = Bitmap.createScaledBitmap(insta, 5, 5, false);
        tweet = BitmapFactory.decodeResource(getResources(), R.mipmap.twitter);
        scaletweet = Bitmap.createScaledBitmap(tweet, 5, 5, false);
        if(count>0){
        scaledimage=Bitmap.createScaledBitmap(image,100,150,false);}
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.arialfont);
        Typeface customTypefacebold = Typeface.create(customTypeface, Typeface.BOLD);
        // create new document
        PdfDocument myPdfDocument = new PdfDocument();

// page description
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(114, 162, 1).create();
// starting page
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        Paint paint = new Paint();
        paint.setColor(mygreen);
        paint.setTextSize(2);
        paint.setTypeface(customTypefacebold);
        canvas.drawText("Corporate Office", 5, 10, paint);
        paint.setColor(myblue);
        paint.setTextSize(2);
        paint.setTypeface(customTypeface);
        canvas.drawText("G1, Shiv Ganga Smruti Apartment, Halyacha", 5, 13, paint);
        canvas.drawText("Pada, Shiv Mandir Road, Ambernath East, Thane,", 5, 16, paint);
        canvas.drawText("Mumbai - 421501", 5, 19, paint);
        paint.setColor(mygreen);
        paint.setTextSize(2);
        paint.setTypeface(customTypefacebold);
        canvas.drawText("Phone:  ", 5, 25, paint);
        paint.setColor(myblue);
        paint.setTextSize(2);
        paint.setTypeface(customTypeface);
        canvas.drawText("91-8484084447", 10, 25, paint);
        canvas.drawBitmap(scaledimglogo, 70, 10, paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(0, 28, 114, 28, paint);

// content
        paint.setColor(myblack);
        paint.setTextSize(3);
        paint.setTypeface(customTypefacebold);
        canvas.drawText("Name  : ", 5, 40, paint);
        canvas.drawText("Age   : ", 5, 45, paint);
        canvas.drawText("Gender: ", 5, 50, paint);
        canvas.drawText("Weight: ", 5, 55, paint);
        canvas.drawText("Contact: ", 55, 40, paint);
        canvas.drawText("Address: ", 55, 45, paint);
        paint.setTypeface(customTypeface);
        canvas.drawText(fname, 20, 40, paint);
        canvas.drawText(dob, 20, 45, paint);
        canvas.drawText(gender, 20, 50, paint);
       // canvas.drawText(String.valueOf(weight), 20, 55, paint);
        canvas.drawText(contact, 71, 40, paint);

        int x = 71, y = 45;
        for (String line : address.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }
        paint.setStrokeWidth(1);
        canvas.drawLine(15, 60, 100, 60, paint);

//medical_history
        paint.setTypeface(customTypefacebold);
        canvas.drawText("Past History   : ", 5, 70, paint);
        canvas.drawText("Current Illness: ", 5, 75, paint);
        canvas.drawText("Blood Group    : ", 5, 80, paint);
        canvas.drawText("Drug History   : ", 5, 85, paint);
        canvas.drawText("Allergy        : ", 5, 90, paint);
        canvas.drawText("Insurance      : ", 5, 95, paint);
        paint.setTypeface(customTypeface);
        canvas.drawText(past_med, 30, 70, paint);
        canvas.drawText(current_ill, 30, 75, paint);
        canvas.drawText(blood_grp, 30, 80, paint);
        canvas.drawText(drug_his, 30, 85, paint);
        canvas.drawText(allergy, 30, 90, paint);
        canvas.drawText(insurance, 30, 95, paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(15, 100, 100, 100, paint);

//Symptoms
        paint.setTypeface(customTypefacebold);
        canvas.drawText("Symptoms  : ", 5, 110, paint);
        canvas.drawText("Since how : ", 5, 115, paint);
        canvas.drawText("many days   ", 5, 118, paint);
        paint.setTypeface(customTypeface);
        canvas.drawText("I have ", 25, 110, paint);
        canvas.drawText("with symptoms ", 53, 110, paint);
        canvas.drawText(plzspecify, 39, 110, paint);
        canvas.drawText(dayssince, 25, 115, paint);
        paint.setTypeface(customTypeface);
        paint.setStrokeWidth(1);
        canvas.drawLine(15, 125, 100, 125, paint);

//documents
        paint.setTypeface(customTypefacebold);
        if(count == 0)
            canvas.drawText("Patient has not attached documents of his past.", 5, 135, paint);
        else {
            canvas.drawText("Patient has attached        documents of his past.", 5, 135, paint);
            canvas.drawText(String.valueOf(count), 40, 135, paint);
        }


//footer
        paint.setColor(mypurple);
        paint.setTextSize(2);
        paint.setTypeface(customTypefacebold);
        canvas.drawText("FOLLOW US: ", 10, 158, paint);
        canvas.drawBitmap(scalefb, 25, 154, paint);
        paint.setColor(mypurple);
        paint.setTextSize(2);
        paint.setTypeface(customTypefacebold);
        canvas.drawText(" Clinicure Healthcare ", 30, 158, paint);
        canvas.drawBitmap(scaleinsta, 55, 154, paint);
        paint.setColor(mypurple);
        paint.setTextSize(2);
        paint.setTypeface(customTypefacebold);
        canvas.drawText(" clinicure_healthcare", 62, 158, paint);
        canvas.drawBitmap(scaletweet, 86, 154, paint);
        paint.setColor(mypurple);
        paint.setTextSize(2);
        paint.setTypeface(customTypefacebold);
        canvas.drawText(" @clinicure", 93, 158, paint);
// finish the page
        myPdfDocument.finishPage(myPage);
//page2
        if (count>0){
        myPageInfo = new PdfDocument.PageInfo.Builder(114, 162, 2).create();
        myPage = myPdfDocument.startPage(myPageInfo);
        canvas = myPage.getCanvas();
        paint = new Paint();
        scaledimage=Bitmap.createScaledBitmap(image,100,150,false);
        canvas.drawBitmap(scaledimage, 7, 6, paint);
        myPdfDocument.finishPage(myPage);}

// write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "mypdf.pdf";
        File filePath = new File(targetPdf);
        try {
            myPdfDocument.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "File downloadedat location"+targetPdf, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        myPdfDocument.close();
    }






    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save_btn:{
                addSymptoms();
            }
            break;
            case R.id.next_btn:{
                ActivityCompat.requestPermissions(symptom.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);
                createMyPDF();
                startActivity(new Intent(symptom.this, menu_drawer.class));}
            break;
            case R.id.upload:
                getPDF();
                break;
            case R.id.save:
                uploadFile(filePath);




        }

    }
}
