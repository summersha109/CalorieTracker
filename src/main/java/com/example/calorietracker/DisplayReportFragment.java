package com.example.calorietracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class DisplayReportFragment extends Fragment{
    View vDisplayReport;
    PieChart pieChart;
    EditText et_reportDate;
    TextView tv_reportDate;
    DatePickerDialog picker;
    int totalCa;
    int totalbu;
    int totalre;
    Button bt_pie;
    BarChart barChart;
    Button bt_bar;
    ArrayList<BarEntry> consumedBar = new ArrayList<>();;
    ArrayList<BarEntry> burnedBar = new ArrayList<>();
    EditText startDate;
    EditText endDate;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        vDisplayReport = inflater.inflate(R.layout.fragement_report, container,false);
        pieChart = vDisplayReport.findViewById(R.id.calorie_pie_chart);
        et_reportDate = vDisplayReport.findViewById(R.id.et_reportDate);
        bt_pie = vDisplayReport.findViewById(R.id.bt_pie);
        barChart = vDisplayReport.findViewById(R.id.calorie_bar_chart);
        bt_bar = vDisplayReport.findViewById(R.id.bt_test);
        startDate = vDisplayReport.findViewById(R.id.et_stDate);
        endDate = vDisplayReport.findViewById(R.id.et_edDate);

        bt_pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportAsyncTask reportAsyncTask = new ReportAsyncTask();
                reportAsyncTask.execute();

            }
        });
        bt_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportPeriodAsyncTask reportPeriodAsyncTask = new ReportPeriodAsyncTask();
                reportPeriodAsyncTask.execute();
            }
        });

        et_reportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                et_reportDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        return vDisplayReport;
    }





    private class ReportAsyncTask extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            int userid = Integer.valueOf(bundle.getString("userid"));
            String date = et_reportDate.getText().toString();
            String totalConsum = null;
            String totalBurn = null;
            String totalRemain = null;
            ArrayList<String> reportResult = new ArrayList<String>();
            String result = RestClient.findCalorieConsumedAndburned(userid,date);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                totalConsum = jsonObject.getString("totalCaloriesConsumed");
                totalBurn = jsonObject.getString("totalCaloriesBurned");
                totalRemain = jsonObject.getString("remainingCalore");
                reportResult.add(totalConsum);
                reportResult.add(totalBurn);
                reportResult.add(totalRemain);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reportResult;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result){
            totalCa = (int) Math.round(Double.valueOf(result.get(0)));
            totalbu = (int)Math.round(Double.valueOf(result.get(1))) ;
            totalre = (int)Math.round(Double.valueOf(result.get(2))) ;
            pieChart.setUsePercentValues(true);

            pieChart.setExtraOffsets(5,5,5,5);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setHoleRadius(40f);

            pieChart.setTransparentCircleColor(Color.BLACK);
            pieChart.setTransparentCircleAlpha(100);
            pieChart.setTransparentCircleRadius(40f);

            pieChart.setRotationEnabled(true);
            pieChart.setRotationAngle(10);

            Legend l = pieChart.getLegend();
            l.setXEntrySpace(0f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            Description description = new Description();
            description.setEnabled(false);
            pieChart.setDescription(description);


            pieChart.setDrawEntryLabels(true);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(10f);

            ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
            pieEntries.add(new PieEntry(totalCa, "Total calories consumed"));
            pieEntries.add(new PieEntry(totalbu,"Total calories burned"));
            pieEntries.add(new PieEntry(totalre, "Remain calorie"));
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(Color.rgb(totalCa,205,205));
            colors.add(Color.rgb(totalbu,188,233));
            colors.add(Color.rgb(totalre,123,124));
            pieDataSet.setColors(colors);

            PieData pieData = new PieData();
            pieData.setDataSet(pieDataSet);

            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(12f);
            pieData.setValueTextColor(Color.BLUE);

            pieChart.setData(pieData);
            pieChart.highlightValues(null);
            pieChart.invalidate();

        }

    }

    private class ReportPeriodAsyncTask extends AsyncTask<String, Void, BarData>{
        ArrayList<String> date = new ArrayList<String>();
        @Override
        protected BarData doInBackground(String... strings) {
            String stDate =startDate.getText().toString();
            String edDate = endDate.getText().toString() ;
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            int userid = Integer.valueOf(bundle.getString("userid"));
            ArrayList<String> reportResult = new ArrayList<String>();
            String result = RestClient.findReport(userid,stDate,edDate);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                  int consume = (int) Math.round(Double.valueOf(jsonObject.getString("totalCaloriesConsumed")));
                  int burn = (int) Math.round(Double.valueOf(jsonObject.getString("totalCaloriesBurned")));
                  date.add(jsonObject.getString("reportdate").substring(5,10));
                    consumedBar.add(new BarEntry(i+1,consume));
                    burnedBar.add(new BarEntry(i+1,burn));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            BarDataSet barDataSet1 = new BarDataSet(consumedBar,"DATA SET 1");
            barDataSet1.setColor(Color.RED);

            BarDataSet barDataSet2 = new BarDataSet(burnedBar, "DATA SET 2");
            barDataSet2.setColor(Color.BLUE);

            BarData data = new BarData(barDataSet1,barDataSet2);
            return data;
        }
        @Override
        protected void onPostExecute(BarData data)
        {
            barChart.setData(data);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(date));
            xAxis.setCenterAxisLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setGranularityEnabled(true);

            barChart.setDragEnabled(true);
            barChart.setVisibleXRangeMaximum(3);

            float barSpace = 0.1f;
            float groupSpace = 0.5f;

            data.setBarWidth(0.15f);
            barChart.getXAxis().setAxisMinimum(0);

            barChart.groupBars(0,groupSpace,barSpace);

            barChart.invalidate();
        }
    }

}



