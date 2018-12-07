package geocaching.app.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import geocaching.app.fragments.MenuFragment;
import geocaching.app.R;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class MainActivity extends AppCompatActivity implements FragmentLoader {
    public Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base);
        getSupportActionBar().hide();
        loadFragment(new MenuFragment());
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