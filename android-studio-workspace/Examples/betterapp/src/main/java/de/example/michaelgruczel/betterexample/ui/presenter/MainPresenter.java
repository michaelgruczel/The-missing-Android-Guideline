package de.example.michaelgruczel.betterexample.ui.presenter;


import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import de.example.michaelgruczel.betterexample.data.CheckTask;
import de.example.michaelgruczel.betterexample.events.CheckEvent;
import timber.log.Timber;

public class MainPresenter {

    private Bus eventsBus;
    private GithubView view;

    public MainPresenter(Bus eventsBus, GithubView view) {
        this.eventsBus = eventsBus;
        this.view = view;
    }

    public void register() {
        Timber.d("register at event bus");
        eventsBus.register(this);
    }

    public void unregister() {
        Timber.d("unregister at event bus");
        eventsBus.unregister(this);
    }

    public void checkTasks(String owner, String repo) {
        new CheckTask(eventsBus, owner, repo).execute();
    }

    @Subscribe
    public void dataAvailable(CheckEvent event) {
        view.showCommits(event.getCommits());
    }

    public interface GithubView {
        public void showCommits(int number);
    }

}
