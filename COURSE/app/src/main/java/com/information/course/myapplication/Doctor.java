package com.information.course.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Doctor extends AppCompatActivity {


    EditText name,id,pass,conpass;
    Button sign;


    String url = "http://rakan.esy.es/db_registeration.php";
    String dr_name;
    String dr_number;
    String dr_password;

    ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);


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
                    Toast.makeText(Doctor.this, "Name Empty", Toast.LENGTH_SHORT).show();
                }
                else if(id.getText().toString().equals("")){
                    Toast.makeText(Doctor.this,"ID Empty",Toast.LENGTH_SHORT).show();
                }else if(pass.getText().toString().equals("")){
                    Toast.makeText(Doctor.this,"Password Empty",Toast.LENGTH_SHORT).show();
                }
                else if(conpass.getText().toString().equals("")){
                    Toast.makeText(Doctor.this,"Confirm Password Empty",Toast.LENGTH_SHORT).show();
                }
                else if(!conpass.getText().toString().matches(pass.getText().toString())){
                    Toast.makeText(Doctor.this,"Password not matches",Toast.LENGTH_SHORT).show();
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


                                    Toast.makeText(getApplicationContext(),"Data Inserted Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(),"fail : "+error, Toast.LENGTH_LONG).show();
                        }
                    })

                    {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", dr_name);
                            params.put("number", dr_number);
                            params.put("password", dr_password);
                            return params;
                        }
                    };



                    Volley.newRequestQueue(Doctor.this).add(postRequest);
                }


            }
        });

    }


















}
