package kz.putandgo.kuanysh.menuexample;

/**
 * Created by Kuanysh on 19.02.2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapMarketList extends Activity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mGoogleMap;

    GoogleApiClient mGoogleApiClient;
    double lat, lng;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    String[] MARKET_NAME = {"Magnum", "Test"};
    String[] MARKET_ID = {"1", "2"};
    String[] MARKET_ADDRES = {"г. Алматы, Алмагуль микрорайон, 18а", "г. Алматы, Манаса, 34а Жандосова, 8а"};
    String[] MARKET_latitude = {"43.205924", "43.235201"};
    String[] MARKET_longitude = {"76.903402", "76.909434"};
    String[] MARKET_IMAGE_URL = {"http://putandgo.000webhostapp.com/image/market/ramstor.png", "http://putandgo.000webhostapp.com/image/market/test.png"};
    String[] MARKET_mode = {"Ежедневно 08:00–23:00", "По будням 09:00–18:00"};
    Spinner spinnerColorChange;
    Button b;
    private  UserDatas userDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL_GET_MARKET_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Log.i("*/*/*/*/*/*/*/", "it is ok");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = null;
                            try {
                                json = array.getJSONObject(i);
                                Log.i("00000000000000000000", json.getString(Config.TAG_MARKET_NAME));
                                MARKET_NAME[i] = json.getString(Config.TAG_MARKET_NAME);
                                MARKET_ID[i] = json.getString(Config.TAG_MARKET_ID);
                                MARKET_ADDRES[i] = json.getString(Config.TAG_MARKET_ADDRES);
                                MARKET_latitude[i] = json.getString(Config.TAG_MARKET_latitude);
                                MARKET_longitude[i] = json.getString(Config.TAG_MARKET_longitude);
                                MARKET_mode[i] = json.getString(Config.TAG_MARKET_MODE);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("=-=-=-=-=-=-=-=", "it is not ok");

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        if (googleServicesAvailable()) {
            userDatas=new UserDatas(this);
            setContentView(R.layout.map_market_list);

            initMap();

            spinnerColorChange = (Spinner) findViewById(R.id.spinner1);
            b=(Button)findViewById(R.id.buttonNext);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MARKET_NAME);
            dataAdapter.setDropDownViewResource(R.layout.custom_textview_to_spinner);
            spinnerColorChange.setAdapter(dataAdapter);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer i = (int) (long) spinnerColorChange.getSelectedItemId();
                    userDatas.markedID(MARKET_ID[i]);
                    Intent intent=new Intent(MapMarketList.this,MainMenuActivity.class);
                    startActivity(intent);

                }
            });
            spinnerColorChange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub



                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                    }

                    try {
                        lat = Double.parseDouble(MARKET_latitude[spinnerColorChange.getSelectedItemPosition()]);
                        lng = Double.parseDouble(MARKET_longitude[spinnerColorChange.getSelectedItemPosition()]);
                    } catch (NumberFormatException e) {
                        Log.e("eroor", e.getMessage());
                    }
                    //Place current location marker
                    LatLng latLng = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(MARKET_NAME[spinnerColorChange.getSelectedItemPosition()]);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                    //move map camera
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                    mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            View v = getLayoutInflater().inflate(R.layout.infor_window, null);
                            ImageView image=(ImageView)v.findViewById(R.id.imageView1);
                            TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                            TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                            TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                            TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);
                            Picasso.with(MapMarketList.this)
                                    .load(MARKET_IMAGE_URL[spinnerColorChange.getSelectedItemPosition()])
                                    .resize(100,100).into(image);
                            LatLng ll = marker.getPosition();
                            tvLocality.setText(marker.getTitle());
                            tvLat.setText(MARKET_ADDRES[spinnerColorChange.getSelectedItemPosition()]);
                            tvLng.setText(MARKET_mode[spinnerColorChange.getSelectedItemPosition()]);
                            tvSnippet.setText(marker.getSnippet());

                            return v;
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

        } else {

        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        goToLocationZoom(43.235295, 76.909390, 12);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
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

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }
    @Override
    protected void onStart(){
    super.onStart();
//        mGoogleApiClient.connect();

    }

}
