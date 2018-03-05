package pankaj.sharma.weather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.SmartisanDrawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lg, lt;

    String curl;
    String furl;

    PullRefreshLayout layout;
    FetchData fd = new FetchData();
    IconChecker ic = new IconChecker();
    String Data, fData;
    JSONObject cdata;

    TextView ctemp, cplace, cdesc,cpress, chumi, ctmin, ctmax, cwind, csset,csrise,ctup, cdt;
    ImageView cimg;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> alName, alDate;
    ArrayList<Integer> alImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ctemp = (TextView)findViewById(R.id.temp);
        cimg = (ImageView)findViewById(R.id.tempicon);
        cplace = (TextView)findViewById(R.id.place);

        cwind = (TextView)findViewById(R.id.wind);
        ctmin = (TextView)findViewById(R.id.tmin);
        ctmax = (TextView)findViewById(R.id.tmax);
        chumi = (TextView)findViewById(R.id.humi);
        cplace = (TextView)findViewById(R.id.place);
        cdesc = (TextView)findViewById(R.id.desc);
        ctup = (TextView)findViewById(R.id.tup);
        csset = (TextView)findViewById(R.id.tsundown);
        csrise = (TextView)findViewById(R.id.tsunup);
        cpress = (TextView)findViewById(R.id.tex45tView);
        cplace = (TextView)findViewById(R.id.place);
        cdt = (TextView)findViewById(R.id.dt);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        cdt.setText("Today, "+formattedDate);


       // mChart = (LineChart) findViewById(R.id.chart1);

        refreshdata();

        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        // layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        refreshdata();
                    }
                }, 3000);
            }
        });
        layout.setColorSchemeColors(Color.WHITE);
        //layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setRefreshDrawable(new SmartisanDrawable(this, layout));


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }
    public void refreshdata()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    protected class AsyncTaskRunner extends AsyncTask<String, String, String>{
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... strings) {

            if(lt!=null && lg!=null){
                fd.getURLContent(curl);
                Data = fd.getString;
                Log.d("Error", "Fetched Data"+Data);
            }
            else{
                Log.d("Error", "Data Not Fetched");
            }
            if(lt!=null && lg!=null){
                fd.getURLContent(furl);
                fData = fd.getString;
                Log.d("Error", "Fetched Data"+fData);
            }
            else{
                Log.d("Error", "Data Not Fetched");
            }
            return Data;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "ProgressDialog", "Wait for seconds");
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            CurrentWeatherData(Data);
           // LineChartTemp(fData);
            WeatherForecast(fData);
        }
    }


    public void CurrentWeatherData(String data1)
    {
        try{
            cdata= new JSONObject(data1);
            JSONObject details = cdata.getJSONArray("weather").getJSONObject(0);
            JSONObject main = cdata.getJSONObject("main");
            JSONObject sys = cdata.getJSONObject("sys");
            JSONObject wind = cdata.getJSONObject("wind");
            DateFormat df = DateFormat.getDateTimeInstance();

            String temperature = String.format("%.1f", (main.getDouble("temp")-273.15F))+ " °C";
            ctemp.setText(temperature);

            String description = details.getString("description").toUpperCase(Locale.US);
            ctup.setText(description);

            String updatedOn = df.format(new Date(cdata.getLong("dt")*1000));
            cdesc.setText(updatedOn);

            String wspeed = wind.getString("speed") + "km/h";
            cwind.setText(wspeed);

            String sunrise = df.format(new Date(cdata.getJSONObject("sys").getLong("sunrise") * 1000));
            csrise.setText(sunrise);

            String sunset = df.format(new Date(cdata.getJSONObject("sys").getLong("sunset") * 1000));
            csset.setText(sunset);

            String tmin = String.format("%.1f", (main.getDouble("temp_min")-273.15F))+ " °C";
            ctmin.setText(tmin);

            String tmax = String.format("%.1f", (main.getDouble("temp_max")-273.15F))+ " °C";
            ctmax.setText(tmax);

            String humidity = main.getString("humidity") + "%";
            chumi.setText(humidity);

            String pressure = main.getString("pressure") + " hPa";
            cpress.setText(pressure);

            String city = cdata.getString("name").toUpperCase(Locale.US) + ", " + cdata.getJSONObject("sys").getString("country");
            cplace.setText(city);

            String icon = details.getString("icon");
            ic.getIconLocation(icon, description);

            cimg.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, ic.iconLocation));

        }
        catch(Exception e){
            Toast.makeText(this,""+e,Toast.LENGTH_LONG).show();
        }

    }
    private void WeatherForecast(String fData) {
        JSONObject mOB, list;
        JSONArray ja;
        alName = new ArrayList<>();
        alDate = new ArrayList<>();
        alImage = new ArrayList<Integer>();
        try
        {
            mOB = new JSONObject(fData);
            ja = mOB.getJSONArray("list");
            for(int i=0; i<ja.length(); i++)
            {
                list = mOB.getJSONArray("list").getJSONObject(i);
                JSONObject details = list.getJSONArray("weather").getJSONObject(0);
                JSONObject main = list.getJSONObject("main");
                double m = main.getDouble("temp");
                float ss = Float.parseFloat(String.format("%.1f",m - 273.15F));
                alName.add(""+ss+" °C");

                String description = details.getString("description").toUpperCase(Locale.US);

                String icon = details.getString("icon");
                ic.getIconLocation(icon, description);
                alImage.add(ic.iconLocation);

                String updatedOn = list.getString("dt_txt");
                alDate.add(updatedOn);

            }
        }
        catch (Exception e){

        }
        mAdapter = new HLVAdapter(MainActivity.this, alName, alImage, alDate);
        mRecyclerView.setAdapter(mAdapter);

    }

    /* Location check methods */
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lg = String.valueOf(latti);
                lt = String.valueOf(longi);
                if(!lt.isEmpty()&&!lg.isEmpty())
                {
                     curl="http://api.openweathermap.org/data/2.5/weather?lat="+lg+"&lon="+lt+"&appid='Enter Your openweathermap Api Key'";
                     furl="http://api.openweathermap.org/data/2.5/forecast?lat="+lg+"&lon="+lt+"&appid='Enter Your openweathermap Api Key'";
                     new AsyncTaskRunner().execute();
                }
                else{
                    Toast.makeText(this,"Sorry! Not connected to internet",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}