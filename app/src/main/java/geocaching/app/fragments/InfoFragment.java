package geocaching.app.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geocaching.app.R;
import geocaching.app.activities.MainActivity;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class InfoFragment extends Fragment implements KeyEventListener, FragmentLoader {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            loadFragment(new MenuFragment());
            return false;
        }
        return onKeyDown(keyCode,event);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        ((MainActivity)getActivity()).setCurrentFragment(fragment);
    }
}
