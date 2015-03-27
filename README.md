# The missing Android Guideline

first at all, the android sdk documentation is great:

http://developer.android.com/guide/index.html

If you have read the guides and the training section, than you can write Android Apps.

But if you want to create and maintain Android Apps in a professional environment,
you should know something more.

I will give you a very short introduction to:

* How to make some prototypes before implementing it
* What frameworks exist for implementing android apps
* In case of native android app implementation, which libraries will simplify your work
* How to track the usage of your app
* how to monitor your app
* testing your app

## Discovery

If you want to test whether a UI would look great and whether the users uderstand the UI,
you can do it often without even writing code.

So are a lot of prototype tools like for example:

* http://www.invisionapp.com
* https://marvelapp.com
* https://www.flinto.com
* https://www.fluidui.com
* http://www.axure.com
* ...

You can create mockups (no real data) with such tools and show them to your
customers before building it. That's often faster then a real implementation.

## Develop

### Selection of a framework

There are several solutions to develop apps

#### Html/Javascript based

There are a lot of Html/Javascript based Solution to develop Apps.
Normally an app warpper just openes a webview (embedded browser) for that.

Such apps often do not feel fluid, but the developed html solutions can easily converted to IOS apps.
Apart from that you can use a different skillset (Frontend developers instead of App/Java developers) and thery are often easy to integrate with websites
Cordova based setups like PhoneGap or Ionic are famous in this area.

#### Native

These types of apps have the advantages of:

* fast
* fluid
* full access of device capabilities
* updates by apps
* easy to handle offline cases

But if you want to create apps for several platform, you have to code it twice (or more often)

#### hybrid:

very often you will find hybrid solutions. That means they contain
native parts for full functionality
and html parts for online maintained elements

#### Multiplatform solutions

There are some solutions which are creating native apps, often for several platforms.
Often they offer a metalanguage and generate C++ code under the hood or you write c++ code directly.
The pro and cons depending on the selected solution.
The solutions are often used by gaming companies because they want to develop for several platforms and they are often not interested in platform specific functions but in customized UIs.

Famous frameworks are:

* Marmelade
* Xamarin
* Unity

### Native App Libraries

Lets say you want to implement a native android App, then you should take a
look at some libraries and concepts, which will simplify your work.

#### Annotation frameworks

Annotation frameworks like ButterKnife will just simplifiy your life.
Take a look at Butterknife as one option and you will realize this immediatelly.

With butternkife you can write

    @InjectView(R.id.check) Button check;
    ...
    @OnClick(R.id.check)
    public void check(View view) {
        // do stuff
    }

instead of for example

    private Button check;
    ...
    check = (Button) findViewById(R.id.check);
    check.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // do stuff
        }
    });

That's only a small thing, but if you have big classes with a huge amount of functions,
it will already make a difference

See http://jakewharton.github.io/butterknife/ for details

#### Decoupling by a bus system

Complex and slow logic should not be done on the UI thread, because this
will block the UI interactions. So you extract the logic to asynchronous backend tasks.
Often you want to mainipulate the UI directly after the logic was executed.
This often force you to put the backend classesin the UI layer (e.g. Activity)
or to pass all the UI elements to the backend tasks. The better way is to trigger the
backend task and to register on a bus. The backend tasks will execute all the
logic and after the result is available it will add the result to the bus to inform the ui.

