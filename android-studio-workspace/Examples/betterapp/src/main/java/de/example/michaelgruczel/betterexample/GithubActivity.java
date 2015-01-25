package de.example.michaelgruczel.betterexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.example.michaelgruczel.betterexample.data.CheckTask;
import de.example.michaelgruczel.betterexample.events.CheckEvent;
import timber.log.Timber;

public class GithubActivity extends Activity {

    @InjectView(R.id.owner) EditText owner;
    @InjectView(R.id.repo) EditText repo;
    @InjectView(R.id.check) Button check;
    @InjectView(R.id.result) TextView result;

    @Inject
    Bus eventsBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.get(getApplicationContext()).inject(this);
        setContentView(R.layout.activity_github);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.check)
    public void check(View view) {
        new CheckTask(eventsBus, owner.getText().toString(), repo.getText().toString()).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("register at event bus");
        eventsBus.register(this);
    }

    @Override
    public void onPause() {
        super.onStop();
        Timber.d("unregister at event bus");
        eventsBus.unregister(this);
    }

    @Subscribe
    public void dataAvailable(CheckEvent event) {
        result.setText("" + event.getCommits());
    }

}



