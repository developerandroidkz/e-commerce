package kz.putandgo.kuanysh.menuexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Kuanysh on 30.03.2017.
 */

public class Scanner extends AppCompatActivity {
    public  FloatingActionButton fab,oplata;
    private  ZXingScannerView scannerView;
    List<Products> productsList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapterScan adapter;
    private Parcelable recyclerViewState;
    private  UserDatas userDatas;
    ProgressDialog pd;
    SqlLiteScan sqLiteDB;
    String marketId,barcodes;
    MediaPlayer mediaPlayer;
    Button summa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_simple_scanner);
        mediaPlayer=new MediaPlayer();
        pd=new ProgressDialog(this);
        fab= (FloatingActionButton) findViewById(R.id.scansss);
        oplata=(FloatingActionButton)findViewById(R.id.end) ;
        scannerView= new ZXingScannerView(getBaseContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewScan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,1);
        layoutManager.onSaveInstanceState();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        userDatas=new UserDatas(this);
        marketId=userDatas.getMatketId();
        sqLiteDB=new SqlLiteScan(getBaseContext());
        sqLiteDB.open();

        adapter = new CardAdapterScan(sqLiteDB.getProducts(marketId), getBaseContext());
        recyclerView.setAdapter(adapter);
        summa=(Button)findViewById(R.id.summaScan);
        summa.setText("Посчитать сумму всего товара");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Дайте сканировать");
        builder.setPositiveButton("Закончить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqLiteDB.deleteAll();
                Intent intent=new Intent(Scanner.this,MainMenuActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.go_pro_dialog_layout, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        summa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summa.setText("Сумма: "+sqLiteDB.summa(marketId));

            }
        });
        summa.setCompoundDrawablesWithIntrinsicBounds(kz.putandgo.kuanysh.menuexample.R.drawable.reload,0,0,0);
        final QRCodeWriter writer = new QRCodeWriter();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {

                try {
                    BitMatrix bitMatrix = writer.encode(sqLiteDB.brutto(marketId), BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    ((ImageView)dialog.findViewById(R.id.goProDialogImage)).setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });
        oplata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(scannerView);
                scannerView.startCamera();
                scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {
                        String resultCode=result.getText();
                        barcodes=resultCode;
                        pd.show();

                       // Toast.makeText(Scanner.this,resultCode,Toast.LENGTH_LONG).show();setContentView(R.layout.activity_simple_scanner);

                        try {
                            sendDataToServer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void sendDataToServer() throws JSONException {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("marketId", marketId);
            jsonBody.put("barcode", barcodes);
            final String mRequestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DATA_URL_SCAN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(Scanner.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
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
                                if(json.equals("[]")){
                                    mediaPlayer = MediaPlayer.create(Scanner.this, R.raw.error);
                                    mediaPlayer.start();
                                }
                                try {
                                    parseData(json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pd.dismiss();

                                scannerView.stopCamera();
                                Intent intent = new Intent(Scanner.this, Scanner.class);
                                startActivity(intent);
                            }

                            pd.dismiss();
                            break;
                        case 404:
                            pd.dismiss();
                            Toast.makeText(Scanner.this,getResources().getString(R.string.wrong_data),Toast.LENGTH_LONG).show();
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
            Products products = new Products();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                products.setProduct_id((json.getString(Config.TAG_PRODUCT_ID)));
                products.setBarcode(json.getString(Config.TAG_PRODUCT_BARCODE));
                products.setName(json.getString(Config.TAG_PRODUCT_NAME));
                products.setNik_name(json.getString(Config.TAG_PRODUCT_NIK_NAME));
                products.setDescription(json.getString(Config.TAG_PRODUCT_DESCRIPTION));
                products.setType(json.getString(Config.TAG_PRODUCT_TYPE));
                products.setType_name(json.getString(Config.TAG_PRODUCT_TYPE_NAME));
                products.setMassa_netto(json.getString(Config.TAG_PRODUCT_MASSA_NETTO));
                products.setMassa_brutto(json.getString(Config.TAG_PRODUCT_MASSA_BRUTTO));
                products.setRazdel_id(json.getString(Config.TAG_PRODUCT_RAZDEL_ID));
                products.setManufacturer_name(json.getString(Config.TAG_PRODUCT_MANUFACTERER_NAME));
                products.setSposobiy_hranenie(json.getString(Config.TAG_PRODUCT_SPOSOBIY_HRANENIE));
                products.setImage_1_url(json.getString(Config.TAG_PRODUCT_IMAGE_URL_1));
                products.setImage_2_url(json.getString(Config.TAG_PRODUCT_IMAGE_URL_2));
                products.setImage_3_url(json.getString(Config.TAG_PRODUCT_IMAGE_URL3_));
                products.setImage_4_url(json.getString(Config.TAG_PRODUCT_IMAGE_URL_4));
                products.setMarket_id(json.getString(Config.TAG_PRODUCT_MARKET_ID));
                products.setPrice(json.getString(Config.TAG_PRODUCT_PRICE));
                String a=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                sqLiteDB.addproduct(json.getString(Config.TAG_PRODUCT_ID),json.getString(Config.TAG_PRODUCT_BARCODE),json.getString(Config.TAG_PRODUCT_NAME)
                ,json.getString(Config.TAG_PRODUCT_NIK_NAME),json.getString(Config.TAG_PRODUCT_DESCRIPTION),
                        json.getInt(Config.TAG_PRODUCT_TYPE),json.getString(Config.TAG_PRODUCT_TYPE_NAME),json.getString(Config.TAG_PRODUCT_MASSA_NETTO),
                        json.getString(Config.TAG_PRODUCT_MASSA_BRUTTO), json.getString(Config.TAG_PRODUCT_MANUFACTERER_NAME),
                        json.getString(Config.TAG_PRODUCT_SPOSOBIY_HRANENIE),json.getString(Config.TAG_PRODUCT_IMAGE_URL_1),
                        json.getString(Config.TAG_PRODUCT_IMAGE_URL_2),json.getString(Config.TAG_PRODUCT_IMAGE_URL3_),
                        json.getString(Config.TAG_PRODUCT_IMAGE_URL_4),json.getInt(Config.TAG_PRODUCT_MARKET_ID),a,
                        json.getInt(Config.TAG_PRODUCT_PRICE),1,marketId);
                mediaPlayer = MediaPlayer.create(Scanner.this, R.raw.success);
                mediaPlayer.start();
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
      //  scannerView.stopCamera();
    }
}