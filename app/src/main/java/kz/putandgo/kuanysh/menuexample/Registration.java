package kz.putandgo.kuanysh.menuexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Created by Kuanysh on 16.03.2017.
 */

public class Registration extends AppCompatActivity {
    TextView to_login;
    EditText reg_name,reg_email,reg_number,reg_password;
    Button reg_button;
    CheckBox checkBox;
    String Username,email,number,password;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        to_login=(TextView)findViewById(R.id.to_login); //the textView for view login activity
        reg_name=(EditText)findViewById(R.id.reg_name); // the Edit text variable to get name which user write
        reg_email=(EditText)findViewById(R.id.reg_email);
        reg_number=(EditText)findViewById(R.id.reg_number);
        reg_password=(EditText)findViewById(R.id.reg_password);
        reg_button=(Button)findViewById(R.id.reg_button);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        reg_button.setEnabled(false);
        pd = new ProgressDialog(this);
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//this method will be open the login activity when user clicked to_login TextView
                Intent to_login=new Intent(Registration.this,Login.class);
                startActivity(to_login);

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    reg_button.setEnabled(true);
                    String url = "https://putandgo.000webhostapp.com/Terms_of_use.pdf";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }else{
                    reg_button.setEnabled(false);
                }
            }



        });
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if(reg_password.getText().length()>6){
                Username=reg_name.getText().toString();
                email=reg_email.getText().toString();
                number=reg_number.getText().toString();
                password=reg_password.getText().toString();
                    try {
                        sendDataToServer();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Snackbar sc= Snackbar.make(v, getResources().getString(R.string.passwordCheck), Snackbar.LENGTH_LONG).setAction("Action", null);
                    View SnacView=sc.getView();
                    SnacView.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.neuspewno));
                    sc.show();
                    reg_password.setError(getResources().getString(R.string.login_password_error));
                }
            }
        });
    }
    private void sendDataToServer() throws JSONException {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", Username);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("phone", number);
            final String mRequestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(Registration.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
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
                                Intent intent = new Intent(Registration.this, Login.class);
                                startActivity(intent);
                            pd.dismiss();
                            break;
                        case 404:
                            pd.dismiss();
                            Toast.makeText(Registration.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
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
                if(status.equals("user_allready_exist")){
                    Toast.makeText(Registration.this,getResources().getString(R.string.already_exist),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Registration.this,ForgotPassword.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(Registration.this,Login.class);
                    startActivity(intent);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
