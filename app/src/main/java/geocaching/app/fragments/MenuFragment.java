package geocaching.app.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;

import java.net.InetAddress;

import geocaching.app.R;
import geocaching.app.activities.MainActivity;
import geocaching.app.helpers.SharedPrefHelper;
import geocaching.app.helpers.Utils;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class MenuFragment extends Fragment implements KeyEventListener, FragmentLoader {
    public static final int SUCCESS = 0;
    public static final int SERVICE_MISSING = 1;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
    public static final int SERVICE_DISABLED = 3;

    private SharedPrefHelper sharedPrefHelper;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        sharedPrefHelper = new SharedPrefHelper(getContext());

        final Button playButton = view.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefHelper.getCacheSelection() != 0 && sharedPrefHelper.getCacheSelection() <= Utils.NUMBER_OF_CACHES) {
                    loadFragment(new GameFragment());
                } else {
                    Toast.makeText(getContext(), "Select a treasure cache from MAP first!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button mapButton = view.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()) {
                    loadFragment(new CacheListFragment());
                } else {
                    Toast.makeText(getContext(), "You need an Internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SettingsFragment());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if(result == SUCCESS) {
            Log.d("GoogleApiAvailability", "Connection OK!");
        }else if (result == SERVICE_MISSING || result == SERVICE_VERSION_UPDATE_REQUIRED || result == SERVICE_DISABLED) {
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),result,result);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE: {
                            break;
                        }
                        case DialogInterface.BUTTON_NEGATIVE: {
                            getActivity().moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                            break;
                        }
                    }
                }
            };
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to exit the application?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                        }
                    }
            );
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

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}