package com.example.calorietracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnLogin;
    EditText username;
    EditText password;
    String firstName;
    String userid;
    String userAdress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLogin = findViewById(R.id.btn_login2);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()) {

                    UsersAsyncTask usersAsyncTask = new UsersAsyncTask();
                    usersAsyncTask.execute();
                }
                else{
                    AlertDialog();
                }
            }
        });

    }

    //Vlidate enter user name
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private class UsersAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            String result = RestClient.findByUsername(username.getText().toString());
            String pass = null;
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                pass = jsonObject.getString("password");
                //get other join table need use jsonObject.getJSONObject("userid") if just get current table infor, use:  jsonArray.getJSONObject(index:0)
                firstName = jsonObject.getJSONObject("userid").getString("name");
                userid = jsonObject.getJSONObject("userid").getString("userid");
                userAdress = jsonObject.getJSONObject("userid").getString("address");



            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            if (pass.equals(password.getText().toString()) ) {

                return true;

            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("firstName",firstName);
                bundle.putString("userid",userid);
                bundle.putString("username",username.getText().toString());
                bundle.putString("address",userAdress);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                AlertDialog();
            }

        }
    }

        boolean checkDataEntered(){
        if(isEmpty(username)){
            username.setError("Username is required");
            return false;
        }


        if(isEmpty(password)){
            password.setError("Password is required");
            return false;
        }

        return true;

    }
    private void AlertDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setMessage("Your username or password is wrong! Please enter correct username and password").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert2 = alert.create();
        alert2.show();
    }


}
