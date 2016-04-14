package com.information.course.myapplication;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Update_Delete extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    private RequestQueue mRequestQueue;
    private static Update_Delete mInstance;

    ProgressDialog PD;
    EditText name,lab;
    Button update,delete,time;
    Spinner day, state_spinner;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID2 = 1;
     String[] Days,State;
    int hour_x ;
    int minute_x;
    int  dr_id;

    String id;
    String name1;
    String lab1;
    String time1;
    String state1;
    String days1,days2,days3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete);
        mInstance = this;

        PD = new ProgressDialog(this);
        PD.setMessage("please wait.....");
        PD.setCancelable(false);

        showTimePickerDialog();

        delete=(Button) findViewById(R.id.delete_course);
        update=(Button) findViewById(R.id.update_course);

        name = (EditText) findViewById(R.id.name_coursee);
        lab = (EditText) findViewById(R.id.lab_coursee);


        day = (Spinner) findViewById(R.id.days_coursee);
        state_spinner = (Spinner) findViewById(R.id.spinner3);

        Intent i = getIntent();

        final HashMap<String, String> cour = (HashMap<String, String>) i
                .getSerializableExtra("courses");

        id = cour.get(Schedule._ID);
        name.setText(cour.get(Schedule._NAME));
        lab.setText(cour.get(Schedule._LAB));
        time.setText(cour.get(Schedule._TIME));
        final String days=cour.get(Schedule._DAYS);
        final String state=cour.get(Schedule._STATE);

        State =getResources().getStringArray(R.array.State);
        Days =getResources().getStringArray(R.array.Days);

        state_spinner.post(new Runnable() {
            @Override
            public void run() {
                if(state.equals("OnTime")){
                    state1="OnTime";
                    state_spinner.setSelection(0);
                }
                else if(state.equals("NoClass")){
                    state1="NoClass";
                    state_spinner.setSelection(1);
                }

            }
        });
        day.post(new Runnable() {
            @Override
            public void run() {
                if(days.equals("Sunday")){
                    days1="Sunday";
                    day.setSelection(0);
                }
                else if(days.equals("Monday")){
                    days1="Monday";
                    day.setSelection(1);
                }
                else if(days.equals("Tuesday")){
                    days1="Tuesday";
                    day.setSelection(2);
                }
                else if(days.equals("Wednesday")){
                    days1="Wednesday";
                    day.setSelection(3);
                }
                else if(days.equals("Thursday")){
                    days1="Thursday";
                    day.setSelection(4);
                }
                else if(days.equals("Sunday - Wednesday - Thursday")){
                    days1="Sunday";
                    days2="Wednesday";
                    days3="Thursday";
                    day.setSelection(5);
                }
                else if(days.equals("Monday - Tuesday")){
                    days1="Monday";
                    days1="Tuesday";
                    day.setSelection(6);
                }

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Days);

        day.setAdapter(adapter);

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
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
                else if(Days[index].equals("Sunday - Wednesday - Thursday")){
                    days1="Sunday";
                    days2="Wednesday";
                    days3="Thursday";
                    day.setSelection(6);
                }
                else if(Days[index].equals("Monday - Tuesday")){
                    days1="Monday";
                    days2="Tuesday";
                    day.setSelection(7);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, State);

        state_spinner.setAdapter(adapter2);

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                int index = arg0.getSelectedItemPosition();

                if(State[index].equals("OnTime")){
                    state1="OnTime";
                }
                else if(State[index].equals("NoClass")){
                    state1="NoClass";
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete.this, "Name Empty", Toast.LENGTH_SHORT).show();
                } else if (lab.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete.this, "Lab Empty", Toast.LENGTH_SHORT).show();
                }  else {


                name1 = name.getText().toString();
                lab1 = lab.getText().toString();
                time1 = time.getText().toString();


                PD.show();
                String update_url = "http://rakan.esy.es/update.php?id=" + id + "&name=" + name1 + "&lab=" + lab1 + "&time=" + time1 + "&state=" + state1+ "&days=" + days1;

                JsonObjectRequest update_request = new JsonObjectRequest(update_url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                // redirect to Schedule
                                MoveToSchedule();
                            } else {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "failed to update", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                        PD.dismiss();
                    }
                });

                // Adding request to request queue
                Update_Delete.getInstance().addToReqQueue(update_request);

            }


            }
        });




       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               PD.show();
               String delete_url = "http://rakan.esy.es/delete.php?id="+ id;

               JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                       null, new Response.Listener<JSONObject>() {

                   @Override
                   public void onResponse(JSONObject response) {

                       try {
                           int success = response.getInt("success");

                           if (success == 1) {
                               PD.dismiss();
                               Toast.makeText(getApplicationContext(), "Deleted Successfully",Toast.LENGTH_SHORT).show();
                               // redirect to Schedule
                               MoveToSchedule();
                           } else {
                               PD.dismiss();
                               Toast.makeText(getApplicationContext(),"failed to delete", Toast.LENGTH_SHORT).show();
                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
               }, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(getApplicationContext(),"fail", Toast.LENGTH_SHORT).show();
                       PD.dismiss();
                   }
               });

               // Adding request to request queue
               Update_Delete.getInstance().addToReqQueue(delete_request);


           }
       });

    }
    private void MoveToSchedule() {
        Intent read_intent = new Intent(Update_Delete.this, Schedule.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(read_intent);

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

        time = (Button) findViewById(R.id.time_coursee);
        time.setOnClickListener(
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
            return new TimePickerDialog(Update_Delete.this, kTimePickerListener, hour_x, minute_x,false);
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



    public static synchronized Update_Delete getInstance() {
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


}