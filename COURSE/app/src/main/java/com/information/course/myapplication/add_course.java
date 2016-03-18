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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class add_course extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RequestQueue mRequestQueue;
    private static add_course mInstance;
    String url = "http://bugshan.96.lt/db_add_course.php";
    String cou_name;
    String cou_time;
    String cou_lab;
    String cou_state;
    Button timePicker;
    Button datePicker;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID2 = 1;
    int year_x;
    int month_x;
    int day_x;
    int hour_x ;
    int minute_x;
    int  dr_id;

    ProgressDialog PD;


    EditText name, time, lab, state;
    Button  add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mInstance = this;

        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);


        name = (EditText) findViewById(R.id.name_course);
        time = (EditText) findViewById(R.id.time_course);
        lab = (EditText) findViewById(R.id.lab_course);
        state = (EditText) findViewById(R.id.state_course);
        add = (Button) findViewById(R.id.add_course);


        showTimePickerDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")) {
                    Toast.makeText(add_course.this, "Name Empty", Toast.LENGTH_SHORT).show();
                } else if (time.getText().toString().equals("")) {
                    Toast.makeText(add_course.this, "Time Empty", Toast.LENGTH_SHORT).show();

                } else if (lab.getText().toString().equals("")) {
                    Toast.makeText(add_course.this, "Lab Empty", Toast.LENGTH_SHORT).show();

                } else if (state.getText().toString().equals("")) {
                    Toast.makeText(add_course.this, "State Empty", Toast.LENGTH_SHORT).show();

                } else {
                    PD.show();
                    cou_name = name.getText().toString();
                    cou_time = time.getText().toString();
                    cou_lab = lab.getText().toString();
                    cou_state = state.getText().toString();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(add_course.this);
                     dr_id = preferences.getInt("id", 0);



                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    PD.dismiss();


                                    startActivity(new Intent(add_course.this, Schedule.class));
                                    Toast.makeText(getApplicationContext(), "Data Inserted Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(), "failed to insert" + error, Toast.LENGTH_LONG).show();
                        }
                    })

                    {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("course_name", cou_name);
                            params.put("course_time", cou_time);
                            params.put("course_lab", cou_lab);
                            params.put("course_state", cou_state);
                            params.put("dr_id", String.valueOf(dr_id));
                            return params;
                        }
                    };


                    // Adding request to request queue
                    add_course.getInstance().addToReqQueue(postRequest);


                }


            }
        });


    }

    public static synchronized add_course getInstance() {
        return mInstance;
    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        }

        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {

        getReqQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req) {

        getReqQueue().add(req);
    }

    public void cancelPendingReq(Object tag) {

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }







    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showTimePickerDialog()
    {

        timePicker = (Button) findViewById(R.id.set_time);
        timePicker.setOnClickListener(
                new View.OnClickListener() {                                                       //
                    @Override                                                                      //
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }                                                                              //
                }
        );

    }

    @Override
    protected Dialog onCreateDialog(int id){

        if(id == DIALOG_ID)
            return new TimePickerDialog(add_course.this, kTimePickerListener, hour_x, minute_x,false);
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

                    if(minute < 10) {
                        time.setText(hour_x + ":0" + minute_x);                                                                              //
                    }
                    else{
                        time.setText(hour_x + ":" + minute_x);
                    }
                }
            };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////







}
