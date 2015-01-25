package de.example.michaelgruczel.betterexample;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Provides;

public class MyModule {

    private final Application app;

    public MyModule(Application application) {
        this.app = application;
    }

    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    public Bus provideEventsBus() {
        return new Bus();
    }
}
