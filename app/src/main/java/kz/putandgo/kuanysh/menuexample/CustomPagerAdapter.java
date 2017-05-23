package kz.putandgo.kuanysh.menuexample;

/**
 * Created by Kuanysh on 23.03.2017.
 */
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    Activity activity;
    String[] images;
    LayoutInflater layoutInflater;


    public CustomPagerAdapter(Activity activity, String[] resources) {
        this.activity=activity;
        layoutInflater=(LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images=resources;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = layoutInflater.inflate(R.layout.pager_item,container,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.slideShow);
        DisplayMetrics dm;
        dm= new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heigth=(dm.heightPixels*50)/100;
        int width=dm.widthPixels;

//        imageView.setMaxHeight(50);
//        imageView.setMaxHeight();Width(50);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,heigth);
        imageView.setLayoutParams(parms);
        try {
            Picasso.with(activity.getApplicationContext())
                    .load(images[position])
                    .placeholder(R.drawable.spinningwheel)
                    .error(R.drawable.net_foto)
                    .into(imageView);
        }
        catch (Exception ex){

        }
           /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(950, 950);
            imageView.setLayoutParams(layoutParams);*/
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}