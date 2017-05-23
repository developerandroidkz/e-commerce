package kz.putandgo.kuanysh.menuexample;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Kuanysh on 16.02.2017.
 */

    public class Login extends AppCompatActivity {
    private String PhoneNumber="";
    private String Password="";
    EditText password;
    Button login;
    TextView to_regitration, to_forgot;
    ProgressDialog pd;
    private  UserDatas userDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        userDatas=new UserDatas(this);
        if(!userDatas.checkLoginValidate()){
            setContentView(R.layout.login);
        }else{
            Intent intent = new Intent(Login.this, MapMarketList.class);
            startActivity(intent);
        }
        to_regitration=(TextView)findViewById(R.id.textView2);
        to_forgot=(TextView)findViewById(R.id.textView);
        password=(EditText)findViewById(R.id.editText3);
        final MaskedEditText m = (MaskedEditText)findViewById(R.id.editText2);
        login=(Button)findViewById(R.id.button);
        pd = new ProgressDialog(this);
        to_regitration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });
        to_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                PhoneNumber=m.getText(false).toString();
                Password=password.getText().toString();
                if(isInternetAvailable()){//If user have access to Internet
                    if(Password.length()<=6){
                        password.setError(getResources().getString(R.string.login_password_error));
                    }else{
                        try {
                            sendDataToServer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{ //if user don't have access to Internet
                    showDialog(1);
                }
            }
        });
    }
    private void sendDataToServer() throws JSONException {
        try {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("PhoneNumber", PhoneNumber);
        jsonBody.put("Password", Password);
        final String mRequestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Login.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
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
                            if(!json.equals("not_success")){
                                Log.i("DATA", json);
                                try {
                                    parseData(json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pd.dismiss();
                            Intent intent = new Intent(Login.this, MapMarketList.class);
                            startActivity(intent);
                                }
                                else{
                                Toast.makeText(Login.this,getResources().getString(R.string.login_password_error),Toast.LENGTH_LONG).show();
                            }
                        pd.dismiss();
                        break;
                    case 404:
                        pd.dismiss();
                        Toast.makeText(Login.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
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
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // title for alert dialog
            adb.setTitle(getResources().getString(R.string.login_pd_title));
            // the body of alert dialog, message
            adb.setMessage(getResources().getString(R.string.login_pd_message));
            // button OK
            adb.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    public boolean isInternetAvailable() {// for checkin Internet connection
        ConnectivityManager connectivityManager=(ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void parseData(String data) throws JSONException {
        JSONArray array = new JSONArray(data);
        Integer id;
        String username,tellnumber,email, is_active,date_registrated;

        for(int i = 0; i<array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                id=json.getInt(Config.TAG_ID);
                Log.i("ididid",id+"");
                username=json.getString(Config.TAG_USERNAME);
                tellnumber=json.getString(Config.TAG_TELL_NUMBER);
                email=json.getString(Config.TAG_EMAIL);
                Log.i("*/*/*/*/*/*/*/*/*/*/*",email);
                is_active=json.getString(Config.TAG_IS_ACTIVE);
                date_registrated=json.getString(Config.TAG_DATE_REGISTRATED);
                userDatas.registerUser(id,username,tellnumber,email,is_active,date_registrated);
                Log.i("username",username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Login.this,Login.class);
        startActivity(i);
        super.onBackPressed();
    }
}
