## Introduction

Android library to support API mocking by modifying HTTP responses.

Features
- Enable HTTP traffic capture
- Select one of the HTTP responses for mocking, edit the response, and save
- Enable/disable as needed
- Also, edit the responses on the web using the [mocker-react](https://github.com/sandymist/mocker-react) app

NOTE: This is intended ONLY for the debug builds, NEVER in production.

## Usage

- Clone this repository and cd to the project root directory
- Build `./gradlew assembleDebug`
- Publish locally `./gradlew publishToMavenLocal`
- Add the following to the app's Gradle file (find the latest version from this repository)
```
    debugImplementation("com.sandymist.android.mocker:apimock:$API_MOCK_VERSION")
    releaseImplementation("com.sandymist.android.mocker:apimock-no-op:$API_MOCK_VERSION")
```
- Add as an interceptor to OkHTTPClient builder
```
    // OkHTTPClient.Builder builder = ...
    if (BuildConfig.DEBUG) {
        APIMockInterface apiMockInterface = APIMockModuleKt.getDefaultAPIMockInterface();
        apiMockInterface.initWithContext(context, builder);
    }
```
- Invoke UI entry point from one of the app's settings menu
```
     StandAloneAPIMockActivity.Companion.launchActivity(context)
```

  
