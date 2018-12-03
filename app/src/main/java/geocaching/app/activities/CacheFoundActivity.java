package geocaching.app.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import geocaching.app.R;
import geocaching.app.fragments.CacheFoundFragment;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class CacheFoundActivity extends AppCompatActivity implements FragmentLoader {
    Fragment currentFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base);
        getSupportActionBar().hide();
        loadFragment(new CacheFoundFragment());
    }

    @Override
    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        setCurrentFragment(fragment);
    }

    public void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(currentFragment != null && currentFragment instanceof KeyEventListener) {
            ((KeyEventListener) currentFragment).onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
