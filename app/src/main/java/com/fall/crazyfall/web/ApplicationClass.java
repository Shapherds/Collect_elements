package com.fall.crazyfall.web;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.fall.crazyfall.MainActivity;
import com.fall.crazyfall.StaticData;

import java.util.Map;
import java.util.Objects;

public class ApplicationClass extends Application {
    boolean isNonOrganic = false;
    String campaing = "";
    @Override
    public void onCreate() {
        super.onCreate();
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {


            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                Log.e("LOG_TAG", "attribute: " + " = " + conversionData.toString());
                for (String attrName : conversionData.keySet()) {
                    if (attrName.equals("af_status") && Objects.requireNonNull(conversionData.get(attrName)).toString().equals("Non-organic")) {
//                        Log.e("Logs", "campaining : " + Objects.requireNonNull(conversionData.get(attrName)).toString() );
                        isNonOrganic = true;
                    }
                    if (attrName.equals("campaign")) {
//                        Log.e("Logs", "campaining : "+  Objects.requireNonNull(conversionData.get(attrName)).toString() );
                        campaing = Objects.requireNonNull(conversionData.get(attrName)).toString();
                    }
                }
                if (isNonOrganic) {
                    MainActivity.activity.initWebData(campaing);
                } else {
                    MainActivity.activity.initWebData("");
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {

                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(StaticData.AF_DEV_KEY.getData(), conversionListener, this);
        AppsFlyerLib.getInstance().start(this);
    }
}