package kz.putandgo.kuanysh.menuexample;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
 * Created by Kuanysh on 29.03.2017.
 */

public class History extends Fragment {
    List<Order> orderList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapterOrder adapter;
    private Parcelable recyclerViewState;
    private  UserDatas userDatas;
    private Order order;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history, container, false);
        getActivity().setTitle(getResources().getString(R.string.history));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewHistory);
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL_GET_ORDER+userDatas.getId(),
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
            Order orders = new Order();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                orders.setOrder_id((json.getString(Config.TAG_ORDER_ID)));
                orders.setDate_delivery(json.getString(Config.TAG_ORDER_DATE_DELIVERY));
                orders.setTime_delivery(json.getString(Config.TAG_ORDER_TIME));
                orders.setMarket_id(json.getString(Config.TAG_ORDER_MARKET_ID));
                orders.setDate_inserted(json.getString(Config.TAG_ORDER_DATE_INSERTED));
                orders.setData(json.getString(Config.TAG_ORDER_DATA));
                orders.setStatus(json.getString(Config.TAG_ORDER_STATUS));
                orders.setMerchandiser(json.getString(Config.TAG_ORDER_MERCHENDAISER));
                orders.setCourier(json.getString(Config.TAG_ORDER_COURIER));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            orderList.add(orders);
        }
        recyclerView.removeAllViewsInLayout();
        adapter = new CardAdapterOrder(orderList, getContext());
        recyclerView.setAdapter(adapter);
    }
}
