package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {
    View vMain;
    TextView resultTextView;
    EditText et_goal;
    Button bt_goal;
    StepDatabase db = null;
    TextView tv_goal;
    TextView tv_totalStep;
    TextView tv_totalBurn;
    TextView tv_totalConsum;
    Button submitReport;
    int totalStep1;
    Double totalBurn1;
    Double totalConsum1;
    String totalGoal1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        resultTextView = vMain.findViewById(R.id.userResult);
        et_goal = vMain.findViewById(R.id.et_goal);
        bt_goal = vMain.findViewById(R.id.bt_goal);
        tv_goal = vMain.findViewById(R.id.tv_goal);
        tv_totalStep = vMain.findViewById(R.id.tv_totalstep);
        tv_totalBurn = vMain.findViewById(R.id.tv_toalburn);
        tv_totalConsum = vMain.findViewById(R.id.tv_totalconsum);
        submitReport = vMain.findViewById(R.id.bt_submitReport);
        db =  Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase")
                .fallbackToDestructiveMigration()
                .build();
        ImageView fitnessImage = vMain.findViewById(R.id.fitnessImage);
        TextView timeText = vMain.findViewById(R.id.time);
        fitnessImage.setImageResource(R.drawable.fitness);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        String firstName = bundle.getString("firstName");
        resultTextView.setText("Welcome " + firstName );


        @SuppressLint("SimpleDateFormat")SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        timeText.setText(str);

        SharedPreferences sharedPref =  getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        tv_goal.setText("Goal: " + getGoal());
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute();

        bt_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("goal",et_goal.getText().toString());
                editor.apply();
                Toast.makeText(getContext(),"Changed goal!", Toast.LENGTH_SHORT).show();
            }
        });

        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostToServer postToServer = new PostToServer();
                postToServer.execute();
                editor.putString("goal","0");
                editor.apply();
            }
        });
        return vMain;
    }

    String getGoal(){
        SharedPreferences prefs = getContext().getSharedPreferences("pref",Context.MODE_PRIVATE);
        String goal = prefs.getString("goal",null);
        if(goal == null){
            goal = "0";
        }
       return goal;
    }

    private class ReadDatabase extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            int totalStep = 0;
            ArrayList<String> result = new ArrayList<String>();
            List<Step> user = db.stepDao().getAll();
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            int userid = Integer.valueOf(bundle.getString("userid"));
            String stepCalorie = RestClient.findCaloriesPerStep(userid);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date();
            String curdate = formatter.format(curDate);
            String consum = "0";
            if((RestClient.findTotalCalorieConsumed(userid, curdate).isEmpty()))
            {
                consum = "0";
            }
            else{
                consum = RestClient.findTotalCalorieConsumed(userid, curdate);
                }

            if (!(user.isEmpty() || user == null)) {
                for (Step temp : user) {
                    int steps = Integer.valueOf(temp.getSteps());
                    totalStep = totalStep + steps;
                }
                totalStep1 = totalStep;
            }
            double burn = Double.valueOf(totalStep) * Double.valueOf(stepCalorie.substring(0, 4));
            result.add(consum);
            result.add(String.valueOf(burn));
            result.add(String.valueOf(totalStep));
            totalConsum1 = Double.parseDouble(consum);
            totalBurn1 = burn;
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<String> details) {
            tv_totalStep.setText("Total steps:" + details.get(2));
            tv_totalBurn.setText("Total burn calories: " + details.get(1));
            tv_totalConsum.setText("Total consoumption calories: " + details.get(0));
        }
    }

    private  class PostToServer extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            totalGoal1 = getGoal();
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            String username = bundle.getString("username");
            String userResult = RestClient.findByUsername(username);
            UserTable user = null;
            JSONArray jsonArray = null;
            JSONArray reportResult = null;
            String allReport = RestClient.findAllReport();
            try {
                jsonArray = new JSONArray(userResult);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0).getJSONObject("userid");
                Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                user = gson1.fromJson(jsonObject1.toString(), UserTable.class);
                reportResult = new JSONArray(allReport);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date curDate = new Date(System.currentTimeMillis());
            String reportid = "r" + String.valueOf(reportResult.length()+1);
            Report report = new Report();
            report.setTotalStepsTaken(totalStep1);
            report.setTotalCaloriesBurned(BigDecimal.valueOf(totalBurn1));
            report.setTotalCaloriesConsumed(BigDecimal.valueOf(totalConsum1));
            report.setCalorieGoal(Integer.valueOf(totalGoal1));
            report.setUserid(user);
            report.setRepid(reportid);
            report.setReportdate(curDate);
            RestClient.CreateReport(report);
            db.stepDao().deleteALL();
            return "Sucess";
        }
        @Override
        protected void onPostExecute(String response) {
            String reult = response;
        }
    }
}
