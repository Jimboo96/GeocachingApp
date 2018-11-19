package geocaching.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import okhttp3.internal.Util;

public class GameActivity extends Activity {
    private ProximityObserver proximityObserver;
    private TextView infoText;
    private TextView triesLeftText;
    private String hotnessLVL = "COLD";
    private int cacheID;
    private int numOfTries = 0;

    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPrefHelper = new SharedPrefHelper(this);
        checkForLimiter();

        final TextView cacheIDText = findViewById(R.id.cacheID);
        infoText = findViewById(R.id.textView);

        cacheID = getIntent().getIntExtra("selectedCacheID", 0);
        cacheIDText.append(" " + cacheID + ".");

        final Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPrefHelper.getLimiterSetting()) {
                    if (numOfTries < Utils.AMOUNT_OF_TRIES) {
                        checkHotness();
                        numOfTries += 1;
                        checkForLimiter();
                        if (numOfTries == Utils.AMOUNT_OF_TRIES) {
                            checkButton.setVisibility(View.GONE);
                            cacheIDText.setVisibility(View.GONE);
                            triesLeftText.setVisibility(View.GONE);
                            infoText.setVisibility(View.GONE);
                        }
                    }
                } else {
                    checkHotness();
                }

            }
        });

        EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials("my-app-2vi", "8d4cf42cfb254f328f55eeaf051f8b90");

        this.proximityObserver =
                new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
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
                .fulfillRequirements(this,
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
                            Intent i = new Intent(GameActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            break;
                        }
                    }
                }};
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                            builder.setMessage("Are you sure you want to exit?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                        }
                    }
            );
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    ProximityZone warmZone = new ProximityZoneBuilder()
            .forTag("treasure")
            .inCustomRange(Utils.CACHE_WARM_ZONE)
            .onEnter(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    String treasureNum = context.getAttachments().get("treasure");
                    //textView.setText("You are in the cold zone of treasure " + treasureNum);
                    hotnessLVL = "WARM";
                    Log.d("app", "You are in the warm zone of treasure " + treasureNum);
                    return null;
                }
            })
            .onExit(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
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
                    String treasureNum = context.getAttachments().get("treasure");
                    //if(treasureNum.equals(String.valueOf(cacheID))) {
                    //Toast.makeText(GameActivity.this, "yes it works", Toast.LENGTH_LONG).show();
                    //textView.setText("You are in the hot zone of treasure " + treasureNum);
                    hotnessLVL = "HOT";
                    Log.d("app", "You are in the hot zone of treasure " + treasureNum);
                    //}
                    return null;
                }
            })
            .onExit(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    hotnessLVL = "WARM";
                    Log.d("app", "Exited hot zone!");
                    return null;
                }
            })
            .build();

    private void checkHotness() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibrationMs = 100;

        if(hotnessLVL == "COLD") {
            infoText.setText(getResources().getString(R.string.cold_zone_text));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs / 2, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs / 2);
            }

        } else if (hotnessLVL == "WARM") {
            infoText.setText(getResources().getString(R.string.warm_zone_text));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs);
            }

        } else if (hotnessLVL == "HOT") {
            infoText.setText(getResources().getString(R.string.hot_zone_text));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs * 2, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs * 3);
            }
        }
    }

    private void checkForLimiter() {
        if(sharedPrefHelper.getLimiterSetting()) {
            triesLeftText = findViewById(R.id.triesLeft);
            triesLeftText.setVisibility(View.VISIBLE);
            triesLeftText.setText((Utils.AMOUNT_OF_TRIES - numOfTries) + ".");
        }
    }
}
