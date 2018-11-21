package geocaching.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static java.lang.Math.abs;

public class CacheFoundActivity extends Activity {
    private SharedPrefHelper sharedPrefHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_found);
        sharedPrefHelper = new SharedPrefHelper(this);

        TextView textView = findViewById(R.id.textView);
        textView.append(" " + sharedPrefHelper.getCacheSelection() + "!");

        setScore();
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
        sharedPrefHelper.setCacheSelection(0);
    }

    private void setScore() {
        int baseScore = 0;
        int calculatedScore = baseScore;

        if(sharedPrefHelper.getLimiterSetting()) {
            int amountOfTriesLeft = sharedPrefHelper.getAmountOfTriesLeft();
            baseScore = (int) (Utils.BASE_SCORE * (1.25 * amountOfTriesLeft));
            calculatedScore += baseScore;
        }
        //TODO: timer score calculations.
        if(sharedPrefHelper.getTimerSetting()) {
            baseScore = (int) (Utils.BASE_SCORE * 2.5);
            calculatedScore += baseScore;
        }
        if(sharedPrefHelper.getThirdSetting()) {
            int amountOfTries = sharedPrefHelper.getAmountOfTries();
            baseScore = (int) (Utils.BASE_SCORE - (amountOfTries*2.75));
            calculatedScore += baseScore;
        }

        TextView scoreView = findViewById(R.id.scoreView);
        scoreView.append(" " + calculatedScore + ".");

        switch(sharedPrefHelper.getCacheSelection()) {
            case 1: sharedPrefHelper.setCache1Score(calculatedScore);
                break;
            case 2: sharedPrefHelper.setCache2Score(calculatedScore);
                break;
            case 3: sharedPrefHelper.setCache3Score(calculatedScore);
                break;
            case 4: sharedPrefHelper.setCache4Score(calculatedScore);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(CacheFoundActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
