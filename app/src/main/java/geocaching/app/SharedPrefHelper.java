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

    void resetCaches() {
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_1_state), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_2_state), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_3_state), false).apply();
        sharedPreferences.edit().putBoolean(context.getString(R.string.shared_pref_cache_4_state), false).apply();
    }
}
