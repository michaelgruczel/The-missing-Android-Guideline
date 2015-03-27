package de.example.michaelgruczel.betterexample;

import android.app.Application;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.example.michaelgruczel.betterexample.ui.view.GithubActivity;

@Module(
        complete = true,
        library = true,
        injects = { MyApplication.class, GithubActivity.class }
)
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

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        return client;
    }
}
