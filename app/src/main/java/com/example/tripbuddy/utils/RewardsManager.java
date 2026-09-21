package com.example.tripbuddy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class RewardsManager {
    private static final String PREF_NAME = "tripbuddy_rewards";
    private static final String KEY_TRIP_COUNT = "trip_count";
    private SharedPreferences prefs;
    public RewardsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public void incrementTripCount() {
        int current = prefs.getInt(KEY_TRIP_COUNT, 0);
        prefs.edit().putInt(KEY_TRIP_COUNT, current + 1).apply();
    }
    public int getTripCount() {
        return prefs.getInt(KEY_TRIP_COUNT, 0);
    }
    public boolean isEligibleForDiscount() {
        return getTripCount() >= 3;
    }
}