package geocaching.app.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.List;

import geocaching.app.R;
import geocaching.app.activities.MainActivity;
import geocaching.app.helpers.SharedPrefHelper;
import geocaching.app.helpers.Utils;
import geocaching.app.interfaces.FragmentLoader;
import geocaching.app.interfaces.KeyEventListener;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class GameFragment extends Fragment implements KeyEventListener, FragmentLoader {
    private ProximityObserver proximityObserver;
    private TextView infoText;
    private TextView triesLeftText;
    private TextView timerTextView;
    private TextView amountOfTriesTextView;
    private String hotnessLVL = "COLD";
    private SharedPrefHelper sharedPrefHelper;
    private int numOfTries = 0;
    long startTime = 0;
    Vibrator v;
    int vibrationMs = 100;
    int treasureNum = 0;
    View view;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            sharedPrefHelper.setTime(minutes,seconds);
            timerTextView.setText(getResources().getString(R.string.timer_text_view, minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game, container, false);

        sharedPrefHelper = new SharedPrefHelper(getContext());
        sharedPrefHelper.setAmountOfTries(numOfTries);
        sharedPrefHelper.setAmountOfTriesLeft(Utils.AMOUNT_OF_TRIES);

        v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        checkSettings();

        infoText = view.findViewById(R.id.textView);
        TextView cacheIDText = view.findViewById(R.id.cacheID);
        cacheIDText.setText(getResources().getString(R.string.cache_ID_text, sharedPrefHelper.getCacheSelection()));

        final Button surrenderButton = view.findViewById(R.id.surrenderButton);
        surrenderButton.setOnClickListener(new View.OnClickListener() {
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
                                sharedPrefHelper.setNumberOfSurrenders();
                                loadFragment(new MenuFragment());
                                break;
                            }
                        }
                    }};
                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure you want to give up the search?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                            }
                        }
                );
            }
        });

        final Button checkButton = view.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            int amountOfTriesLeft;
            @Override
            public void onClick(View v) {
                if (sharedPrefHelper.getLimiterSetting()) {
                    if (numOfTries < Utils.AMOUNT_OF_TRIES) {
                        numOfTries += 1;
                        checkHotness();
                        amountOfTriesLeft = Utils.AMOUNT_OF_TRIES - numOfTries;
                        triesLeftText.setText(getResources().getString(R.string.tries_left_info, amountOfTriesLeft));
                        sharedPrefHelper.setAmountOfTriesLeft(amountOfTriesLeft);
                        // Reaching the limit of tries.
                        if (numOfTries == Utils.AMOUNT_OF_TRIES) {
                            surrenderButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        infoText.setText(getResources().getString(R.string.out_of_tries));
                    }
                } else {
                    numOfTries += 1;
                    checkHotness();
                }

                if(sharedPrefHelper.getThirdSetting()) {
                    amountOfTriesTextView.setText(getResources().getString(R.string.amount_info,numOfTries));
                    sharedPrefHelper.setAmountOfTries(numOfTries);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials("my-app-2vi", "8d4cf42cfb254f328f55eeaf051f8b90");

        this.proximityObserver =
                new ProximityObserverBuilder(getActivity().getApplicationContext(), cloudCredentials)
                        .onError(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withBalancedPowerMode()
                        .build();

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(getActivity(),
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.startObserving(hotZone);
                                proximityObserver.startObserving(warmZone);
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
    }

    ProximityZone warmZone = new ProximityZoneBuilder()
            .forTag("treasure")
            .inCustomRange(Utils.CACHE_WARM_ZONE)
            .onEnter(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    treasureNum = Integer.parseInt(context.getAttachments().get("treasure"));
                    hotnessLVL = "WARM";
                    Log.d("app", "Entered warm zone!");
                    return null;
                }
            })
            .onExit(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    treasureNum = Integer.parseInt(context.getAttachments().get("treasure"));
                    hotnessLVL = "COLD";
                    Log.d("app", "Exited warm zone!");
                    return null;
                }
            })
            .build();

    ProximityZone hotZone = new ProximityZoneBuilder()
            .forTag("treasure")
            .inCustomRange(Utils.CACHE_HOT_ZONE)
            .onEnter(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    treasureNum = Integer.parseInt(context.getAttachments().get("treasure"));
                    hotnessLVL = "HOT";
                    Log.d("app", "Entered hot zone!");
                    return null;
                }
            })
            .onExit(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    treasureNum = Integer.parseInt(context.getAttachments().get("treasure"));
                    hotnessLVL = "WARM";
                    Log.d("app", "Exited hot zone!");
                    return null;
                }
            })
            .build();



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
                            if (numOfTries == Utils.AMOUNT_OF_TRIES && sharedPrefHelper.getLimiterSetting()) {
                                sharedPrefHelper.setNumberOfSurrenders();
                            }
                            loadFragment(new MenuFragment());
                            break;
                        }
                    }
                }};
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            if (numOfTries == Utils.AMOUNT_OF_TRIES && sharedPrefHelper.getLimiterSetting()) {
                                builder.setMessage("Are you sure you want to give up the search?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                            }else {
                                builder.setMessage("Are you sure you want to exit?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                            }

                        }
                    }
            );
            return false;
        }
        return onKeyDown(keyCode,event);
    }

    @Override
    public void onPause() {
        Log.d("lifecycle", "pause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("lifecycle", "stop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("lifecycle", "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("lifecycle", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("lifecycle", "onDetach");
        stopTimer();
        super.onDetach();
    }

    @Override
    public void loadFragment(Fragment fragment) {
        stopTimer();
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        ((MainActivity)getActivity()).setCurrentFragment(fragment);
    }

    private void checkHotness() {
        if(hotnessLVL.equals("COLD")) {
            infoText.setText(getResources().getString(R.string.cold_zone_text));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs / 2, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs / 2);
            }

        } else if (hotnessLVL.equals("WARM")) {
            sharedPrefHelper.setNearbyCache(treasureNum);
            infoText.setText(getResources().getString(R.string.warm_zone_text, treasureNum));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs);
            }

        } else if (hotnessLVL.equals("HOT")) {
            sharedPrefHelper.setNearbyCache(treasureNum);
            infoText.setText(getResources().getString(R.string.hot_zone_text, treasureNum));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs * 2, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs * 2);
            }
        }
    }

    protected void checkSettings() {
        if(sharedPrefHelper.getLimiterSetting()) {
            triesLeftText = view.findViewById(R.id.triesLeft);
            triesLeftText.setText(getResources().getString(R.string.tries_left_info, (Utils.AMOUNT_OF_TRIES - numOfTries)));
            triesLeftText.setVisibility(View.VISIBLE);
        }

        if(sharedPrefHelper.getTimerSetting()) {
            timerTextView = view.findViewById(R.id.timerTextView);
            timerTextView.setVisibility(View.VISIBLE);
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        }

        if(sharedPrefHelper.getThirdSetting()) {
            amountOfTriesTextView = view.findViewById(R.id.amountOfTriesInfo);
            amountOfTriesTextView.setText(getResources().getString(R.string.amount_info, numOfTries));
            amountOfTriesTextView.setVisibility(View.VISIBLE);
        }
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }
}
