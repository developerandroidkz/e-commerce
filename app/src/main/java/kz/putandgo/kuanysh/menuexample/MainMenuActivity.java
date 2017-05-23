package kz.putandgo.kuanysh.menuexample;

import android.content.Intent;
import android.graphics.Color;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public  NavigationView navigationView;
    public  Menu menuNav;
    public   int i=5;
    public  FloatingActionButton fab,fabSend;
    public CircleImageView userPhoto;
        public TextView nameofUser,nikname,emailOfUser;
    public   Fragment fragment;
    private  UserDatas userDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDatas=new UserDatas(this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragment = new First();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();


        fab= (FloatingActionButton) findViewById(R.id.fab);
        fabSend = (FloatingActionButton) findViewById(R.id.fabSend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new BasketFragment();
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    fab.hide();
                    fabSend.show();
                }

            }
        });
        fab.show();
        fabSend.hide();
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainMenuActivity.this,GetAddressAndTimeDelivery.class);
                startActivity(intent);
          //        Snackbar.make(v, "sdfsdsdf", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
         menuNav = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        userPhoto=(CircleImageView)hView.findViewById(R.id.profile_image);
        nameofUser=(TextView)hView.findViewById(R.id.nameOfUser);
        nameofUser.setText(userDatas.getName());
        emailOfUser=(TextView)hView.findViewById(R.id.emailOfUser);
        emailOfUser.setText(userDatas.getTellNumber());
            Picasso.with(MainMenuActivity.this)
                .load(Config.DATA_URL_USER_IMAGE+userDatas.getId()+".jpg")
                .error(R.drawable.user)
                .placeholder(R.drawable.spinningwheel)
                .into(userPhoto);
        //.resize(100,100).into(userPhoto)
        Log.i("aaaaaaa",userDatas.getId()+"");
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            i++;

            drawer.closeDrawer(GravityCompat.START);
        } else {
            i--;
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;
       if (id == R.id.first) {
           fab.show();
           fabSend.hide();
           fragment = new First();
        } else if (id == R.id.second) {
           fab.show();
           fabSend.hide();
            fragment=new Second();
        } else if (id == R.id.third) {
           fab.show();
           fabSend.hide();
           fragment=new Third();
        } else if (id == R.id.fourth) {
           fab.show();
           fabSend.hide();
           fragment=new Four();
        } else if (id == R.id.fifth) {
           fab.show();
           fabSend.hide();
           fragment=new Fifth();
        } else if (id == R.id.sixth) {
           fab.show();
           fabSend.hide();
           fragment=new Sixth();
        } else if (id == R.id.seventh) {
           fab.show();
           fabSend.hide();
           fragment=new Seven();
        } else if (id == R.id.eigth) {
           fab.show();
           fabSend.hide();
           fragment=new Eigth();
        } else if (id == R.id.ninth) {
           fab.show();
           fabSend.hide();
           fragment=new Nine();
        } else if (id == R.id.tenth) {
           fab.show();
           fabSend.hide();
           fragment=new Ten();
        } else if (id == R.id.eleventh) {
           fab.show();
           fabSend.hide();
           fragment=new Eleven();
        }else if (id == R.id.history) {
           fab.show();
           fabSend.hide();
           fragment = new History();
       }
        else if (id == R.id.twelfth) {
           fab.show();
           fabSend.hide();
           fragment=new Twelf();
        } else if (id == R.id.thirteenth) {
           fab.show();
           fabSend.hide();
           fragment=new Thirteen();
        } else if (id == R.id.to_basket) {

           fab.hide();
           fabSend.show();
           fragment = new BasketFragment();
        } else if (id == R.id.settings) {
           fab.show();
           fabSend.hide();
           fragment = new Settings();
        }
       else if (id == R.id.changeMarket) {
        Intent intent = new Intent(MainMenuActivity.this,MapMarketList.class);
           startActivity(intent);
       }else if (id == R.id.barcode) {
           Intent intent = new Intent(MainMenuActivity.this,Scanner.class);
           startActivity(intent);
       }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
