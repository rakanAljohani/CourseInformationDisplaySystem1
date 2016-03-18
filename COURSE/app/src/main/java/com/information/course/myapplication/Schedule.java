package com.information.course.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Schedule extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private static Schedule mInstance;

    ArrayList<HashMap<String, String>> COUR_List;
    ProgressDialog PD;
    SimpleAdapter adapter;
    ListView myList;
    // JSON Node names
    public static final String _ID = "id";
    public static final String _NAME = "name";
    public static final String _LAB = "lab";
    public static final String _STATE = "state";
    public static final String _TIME = "time";
    int dr_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mInstance = this;
        COUR_List = new ArrayList<HashMap<String, String>>();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Schedule.this);
        dr_id = preferences.getInt("id", 0);

        myList = (ListView) findViewById(R.id.listView);

        ReadDataFromDB();

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, final long id) {

                Intent modify_intent = new Intent(Schedule.this,Update_Delete.class);

                modify_intent.putExtra("courses",COUR_List.get(position));

                startActivity(modify_intent);

            }
        });

    }

    private void ReadDataFromDB() {

        PD.show();

        String url = "http://bugshan.96.lt/read_all_courses.php?id="+ dr_id;


        JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("courses");

                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jobj = ja.getJSONObject(i);
                                    HashMap<String, String> item = new HashMap<String, String>();
                                    item.put(_ID, jobj.getString(_ID));
                                    item.put(_NAME,jobj.getString(_NAME));
                                    item.put(_LAB, jobj.getString(_LAB));
                                    item.put(_STATE,jobj.getString(_STATE));
                                    item.put(_TIME, jobj.getString(_TIME));



                                    COUR_List.add(item);

                                } // for loop ends

                                String [] fromFieldNames = new String[] { _NAME , _LAB , _STATE , _TIME };
                                int [] toViewIDs = new int [] { R.id.course_name, R.id.course_lab,R.id.course_state,R.id.course_time};

                                adapter = new SimpleAdapter(getApplicationContext(), COUR_List,R.layout.schedule_data, fromFieldNames, toViewIDs);

                                myList.setAdapter(adapter);
                                PD.dismiss();

                            }
                            else if (success == 0) {
                                Toast.makeText(Schedule.this,"No Courses Found",Toast.LENGTH_SHORT).show();

                                PD.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        })





                ;


        // Adding request to request queue
        Schedule.getInstance().addToReqQueue(jreq);

    }

    public static synchronized Schedule getInstance() {
        return mInstance;
    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req) {

        getReqQueue().add(req);
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Add:
         startActivity(new Intent(this,add_course.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