So instead of having your tasks in the same class like the activity
or passing ui elements as parameters, extract your async tasks and
if the task is finished send an event.

    // Activity handles UI
    public class YourActivity extends Activity {
    ...
    @Subscribe
    public void dataAvailable(YourEvent event) {
        // do something in the UI
    }
    ...


    // decoupled backend task
    public class YourTask extends AsyncTask<Void, Void, Integer> {
    ...
    @Override
    protected void onPostExecute(final Integer result) {
       eventBus.post(new YourEvent(result));
    }
    ...

Every class can register and unregister at the bus to get messages.

One library to do this is: http://square.github.io/otto/

#### Dependency Injection

There are a lot of advantages of dependency injection like:

* Better tests by easy mocking of dependencies
* Often less code
* Better decoupling of logic
* ...

In the java world Spring and Guice are famous frameworks.
In android one famous framework for this is dagger

So instead of defining instances of everything in your classes or have a huge amount of static elements, it makes sense to inject elements.

Its easy to do with dagger:

* define the application class which creates an object Graph to handle dependencies. Thats boilerplate but you need it only once.

For example

    public class MyApplication extends Application {

    private ObjectGraph objectGraph;

    public static MyApplication get(Context applicationContext) {
        return (MyApplication) applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new MyModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
    ...

* Define one module to define the things you want to inject. In this case we will only provide a singleton of type HelloWordService to our Activity.

For example


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
        public HelloWordService provideEventsBus() {
            return new HelloWordService();
        }
        ...

* Inject the elements by the @Inject annotation in your code.

For example


    public class MyActivity extends Activity {

        @Inject
        HelloWordService helloWordService;
        // ... use the service wherever you want
        // there will only one instance of it

    }

see http://square.github.io/dagger/ for details

#### MVP

MVP stand for Model-View-Presenter. This is not library. It is a pattern to decouple frontend UI from frontend logic.
This has the advantage of cleaner code, better tests and you have chance to write different UIs with the same logic (e.g. one UI for tablet and one for phones).

So the view (which can be an activity or an implementation of a view) only contains UI specific elements, like make login button with a given id invisible.
User interactions like user clicked login button are delegated to the presenter.
The view offers methods to implement the commands from the presenter.
For example if the presenter calls hide login area, then the view knows which elements needs to be hidden.

The presenter interacts with the backend and triggers depending on the results UI actions. For example, after the user clicked the login button, the view delegates the login data to the presenter. The presenter tries the login in the backend.
If the login succeeds the presenter triggers the hiding of the login area on the view.
The presenter has no knowledge about the exact fields or UI layout, but the presenter decides that the login area should not be visible any more.

The model is the domain layer.

One simple way to do it is to create a presenter which handles all
the business logic and defines an interface which must be implemented by the views. The view delegate all actions to the presenter:

    public class MainPresenter {

      private GithubView view;

      public MainPresenter(GithubView view) {
        this.view = view;
      }

      public void retrieveNumberOfCommits(String owner, String repo) {
        // retrieve the data
        int numberOfCommits = ...
        view.showCommits(numberOfCommits);
      }

      public interface GithubView {
        public void showNumberOfCommits(int number);
      }

    }  

    public class GithubActivity extends Activity implements MainPresenter.GithubView {

      EditText owner;
      EditText repo;
      Button check;
      TextView result;
      private MainPresenter presenter;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
        ...
        presenter = new MainPresenter(eventsBus, this);
      }

      @OnClick(R.id.check)
      public void check(View view) {
          presenter.retrieveNumberOfCommits(owner.getText().toString(), repo.getText().toString());
      }  

      public void showNumberOfCommits(int number) {
        result.setText(String.valueOf(number));
      }
  }



See for example:

* https://github.com/michaelgruczel/The-missing-Android-Guideline/tree/master/android-studio-workspace/Examples/betterapp/src/main/java/de/example/michaelgruczel/betterexample/ui
* http://antonioleiva.com/mvp-android/
* https://github.com/antoniolg/androidmvp/tree/master/app/src/main/java/com/antonioleiva/mvpexample/app/Login


#### Http Calls

Depending on your needs, you mabye need http calls.
In this case you can simplify a lot.

You can use for example OkHttp (http://square.github.io/okhttp/) or Retrofit (https://github.com/square/retrofit)

A very simple retrofit example you can find under:
  https://github.com/square/retrofit/blob/master/samples/github-client/src/main/java/com/example/retrofit/GitHubClient.java  

I have added more or less the same code here only for completeness

If you call https://api.github.com/repos/michaelgruczel/The-missing-Android-Guideline/contributors you will get something like:

    [
    {
    "login": "michaelgruczel",
    "id": 3057985,
    "avatar_url": "https://avatars.githubusercontent.com/u/3057985?v=3",
    "gravatar_id": "",
    "url": "https://api.github.com/users/michaelgruczel",
    "html_url": "https://github.com/michaelgruczel",
    "followers_url": "https://api.github.com/users/michaelgruczel/followers",
    "following_url": "https://api.github.com/users/michaelgruczel/following{/other_user}",
    "gists_url": "https://api.github.com/users/michaelgruczel/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/michaelgruczel/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/michaelgruczel/subscriptions",
    "organizations_url": "https://api.github.com/users/michaelgruczel/orgs",
    "repos_url": "https://api.github.com/users/michaelgruczel/repos",
    "events_url": "https://api.github.com/users/michaelgruczel/events{/privacy}",
    "received_events_url": "https://api.github.com/users/michaelgruczel/received_events",
    "type": "User",
    "site_admin": false,
    "contributions": 16
    }
    ]

Instead of parsing elements with a json parser you can parse it very simple to a java object. The mapping code you need is

    static class Contributor {
      String login;
      int contributions;
    }

    interface GitHub {
      @GET("/repos/{owner}/{repo}/contributors")
      List<Contributor> contributors(
        @Path("owner") String owner,
        @Path("repo") String repo
      );
    }

and then you can retrieve the object by

    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint("https://api.github.com")
        .build();

    // Create an instance of our GitHub API interface.
    GitHub github = restAdapter.create(GitHub.class);

 see https://github.com/square/retrofit/blob/master/samples/github-client/src/main/java/com/example/retrofit/GitHubClient.java for the complete example

#### Bring it together

In this repo you will find 2 apps which are doing the same thing. They count the number of github commits for a given repo of an owner.

The version 1 is done like it would be done on base of the Android guideline.

see https://github.com/michaelgruczel/The-missing-Android-Guideline/tree/master/android-studio-workspace/Examples/dirtyapp/src/main/java/de/example/michaelgruczel/dirtyexample

Version 2 is a litle bit more cleaner by using dagger (DI), butterknife (annotations), retrofit (restcalls) and a bus (decoupling)

see https://github.com/michaelgruczel/The-missing-Android-Guideline/tree/master/android-studio-workspace/Examples/betterapp/src/main/java/de/example/michaelgruczel/betterexample

It is not less code and probably its a lot of boilerplate for such a small app.
But its much better seperated and if apps are getting bigger it will have a huge advantage.

#### Other tools

The list is endless, depending on your needs. Take a look at ORM mappers (e.g. activedroid, GreenDAO, ORMlite), imageloader (e.g. picasso or Universal-Image-Loader) and many other.

Maybe some links can help you:

* https://android-arsenal.com
* http://www.vogella.com/tutorials/AndroidUsefulLibraries/article.html
* https://github.com/codepath/android_guides/wiki/Must-Have-Libraries

## Debug App

### Stetho

Stetho visualises http calls in chrome like you know it e.g. from firebug for websites.
Take a look at http://facebook.github.io/stetho/

## Test Apps

### OkHttp Mock Server

Sometimes you need external services and its easier to mock them in a test.
There is a simple server you can start in your test to simulate the external resource.

see https://github.com/square/okhttp/tree/master/mockwebserver for details

### Other

* Appium

## cont delivery

 * jenkins
 * gradle
 * jenkins job generation
 * upload by Google api

## Monitor App

There are several providers avaliable which helps you to track crashes.
2 Examples are crashlytics and crittercism
I will only show one example with crittercism.

register at http://www.crittercism.com

add the lib to your dependencies and add Crittercism.initialize(getApplicationContext(), "your code"); to the start up of your app or activity.
![crittercism 1][1]
![crittercism 2][2]
![crittercism 3][3]

Let us now create a crash

  public class MyApplication extends Application {

    public static MyApplication get(Context applicationContext) {

        return (MyApplication) applicationContext;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        ...
        Crittercism.initialize(getApplicationContext(), "...8cc8cc8cc8cc");
        throw new RuntimeException("stupid mistake");
    }
  }

The crash data will ow be send to crittercism and is visible in your dashboard.

![crittercism 4][4]
![crittercism 5][5]

There are additional features like request times, organizing by version, manufacturer, integrated bugtracking, ...
I will not summarize it here and i will not show everything here and I will not compare the providers, but take sure that you have at least one running monitoring system for your apps.



## Track user behaviour

 * Track user behaviour e.g. Adjust
 * A/B Test e.g. appium
 * user feedback e.g. helpshift


 [1]: https://github.com/michaelgruczel/The-missing-Android-Guideline/crittercism1.png
 [2]: https://github.com/michaelgruczel/The-missing-Android-Guideline/crittercism2.png
 [3]: https://github.com/michaelgruczel/The-missing-Android-Guideline/crittercism3.png
 [4]: https://github.com/michaelgruczel/The-missing-Android-Guideline/crittercism4.png
 [5]: https://github.com/michaelgruczel/The-missing-Android-Guideline/crittercism5.png
