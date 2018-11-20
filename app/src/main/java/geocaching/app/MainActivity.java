package geocaching.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    public static final int SUCCESS = 0;
    public static final int SERVICE_MISSING = 1;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
    public static final int SERVICE_DISABLED = 3;

    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        sharedPrefHelper = new SharedPrefHelper(this);

        final Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefHelper.getCacheSelection() != 0) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("selectedCacheID", sharedPrefHelper.getCacheSelection());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Select a treasure cache from MAP first!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CacheListActivity.class);
                startActivity(intent);
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(result == SUCCESS) {
            //Connection OK
        }else if (result == SERVICE_MISSING || result == SERVICE_VERSION_UPDATE_REQUIRED || result == SERVICE_DISABLED) {
            GoogleApiAvailability.getInstance().getErrorDialog(this,result,result);
        }
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
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                            break;
                        }
                    }
                }};
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Are you sure you want to exit the application?").setNegativeButton("YES", dialogClickListener).setPositiveButton("NO", dialogClickListener).show();
                        }
                    }
            );
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}