# Square-Directory-App
An Open Source sample Android employee directory app that shows a list of employees from an endpoint

When interviewing Square for Android role you will be asked to complete a mobile take home project. You'll be building an employee directory app. The prompt for the project can be found [here](https://square.github.io/microsite/mobile-interview-project/).
 
## Build tools & versions used
- Android Studio Giraffe | 2022.3.1 Patch 4
- Gradle 8.0

## Steps to run the app
1. Import the project into Android Studio
2. Click "Run app", or run `./gradlew installDebug` from the command line to run the app

## What areas of the app did you focus on?
- Modernization (MVVM, Jetpack Navigation, DI, Material Design)
- Modularization
- API/Repository stability and error handling
- Writing unit testable code

## What was the reason for your focus? What problems were you trying to solve?
- To ensure stability, reusability and ensure adding new features won't be cumbersome

## How long did you spend on this project?
- 7 Hours

## Did you make any trade-offs for this project? What would you have done differently with more time?
- Adding a toml file for proper dependency management would have been ideal if time permitting
- I would have liked to re-implement the main screen in compose but chose to work with what is most familiar to start

## What do you think is the weakest part of your project?
- There is limited documentation. I typically would use a tool like detekt to enforce proper documentation.
- Overall, staying within the constraints outline in the prompt, I'm very proud of this work

## Did you copy any code or dependencies? Please make sure to attribute them here!
- No code was copied, I did use the following Android libraries:
  - [Hilt](https://github.com/google/dagger/tree/master/java/dagger/hilt) - dependency injection
  - [Moshi](https://github.com/square/moshi) - JSON parsing an serialization
  - [Retrofit](https://github.com/square/retrofit) - HTTP client
  - [OkHttp](https://github.com/square/okhttp) - network requests
  - [Timber](https://github.com/JakeWharton/timber) - logging
  - [Glide](https://github.com/bumptech/glide) - image loading and caching
  - [Android Simple Tooltip](https://github.com/douglasjunior/android-simple-tooltip) - UI tooltips

## Is there any other information you’d like us to know?
- I created a private Github repository and committed my code there
- Square makes great Open Source libraries, so thank you!