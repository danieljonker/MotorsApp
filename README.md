## MOTORS APP

### Architecture
This app is using MVVM architecture pattern and the repository pattern to manage the data source.
Currently it is all just in the app module, given more time, I would look at breaking it up into a feature module (for search) and a network module below that

### Tests
Currently, the ViewModel, Repo and Progress bar visibility are all being unit tested. 
There is a token Espresso test. With more time, I would add proper integration tests, by replacing the dependencies in tests with Dagger

### Third party Libraries used:
- hilt for dependency injection
- Retrofit and Moshi for fetching data from the API
- mockk for mocking in unit tests 

