package com.information.course.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Update_Delete extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private static Update_Delete mInstance;
    ProgressDialog PD;
    EditText name,lab,state,time;
    Button update,delete;
    int dr_id;
    String id;
    String name1;
    String lab1;
    String time1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete);
        mInstance = this;

        PD = new ProgressDialog(this);
        PD.setMessage("please wait.....");
        PD.setCancelable(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Update_Delete.this);
        dr_id = preferences.getInt("id", 0);

        delete=(Button) findViewById(R.id.delete_course);
        update=(Button) findViewById(R.id.update_course);

        name = (EditText) findViewById(R.id.name_coursee);
        lab = (EditText) findViewById(R.id.lab_coursee);
        state = (EditText) findViewById(R.id.state_coursee);
        time = (EditText) findViewById(R.id.time_coursee);

        Intent i = getIntent();

        HashMap<String, String> cour = (HashMap<String, String>) i
                .getSerializableExtra("courses");

        id = cour.get(Schedule._ID);
        name.setText(cour.get(Schedule._NAME));
        lab.setText(cour.get(Schedule._LAB));
        state.setText(cour.get(Schedule._STATE));
        time.setText(cour.get(Schedule._TIME));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name1 = name.getText().toString();
                lab1 = lab.getText().toString();
                time1= time.getText().toString();

                PD.show();
                String update_url = "http://bugshan.96.lt/update.php?id="+id+"&name="+name1+"&lab="+lab1+"&time="+time1+"&state="+state.getText().toString();

                JsonObjectRequest update_request = new JsonObjectRequest(update_url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(), "Update Successfully",Toast.LENGTH_SHORT).show();
                                // redirect to Schedule
                                MoveToSchedule();
                            } else {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(),"failed to update", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                // Adding request to request queue
                Update_Delete.getInstance().addToReqQueue(update_request);




            }
        });




       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               PD.show();
               String delete_url = "http://bugshan.96.lt/delete.php?id="+ id;

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