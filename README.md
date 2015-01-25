# The missing Android Guideline

```
! This repository is in beta and does not contain content which can be used without adaptions so far
```

first at all, the android sdk documentation is great:

http://developer.android.com/guide/index.html

If you have read the guides and the training section, than you can write Android Apps.

But if you want to create and maintain Android Apps in a professional environment,
you should know something more.

I will give a very short introduction to:

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

### Selection of a Frameworks

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

#### MVP

MVP stand for Model-View-Presenter. This is not library. It is a pattern to
decouple frontend UI from frontend logic.
This has the advantage of cleaner code, better tests and you have chance to
write different UI with the same logic (e.g. one UI for tablet and one for phones).

So the view (which can be an activity or an implementation of a view) only
contains UI specific elements, like make login button with a given id invisible.
User interactions like user clicked login button are delegated to the presenter.
The view offers methods to implement the commands from the presenter.
For example if the presenter calls hide login area, then the view knows which
elements needs to be hidden.

The presenter interacts with the backend and triggers depending on the results
UI actions. For example, after the user clicked the login button, the view delegates the
login data to the presenter. The presenter tries the login in the backend.
If the login succeeds the presenter triggers the hiding of the login area on the view.
The presenter has no knowledge about the exact fields or UI layout, but the presenter decides
that the login area should not be visible any more.

The model is the domain layer.


See for example:

* http://antonioleiva.com/mvp-android/
* https://github.com/antoniolg/androidmvp/tree/master/app/src/main/java/com/antonioleiva/mvpexample/app/Login

#### Dependency Injection

There are a lot of advantages of dependency injection like:

* Better tests by easy mocking of dependencies
* Often less code
* Better decoupling of logic
* ...

In the java world Spring and Guice are famous frameworks.
In android one famous framework for this is dagger

http://square.github.io/dagger/

#### Decoupling by a bus system

Complex and slow logic should not be done on the UI thread, because this
will block the UI interactions. So you extract the logic to asynchronous backend tasks.
Often you want to mainipulate the UI directly after the logic was executed.
This often force you to put the backend classesin the UI layer (e.g. Activity)
or to pass all the UI elements to the backend tasks. The better way is to trigger the
backend task and to register on a bus. The backend tasks will execute all the
logic and after the result is available it will add the result to the bus to inform the ui.

One library to do this is:

http://square.github.io/otto/

#### Annotation frameworks

Annotation frameworks like ButterKnife will just simplifiy your life.
Take a look at Butterknife as one option and you will realize this immedialtelly.

http://jakewharton.github.io/butterknife/

#### Http Calls

Depending on your needs, you should simplify you http calls.

You can use for example OkHttp (http://square.github.io/okhttp/) or Retrofit

TODO

https://api.github.com/repos/square/retrofit/contributors
https://github.com/square/retrofit/blob/master/samples/github-client/src/main/java/com/example/retrofit/GitHubClient.java

#### Other tools

The list is endless, depending on your need tale a look at ORM mappers (e.g. activedroid, GreenDAO, ORMlite), imageloader (e.g. picasso or Universal-Image-Loader) and many other.

Maybe some links can help you:

* https://android-arsenal.com
* http://www.vogella.com/tutorials/AndroidUsefulLibraries/article.html
* https://github.com/codepath/android_guides/wiki/Must-Have-Libraries

## Test Apps

## Monitor App

TODO:

* crashlytics
* crittercism
* ...

## Track user behaviour

## make some A/B Tests
