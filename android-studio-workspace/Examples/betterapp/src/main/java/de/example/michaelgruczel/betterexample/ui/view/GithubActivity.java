package de.example.michaelgruczel.betterexample.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.example.michaelgruczel.betterexample.MyApplication;
import de.example.michaelgruczel.betterexample.R;
import de.example.michaelgruczel.betterexample.ui.presenter.MainPresenter;

public class GithubActivity extends Activity implements MainPresenter.GithubView {

    @InjectView(R.id.owner) EditText owner;
    @InjectView(R.id.repo) EditText repo;
    @InjectView(R.id.check) Button check;
    @InjectView(R.id.result) TextView result;
    private MainPresenter presenter;

    @Inject
    Bus eventsBus;

    @Inject
    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.get(getApplicationContext()).inject(this);
        setContentView(R.layout.activity_github);
        ButterKnife.inject(this);

        presenter = new MainPresenter(eventsBus, this, okHttpClient);
    }

    @OnClick(R.id.check)
    public void check(View view) {
        presenter.checkTasks(owner.getText().toString(), repo.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.register();
    }

    @Override
    public void onPause() {
        super.onStop();
        presenter.unregister();
    }

    public void showCommits(int number) {
        result.setText("" + number);
    }

}



