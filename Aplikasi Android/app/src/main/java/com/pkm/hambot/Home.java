package com.pkm.hambot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.ekn.gruzer.gaugelibrary.ArcGauge;
import android.widget.Switch;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    TextView idnama, time, pergerakan, jarak, hama, semprotan;
    EditText editTextTime;
    Button Btnpengaturan,Btnhistory, simpan;
    private Switch switch2;
    private HalfGauge arcGaugenilai, arcGaugenilai2;

    private float minGaugeValue = 0.0f; // Batas minimal
    private float maxGaugeValue = 100.0f; // Batas maksimal


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Btnpengaturan = findViewById(R.id.Btnpengaturan);
        Btnhistory = findViewById(R.id.Btnhistory);
        simpan = findViewById(R.id.simpan);

        idnama = findViewById((R.id.idnama));
        time = findViewById((R.id.time));
        pergerakan = findViewById((R.id.pergerakan));
        jarak = findViewById((R.id.jarak));
        hama = findViewById((R.id.hama));
        semprotan = findViewById((R.id.semprotan));

        editTextTime = findViewById((R.id.editTextTime));

        arcGaugenilai = findViewById(R.id.arcGaugenilai);
        arcGaugenilai.setMaxValue(100);
        arcGaugenilai.setMinValue(0);

        arcGaugenilai2 = findViewById(R.id.arcGaugenilai2);
        arcGaugenilai2.setMaxValue(100);
        arcGaugenilai2.setMinValue(0);

        Range range = new Range();
        range.setColor(Color.parseColor("#ce0000"));
        range.setFrom (0.0);
        range.setTo (25.0);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#E3E500"));
        range2.setFrom (25.0);
        range2.setTo (70.0);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#00b20b"));
        range3.setFrom (70.0);
        range3.setTo (100.0);

        arcGaugenilai.addRange(range);
        arcGaugenilai.addRange(range2);
        arcGaugenilai.addRange(range3);

        arcGaugenilai2.addRange(range);
        arcGaugenilai2.addRange(range2);
        arcGaugenilai2.addRange(range3);


        switch2 = findViewById(R.id.switch2);


        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpantime();
            }
        });



        Btnpengaturan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String HWID = null;
                if (bundle != null) {
                    HWID = bundle.getString("HWID");
                }
                Intent intent = new Intent(Home.this, Pengaturan.class);
                intent.putExtra("HWID", HWID);
                startActivity(intent);
            }
        });

        Btnhistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String HWID = null;
                if (bundle != null) {
                    HWID = bundle.getString("HWID");
                }
                Intent intent = new Intent(Home.this, historysensor.class);
                intent.putExtra("HWID", HWID);
                startActivity(intent);
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    UpdateswitchOn();
                } else {
                    UpdateswitchOff();
                }
            }
        });


        ambilData();
    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.


    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                ambilData();
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    private void ambilData() {
        Bundle bundle = getIntent().getExtras();
        String HWID = null;
        if (bundle != null) {
            HWID = bundle.getString("HWID");
        } else {
            Intent intent = getIntent();
            HWID = intent.getStringExtra("HWID");}

        String url = "http://192.168.1.3/hambot/rest/user/" + HWID;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showsensor(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showsensor(String response) {
        String idnama2 = "";
        String time2 = "";
        String pergerakan2 = "";
        String jarak2 = "";
        String hama2 = "";
        String semprotan2 = "";
        String editTextTime2 = "";
        String mode_penyemprotan2 = "";
        String baterai2 ="";
        String tangki_pestisida2 = "";



        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("user");
            JSONObject collegeData = result.getJSONObject(0);
            idnama2 = collegeData.getString("nama");
            time2 = collegeData.getString("time");
            pergerakan2 = collegeData.getString("pergerakan_robot");
            jarak2 = collegeData.getString("jarak_hama");
            hama2 = collegeData.getString("deteksi_hama");
            semprotan2 = collegeData.getString("penyemprotan_hama");
            editTextTime2 = collegeData.getString("waktu_penyemprotan");

            mode_penyemprotan2 = collegeData.getString("mode_penyemprotan");

            baterai2 = collegeData.getString("baterai");
            tangki_pestisida2 = collegeData.getString("tangki_pestisida");






        } catch (JSONException e) {
            e.printStackTrace();
        }

        idnama.setText(idnama2);
        time.setText(time2);
        pergerakan.setText(pergerakan2);
        jarak.setText(jarak2);
        hama.setText(hama2);
        semprotan.setText(semprotan2);
        editTextTime.setText(editTextTime2);


         if ("otomatis".equals(mode_penyemprotan2)) {switch2.setChecked(true);} else {switch2.setChecked(false);}

         arcGaugenilai.setValue(Float.parseFloat(baterai2));
         arcGaugenilai2.setValue(Float.parseFloat(tangki_pestisida2));



    }


    private void UpdateswitchOn(){
        final String mode_penyemprotan = "otomatis";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.3/hambot/rest/updatemodepenyemprotan/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode_penyemprotan", mode_penyemprotan);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void UpdateswitchOff(){
        final String mode_penyemprotan = "manual";

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.3/hambot/rest/updatemodepenyemprotan/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        }
                        if (response.contains("gagal")) {

                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mode_penyemprotan", mode_penyemprotan);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void simpantime() {
        final String waktu_penyemprotan2 = editTextTime.getText().toString().trim();

        Intent intent = getIntent();
        String HWID = intent.getStringExtra("HWID");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.3/hambot/rest/updateswaktupenyemprotan/" + HWID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("sukses")) {
                            Toast.makeText(getApplicationContext(), "Update Data Berhasil", Toast.LENGTH_LONG).show();
                        } else if (response.contains("gagal")) {
                            Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getApplicationContext(), "Update Data Gagal", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("waktu_penyemprotan", waktu_penyemprotan2);
                params.put("HWID", HWID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}