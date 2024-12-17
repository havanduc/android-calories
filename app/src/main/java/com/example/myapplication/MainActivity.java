package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragment.FavoriteFragment;
import com.example.myapplication.fragment.HistoryFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.MyFood;
import com.example.myapplication.fragment.MyMeals;
import com.example.myapplication.fragment.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FAVORITE = 1;
    private static final int FRAGMENT_HISTORY = 2;
    private static final int FRAGMENT_PERSON = 3;
    private static final int FRAGMENT_MYFOOD = 4;
    private static final int FRAGMENT_MYMEALS = 5;
    private int mCurrentFragment = FRAGMENT_HOME;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    if (mCurrentFragment != FRAGMENT_HOME) {
                        replaceFragment(new HomeFragment());
                        mCurrentFragment = FRAGMENT_HOME;
                    }
                } else if (id == R.id.nav_food) {
                    if (mCurrentFragment != FRAGMENT_MYFOOD) {
                        replaceFragment(new MyFood());
                        mCurrentFragment = FRAGMENT_MYFOOD;
                    }
                } else if (id == R.id.nav_food_bank) {
                    if (mCurrentFragment != FRAGMENT_MYMEALS) {
                        replaceFragment(new MyMeals());
                        mCurrentFragment = FRAGMENT_MYMEALS;
                    }
                } else if (id == R.id.nav_person) {
                    if (mCurrentFragment != FRAGMENT_PERSON) {
                        replaceFragment(new PersonFragment());
                        mCurrentFragment = FRAGMENT_PERSON;
                    }
                }
                return true;
            }
        });

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_favorite) {
            if (mCurrentFragment != FRAGMENT_FAVORITE) {
                replaceFragment(new FavoriteFragment());
                mCurrentFragment = FRAGMENT_FAVORITE;
            }
        } else if (id == R.id.nav_history) {
            if (mCurrentFragment != FRAGMENT_HISTORY) {
                replaceFragment(new HistoryFragment());
                mCurrentFragment = FRAGMENT_HISTORY;
            }
        } else if (id == R.id.nav_person) {
            if (mCurrentFragment != FRAGMENT_PERSON) {
                replaceFragment(new PersonFragment());
                mCurrentFragment = FRAGMENT_PERSON;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}
