package de.example.michaelgruczel.betterexample;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.otto.Bus;

import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mockito;

import de.example.michaelgruczel.betterexample.data.CheckTask;
import de.example.michaelgruczel.betterexample.events.CheckEvent;

public class CheckTaskTest extends TestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //System.setProperty("dexmaker.dexcache", this.getContext().getCacheDir().toString());
    }

    public void test() throws Exception {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("hello, world!"));
        server.start();
        Bus bus = Mockito.mock(Bus.class);
        new CheckTask(bus, "michaelgruczel", "The-missing-android-guideline", new OkHttpClient()).execute();

        wait(2000);

        Mockito.verify(bus, Mockito.times(1)).post(Matchers.eq(CheckEvent.class));

        server.shutdown();

    }
}
