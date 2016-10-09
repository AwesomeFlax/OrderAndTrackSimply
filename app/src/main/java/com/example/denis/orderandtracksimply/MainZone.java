package com.example.denis.orderandtracksimply;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denis.orderandtracksimply.fragments.fragmentAboutUs;
import com.example.denis.orderandtracksimply.fragments.fragmentNewOrder;
import com.example.denis.orderandtracksimply.fragments.fragmentOrders;
import com.example.denis.orderandtracksimply.fragments.fragmentSettlments;

public class MainZone extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    fragmentNewOrder fNewOrder;
    fragmentOrders fOrders;
    fragmentAboutUs fAboutUs;
    fragmentSettlments fSettlments;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //инициализация шрифтов
        Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        Typeface medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        // считали данные с логин скрина
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

        // заполняем информацию о пользователе
        String phone = intent.getStringExtra("phone");
        String e_mail = intent.getStringExtra("email");

        // ловим то поле, куда впишем в последствии идентификатор
        View hView =  navigationView.getHeaderView(0);
        TextView user_id = (TextView) hView.findViewById(R.id.user_id);
        ImageView login_type = (ImageView) hView.findViewById(R.id.login_type);

        // проверка на уровне помойки, но надежно
        if (!phone.equals("empty"))
        {
            user_id.setText(phone);
            login_type.setImageResource(R.drawable.phone);
        }
        else
            user_id.setText(e_mail);

        user_id.setTypeface(regular);

        //определяем фрагменты
        fNewOrder = new fragmentNewOrder();
        fOrders = new fragmentOrders();
        fAboutUs = new fragmentAboutUs();
        fSettlments = new fragmentSettlments();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // при выборе пункта меню открывается соответствующий контейнер
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

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
            fragmentTransaction.replace(R.id.container, fSettlments);
        }
        else if (id == R.id.nav_about_us)
        {
            fragmentTransaction.replace(R.id.container, fAboutUs);
        }
        else if (id == R.id.nav_exit)
        {
            // выход
        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
