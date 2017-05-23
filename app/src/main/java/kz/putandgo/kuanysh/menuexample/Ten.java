package kz.putandgo.kuanysh.menuexample;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kuanysh on 22.03.2017.
 */

public class Ten extends Fragment {
    List<Products> productsList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapter adapter;
    private Parcelable recyclerViewState;
    private  UserDatas userDatas;
    int pages=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pages=1;
        View view = inflater.inflate(R.layout.first, container, false);
        getActivity().setTitle(getResources().getString(R.string.tenth));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewFirst);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(),1);
        layoutManager.onSaveInstanceState();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        userDatas=new UserDatas(getContext());
        getData();
        return view;

    }

    private void getData(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL_GET_PRODUCT+userDatas.getMatketId()+"/10",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(":","eroor");

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            Products products = new Products();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                products.setProduct_id((json.getString(Config.TAG_PRODUCT_ID)));
                products.setBarcode(json.getString(Config.TAG_PRODUCT_ID));
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            productsList.add(products);
        }
        recyclerView.removeAllViewsInLayout();
        adapter = new CardAdapter(productsList, getContext());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    public void onResume() {
        super.onResume();

    }


}