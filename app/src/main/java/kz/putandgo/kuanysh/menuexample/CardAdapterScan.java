package kz.putandgo.kuanysh.menuexample;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Kuanysh on 30.03.2017.
 */

public class CardAdapterScan   extends RecyclerView.Adapter<CardAdapterScan.ViewHolder> implements View.OnClickListener {
    private CheckBox baskets;
    private ImageLoader imageLoader;
    private Context context;
    private RecyclerView recyclerView;
    private SqlLiteScan sqLiteDB;
    //List of superHeroes
    List<Basket> basket;

    public CardAdapterScan(List<Basket> basket, Context context){
        super();
        //Getting all the superheroes
        this.basket = basket;
        this.context = context;
        sqLiteDB=new SqlLiteScan(context);
        sqLiteDB.open();
    }

    @Override
    public CardAdapterScan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_basket, parent, false);
        CardAdapterScan.ViewHolder viewHolder = new CardAdapterScan.ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final CardAdapterScan.ViewHolder holder, final int position) {
        final Basket baskets =  basket.get(position);
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(baskets.getImage_1_url(), ImageLoader.getImageListener(holder.imageView, R.drawable.spinningwheel, android.R.drawable.ic_dialog_alert));
        holder.imageView.setImageUrl(baskets.getImage_1_url(), imageLoader);
        holder.textViewName.setText(baskets.getName());
        holder.priceText.setText("Цена за "+baskets.getType()+" "+baskets.getType_name()+".");
        holder.PriceTG.setText(baskets.getPrice()+" тг.");
        holder.count.setText(sqLiteDB.getCount(baskets.getBarcode(),baskets.getMarket_id()+""));
        holder.date.setText("Добавлено в "+baskets.getDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDB.isDeletedInDb(baskets.getBarcode(),baskets.getMarket_id()+"");
                Snackbar sc= Snackbar.make(v, baskets.getName()+" удалено из корзины", Snackbar.LENGTH_LONG).setAction("Action", null);
                View SnacView=sc.getView();
                SnacView.setBackgroundColor(ContextCompat.getColor(context,R.color.neuspewno));
                sc.show();
                basket.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,basket.size());
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=holder.count.getText().toString();
                int b=Integer.parseInt(a);
                b++;
                holder.count.setText(b+"");
                sqLiteDB.changeCount(baskets.getBarcode(),baskets.getMarket_id()+"",b+"");
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=holder.count.getText().toString();
                int b=Integer.parseInt(a);
                b--;
                if(b<0){
                    b=0;
                }
                holder.count.setText(b+"");
                sqLiteDB.changeCount(baskets.getBarcode(),baskets.getMarket_id()+"",b+"");
            }
        });
        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d=holder.count.getText().toString();

                sqLiteDB.changeCount(baskets.getBarcode(),baskets.getMarket_id()+"",d);
            }
        });
    }

    @Override
    public int getItemCount() {
        return basket.size();
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView textViewName,priceText,PriceTG,date;
        public EditText count;
        public Button plus,minus;
        public ImageButton delete;
        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewBasket);
            textViewName = (TextView) itemView.findViewById(R.id.textViewNameBasket);

            priceText=(TextView)itemView.findViewById(R.id.priceTextBasket);
            PriceTG=(TextView)itemView.findViewById(R.id.priceTGBasket);
            count=(EditText)itemView.findViewById(R.id.display);
            plus=(Button) itemView.findViewById(R.id.decrement);
            minus=(Button)itemView.findViewById(R.id.increment);
            date=(TextView)itemView.findViewById(R.id.date);
            delete=(ImageButton)itemView.findViewById(R.id.deleteProduct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*    Intent i=new Intent(context,DetailActivity.class);
                    int a=0;
                    final String ProductId=basket.get(getPosition()).getProduct_id();
                    final String Barcode=basket.get(getPosition()).getBarcode();
                    final String Name=basket.get(getPosition()).getName();
                    final String Nik_name=basket.get(getPosition()).getNik_name();
                    final String Description=basket.get(getPosition()).getDescription();
                   // final String Type=basket.get(getPosition()).getType();
                    final String Type_name=basket.get(getPosition()).getType_name();
                    final String Massa_netto=basket.get(getPosition()).getMassa_netto();
                    final String Massa_brutto=basket.get(getPosition()).getMassa_brutto();
                 //   final String Razdel_id=basket.get(getPosition()).getRazdel_id();
                    final String Manufacturer_name=basket.get(getPosition()).getManufacturer_name();
                    final String Sposobiy_hranenie=basket.get(getPosition()).getSposobiy_hranenie();
                    final String image_1_url=basket.get(getPosition()).getImage_1_url();
                    if(!image_1_url.equals("null")){
                        a++;
                    }
                    final String image_2_url=basket.get(getPosition()).getImage_2_url();
                    if(!image_2_url.equals("null")){
                        a++;
                    }
                    final String image_3_url=basket.get(getPosition()).getImage_3_url();
                    if(!image_3_url.equals("null")){
                        a++;
                    }
                    final String image_4_url=basket.get(getPosition()).getImage_4_url();
                    if(!image_4_url.equals("null")){
                        a++;
                    }
               //     final String market_id=basket.get(getPosition()).getMarket_id();
              //     final String price=basket.get(getPosition()).getPrice();
                    i.putExtra("Size",a);
                    i.putExtra("ProductId",ProductId);
                    i.putExtra("Barcode",Barcode);
                    i.putExtra("Name",Name);
                    i.putExtra("Nik_name",Nik_name);
                    i.putExtra("Description",Description);
                //    i.putExtra("Type",Type);
                    i.putExtra("Type_name",Type_name);
                    i.putExtra("Massa_netto",Massa_netto);
                    i.putExtra("Massa_brutto",Massa_brutto);
                 //   i.putExtra("Razdel_id",Razdel_id);
                    i.putExtra("Manufacturer_name",Manufacturer_name);
                    i.putExtra("Sposobiy_hranenie",Sposobiy_hranenie);
                    i.putExtra("image_1_url",image_1_url);
                    i.putExtra("image_2_url",image_2_url);
                    i.putExtra("image_3_url",image_3_url);
                    i.putExtra("image_4_url",image_4_url);
                //    i.putExtra("market_id",market_id);
               //     i.putExtra("price",price);
                    context.startActivity(i);
                    */
                }
            });

        }


    }
}
