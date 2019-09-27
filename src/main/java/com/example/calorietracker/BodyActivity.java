package com.example.calorietracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static com.example.calorietracker.RestClient.findAllUers;

public class BodyActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    Button btnNext;

    EditText height;
    EditText weight;
    EditText level;
    EditText steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        spinner = (Spinner) findViewById(R.id.et_level);
        height = findViewById(R.id.et_height);
        weight = findViewById(R.id.et_weight);
        level = findViewById(R.id.level);
        steps = findViewById(R.id.et_steps);

        adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + "selected", Toast.LENGTH_LONG).show();
                String le = spinner.getSelectedItem().toString();
                level.setText(le);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        btnNext = findViewById(R.id.btn_next2);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()) {
                    Intent intent = new Intent(BodyActivity.this, LoginActivity.class);
                    Bundle bundle= getIntent().getExtras();
                    bundle.putString("height",height.getText().toString());
                    bundle.putString("weight",weight.getText().toString());
                    bundle.putString("level",level.getText().toString());
                    bundle.putString("step",steps.getText().toString());
                    intent.putExtras(bundle);
                    PostAsyncTask postAsyncTask = new PostAsyncTask();
                    postAsyncTask.execute();
                    startActivity(intent);
                } else {
                        AlertDialog();
                }




            }


        });


    }







    private class PostAsyncTask extends AsyncTask<String, Void, String>
    {



        @Override
        protected String doInBackground(String...params){

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String name = bundle.getString("firstname");
            String surname = bundle.getString("surname");
            String email = bundle.getString("email");
            String dob = bundle.getString("dob");
            String postcode = bundle.getString("postcode");
            String gender = bundle.getString("gender");
            String address = bundle.getString("adress");
            String height = BodyActivity.this.height.getText().toString();
            String weight = BodyActivity.this.weight.getText().toString();//bundle.getString("weight");
            String level =BodyActivity.this.level.getText().toString(); //bundle.getString("level");
            String step = BodyActivity.this.steps.getText().toString();//bundle.getString("step");
            String password = bundle.getString("password");
            String username = bundle.getString("username");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dob2 = null;
            try {
                dob2 = formatter.parse(dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            JSONArray users = null;
            String allUser = findAllUers();
            try {
                users = new JSONArray(allUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int userid = users.length()+1;
            UserTable userTable = new UserTable(userid);
            Credential credential = new Credential(userid);
            //userTable.setUserid(6);
            userTable.setName(name);
            userTable.setDob(dob2);
            userTable.setSurname(surname);
            userTable.setEmail(email);
            userTable.setPostcode(postcode);
            userTable.setGender(gender);
            userTable.setAddress(address);
            userTable.setHeight(BigDecimal.valueOf(Double.parseDouble(height)));
            userTable.setWeight(BigDecimal.valueOf(Double.parseDouble(weight)));
            userTable.setLevelOfActivity(Short.parseShort(level));
            userTable.setStepPerMile(Short.parseShort(step));
            credential.setUserid(userTable);
            credential.setPassword(password);
            credential.setUsername(username);
            credential.setSignUpDate(dob2);
           RestClient.createUserTable(userTable);
           RestClient.createCredential(credential);
            return "Registration has been successful";

        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(BodyActivity.this, response, Toast.LENGTH_SHORT).show();

        }
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean checkDataEntered(){
        if(isEmpty(height)){
            height.setError("Please input your height");
            return false;
        }
        if(isEmpty(weight)){
            weight.setError("Please input your weight");
            return false;
        }
        if(isEmpty(level)){
            level.setError("Please select a level");
            return false;
        }
        if(isEmpty(steps)){
            steps.setError("Please input steps per mile");
            return false;
        }
        if (!height.getText().toString() .matches("^(0|[1][0-9]{1,2})$")){
            height.setError("Invalid height!");
            return false;
        }
        if (!weight.getText().toString().matches("^(0|[1-9][0-9]{1,2})$")){
            weight.setError("Invalid weight!");
            return false;
        }
        if (!steps.getText().toString().matches("^(0|[1-9][0-9]{0,3})$")){
            steps.setError("Invalid step!");
            return false;
        }
        return true;
    }

    private void AlertDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(BodyActivity.this);
        alert.setMessage("Please input the form correctly!").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert2 = alert.create();
        alert2.show();
    }
}
