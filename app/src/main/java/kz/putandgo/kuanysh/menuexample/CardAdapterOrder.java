package kz.putandgo.kuanysh.menuexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Kuanysh on 30.03.2017.
 */

public class CardAdapterOrder  extends RecyclerView.Adapter<CardAdapterOrder.ViewHolder> implements View.OnClickListener {
    private CheckBox basket;
    private Context context;
    private RecyclerView recyclerView;
    List<Order> orderList;
    String checkOut="";
    int summa=0;
    public CardAdapterOrder(List<Order> orderList, Context context){
        super();
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public CardAdapterOrder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        CardAdapterOrder.ViewHolder viewHolder = new CardAdapterOrder.ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CardAdapterOrder.ViewHolder holder, int position) {
        final Order orders =  orderList.get(position);
        try {
            parseData(orders.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.data.setText(checkOut);
        checkOut="";
        summa=0;
        if(!orders.getStatus().equals("0")){
            holder.inserted.setChecked(true);
        }
        if(!orders.getMerchandiser().equals("0")){
            holder.merchandiser.setChecked(true);
        }
        if(!orders.getCourier().equals("0")){
            holder.courier.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView data;
        public CheckBox inserted,merchandiser,courier,delivered;

        public ViewHolder(final View itemView) {
            super(itemView);
            data=(TextView)itemView.findViewById(R.id.checkOrder) ;
            inserted=(CheckBox)itemView.findViewById(R.id.inserted);
            merchandiser=(CheckBox)itemView.findViewById(R.id.merchaindaiser);
            courier=(CheckBox)itemView.findViewById(R.id.courier);
            delivered=(CheckBox)itemView.findViewById(R.id.delivered);

        }


    }
    private void parseData(String data) throws JSONException {
        JSONArray array = new JSONArray(data);
        int tp,quantity,price,counter;
        String name,type_name;

        for(int i = 0; i<array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                name=json.getString(Config.TAG_PRODUCT_NAME);
                tp=json.getInt(Config.TAG_PRODUCT_TYPE);
                type_name=json.getString(Config.TAG_ORDER_TYPE_NAME);
                quantity=json.getInt(Config.TAG_ORDER_QUANTITY);
                price=json.getInt(Config.TAG_PRODUCT_PRICE);

                counter=(quantity/tp)*price;
                summa=summa+counter;
                checkOut=checkOut+(i+1)+". "+name+"\n     "+tp+" "+type_name+" x "+quantity+" = "+counter+" тг.\n";
                if(i==(array.length()-1)){
                    checkOut=checkOut+"\n Итого: "+summa+" тг.";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}