package kz.putandgo.kuanysh.menuexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Kuanysh on 22.03.2017.
 */

public class DetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener  {
    TextView descriptionOfProduct,nikNameOfProduct,nettoOfProduct,sposobHranenie,manufacturerNameOfProduct;

    Button PriceButton;
    ViewPager mViewPager;
    private CustomPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.toBasket);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        descriptionOfProduct=(TextView)findViewById(R.id.descriptionOfProduct);
        nikNameOfProduct=(TextView)findViewById(R.id.NikName);
        nettoOfProduct=(TextView)findViewById(R.id.nettoOfProduct);
        sposobHranenie=(TextView)findViewById(R.id.SposobHranenieOfProduct);
        manufacturerNameOfProduct=(TextView)findViewById(R.id.ManufacturerNameOfProduct);
        PriceButton=(Button)findViewById(R.id.priceButton);
         Intent i =DetailActivity.this.getIntent();
         String ProductId=i.getExtras().getString("ProductId");
         String Barcode=i.getExtras().getString("Barcode");
         String Name=i.getExtras().getString("Name");
         DetailActivity.this.setTitle(Name);

         String Nik_name=i.getExtras().getString("Nik_name");
        nikNameOfProduct.setText(Nik_name);
         String Description=i.getExtras().getString("Description");
        descriptionOfProduct.setText(Description);
         String Type=i.getExtras().getString("Type");
         String Type_name=i.getExtras().getString("Type_name");
         String Massa_netto=i.getExtras().getString("Massa_netto");
        nettoOfProduct.setText(Massa_netto+" гр.");
         String Massa_brutto=i.getExtras().getString("Massa_brutto");
         String Razdel_id=i.getExtras().getString("Razdel_id");
         String Manufacturer_name=i.getExtras().getString("Manufacturer_name");
        manufacturerNameOfProduct.setText(Manufacturer_name);
         String Sposobiy_hranenie=i.getExtras().getString("Sposobiy_hranenie");
        sposobHranenie.setText(Sposobiy_hranenie);
         String image_1_url=i.getExtras().getString("image_1_url");
         String image_2_url=i.getExtras().getString("image_2_url");
         String image_3_url=i.getExtras().getString("image_3_url");
         String image_4_url=i.getExtras().getString("image_4_url");
         String market_id=i.getExtras().getString("market_id");

         String price=i.getExtras().getString("price");
        String priceBottom="Цена за "+Type+" "+Type_name+": "+price+" тг.";
        PriceButton.setText(priceBottom);
        int a=i.getExtras().getInt("Size");
        String[] images= new String[a];
        for(int w=0;w<a;w++){
            String f="image_"+(w+1)+"_url";
            images[w]=i.getExtras().getString(f);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new CustomPagerAdapter(DetailActivity.this, images);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(this);
        setPageViewIndicator();
    }
    private void setPageViewIndicator() {

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Log.d("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {

        } else {

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}