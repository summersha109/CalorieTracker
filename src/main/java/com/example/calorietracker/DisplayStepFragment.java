package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DisplayStepFragment extends Fragment{
    View vDisplayStpe;
    StepDatabase db = null;
    EditText et_step;
    Button bt_step;
    TextView tv_detial;
    Button bt_showall;
    TextView tv_showall;
    EditText et_update;
    Button bt_update;
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        vDisplayStpe = inflater.inflate(R.layout.fragment_steps, container,false);
        db =  Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase")
                .fallbackToDestructiveMigration()
                .build();
        et_step = vDisplayStpe.findViewById(R.id.et_steps);
        bt_step = vDisplayStpe.findViewById(R.id.bt_step);
        tv_detial = vDisplayStpe.findViewById(R.id.tv_detail);
        bt_showall = vDisplayStpe.findViewById(R.id.bt_showall);
        tv_showall = vDisplayStpe.findViewById(R.id.tv_showall);
        et_update = vDisplayStpe.findViewById(R.id.et_update);
        bt_update = vDisplayStpe.findViewById(R.id.bt_update);
        bt_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDatabase addDatabase = new InsertDatabase();
                addDatabase.execute();
            }
        });
        bt_showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();



            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute();
            }
        });
        return vDisplayStpe;
    }

    private class InsertDatabase extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
             String detail = et_step.getText().toString();
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
             username = bundle.getString("username");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date curDate = new Date(System.currentTimeMillis());
            String curdate = formatter.format(curDate);
            Step step = new Step(username,curdate,detail);
            long id = db.stepDao().insert(step);
            return (detail + "Time" + curdate);
        }

        @Override
        protected void onPostExecute(String details) {
            tv_detial.setText("Added Record: " + "Steps: " + details);
        }


    }

    private class ReadDatabase extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            List<Step> user = db.stepDao().getAll();

            if (!(user.isEmpty() || user == null) ){
                String allUsers = "";
                for (Step temp : user) {
                    String userstr = ("Step ID: " + temp.getId()  + " " + "Steps: " + temp.getSteps() + " " + "Time: " + temp.getDate() + "\n" );
                    allUsers = allUsers + userstr;

                }
                return allUsers;
            }
            else
                return "";
        }
        @Override
        protected void onPostExecute(String details) {
            tv_showall.setText("All data: \n" + details);
        }
    }

    private class UpdateDatabase extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            Step step = null;
            String [] details = et_update.getText().toString().split("-");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String time = null;
            time = format.format(new Date());

            if (details.length == 2){
                int id = Integer.parseInt(details[0]);
                step = db.stepDao().findByUid(id);
                step.setUsername(username);
                step.setDate(time);
                step.setSteps(details[1]);
            }
            if (step != null){
                db.stepDao().updateUsers(step);
                return (details[0] + "" + details[1]);
            }
            return "";
        }
        @Override
        protected void onPostExecute(String details){
            String de = details;
        }
    }
}


