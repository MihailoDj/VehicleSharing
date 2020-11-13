package com.example.mpos_projekat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

public class App extends Application {
    public static final String CHANEL_1_ID = "CHANEL_1";

    @Override
    public void onCreate() {
        super.onCreate();

        kreirajKanale();
    }

    public void kreirajKanale() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chanel1 = new NotificationChannel(CHANEL_1_ID, "chanel1", NotificationManager.IMPORTANCE_HIGH);

            chanel1.enableLights(true);
            chanel1.setLightColor(Color.GREEN);
            chanel1.enableVibration(false);
            chanel1.setDescription("Ovo su uspesno obavljene operacije nad bazom");

            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanel1);
        }
    }
}
