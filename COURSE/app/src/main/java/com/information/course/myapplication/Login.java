package com.information.course.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    String url = "http://rakan.esy.es/login.php";

    EditText _id,pass;
    TextView create;
    String id,password;
    ProgressDialog PD;
    Button  buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);



        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);


        _id=(EditText)findViewById(R.id.enterid);
        pass=(EditText)findViewById(R.id.enterPassword);
        create=(TextView)findViewById(R.id.account);
        buttonLogin=(Button)findViewById(R.id.login);




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Doctor.class));


            }
        });





        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = _id.getText().toString();
                password = pass.getText().toString();

                if(id.equals("")){
                    Toast.makeText(Login.this,"ID Empty",Toast.LENGTH_SHORT).show();

                }else if(password.equals("")){
                    Toast.makeText(Login.this,"Password Empty",Toast.LENGTH_SHORT).show();

                }else {


                    PD.show();

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    PD.dismiss();

                                    int res = 0;
                                    JSONObject Json = null;

                                    try {
                                         Json = new JSONObject(response);
                                        res = Json.getInt("id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }



                                    if(res !=0) {

                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putInt("id",res);
                                        editor.apply();

                                        Toast.makeText(getApplicationContext(), "Login Successed", Toast.LENGTH_LONG).show();

                                     startActivity(new Intent(Login.this,MainMenu.class));

                                        finish();

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Login Failed ... Try Again", Toast.LENGTH_LONG).show();

                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(),"Fail", Toast.LENGTH_LONG).show();
                        }
                    })

                    {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("number", id);
                            params.put("password", password);
                            return params;
                        }
                    };



                    Volley.newRequestQueue(Login.this).add(postRequest);

                }



            }
        });




    }






}
