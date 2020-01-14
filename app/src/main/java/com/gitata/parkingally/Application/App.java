package com.gitata.parkingally.Application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //set database to be persistent
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
