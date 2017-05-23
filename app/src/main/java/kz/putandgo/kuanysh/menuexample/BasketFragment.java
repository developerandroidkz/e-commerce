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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

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
 * Created by Kuanysh on 24.03.2017.
 */

public class BasketFragment extends Fragment {
    List<Products> productsList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapterBasket adapter;
    private Parcelable recyclerViewState;
    String marketid="2";
    private Button summa;
    private SQLiteDB sqLiteDB;
    private  UserDatas userDatas;
    int pages=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pages=1;
        View view = inflater.inflate(R.layout.basket, container, false);
        getActivity().setTitle(getResources().getString(R.string.basket));
        userDatas=new UserDatas(getContext());
        marketid=userDatas.getMatketId();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBasket);
        summa=(Button)view.findViewById(R.id.summa);
        summa.setText("Посчитать сумму всего товара");

        summa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            summa.setText("Сумма: "+sqLiteDB.summa(marketid));

            }
        });
        summa.setCompoundDrawablesWithIntrinsicBounds(kz.putandgo.kuanysh.menuexample.R.drawable.reload,0,0,0);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(),1);
        layoutManager.onSaveInstanceState();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
       sqLiteDB=new SQLiteDB(getContext());
        sqLiteDB.open();
        adapter = new CardAdapterBasket(sqLiteDB.getProducts(marketid), getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();

    }
    public void onResume() {
        super.onResume();

    }


}