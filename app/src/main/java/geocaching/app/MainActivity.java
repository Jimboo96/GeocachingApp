package geocaching.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;

import org.w3c.dom.Text;

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

        Button playButton = findViewById(R.id.playButton);
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
                Intent intent = new Intent(MainActivity.this, PreGameActivity.class);
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
}