package de.example.michaelgruczel.betterexample;

import android.app.Activity;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GithubActivity extends Activity {

    private EditText owner;
    private EditText repo;
    private Button check;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        owner = (EditText) findViewById(R.id.owner);
        repo = (EditText) findViewById(R.id.repo);
        check = (Button) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckTask(owner.getText().toString(), repo.getText().toString()).execute();
            }
        });
        result = (TextView) findViewById(R.id.result);
    }

    public class CheckTask extends AsyncTask<Void, Void, Integer> {

        private final String anOwner;
        private final String aRepo;

        CheckTask(String anOwner, String aRepo) {
            this.anOwner = anOwner;
            this.aRepo = aRepo;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("https://api.github.com/repos/" + anOwner + "/" + aRepo + "/contributors");
            HttpResponse response;
            int contributionsSum = 0;
            try {
                    response = httpclient.execute(httpget);
                    Log.i(this.getClass().getName(), response.getStatusLine().toString());
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String res = convertStreamToString(instream);
                        JSONArray obj = new JSONArray(res);
                        // e.g. https://api.github.com/repos/square/retrofit/contributors
                        for(int i = 0; i < obj.length(); i++) {
                            JSONObject pers = (JSONObject) obj.get(i);
                            Log.i(this.getClass().getName(), pers.get("contributions").toString());
                            int contributions = (Integer) pers.get("contributions");
                            contributionsSum += contributions;
                        }
                        instream.close();
                    } else {
                        return 0;
                    }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            return contributionsSum;
        }

        @Override
        protected void onPostExecute(final Integer taskResult) {
            result.setText("" + taskResult);
        }

        private String convertStreamToString(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    }
}



