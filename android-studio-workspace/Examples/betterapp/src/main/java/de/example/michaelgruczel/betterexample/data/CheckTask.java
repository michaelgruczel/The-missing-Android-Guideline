package de.example.michaelgruczel.betterexample.data;

import android.os.AsyncTask;

import com.squareup.otto.Bus;

import java.util.List;

import de.example.michaelgruczel.betterexample.events.CheckEvent;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;


public class CheckTask extends AsyncTask<Void, Void, Integer> {

    private static final String API_URL = "https://api.github.com";

    static class Contributor {
        int contributions;
    }

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }

    private final String anOwner;
    private final String aRepo;
    private Bus eventBus;

    public CheckTask(Bus eventBus, String anOwner, String aRepo) {
        this.eventBus = eventBus;
        this.anOwner = anOwner;
        this.aRepo = aRepo;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int contributionsSum = 0;
        try {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
            GitHub github = restAdapter.create(GitHub.class);
            List<Contributor> contributors = github.contributors(anOwner, aRepo);
            for (Contributor contributor : contributors) {
                contributionsSum += contributor.contributions;
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
        return contributionsSum;
    }

    @Override
    protected void onPostExecute(final Integer taskResult) {
        eventBus.post(new CheckEvent(taskResult.intValue()));
    }


}