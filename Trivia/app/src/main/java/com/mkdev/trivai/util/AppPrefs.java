package com.mkdev.trivai.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class AppPrefs {
    private SharedPreferences appPreferences;

    public AppPrefs(Activity activity) {
        this.appPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {
        int currentScore = score;
        int lastScore = appPreferences.getInt("high_score", 0);

        if(currentScore > lastScore) {
            appPreferences.edit().putInt("high_score", currentScore).apply();
        }
    }

    public int getHighScore() {
        return appPreferences.getInt("high_score", 0);
    }
}
