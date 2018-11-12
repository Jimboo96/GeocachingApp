package geocaching.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button resetButton = findViewById(R.id.resetButton);
        Switch switch1 = findViewById(R.id.switch1);
        Switch switch2 = findViewById(R.id.switch2);
        Switch switch3 = findViewById(R.id.switch3);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        boolean switch1CheckState = prefs.getBoolean("switch1Checked", false);
        if (switch1CheckState) {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }

        boolean switch2CheckState = prefs.getBoolean("switch2Checked", false);
        if (switch2CheckState) {
            switch2.setChecked(true);
        } else {
            switch2.setChecked(false);
        }

        boolean switch3CheckState = prefs.getBoolean("switch3Checked", false);
        if (switch3CheckState) {
            switch3.setChecked(true);
        } else {
            switch3.setChecked(false);
        }


        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Reset found cache states.
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putBoolean("switch1Checked", false);
                    editor.putString("name", "Elena");
                    editor.putInt("idName", 12);
                    editor.apply();
                } else {
                    editor.putBoolean("switch1Checked", true);
                    editor.putString("name", "Rat");
                    editor.putInt("idName", 15);
                    editor.apply();
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putBoolean("switch2Checked", false);
                    editor.putString("name", "Elena");
                    editor.putInt("idName", 12);
                    editor.apply();
                } else {
                    editor.putBoolean("switch2Checked", true);
                    editor.putString("name", "Rat");
                    editor.putInt("idName", 15);
                    editor.apply();
                }
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putBoolean("switch3Checked", false);
                    editor.putString("name", "Elena");
                    editor.putInt("idName", 12);
                    editor.apply();
                } else {
                    editor.putBoolean("switch3Checked", true);
                    editor.putString("name", "Rat");
                    editor.putInt("idName", 15);
                    editor.apply();
                }
            }
        });
    }
}
