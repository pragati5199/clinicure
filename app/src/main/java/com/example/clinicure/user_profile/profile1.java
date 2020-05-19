package com.example.clinicure.user_profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clinicure.LoadingActivity;
import com.example.clinicure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class profile1 extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner, spinner1, spinner2 ;
    String[] spinnerValue = {
            "Add History",
            "None",
            "Kidney failure",
            "Diabetes",
            "Cardiac Arrest",
            "Thyroid",
            "Cancer",
            "Others",
    };
    String[] spinnerValue1 = {
            "Add Illness",
            "None",
            "Diabetes",
            "Blood Pressure",
            "Fever",
            "Others",
    };

    String[] spinnerValue2 = {
            "Add Blood Group",
            "A+",
            "A- ",
            "B+",
            "B-",
            "AB+ ",
            "AB-",
            "O+",
            "O- ",
            "Don't Know",
    };

    String past_med,current_ill,blood_grp,drug_his,allergy,insurance;
    String spinval1,spinval2,spinval3;
    EditText et1,et2,et3,et4,et5;
    List<String> values,values1,values2;
    int weight;
    RadioGroup ins;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        findViewById(R.id.btnn).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner2);
        spinner2 = (Spinner) findViewById(R.id.spinner4);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        et4 = (EditText) findViewById(R.id.editText5);
        et5 = (EditText) findViewById(R.id.editText6);
        values = new ArrayList<>(Arrays.asList(spinnerValue));
        values1 = new ArrayList<>(Arrays.asList(spinnerValue1));
        values2 = new ArrayList<>(Arrays.asList(spinnerValue2));
        ins =findViewById(R.id.radioGroup2);
        //Spinner 1

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, values) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinval1 = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + spinval1, Toast.LENGTH_SHORT)
                            .show();
                }

                if (spinval1 == "Others") {

                    et1.setVisibility(View.VISIBLE);
                    et1.requestFocus();
                    spinval1 = et1.getText().toString().trim();
                } else {
                    et1.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner 2
        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                this, R.layout.spinner_item, values1) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(spinnerArrayAdapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinval2 = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + spinval2, Toast.LENGTH_SHORT)
                            .show();
                }

                if (spinval2 == "Others") {

                    et2.setVisibility(View.VISIBLE);
                    et2.requestFocus();
                    spinval2 = et2.getText().toString().trim();
                } else {
                    et2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Spinner3
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.spinner_item, values2) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(spinnerArrayAdapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinval3 = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + spinval3, Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void addtoDatabase(){



        past_med=spinval1;
        current_ill=spinval2;
        blood_grp=spinval3;
        drug_his  = et3.getText().toString().trim();
        allergy  = et4.getText().toString().trim();
        weight  = Integer.parseInt(et5.getText().toString().trim());
        int selectedId = ins.getCheckedRadioButtonId();
        if(selectedId == R.id.radioButton5)
            insurance = "YES";
        else if(selectedId == R.id.radioButton4)
            insurance = "NO";
        if (spinval1 == "Add History") {
            Animation shake = AnimationUtils.loadAnimation(profile1.this, R.anim.shake);
            spinner.startAnimation(shake);
            ((TextView)spinner.getSelectedView()).setError("Please select one option");
            spinner.requestFocus();
        } else if (spinval2 == "Add Illness") {
            Animation shake = AnimationUtils.loadAnimation(profile1.this, R.anim.shake);
            spinner1.startAnimation(shake);
            ((TextView)spinner.getSelectedView()).setError("Please select one option");
            spinner1.requestFocus();

        } else {
            final LoadingActivity cddd = new LoadingActivity(profile1.this);
            cddd.show();
            User user = new User(
                    past_med, current_ill, blood_grp, drug_his, allergy, weight, insurance
            );

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("medical_history")
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    cddd.dismiss();
                    if (task.isSuccessful()) {

                        Toast.makeText(profile1.this, "Your data have been successfully saved...", Toast.LENGTH_LONG).show();

                    } else

                        Toast.makeText(profile1.this, "Unsuccessfull", Toast.LENGTH_LONG).show();

                }
            });


        }

    }



    @Override
    public void onClick(View v) {switch(v.getId()){
        case R.id.btnn:
            addtoDatabase();
            break;
        case R.id.btn:
            startActivity(new Intent(profile1.this, symptom.class));
            break;




    }




    }
}
