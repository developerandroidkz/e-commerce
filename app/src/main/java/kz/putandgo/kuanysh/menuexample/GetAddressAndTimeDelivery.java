package kz.putandgo.kuanysh.menuexample;

import android.*;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Kuanysh on 25.03.2017.
 */

public class GetAddressAndTimeDelivery extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mGoogleMap;
    private SQLiteDB sqLiteDB;
    GoogleApiClient mGoogleApiClient;
    double lat=0, lng=0;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public String data="";
    public static TextView dates,carrentTime;
    UserDatas userDatas;
    Button buttonSend;
    ProgressDialog pd;
    List<Basket> basket;

    private static int startYear,starthMonth,startDay;
    public String user_id,user_phone_number,date_delivery,time_delivery,market_id,address_long="0",address_lat="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_address_and_time);
        dates=(TextView)findViewById(R.id.Currentdate);
        carrentTime=(TextView)findViewById(R.id.CurrentTime);
        buttonSend=(Button)findViewById(R.id.buttonSend);
        pd = new ProgressDialog(this);
        sqLiteDB=new SQLiteDB(this);
        userDatas=new UserDatas(this);
        sqLiteDB.open();
        String startYears=new SimpleDateFormat("yyyy").format(new java.util.Date());
        String starthMonths=new SimpleDateFormat("MM").format(new java.util.Date());
        String startDays=new SimpleDateFormat("dd").format(new java.util.Date());
        Calendar mcurrentTimes = Calendar.getInstance();
        int hour = mcurrentTimes.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTimes.get(Calendar.MINUTE);

        carrentTime.setText(hour+":"+minute);
        carrentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(GetAddressAndTimeDelivery.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        carrentTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Выберите время доставки товаров");
                mTimePicker.show();
            }
        });
        startYear=Integer.parseInt(startYears);
        starthMonth=Integer.parseInt(starthMonths);
        startDay=Integer.parseInt(startDays);
        dates.setText(startDays+"/"+starthMonths+"/"+startYears);

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePiPicker");
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address_long.equals("0") || address_lat.equals("0")){
                    Toast.makeText(GetAddressAndTimeDelivery.this,"Укажите место доставки на карте",Toast.LENGTH_LONG).show();
                }
                else {
                    pd.show();
                    user_id = userDatas.getId() + "";
                    user_phone_number = userDatas.getTellNumber();
                    date_delivery = dates.getText().toString();
                    time_delivery = carrentTime.getText().toString();
                    market_id = userDatas.getMatketId();
                    data = data + "[";
                    basket = sqLiteDB.getProducts(userDatas.getMatketId());
                    for (int i = 0; i < basket.size(); i++) {
                        final Basket baskets = basket.get(i);
                        data = data + "{\"barcode\":\"" + baskets.getBarcode() + "\",";
                        data = data + "\"quantity\":\"" + baskets.getCount() + "\",";
                        data = data + "\"type\":\"" + baskets.getType() + "\",";
                        data = data + "\"typename\":\"" + baskets.getType_name() + "\",";
                        data = data + "\"price\":\"" + baskets.getPrice() + "\",";
                        String name = baskets.getName();
                        char[] myNameChars = name.toCharArray();
                        char a = '"';
                        for (int j = 0; j < name.length(); j++) {
                            if (name.charAt(j) == a) {
                                myNameChars[j] = '\'';
                            }
                        }
                        name = String.valueOf(myNameChars);
                        data = data + "\"name\":\"" + name + "\",";
                        if (i == basket.size() - 1)
                            data = data + "\"photo_url\":\"" + baskets.getImage_1_url() + "\"}";
                        else
                            data = data + "\"photo_url\":\"" + baskets.getImage_1_url() + "\"},";
                    }
                    data = data + "]";
                    try {
                        sendDataToServer();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("datadata", data);
                }
            }
        });

        initMap();
    }
    private void sendDataToServer() throws JSONException {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", user_id);
            jsonBody.put("user_phone_number", user_phone_number);
            jsonBody.put("date_delivery", date_delivery);
            jsonBody.put("time_delivery", time_delivery);
            jsonBody.put("market_id", market_id);
            jsonBody.put("address_long", address_long);
            jsonBody.put("address_lat", address_lat);
            jsonBody.put("data", data);

            final String mRequestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL_SEND_ORDER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(GetAddressAndTimeDelivery.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    String json = null;
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        Log.i("ResponseString", responseString);
                    }
                    switch(response.statusCode){
                        case 200:
                            json = new String(response.data);

                                try {
                                    parseData(json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pd.dismiss();



                            break;
                        case 404:
                            pd.dismiss();
                            Toast.makeText(GetAddressAndTimeDelivery.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
                            break;
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseData(String data) throws JSONException {
        JSONArray array = new JSONArray(data);
        for(int i = 0; i<array.length(); i++) {
            JSONObject json = null;
            try {
                String status="";
                json = array.getJSONObject(i);
                Log.i("0123456789",json.getString(Config.TAG_STATUS));
                status=json.getString(Config.TAG_STATUS);
                if(status.equals("success_inserted")){
                    pd.dismiss();

                           Intent intent=new Intent(GetAddressAndTimeDelivery.this,MainMenuActivity.class);
                            startActivity(intent);
                }
                else{
                    pd.dismiss();
                 Intent intent=new Intent(GetAddressAndTimeDelivery.this,BasketFragment.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragmentDelivery);
        DisplayMetrics dm;
        dm= new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heigth=dm.heightPixels;
        ViewGroup.LayoutParams parms = mapFragment.getView().getLayoutParams();
        parms.height=(heigth*68)/100;
        mapFragment.getView().setLayoutParams(parms);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        goToLocationZoom(43.235295, 76.909390, 12);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.clear();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("Место доставки");

                mGoogleMap.addMarker(marker);
                address_long=latLng.longitude+"";
                address_lat=latLng.latitude+"";
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.clear();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("Место доставки");

                mGoogleMap.addMarker(marker);
               address_long=latLng.longitude+"";
                address_lat=latLng.latitude+"";
            }
        });
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {




        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, startYear, starthMonth-1, startDay);
           dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            startDay=day;
            starthMonth=month+1;

            startYear=year;
            if(starthMonth<10)
                dates.setText(startDay+"/0"+starthMonth+"/"+startYear);
            else
                dates.setText(startDay+"/"+starthMonth+"/"+startYear);

        }
    }


}
