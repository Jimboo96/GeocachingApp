package geocaching.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
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

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class GameActivity extends Activity {

    private ProximityObserver proximityObserver;
    private TextView textView;
    private String hotnessLVL = "COLD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHotness();
            }
        });

        String appId = "my-app-2vi";
        String appToken = "8d4cf42cfb254f328f55eeaf051f8b90";

        EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials( appId,appToken);

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
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.startObserving(hotZone);
                                proximityObserver.startObserving(warmZone);
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
    }

    ProximityZone warmZone = new ProximityZoneBuilder()
            .forTag("treasure")
            .inCustomRange(8.0)
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
                    //textView.setText("Exited warm zone");
                    hotnessLVL = "COLD";
                    Log.d("app", "Exited warm zone!");
                    return null;
                }
            })
            .build();

    ProximityZone hotZone = new ProximityZoneBuilder()
            .forTag("treasure")
            .inCustomRange(2.0)
            .onEnter(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    String treasureNum = context.getAttachments().get("treasure");
                    //textView.setText("You are in the hot zone of treasure " + treasureNum);
                    hotnessLVL = "HOT";
                    Log.d("app", "You are in the hot zone of treasure " + treasureNum);
                    return null;
                }
            })
            .onExit(new Function1<ProximityZoneContext, Unit>() {
                @Override
                public Unit invoke(ProximityZoneContext context) {
                    //textView.setText("Exited hot zone");
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
            textView.setText("You are in the cold zone.");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs / 2, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs / 2);
            }

        } else if (hotnessLVL == "WARM") {
            textView.setText("You are in the warm zone.");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs);
            }

        } else if (hotnessLVL == "HOT") {
            textView.setText("You are in the hot zone.");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(vibrationMs * 2, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(vibrationMs * 3);
            }
        }
    }
}
