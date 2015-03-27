package de.example.michaelgruczel.betterexample;

import android.app.Application;
import android.content.Context;

import com.crittercism.app.Crittercism;
import com.facebook.stetho.Stetho;

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
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        //Crittercism.initialize(getApplicationContext(), "...8cc");
        // enable for test cases
        //throw new RuntimeException("stupid mistake");
    }

    public void inject(Object object) {

        objectGraph.inject(object);
    }

}