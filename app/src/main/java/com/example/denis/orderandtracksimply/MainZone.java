package com.example.denis.orderandtracksimply;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.denis.orderandtracksimply.fragments.fragmentNewOrder;
import com.example.denis.orderandtracksimply.fragments.fragmentOrders;
import com.example.denis.orderandtracksimply.fragments.fragmentAboutUs;
import com.example.denis.orderandtracksimply.fragments.fragmentSettlements;

public class MainZone extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    fragmentNewOrder fNewOrder;
    fragmentOrders fOrders;
    fragmentAboutUs fAboutUs;
    fragmentSettlements fSettlements;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fonts init
        Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        Typeface medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        // making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        // getting login data
        intent = getIntent();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        // using login info in ND
        String phone = intent.getStringExtra("phone");
        String e_mail = intent.getStringExtra("email");

        // getting element and filling info into
        View hView =  navigationView.getHeaderView(0);
        TextView user_id = (TextView) hView.findViewById(R.id.user_id);
        ImageView login_type = (ImageView) hView.findViewById(R.id.login_type);

        // setting login type logo
        if (!phone.equals("empty"))
        {
            user_id.setText(phone);
            login_type.setImageResource(R.drawable.phone);
        }
        else
            user_id.setText(e_mail);

        user_id.setTypeface(regular);

        // catching fragments
        fNewOrder = new fragmentNewOrder();
        fOrders = new fragmentOrders();
        fSettlements = new fragmentSettlements();
        fAboutUs = new fragmentAboutUs();

        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    // container select onclick
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_new_order)
        {
            fragmentTransaction.replace(R.id.container, fNewOrder);
        }
        else if (id == R.id.nav_orders)
        {
            fragmentTransaction.replace(R.id.container, fOrders);
        }
        else if (id == R.id.nav_settlements)
        {
            fragmentTransaction.replace(R.id.container, fSettlements);
        }
        else if (id == R.id.nav_about_us)
        {
            fragmentTransaction.replace(R.id.container, fAboutUs);
        }
        else if (id == R.id.nav_exit)
        {
            // exit
            intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
