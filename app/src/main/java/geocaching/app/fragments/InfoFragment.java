package geocaching.app.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        final TextView infoTextView = view.findViewById(R.id.infoText);

        Button infoButton1 = view.findViewById(R.id.info1);
        infoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoTextView.setText(getResources().getString(R.string.track_info));
            }
        });

        Button infoButton2 = view.findViewById(R.id.info2);
        infoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoTextView.setText(getResources().getString(R.string.list_info));
            }
        });

        Button infoButton3 = view.findViewById(R.id.info3);
        infoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoTextView.setText(getResources().getString(R.string.settings_info));
            }
        });

        Button infoButton4 = view.findViewById(R.id.info4);
        infoButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoTextView.setText(getResources().getString(R.string.surrender_info));
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
}
