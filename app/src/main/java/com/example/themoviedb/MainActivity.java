package com.example.themoviedb;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MovieMain";

    public static final int MENU_START = 0;
    public static final int MENU_MAIN = 1;
    public static final int FRAGMENT_LOGIN = 2;
    public static final int FRAGMENT_REGISTER = 3;
    public static final int FRAGMENT_HOME = 4;
    public static final int FRAGMENT_FAVORITES = 5;
    public static final int FRAGMENT_PROFILE = 6;

    public static FragmentManager mFragmentManager;

    public static String currentUser;
    public static int currentUserID;

    private Fragment loginFragment = new FragmentLogin();
    private Fragment registerFragment = new FragmentRegister();
    private Fragment homeFragment = new FragmentHome();
    private Fragment profileFragment = new FragmentProfile();
    private Fragment favouritesFragment = new FragmentFavourites();

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mFragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container)!=null) {

            if (savedInstanceState != null) {
                return;
            }

            mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
            mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.login:
                            Log.d(TAG, "Go to login");
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, loginFragment, null)
                                    .commit();
                            break;
                        case R.id.sign_up:
                            Log.d(TAG, "Go to register");
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, registerFragment, null)
                                    .commit();
                            break;
                        case R.id.favorites:
                            Log.d(TAG, "Go to favorites");
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, favouritesFragment, null)
                                    .commit();
                            break;
                        case R.id.home:
                            Log.d(TAG, "Go to home");
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, homeFragment, null)
                                    .commit();
                            break;
                        case R.id.profile:
                            Log.d(TAG, "Go to profile");
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, profileFragment, null)
                                    .commit();
                            break;
                    }
                    return false;
                }
            });

            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, new FragmentLogin(), null)
                    .commit();
        }
    }

    public void setBottomNavigationMenu(int menu){
        mBottomNavigationView.getMenu().clear();
        switch(menu){
            case MENU_START:
                mBottomNavigationView.inflateMenu(R.menu.menu_start);
                break;
            case MENU_MAIN:
                mBottomNavigationView.inflateMenu(R.menu.menu_main);
                break;
        }
    }

    public void replaceFragment(int fragment){
        switch (fragment){
            case FRAGMENT_LOGIN:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, loginFragment, null)
                        .commit();
                break;
            case FRAGMENT_REGISTER:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, registerFragment, null)
                        .commit();
                break;
            case FRAGMENT_HOME:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment, null)
                        .commit();
                break;
            case FRAGMENT_FAVORITES:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, favouritesFragment, null)
                        .commit();
                break;
            case FRAGMENT_PROFILE:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment, null)
                        .commit();
                break;
        }
    }

    public static void showDetails(Movie movie){
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new FragmentDetails(movie), null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
