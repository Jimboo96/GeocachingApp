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
        textView.setText(getResources().getString(R.string.treasure_found_text,sharedPrefHelper.getCacheSelection()));

        setScore();

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

    private void setScore() {
        int baseScore = 0;
        int calculatedScore = baseScore;

        if(sharedPrefHelper.getLimiterSetting()) {
            int amountOfTriesLeft = sharedPrefHelper.getAmountOfTriesLeft();
            baseScore = (int) (Utils.BASE_SCORE * amountOfTriesLeft);
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

        switch(sharedPrefHelper.getCacheSelection()) {
            case 1:
                sharedPrefHelper.setCache1Found();
                sharedPrefHelper.setCache1Score(calculatedScore);
                sharedPrefHelper.setCache1Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                break;
            case 2:
                sharedPrefHelper.setCache2Found();
                sharedPrefHelper.setCache2Score(calculatedScore);
                sharedPrefHelper.setCache2Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                break;
            case 3:
                sharedPrefHelper.setCache3Found();
                sharedPrefHelper.setCache3Score(calculatedScore);
                sharedPrefHelper.setCache3Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                break;
            case 4:
                sharedPrefHelper.setCache4Found();
                sharedPrefHelper.setCache4Score(calculatedScore);
                sharedPrefHelper.setCache4Time(sharedPrefHelper.getMinutes(),sharedPrefHelper.getSeconds());
                break;
        }
        sharedPrefHelper.setCacheSelection(0);

        TextView scoreView = findViewById(R.id.scoreView);
        scoreView.setText(getResources().getString(R.string.score_text,calculatedScore));
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
