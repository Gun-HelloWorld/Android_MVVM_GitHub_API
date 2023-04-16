package com.gun.githubapi.api.okhttp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import okhttp3.logging.HttpLoggingInterceptor;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

class PrettyLogger implements HttpLoggingInterceptor.Logger {
    private final Gson mGson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void log(String message) {
        String trimMessage = message.trim();
        if ((trimMessage.startsWith("{") && trimMessage.endsWith("}"))
                || (trimMessage.startsWith("[") && trimMessage.endsWith("]"))) {
            try {
                String prettyJson = mGson.toJson(JsonParser.parseString(message));
                Log.d(TAG, prettyJson, null);
            } catch (Exception e) {
                Log.d(TAG, message, e);
            }
        } else {
            Log.d(TAG, message, null);
        }
    }
}
