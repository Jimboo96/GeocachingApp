package geocaching.app.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import geocaching.app.R;
import geocaching.app.helpers.Utils;

public class SharedPrefHelper {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
    }

    public void saveTimerSetting(boolean state) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting1), state).apply();
    }

    public void changeTimerSetting() {
        boolean state = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting1), false);
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting1), !state).apply();
    }

    public boolean getTimerSetting() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting1), false);
    }

    public void saveLimiterSetting(boolean state) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting2), state).apply();
    }

    public void changeLimiterSetting() {
        boolean state = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting2), false);
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting2), !state).apply();
    }

    public boolean getLimiterSetting() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting2), false);
    }

    public void saveThirdSetting(boolean state) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting3), state).apply();
    }

    public void changeThirdSetting() {
        boolean state = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting3), false);
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting3), !state).apply();
    }

    public boolean getThirdSetting() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting3), false);
    }

    public void setCacheSelection(int selectedCacheNumber) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_selection), selectedCacheNumber).apply();
    }

    public int getCacheSelection() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_selection), 0);
    }

    public void setNearbyCache(int nearbyCacheNumber) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_nearby), nearbyCacheNumber).apply();
    }

    public int getCacheNearby() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_nearby), 0);
    }

    public void setCache1Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_1_state), true).apply();
    }

    public boolean getCache1State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_1_state), false);
    }

    public void setCache2Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_2_state), true).apply();
    }

    public boolean getCache2State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_2_state), false);
    }

    public void setCache3Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_3_state), true).apply();
    }

    public boolean getCache3State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_3_state), false);
    }

    public void setCache4Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_4_state), true).apply();
    }

    public boolean getCache4State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_4_state), false);
    }

    public void setCache1Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_score), score).apply();
    }

    public int getCache1Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_1_score), 0);
    }

    public void setCache2Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_score), score).apply();
    }

    public int getCache2Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_2_score), 0);
    }

    public void setCache3Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_score), score).apply();
    }

    public int getCache3Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_3_score), 0);
    }

    public void setCache4Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_score), score).apply();
    }

    public int getCache4Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_4_score), 0);
    }

    public void setAmountOfTries(int amount) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_amount_of_tries), amount).apply();
    }

    public int getAmountOfTries() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_amount_of_tries), 0);
    }

    public void setAmountOfTriesLeft(int amount) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_amount_of_tries_left), amount).apply();
    }

    public int getAmountOfTriesLeft() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_amount_of_tries_left), 0);
    }

    public void setTime(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_seconds), seconds).apply();
    }

    public int getSeconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_seconds), 0);
    }

    public int getMinutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_minutes), 0);
    }

    public void setNumberOfSurrenders() {
        int amount = getNumberOfSurrenders() + 1;
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_number_of_surrenders), amount).apply();
    }

    public void resetNumberOfSurrenders() {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_number_of_surrenders), 0).apply();
    }

    public int getNumberOfSurrenders() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_number_of_surrenders), 0);
    }

    public void setCache1Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_seconds), seconds).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_1_time_set), true).apply();
    }

    public int getCache1Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_1_seconds), 0);
    }

    public int getCache1Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_1_minutes), 0);
    }

    public boolean getCache1TimeSet() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_1_time_set), true);
    }

    public void setCache2Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_seconds), seconds).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_2_time_set), true).apply();
    }

    public int getCache2Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_2_seconds), 0);
    }

    public int getCache2Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_2_minutes), 0);
    }

    public boolean getCache2TimeSet() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_2_time_set), true);
    }

    public void setCache3Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_seconds), seconds).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_3_time_set), true).apply();
    }

    public int getCache3Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_3_seconds), 0);
    }

    public int getCache3Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_3_minutes), 0);
    }

    public boolean getCache3TimeSet() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_3_time_set), true);
    }

    public void setCache4Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_seconds), seconds).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_4_time_set), true).apply();
    }

    public int getCache4Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_4_seconds), 0);
    }

    public int getCache4Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_4_minutes), 0);
    }

    public boolean getCache4TimeSet() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_4_time_set), true);
    }

    public void resetCaches() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_1_state), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_2_state), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_3_state), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_4_state), false).apply();

        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_score), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_score), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_score), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_score), 0).apply();

        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_amount_of_tries), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_amount_of_tries_left), Utils.AMOUNT_OF_TRIES).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_minutes), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_seconds), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_number_of_surrenders), 0).apply();

        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_minutes), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_seconds), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_minutes), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_seconds), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_minutes), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_seconds), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_minutes), 0).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_seconds), 0).apply();

        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_1_time_set), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_2_time_set), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_3_time_set), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_4_time_set), false).apply();

        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_nearby), 0).apply();
    }
}
