package com.farhatty.user.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.farhatty.user.R;
import com.farhatty.user.Utiliti.NavHandlerListener;
import com.farhatty.user.fragment.ContactFragment;
import com.farhatty.user.fragment.FaqFragment;
import com.farhatty.user.fragment.HomeFragment;
import com.farhatty.user.fragment.OffersFragment;
import com.farhatty.user.fragment.ServiceFragment;
import com.farhatty.user.fragment.TrackingFragment;

import static com.farhatty.user.Utiliti.ColorUtil.getNavIconColorState;
import static com.farhatty.user.Utiliti.ColorUtil.getNavTextColorState;
import static com.farhatty.user.Utiliti.UtilMethods.browseUrl;
import static com.farhatty.user.Utiliti.UtilMethods.getGooglePlayUrl;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        NavHandlerListener{

    private NavigationView navigationView = null;
    private NavigationMenuView navigationMenuView = null;
    private DrawerLayout drawer = null;
    private View headerView;
    private RelativeLayout navHeaderImgContainer;
    public NavHandlerListener navHandlerListener = null;
    private boolean isDoubleBackToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);

        navigationView.setNavigationItemSelectedListener(this);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
        navigationView.setItemTextColor(getNavTextColorState());
        navigationView.setItemIconTintList(getNavIconColorState());
        headerView = navigationView.getHeaderView(0);
        navHeaderImgContainer = (RelativeLayout) headerView.findViewById(R.id.navHeaderImgContainer);

        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (navigationView.getMenu().findItem(itemId).isChecked()) {
            return true;
        }

        if (itemId == R.id.nav_home) {
            showHomeScreen ();

        }else

        if (itemId == R.id.nav_service) {
             showServiceScreen();



        } else if (itemId == R.id.nav_offer) {

            showOffersScreen();

        } else if (itemId == R.id.nav_track) {

            showTrackScreen ();

        } else if (itemId == R.id.nav_faq) {
            showFaqScreen();


        } else if (itemId == R.id.nav_contact_us) {
            showContactScreen();


        } else if (itemId == R.id.nav_rate) {
            gotoGooglePlay();
        } else {

        }

        drawer.closeDrawer(GravityCompat.START);
        setTitle(navigationView.getMenu().findItem(itemId).getTitle());
        return true;
    }



    private void showActivityToolbar() {
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
        }
    }

    private void hideActivityToolbar() {
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isDoubleBackToExit) {
                super.onBackPressed();
                finish();
            }
            if (!isDoubleBackToExit) {
                Toast.makeText(this, getString(R.string.re_tap_text), Toast.LENGTH_SHORT).show();
            }
            this.isDoubleBackToExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isDoubleBackToExit = false;
                }
            }, 2000);
        }
    }



    private void showHomeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, HomeFragment.newInstance(" "))
                .commit();
    }


    private void showTrackScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, TrackingFragment.newInstance(" "))
                .commit();
    }


    private void showServiceScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ServiceFragment.newInstance (" "))
                .commit();
    }


    private void showContactScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ContactFragment.newInstance (" "))
                .commit();
    }

    private void showFaqScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, FaqFragment.newInstance (" "))
                .commit();
    }



    private void showOffersScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, OffersFragment.newInstance (" "))
                .commit();
    }


    @Override
    protected void onResume() {
        navHandlerListener = this;
        super.onResume();
    }

    @Override
    public void onNavOpenRequested() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void gotoGooglePlay() {
        browseUrl(MainActivity.this, getGooglePlayUrl(MainActivity.this));
        drawer.closeDrawer(GravityCompat.START);
    }

}
