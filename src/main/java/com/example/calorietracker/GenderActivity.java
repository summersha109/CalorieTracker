package com.example.calorietracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class GenderActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    TextView tvw;
    Button btnNext;
    RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        tvw = (TextView) findViewById(R.id.dob2);
        eText = (EditText)findViewById(R.id.et_dob);
        gender= findViewById(R.id.gender);

        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(GenderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btnGet=(Button)findViewById(R.id.buttondob);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ eText.getText());
            }
        });

        btnNext = findViewById(R.id.btn_next1);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()) {

                    Intent intent = new Intent(GenderActivity.this, BodyActivity.class);
                    Bundle bundle= getIntent().getExtras();
                    bundle.putString("dob",eText.getText().toString());
                    int selectID = gender.getCheckedRadioButtonId();
                    Button genderButton = findViewById(selectID);
                    bundle.putString("gender",genderButton.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                      AlertDialog();
                }
            }
        });


    }

    boolean getGender(){
        int radio = gender.getCheckedRadioButtonId();
        if (radio<= 0)
        {
            return false;
        }
        else {
            return true;
        }
    }

    boolean isEmpty(TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered(){
        if(getGender() == false){
            Toast.makeText(this,"Please select a gender!", Toast.LENGTH_SHORT);
            return false;
        }
        if(isEmpty(tvw)){
            Toast.makeText(this,"Please select date of birth",Toast.LENGTH_SHORT);
            return false;
        }
        return true;

    }

    private void AlertDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(GenderActivity.this);
        alert.setMessage("Please input the form").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert2 = alert.create();
        alert2.show();
    }



}
