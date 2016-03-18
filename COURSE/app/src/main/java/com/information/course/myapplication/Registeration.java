package com.information.course.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registeration extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private static Registeration mInstance;

    EditText name,id,pass,conpass;
    Button sign;

    String url = "http://bugshan.96.lt/db_registeration.php";
    String dr_name;
    String dr_number;
    String dr_password;

    ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        mInstance = this;

        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);


        name=(EditText)findViewById(R.id.enterName);
        id=(EditText)findViewById(R.id.enterID);
        pass=(EditText)findViewById(R.id.enterPassword);
        conpass=(EditText)findViewById(R.id.conPass);


        sign=(Button)findViewById(R.id.sign);


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("")) {
                    Toast.makeText(Registeration.this, "Name Empty", Toast.LENGTH_SHORT).show();
                }
                else if(id.getText().toString().equals("")){
                    Toast.makeText(Registeration.this,"ID Empty",Toast.LENGTH_SHORT).show();
                }else if(pass.getText().toString().equals("")){
                    Toast.makeText(Registeration.this,"Password Empty",Toast.LENGTH_SHORT).show();
                }
                else if(conpass.getText().toString().equals("")){
                    Toast.makeText(Registeration.this,"Confirm Password Empty",Toast.LENGTH_SHORT).show();
                }
                else if(!conpass.getText().toString().matches(pass.getText().toString())){
                    Toast.makeText(Registeration.this,"Password not matches",Toast.LENGTH_SHORT).show();
                }

                else {

                    PD.show();
                    dr_name=name.getText().toString();
                    dr_number=id.getText().toString();
                    dr_password=pass.getText().toString();



                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    PD.dismiss();
                                    name.setText("");
                                    id.setText("");
                                    pass.setText("");
                                    conpass.setText("");

                                    Toast.makeText(getApplicationContext(),"Data Inserted Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(),"failed to insert"+error, Toast.LENGTH_LONG).show();
                        }
                    })

                    {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", dr_name);
                            params.put("number", dr_name);
                            params.put("password", dr_password);
                            return params;
                        }
                    };


                    // Adding request to request queue
                    Registeration.getInstance().addToReqQueue(postRequest);

                }


            }
        });

    }

















    public static synchronized Registeration getInstance() {
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
