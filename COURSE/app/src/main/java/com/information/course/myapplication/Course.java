package com.information.course.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Course extends AppCompatActivity  {

    String url = "http://rakan.esy.es/db_add_course.php";
    String cou_name;
    String cou_time;
    String cou_lab;
    Button timePicker;

    static final int DIALOG_ID = 0;
    static final int DIALOG_ID2 = 1;

    int hour_x ;
    int minute_x;
    int  dr_id;

    String days1,days2="",days3="",state;
    ProgressDialog PD;


    EditText name, lab;
    Button  add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);



        final String[] Days,State;

        Days =getResources().getStringArray(R.array.Days);
        State =getResources().getStringArray(R.array.State);

        Spinner s1 = (Spinner) findViewById(R.id.spinner);
        Spinner s2 = (Spinner) findViewById(R.id.spinner2);

        timePicker = (Button) findViewById(R.id.set_time);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Days);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, State);

        s1.setAdapter(adapter);
        s2.setAdapter(adapter2);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                int index = arg0.getSelectedItemPosition();

              if(Days[index].equals("Sunday")){
                  days1="Sunday";
              }
              else if(Days[index].equals("Monday")){
                  days1="Monday";
              }
              else if(Days[index].equals("Tuesday")){
                  days1="Tuesday";
              }
              else if(Days[index].equals("Wednesday")){
                  days1="Wednesday";
              }
              else if(Days[index].equals("Thursday")){
                  days1="Thursday";
              }
              else if(Days[index].equals("Sunday_Tuesday_Thursday")){
                  days1="Sunday";
                  days2="Tuesday";
                  days3="Thursday";

              }
              else if(Days[index].equals("Monday_Wednesday")){
                  days1="Monday";
                  days2="Wednesday";

              }


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                int index = arg0.getSelectedItemPosition();

                if(State[index].equals("OnTime")){
                    state="OnTime";
                }
                else if(State[index].equals("Running")){
                    state="Running";
                }
                else if(State[index].equals("Canceled")){
                    state="Canceled";
                }
                else if(State[index].equals("Shifted_To")){
                    state="Shifted_To";
                }



            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);


        name = (EditText) findViewById(R.id.name_course);
        lab = (EditText) findViewById(R.id.lab_course);
        add = (Button) findViewById(R.id.add_course);


        showTimePickerDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")) {
                    Toast.makeText(Course.this, "Name Empty", Toast.LENGTH_SHORT).show();
                }
                else if (lab.getText().toString().equals("")) {
                    Toast.makeText(Course.this, "Lab Empty", Toast.LENGTH_SHORT).show();

                } else if (timePicker.getText().toString().equals("Set Time")) {
                    Toast.makeText(Course.this, "Time Empty", Toast.LENGTH_SHORT).show();
                }


                else {
                    PD.show();
                    cou_name = name.getText().toString();
                    cou_time = timePicker.getText().toString();
                    cou_lab = lab.getText().toString();


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Course.this);
                     dr_id = preferences.getInt("id", 0);



                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    PD.dismiss();



                                    Intent read_intent = new Intent(Course.this, Schedule.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(read_intent);

                                    Toast.makeText(Course.this, "Data Inserted Successfully", Toast.LENGTH_LONG).show();

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(), "fail : " + error, Toast.LENGTH_LONG).show();
                        }
                    })

                    {

                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("course_name", cou_name);
                            params.put("course_time", cou_time);
                            params.put("course_lab", cou_lab);
                            params.put("course_state", state);
                            params.put("course_days", days1);
                            if(!days2.equals("")){
                                params.put("course_days2", days2);
                            }
                            if(!days3.equals("")){
                                params.put("course_days3", days3);
                            }
                            params.put("dr_id", String.valueOf(dr_id));
                            return params;
                        }
                    };



                    Volley.newRequestQueue(Course.this).add(postRequest);


                }


            }
        });


    }






    public void showTimePickerDialog()
    {

        timePicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );

    }

    @Override
    protected Dialog onCreateDialog(int id){

        if(id == DIALOG_ID)
            return new TimePickerDialog(Course.this, kTimePickerListener, hour_x, minute_x,false);
        else if(id == DIALOG_ID2){

        }
        return null;

    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {                                                         //

                    hour_x = hourOfDay;
                    minute_x = minute;


                    if(hourOfDay < 10){
                    if(minute < 10) {
                        timePicker.setText(0+""+hour_x + ":0" + minute_x);
                        //09:09
                    }
                    else{
                        timePicker.setText(0+""+hour_x + ":" + minute_x);
                        //08:05
                    }

                    }

                    else{
                        if(minute < 10) {
                            timePicker.setText(hour_x + ":0" + minute_x);
                            //10:08
                        }
                        else{
                            timePicker.setText(hour_x + ":" + minute_x);
                            //10:34
                        }
                    }
                }
            };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////







}
