package geocaching.app.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import geocaching.app.R;
import geocaching.app.activities.CacheFoundActivity;
import geocaching.app.activities.MainActivity;
import geocaching.app.activities.MapsActivity;
import geocaching.app.helpers.SharedPrefHelper;
import geocaching.app.helpers.Utils;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;

public class CacheFoundFragment extends Fragment implements KeyEventListener,FragmentLoader{
    private SharedPrefHelper sharedPrefHelper;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cache_found, container, false);
        sharedPrefHelper = new SharedPrefHelper(getContext());

        TextView textView = view.findViewById(R.id.textView);
        if(sharedPrefHelper.getCacheSelection() == sharedPrefHelper.getCacheNearby()) {
            textView.setText(getResources().getString(R.string.treasure_found_text,sharedPrefHelper.getCacheSelection()));
            setScore();
        } else {
            textView.setText(getResources().getString(R.string.wrong_treasure_found_text));
        }
        sharedPrefHelper.setNearbyCache(0);

        Button okButton = view.findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MenuFragment());
            }
        });
        return view;
    }

    private void setScore() {
        int baseScore = 0;
        int calculatedScore = baseScore;

        if(sharedPrefHelper.getLimiterSetting()) {
            int amountOfTriesLeft = sharedPrefHelper.getAmountOfTriesLeft();
            baseScore = Utils.BASE_SCORE * amountOfTriesLeft;
            calculatedScore += baseScore;
        }

        if(sharedPrefHelper.getTimerSetting()) {
            int minutes = sharedPrefHelper.getMinutes();
            int seconds = sharedPrefHelper.getSeconds();
            baseScore = (int) (Utils.BASE_SCORE * 4.5) -((minutes * 300)+(seconds * 5));
            if(baseScore > 0) {
                calculatedScore += baseScore;
            }
        }

        if(sharedPrefHelper.getThirdSetting()) {
            int amountOfTries = sharedPrefHelper.getAmountOfTries();
            baseScore = (int) (Utils.BASE_SCORE - (amountOfTries*2.75));
            calculatedScore += baseScore;
        }

        if(sharedPrefHelper.getNumberOfSurrenders() > 0) {
            int surrenders = sharedPrefHelper.getNumberOfSurrenders();
            calculatedScore -= surrenders * 500;
            sharedPrefHelper.resetNumberOfSurrenders();
        }

        //If score goes below zero, set it to 0.
        if(calculatedScore < 0) {calculatedScore = 0;}

        TextView scoreView = view.findViewById(R.id.scoreView);
        scoreView.setVisibility(View.VISIBLE);
        scoreView.setText(getResources().getString(R.string.score_text,calculatedScore));

        //If timer is active, set the time found to cache.
        if(sharedPrefHelper.getTimerSetting()) {
            TextView timerView = view.findViewById(R.id.timerView);
            timerView.setVisibility(View.VISIBLE);
            timerView.setText(getResources().getString(R.string.timer_text_view, sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds()));

            switch(sharedPrefHelper.getCacheSelection()) {
                case 1: sharedPrefHelper.setCache1Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                    break;
                case 2: sharedPrefHelper.setCache2Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                    break;
                case 3: sharedPrefHelper.setCache3Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                    break;
                case 4: sharedPrefHelper.setCache4Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                    break;
            }
            sharedPrefHelper.setTime(0,0);
        }

        // Mark caches found and set scores in SharedPreferences.
        switch(sharedPrefHelper.getCacheSelection()) {
            case 1:
                sharedPrefHelper.setCache1Found();
                sharedPrefHelper.setCache1Score(calculatedScore);
                break;
            case 2:
                sharedPrefHelper.setCache2Found();
                sharedPrefHelper.setCache2Score(calculatedScore);
                break;
            case 3:
                sharedPrefHelper.setCache3Found();
                sharedPrefHelper.setCache3Score(calculatedScore);
                break;
            case 4:
                sharedPrefHelper.setCache4Found();
                sharedPrefHelper.setCache4Score(calculatedScore);
                break;
        }
        sharedPrefHelper.setCacheSelection(0);
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
                            loadFragment(new MenuFragment());
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
                            builder.setMessage("Are you sure you want to exit?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                        }
                    }
            );
            return false;
        }
        return onKeyDown(keyCode,event);
    }

    @Override
    public void loadFragment(Fragment fragment) {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        /*
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        ((CacheFoundActivity)getActivity()).setCurrentFragment(fragment);*/
    }
}
