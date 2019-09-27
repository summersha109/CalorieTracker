package com.example.calorietracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Query;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegistActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText firstname;
    EditText surname;
    EditText email;
    EditText adress;
    EditText postcode;
    ArrayList<String> serverInfor = new ArrayList<String>();
    Button btnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        btnRegist = findViewById(R.id.btn_regist);
        username = findViewById(R.id.et_username2);
        password = findViewById(R.id.et_password2);
        firstname = findViewById(R.id.et_firstname);
        surname = findViewById(R.id.et_surname);
        email = findViewById(R.id.et_email);
        adress = findViewById(R.id.et_address);
        postcode = findViewById(R.id.et_postcode);

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDataEntered()) {
                    UsersAsyncTask usersAsyncTask = new UsersAsyncTask();
                    usersAsyncTask.execute();

                } else {
                    AlertDialog();
                }
            }
        });
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered() {
        if (isEmpty(firstname)) {
            Toast.makeText(this, "You must enter first name", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (isEmpty(surname)) {
            surname.setError("Surname is requred!");
            return false;
        }
        if (isEmail(email) == false) {
            email.setError("Enter valid email!");
            return false;
        }
        if (isEmpty(username)) {
            username.setError("Username is required");
            return false;
        }
        if (isEmpty(password)) {
            password.setError("Password is required");
            return false;
        }
        if (isEmpty(adress)) {
            adress.setError("Adress is required");
            return false;
        }
        if (isEmpty(postcode)) {
            postcode.setError("Postcode is required");
            return false;
        }
        if (!postcode.getText().toString().matches("^(0|[1-9][0-9]{3})$")){
            postcode.setError("Invalid postcode!");
            return false;
        }

        return true;
    }

    private void AlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
        alert.setMessage("Please input the form correctly!").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert2 = alert.create();
        alert2.show();
    }

    private class UsersAsyncTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            String resultuserName = RestClient.findByUsername(username.getText().toString());
            String resultEmail = RestClient.findByEmail(email.getText().toString());
            if(!resultuserName.equals("[]")){

                return 1;
            }
            if(!resultEmail.equals("[]")){

                return 2;
            }
            else {
                return 3;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1){
                username.setError("Username has already existed!");

            }
            else if(result ==2){
                email.setError("Email has already existed!");
            }
            else {
                Intent intent = new Intent(RegistActivity.this, GenderActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("password", password.getText().toString());
                bundle.putString("firstname", firstname.getText().toString());
                bundle.putString("surname", surname.getText().toString());
                bundle.putString("adress", adress.getText().toString());
                bundle.putString("postcode", postcode.getText().toString());
                bundle.putString("username",username.getText().toString());
                bundle.putString("email",email.getText().toString());
                intent.putExtras(bundle);

                startActivity(intent);
            }

        }
    }
}


