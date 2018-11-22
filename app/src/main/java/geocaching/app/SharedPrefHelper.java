package geocaching.app;

import android.content.Context;
import android.content.SharedPreferences;

class SharedPrefHelper {
    private SharedPreferences sharedPreferences;
    private Context context;

    SharedPrefHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
    }

    void saveTimerSetting(boolean state) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting1), state).apply();
    }

    void changeTimerSetting() {
        boolean state = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting1), false);
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting1), !state).apply();
    }

    boolean getTimerSetting() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting1), false);
    }

    void saveLimiterSetting(boolean state) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting2), state).apply();
    }

    void changeLimiterSetting() {
        boolean state = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting2), false);
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting2), !state).apply();
    }

    boolean getLimiterSetting() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting2), false);
    }

    void saveThirdSetting(boolean state) {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting3), state).apply();
    }

    void changeThirdSetting() {
        boolean state = sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting3), false);
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_setting3), !state).apply();
    }

    boolean getThirdSetting() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_setting3), false);
    }

    void setCacheSelection(int selectedCacheNumber) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_selection), selectedCacheNumber).apply();
    }

    int getCacheSelection() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_selection), 0);
    }

    void setCache1Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_1_state), true).apply();
    }

    boolean getCache1State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_1_state), false);
    }

    void setCache2Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_2_state), true).apply();
    }

    boolean getCache2State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_2_state), false);
    }

    void setCache3Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_3_state), true).apply();
    }

    boolean getCache3State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_3_state), false);
    }

    void setCache4Found() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_4_state), true).apply();
    }

    boolean getCache4State() {
        return sharedPreferences.getBoolean(context.getString(R.string.shared_pref_cache_4_state), false);
    }

    void setCache1Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_score), score).apply();
    }

    int getCache1Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_1_score), 0);
    }

    void setCache2Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_score), score).apply();
    }

    int getCache2Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_2_score), 0);
    }

    void setCache3Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_score), score).apply();
    }

    int getCache3Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_3_score), 0);
    }

    void setCache4Score(int score) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_score), score).apply();
    }

    int getCache4Score() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_4_score), 0);
    }

    void setAmountOfTries(int amount) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_amount_of_tries), amount).apply();
    }

    int getAmountOfTries() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_amount_of_tries), 0);
    }

    void setAmountOfTriesLeft(int amount) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_amount_of_tries_left), amount).apply();
    }

    int getAmountOfTriesLeft() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_amount_of_tries_left), 0);
    }

    void setTime(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_seconds), seconds).apply();
    }

    int getSeconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_seconds), 0);
    }

    int getMinutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_minutes), 0);
    }

    void setNumberOfSurrenders() {
        int amount = getNumberOfSurrenders() + 1;
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_number_of_surrenders), amount).apply();
    }

    void resetNumberOfSurrenders() {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_number_of_surrenders), 0).apply();
    }

    int getNumberOfSurrenders() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_number_of_surrenders), 0);
    }

    void setCache1Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_1_seconds), seconds).apply();
    }

    int getCache1Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_1_seconds), 0);
    }

    int getCache1Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_1_minutes), 0);
    }

    void setCache2Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_2_seconds), seconds).apply();
    }

    int getCache2Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_2_seconds), 0);
    }

    int getCache2Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_2_minutes), 0);
    }

    void setCache3Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_3_seconds), seconds).apply();
    }

    int getCache3Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_3_seconds), 0);
    }

    int getCache3Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_3_minutes), 0);
    }

    void setCache4Time(int minutes, int seconds) {
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_minutes), minutes).apply();
        sharedPreferences.edit().putInt(context.getString(R.string.shared_pref_cache_4_seconds), seconds).apply();
    }

    int getCache4Seconds() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_4_seconds), 0);
    }

    int getCache4Minutes() {
        return sharedPreferences.getInt(context.getString(R.string.shared_pref_cache_4_minutes), 0);
    }

    void resetCaches() {
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
    }
}
