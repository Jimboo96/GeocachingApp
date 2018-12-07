package geocaching.app.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import geocaching.app.R;
import geocaching.app.activities.MainActivity;
import geocaching.app.helpers.SharedPrefHelper;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class SettingsFragment extends Fragment implements KeyEventListener, FragmentLoader {
    View view;
    private SharedPrefHelper sharedPrefHelper;
    private Switch switch1, switch2, switch3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        switch1 = view.findViewById(R.id.switch1);
        switch2 = view.findViewById(R.id.switch2);
        switch3 = view.findViewById(R.id.switch3);

        sharedPrefHelper = new SharedPrefHelper(getContext());
        checkSettings();

        Button resetDefaultsButton = view.findViewById(R.id.resetDefaultsButton);
        resetDefaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {
                                break;
                            }
                            case DialogInterface.BUTTON_NEGATIVE: {
                                sharedPrefHelper.saveTimerSetting(false);
                                sharedPrefHelper.saveLimiterSetting(false);
                                sharedPrefHelper.saveThirdSetting(false);
                                checkSettings();
                                Toast.makeText(getContext(), "Settings reseted.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }};
                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure you want to reset the default settings?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                            }
                        }
                );
            }
        });

        Button resetProgressButton = view.findViewById(R.id.resetProgressButton);
        resetProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: {
                                break;
                            }
                            case DialogInterface.BUTTON_NEGATIVE: {
                                sharedPrefHelper.resetCaches();
                                sharedPrefHelper.setCacheSelection(0);
                                Toast.makeText(getContext(), "Progress reseted.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }};
                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure you want to reset your progress? You'll have to find all the discovered caches again.").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                            }
                        }
                );
            }
        });

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.changeTimerSetting();
            }
        });
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.changeLimiterSetting();
            }
        });
        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.changeThirdSetting();
            }
        });
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

    private void checkSettings() {
        if(sharedPrefHelper.getTimerSetting()) {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }

        if(sharedPrefHelper.getLimiterSetting()) {
            switch2.setChecked(true);
        } else {
            switch2.setChecked(false);
        }

        if(sharedPrefHelper.getThirdSetting()) {
            switch3.setChecked(true);
        } else {
            switch3.setChecked(false);
        }
    }
}
