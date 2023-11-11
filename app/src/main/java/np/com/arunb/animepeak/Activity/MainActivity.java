package np.com.arunb.animepeak.Activity;

import static np.com.arunb.animepeak.Fragments.HomeFragment.Home_IDList;
import static np.com.arunb.animepeak.Fragments.HomeFragment.Home_TitleUrlList;
import static np.com.arunb.animepeak.Fragments.HomeFragment.Home_imageUrlList;
import static np.com.arunb.animepeak.Fragments.HomeFragment.gogoanime_popular;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import np.com.arunb.animepeak.Fragments.FavouriteFragment;
import np.com.arunb.animepeak.Fragments.HomeFragment;
import np.com.arunb.animepeak.Fragments.SearchFragment;
import np.com.arunb.animepeak.Fragments.SettingsFragment;
import np.com.arunb.animepeak.Functions.Fav_object;
import np.com.arunb.animepeak.Functions.UpdateApp;
import np.com.arunb.animepeak.R;


public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    SearchFragment searchFragment = new SearchFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    public static boolean is_auto_update = false;
    public static boolean is_home = false;
    public static ArrayList<Fav_object> fav_list ;
    public static boolean is_login =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment, "HOME_FRAGMENT_TAG");
        tr.addToBackStack(null);
        tr.commit();

        fav_list = new ArrayList<Fav_object>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (fav_list==null){
            fav_list = new ArrayList<>();
        }
        is_auto_update = sharedPreferences.getBoolean("is_auto_update",false);
        if (is_auto_update){
            is_home = true;
            new UpdateApp.update_app(this).execute();

        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()!=R.id.fav){
                    MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.fav);
                    menuItem.setIcon(R.drawable.baseline_favorite_unselected);
                }
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, homeFragment, "HOME_FRAGMENT_TAG")
                                .commit();
                        return true;
                    case R.id.search:
                        FragmentTransaction tr = getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, searchFragment, "SEARCH_FRAGMENT_TAG");
                        tr.addToBackStack(null);
                        tr.commit();
                        Home_TitleUrlList.clear();
                        Home_imageUrlList.clear();
                        Home_IDList.clear();
                        if (gogoanime_popular != null  ) {
                            gogoanime_popular = null;
                        }

                        return true;
                    case R.id.fav:
                        item.setIcon(R.drawable.baseline_favorite_24_selected);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, favouriteFragment, "FAV_FRAGMENT_TAG")
                                .commit();
                        Home_TitleUrlList.clear();
                        Home_imageUrlList.clear();
                        Home_IDList.clear();
                        if (gogoanime_popular != null  ) {
                           
//                            gogoanime_popular.cancel(true);
                            gogoanime_popular = null;
                        }

                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        Home_TitleUrlList.clear();
                        Home_imageUrlList.clear();
                        Home_IDList.clear();
                        if (gogoanime_popular != null  ) {
                            
//                            gogoanime_popular.cancel(true);
                            gogoanime_popular = null;
                        }

                        return true;
                }

                return false;
            }
        });
    }
    public ArrayList<Fav_object> get_fav_list(){
        // Get the JSON string from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String favListJson = sharedPreferences.getString("favListJson", "");

// Convert the JSON string to an ArrayList
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Fav_object>>() {}.getType();
        return gson.fromJson(favListJson, type);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}