//    private void initBarChart(BarChart barChart){
//        barChart.setBackgroundColor(Color.WHITE);
//        barChart.setDrawGridBackground(false);
//        barChart.setDrawBarShadow(false);
//        barChart.setHighlightFullBarEnabled(false);
//
//        barChart.setDrawBorders(true);
//
//        xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        yAxis = barChart.getAxisLeft();
//
//        barChart.setDrawBorders(false);
//        Description description = new Description();
//        description.setEnabled(false);
//        barChart.setDescription(description);
//
//        xAxis.setDrawAxisLine(false);
//        yAxis.setDrawAxisLine(false);
//        yAxis.setEnabled(false);
//        xAxis.setDrawGridLinesBehindData(false);
//        yAxis.enableGridDashedLine(10f,10f,0f);
//
//
//    }
//
//    private void initBarDataSet(BarDataSet barDataSet, int color){
//        barDataSet.setColor(color);
//        barDataSet.setFormLineWidth(1f);
//        barDataSet.setFormSize(15.f);
//        barDataSet.setDrawValues(true);
//    }
//
//    public void showBarChart(List<String> xValues, LinkedHashMap<String, List<Float>> dataLists,
//                             List<Integer> colors){
//        List<IBarDataSet> dataSets = new ArrayList<>();
//        int currentPosition = 0;
//
//        for (LinkedHashMap.Entry<String, List<Float>> entry : dataLists.entrySet()){
//            String name = entry.getKey();
//            List<Float> yValueList = entry.getValue();
//            List<BarEntry> entries = new ArrayList<>();
//            for (int i = 0; i < yValueList.size(); i++){
//                entries.add(new BarEntry(i, yValueList.get(i)));
//            }
//            BarDataSet barDataSet = new BarDataSet(entries, name);
//            initBarDataSet(barDataSet, colors.get(currentPosition));
//            dataSets.add(barDataSet);
//            currentPosition++;
//        }
//        xAxis.setAxisMinimum(0f);
//        xAxis.setAxisMinimum(xValues.size());
//        xAxis.setCenterAxisLabels(true);
//    }


//        LineChart lineChart = vDisplayReport.findViewById(R.id.calorie_line_chart);
//        List<Entry> entries = new ArrayList<Entry>();
//        float [] xData = {0f,1f,2f,3f,4f}; //xvalue
//        float [] yData = {1000,500,1200,2000,500}; //yvalue
//        for (int i = 0; i<xData.length; i++){
//            entries.add(new Entry(xData[i], yData[i]));
//        }
//
//        String [] date = new String[] {"5-21","5-22","5-23","5-24","5-25"};
//
//
//
//        LineDataSet dataSet = new LineDataSet(entries, "This is Demo");
//        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);
//
//        XAxis xAxisFromChart = lineChart.getXAxis();
//        xAxisFromChart.setDrawAxisLine(true);
//        xAxisFromChart.setValueFormatter(new IndexAxisValueFormatter(date));
//        xAxisFromChart.setGranularity(1f);
//        xAxisFromChart.setPosition(XAxis.XAxisPosition.BOTTOM);




