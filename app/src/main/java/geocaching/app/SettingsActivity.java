package geocaching.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    private SharedPrefHelper sharedPrefHelper;
    private Switch switch1, switch2, switch3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);

        sharedPrefHelper = new SharedPrefHelper(this);
        checkSettings();

        Button resetDefaultsButton = findViewById(R.id.resetDefaultsButton);
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
                                Toast.makeText(SettingsActivity.this, "Settings reseted.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }};
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                                builder.setMessage("Are you sure you want to reset the default settings?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                            }
                        }
                );
            }
        });

        Button resetProgressButton = findViewById(R.id.resetProgressButton);
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
                                Toast.makeText(SettingsActivity.this, "Progress reseted.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }};
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                                builder.setMessage("Are you sure you want to reset your progress? You'll have to find all the found caches again.").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
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
