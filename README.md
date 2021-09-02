# ItunesSearch
  - I have hardcoded the `movie` as entity and `au` as country for simplicity
  - The architecture used is MVVM, I chose this over MVP/MVC because it has the separation of concerns like MVP but doesn't require the instance of the view. It also makes unit testing easier. On the MVVM, the views subscribe to events from the ViewModel via livedata or any observable instances. On top of that, Google even created a native ViewModel class that is lifecycle aware and will help restore data when configuration changes. All these things helps minimizes the risk of memory leaks and state loss.

### Tech stack
  - Retrofit2
  - Dagger Hilt
  - View Binding
  - MVVM Pattern
  - ViewModel
  - LiveData
  - RxJava
  - Room DB
