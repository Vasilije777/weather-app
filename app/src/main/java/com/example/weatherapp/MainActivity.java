package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    TextView country, city, temp, time, longitude, latitude, humidity, sunrise, sunset, pressure, wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        temp = findViewById(R.id.textView4);
        time = findViewById(R.id.dateTime);

        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        longitude = findViewById(R.id.Longitude);
        latitude = findViewById(R.id.Latitude);
        humidity = findViewById(R.id.Humidity);
        sunrise = findViewById(R.id.Sunrise);
        sunset = findViewById(R.id.Sunset);
        pressure = findViewById(R.id.Pressure);
        wind = findViewById(R.id.Wind);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLatLon();
            }
        });
    }

    public void findLatLon(){
        String cityName = editText.getText().toString();
        String url2 = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&appid=4b4369c6d28aa42fa4a615772dfa6cdf";
        ArrayList<Double> arrayList = new ArrayList<Double>();

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //pozivanje api-ja

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    double lat = jsonArray.getJSONObject(0).getDouble("lat");
                    double lon = jsonArray.getJSONObject(0).getDouble("lon");

                    arrayList.add(lat);
                    arrayList.add(lon);

                    findWeather(arrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2.add(stringRequest2);
    }

    public void findWeather(ArrayList arrayList){
        double lat = (double) arrayList.get(0);
        double lon = (double) arrayList.get(1);

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=4b4369c6d28aa42fa4a615772dfa6cdf";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //pozivanje api-ja

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // dobavljanje zemlje
                    JSONObject object1 = jsonObject.getJSONObject("sys");
                    String country2 = object1.getString("country");
                    country.setText(country2);

                    // dobavljanje grada
                    String city2 = jsonObject.getString("name");
                    city.setText(city2);

                    // dobavljanje temperature
                    JSONObject object2 = jsonObject.getJSONObject("main");
                    int temperature = object2.getInt("temp");
                    temp.setText("         " + temperature + "°");

                    // dobavljanje ikonice
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject object3 = jsonArray.getJSONObject(0);
                    String img = object3.getString("icon");
                    Picasso.get().load("http://openweathermap.org/img/wn/" + img + "@2x.png").into(imageView);

                    // dobavljanje datuma i vremena
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ss");
                    String date = sdf.format(calendar.getTime());
                    time.setText(date);

                    // dobavljanje geo sirine
                    JSONObject object4 = jsonObject.getJSONObject("coord");
                    int latitude2 = object4.getInt("lat");
                    latitude.setText(latitude2 + "°");

                    // dobavljanje geo duzine
                    JSONObject object5 = jsonObject.getJSONObject("coord");
                    int longitude2 = object5.getInt("lon");
                    longitude.setText(longitude2 + "°");

                    // dobavljanje vlaznosti
                    JSONObject object6 = jsonObject.getJSONObject("main");
                    int humidity2 = object6.getInt("humidity");
                    humidity.setText(humidity2 + "%");

                    // dobavljanje izlaska sunca
                    String sunrise2 = object1.getString("sunrise");
                    int sunrise3 = Integer.parseInt(sunrise2);
                    Date date1 = new Date();
                    date1.setTime((long)sunrise3*1000);
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String s = df.format(date1);
                    sunrise.setText(s);

                    // dobavljanje zalaska sunca
                    String sunset2 = object1.getString("sunset");
                    int sunset3 = Integer.parseInt(sunset2);
                    Date date2 = new Date();
                    date2.setTime((long)sunset3*1000);
                    DateFormat df2 = new SimpleDateFormat("HH:mm");
                    String s2 = df2.format(date2);
                    sunset.setText(s2);

                    // dobavljanje vazdusnog pritiska
                    int pressure2 = object6.getInt("pressure");
                    pressure.setText(pressure2 + "  hPa");

                    // dobavljanje brzine vetra
                    JSONObject object7 = jsonObject.getJSONObject("wind");
                    double windSpeed = object7.getDouble("speed");
                    wind.setText(windSpeed + "  km/h");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}