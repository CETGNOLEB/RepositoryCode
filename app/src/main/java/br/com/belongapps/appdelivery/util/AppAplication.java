package br.com.belongapps.appdelivery.util;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

public class AppAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //INIT PERSISTENCIA OFF FIREBASE
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //INIT PICASSO
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        //INIT JODA
        JodaTimeAndroid.init(this);

    }
}
