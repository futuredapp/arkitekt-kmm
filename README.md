# Arkitekt kmm

## Km-usecases

Is a Kotlin multiplatform mobile library that helps you with
abstraction of Usecase as a component from clean architecture.
Its main objective is separation of concerns and better domain modeling.
It is backed by `Kotlinx Coroutines`

### Benefits
 - delegate work to background thread
 - cancel on reexecution (optional)
 - error handling
 - auto-cancel of coroutine context
 
### Usage

Library contains two main parts `UseCase` and `FlowUseCase`. 
- `UseCase` is for events that return single response. (e.g. REST API call GET, POST...)
- `FlowUseCase` is for events that return multiple responses. (e.g. Location data updates...)

#### UseCase

UseCase same as FlowUseCase has two generic parameters. The first is argument type, the second specify return type.
If you don't need any of these, just put `Unit` in there.
When creating a usecase, don't forget to `freeze()` it in init block, but after local parameters initialization.
If you use DI, inject before freezing, or you will end up with `InvalidMutabilityException`. This step won't be necessary
after release of New Native Memory Model. You can preview it with Kotlin 1.6.0-M1.

#### Define a usecase in common module

```kotlin 
// Common
class GetCoinsListUseCase : UseCase<Unit, List<Coin>>() {
    private val coinStore: CoinStore = CoinStore(RestApiManager, DatabaseManager)

    init {
        freeze()
    }

    override suspend fun build(arg: Unit): List<Coin> {
        return coinStore.fetchCoins().coins.map {
            Coin(
                it.id,
                it.name,
                it.icon,
                it.symbol,
                it.price
            )
        }
    }
}
 ```


#### Consume the Usecase from Android. 
You can execute the Usecase anywhere you want, but you have to implement the `CoroutineScopeOwner`
interface and specify the `coroutineContext` where the Usecase will be executed. 
So the `ViewModel` is the most common option.

```kotlin
// Android
class CoinsViewModel : ViewModel(), CoroutineScopeOwner {

      override val coroutineScope: CoroutineScope
          get() = viewModelScope
        
      private val getCoinsUseCase = GetCoinsListUseCase()
      var coins by mutableStateOf(emptyList<Coin>())

      fun fetchCoins() {
          getCoinsUseCase.execute(Unit) {
              onSuccess { coins = it.list }
              onError { print(it.message) }
          }
      }
}
```

#### Consume the Usecase from iOS.
As the Swift language also features closing lambdas, you can execute the Usecase nearly in the same fashion as on Android.
```swift
// iOS
class CoinsViewModel : BaseViewModel, ObservableObject {
    @Published var coins = [Coin]()

    private let getCoinsUseCase = GetCoinsListUseCase()

    func getCoins() {
        getCoinsUseCase.execute(self, args: KotlinUnit()) {
            $0.onNext { list in
                guard let coinsList = list?.list else { return }
                self.coins = coinsList as [Coin]
            }
            $0.onError { error in
                print(error)
            }
        }
    }
}
```

### Example
Check out the full example in my other project the [KMM Template](https://github.com/RudolfHladik/Template)
