package geocaching.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CacheFoundActivity extends Activity {
    private SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_found);
        sharedPrefHelper = new SharedPrefHelper(this);

        TextView textView = findViewById(R.id.textView);
        textView.append(" " + sharedPrefHelper.getCacheSelection() + "!");

        setCacheState();

        Button okButton = findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CacheFoundActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void setCacheState() {
        switch(sharedPrefHelper.getCacheSelection()) {
            case 1: sharedPrefHelper.setCache1Found();
                break;
            case 2: sharedPrefHelper.setCache2Found();
                break;
            case 3: sharedPrefHelper.setCache3Found();
                break;
            case 4: sharedPrefHelper.setCache4Found();
                break;
        }
    }
}
