package de.example.michaelgruczel.betterexample;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import timber.log.Timber;

public class MyApplication extends Application {

    private ObjectGraph objectGraph;

    public static MyApplication get(Context applicationContext) {

        return (MyApplication) applicationContext;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        objectGraph = ObjectGraph.create(new MyModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object object) {

        objectGraph.inject(object);
    }

}