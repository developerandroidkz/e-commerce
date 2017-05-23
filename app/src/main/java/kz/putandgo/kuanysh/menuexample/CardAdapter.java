package kz.putandgo.kuanysh.menuexample;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kuanysh on 22.03.2017.
 */

class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements View.OnClickListener {
    private CheckBox basket;
    private ImageLoader imageLoader;
    private Context context;
    private RecyclerView recyclerView;
    private SQLiteDB sqLiteDB;
    //List of superHeroes
    List<Products> productsList;

    public CardAdapter(List<Products> productsList, Context context){
        super();
        //Getting all the superheroes
        this.productsList = productsList;
        this.context = context;
        sqLiteDB=new SQLiteDB(context);
        sqLiteDB.open();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Products products =  productsList.get(position);
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(products.getImage_1_url(), ImageLoader.getImageListener(holder.imageView, R.drawable.spinningwheel, android.R.drawable.ic_dialog_alert));
        holder.imageView.setImageUrl(products.getImage_1_url(), imageLoader);
        holder.textViewName.setText(products.getName());
        String a=products.getNik_name();
        holder.priceText.setText("Цена за "+products.getType()+" "+products.getType_name()+".");
        holder.PriceTG.setText(products.getPrice()+"тг.");
        if(sqLiteDB.isHave(products.getBarcode(),products.getMarket_id(),products.getPrice())){
            basket.setChecked(true);
        }
        else{
            basket.setChecked(false);
        }
        basket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int type=Integer.parseInt(products.getType());
                    int markId=Integer.parseInt(products.getMarket_id());
                    int pricee=Integer.parseInt(products.getPrice());
                    String a=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

                    sqLiteDB.addproduct(products.getProduct_id(),products.getBarcode()
                    ,products.getName(),products.getNik_name(),products.getDescription()
                    ,type,products.getType_name(),products.getMassa_netto(),products.getMassa_brutto(),products.getManufacturer_name()
                    ,products.getSposobiy_hranenie(),products.getImage_1_url(),products.getImage_2_url(),products.getImage_3_url(),products.getImage_4_url(),markId,a,pricee,1);
                    Snackbar sc= Snackbar.make(buttonView, products.getName()+" добавлено в корзину", Snackbar.LENGTH_LONG).setAction("Action", null);
                    View SnacView=sc.getView();
                    SnacView.setBackgroundColor(ContextCompat.getColor(context,R.color.uspewno));
                    sc.show();
                }
                else{
                    sqLiteDB.isDeletedInDb(products.getBarcode(),products.getMarket_id());
                    Snackbar sc= Snackbar.make(buttonView, products.getName()+" удалено из корзины", Snackbar.LENGTH_LONG).setAction("Action", null);
                    View SnacView=sc.getView();
                    SnacView.setBackgroundColor(ContextCompat.getColor(context,R.color.neuspewno));
                    sc.show();
                }
            }
        });
        if(a.length()>60){
            a = a.substring(0,60) + "";
            holder.textViewDetails.setText(a);
            holder.textViewDetails.append("...");
        }
        else{
            holder.textViewDetails.setText(products.getNik_name());
        }

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView textViewName,textViewDetails,priceText,PriceTG;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDetails=(TextView) itemView.findViewById(R.id.textViewDetails);
            priceText=(TextView)itemView.findViewById(R.id.priceText);
            PriceTG=(TextView)itemView.findViewById(R.id.priceTG);
            basket=(CheckBox)itemView.findViewById(R.id.cart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,DetailActivity.class);
                    int a=0;
                    final String ProductId=productsList.get(getPosition()).getProduct_id();
                    final String Barcode=productsList.get(getPosition()).getBarcode();
                    final String Name=productsList.get(getPosition()).getName();
                    final String Nik_name=productsList.get(getPosition()).getNik_name();
                    final String Description=productsList.get(getPosition()).getDescription();
                    final String Type=productsList.get(getPosition()).getType();
                    final String Type_name=productsList.get(getPosition()).getType_name();
                    final String Massa_netto=productsList.get(getPosition()).getMassa_netto();
                    final String Massa_brutto=productsList.get(getPosition()).getMassa_brutto();
                    final String Razdel_id=productsList.get(getPosition()).getRazdel_id();
                    final String Manufacturer_name=productsList.get(getPosition()).getManufacturer_name();
                    final String Sposobiy_hranenie=productsList.get(getPosition()).getSposobiy_hranenie();
                    final String image_1_url=productsList.get(getPosition()).getImage_1_url();
                    if(!image_1_url.equals("null")){
                        a++;
                    }
                    final String image_2_url=productsList.get(getPosition()).getImage_2_url();
                    if(!image_2_url.equals("null")){
                        a++;
                    }
                    final String image_3_url=productsList.get(getPosition()).getImage_3_url();
                    if(!image_3_url.equals("null")){
                        a++;
                    }
                    final String image_4_url=productsList.get(getPosition()).getImage_4_url();
                    if(!image_4_url.equals("null")){
                        a++;
                    }
                    final String market_id=productsList.get(getPosition()).getMarket_id();
                    final String price=productsList.get(getPosition()).getPrice();
                    i.putExtra("Size",a);
                    i.putExtra("ProductId",ProductId);
                    i.putExtra("Barcode",Barcode);
                    i.putExtra("Name",Name);
                    i.putExtra("Nik_name",Nik_name);
                    i.putExtra("Description",Description);
                    i.putExtra("Type",Type);
                    i.putExtra("Type_name",Type_name);
                    i.putExtra("Massa_netto",Massa_netto);
                    i.putExtra("Massa_brutto",Massa_brutto);
                    i.putExtra("Razdel_id",Razdel_id);
                    i.putExtra("Manufacturer_name",Manufacturer_name);
                    i.putExtra("Sposobiy_hranenie",Sposobiy_hranenie);
                    i.putExtra("image_1_url",image_1_url);
                    i.putExtra("image_2_url",image_2_url);
                    i.putExtra("image_3_url",image_3_url);
                    i.putExtra("image_4_url",image_4_url);
                    i.putExtra("market_id",market_id);
                    i.putExtra("price",price);
                    context.startActivity(i);
                }
            });

        }


    }
}