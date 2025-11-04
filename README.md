### Android Kotlin Exercise

This project retrieves news sources and their corresponding articles from a server.

## Features

* Fingerprint authentication.<br>
* List of various news sources (e.g. bbc, abc, cnn, cbs, etc.).<br>
* Filtering source list based on category.<br>
* List of source news headlines.<br>
* Searching keyword among headlines.<br>
* Article details screen.

## Project Structure
```
+---common
+---commonComposables
+---data
|   +---dto
|   +---remote
|   \---repository
+---di
+---domain
|   +---model
|   \---usecase
+---presentation
|   +---ui
|   |   +---fingerPrint
|   |   +---newsDetails
|   |   +---newsHeadline
|   |   \---sourceList
|   \---viewModel
```

## Dependencies
* Retrofit<br>
* okHttp<br>
* Koin<br>
* Coil<br>
* MockWebServer<br>
* Biometic<br>
* Coroutine<br>
* RxJava<br>

## Unit Test

* Validation of fetching news sources.<br>
* Validation of fetching news headlines.

## Instrumented Text

* Validation of application in real condition.
