package com.rp.agrocast;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.rp.agrocast.fragments.HomeFrag;
import com.rp.agrocast.fragments.MarketFrag;
import com.rp.agrocast.fragments.PredictionFrag;
import com.rp.agrocast.fragments.ProfileFrag;


public class MainActivity extends AppCompatActivity {
    private final int ID_HOME = 1;
    private final int ID_CROP = 2;
    private final int ID_PREDICTION = 3;
    private final int ID_PROFILE = 4;

    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomNavigation = findViewById(R.id.bottomNavigationBar);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_CROP, R.drawable.ic_swap));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PREDICTION, R.drawable.ic_table));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_account));

//        bottomNavigation.setCount(3, "5");

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_container,new HomeFrag()).commit();

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                if (item.getId() == 1) {
                    fragment = new HomeFrag();
                } else if (item.getId() == 2) {
                    fragment = new MarketFrag();
                } else if (item.getId() == 3) {
                    fragment = new PredictionFrag();
                } else if (item.getId() == 4) {
                    fragment = new ProfileFrag();
                }

                loadFragment(fragment);
            }
        });

        bottomNavigation.show(1, true);

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container, fragment, null)
                .commit();
    }

}