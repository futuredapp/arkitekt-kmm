## Define FlowUseCase

FlowUseCase is a abstract class with one abstract function. It has two generic parameters. The first is to define
Argument type, the second is to define Return type. FlowUseCase will always return a flow of defined return type.
The flow will be executed on non-ui thread. Don't forget to `freeze()` the FlowUseCase in `init` block.
Any properties has to be initialized/injected above the init block because of freezing.

```kotlin
// shared/src/commonMain/../domain/
class ObserveCoinsUseCase : FlowUseCase<Unit, List<Coin>>() {
    private val coinStore: CoinStore = CoinStore(RestApiManager, DatabaseManager)

    init {
        freeze()
    }

    override fun build(arg: Unit): Flow<List<Coin>> {
        println("ObserveCoinsUseCase building...")
        return coinStore.observeCoins()
    }
        
}
```

Note that only the flow will be executed on non-ui thread. The rest of the `build` function will be executed on ui thread.

## Execute FlowUseCase

To be able to execute the FlowUseCase you have to be in the scope of `CoroutineScopeOwner`. On the Android side
you can take advantage of `viewModelScope` from Android Architecture Components (AAC). On the iOS side
you can use `ArkitektViewModel`from `km-viewmodel`. If you need to execute the FlowUseCase outside of ViewModel,
e.g. in a Service, you need to implement `CoroutineScopeOwner` and provide `CoroutineScope` by yourself.

### Usage on Android

Define a BaseViewModel

```kotlin
// androidApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.futured.arkitekt.kmusecases.scope.CoroutineScopeOwner

abstract class BaseViewModel : ViewModel() , CoroutineScopeOwner {

    override val coroutineScope = viewModelScope
}
```

Execute the UseCase

```kotlin
class CoinsViewModel : BaseViewModel() {
    private val observeCoinsUseCase = ObserveCoinsUseCase()
    
    var coins by mutableStateOf(emptyList<Coin>())
    var isLoading by mutableStateOf(false)

    fun fetchCoins() {
        observeCoinsUseCase.execute(Unit) {
            onStart { isLoading = true }
            onNext { 
                coins = it
                isLoading = false
            }
            onError {
                isLoading = false    
                println(it.message)
            }
        }
    }
}
```
### Usage on iOS

```swift
import shared

class BaseViewModel : Km_viewmodelArkitektViewModel {
    
    deinit {
        onDestroy()
    }
}
```

Execute the UseCase

```swift
import shared

class CoinsViewModel : BaseViewModel, ObservableObject {
    @Published var coins = [Coin]()

    private let getCoinsUseCase = GetCoinsListUseCase()

    func getCoins() {
        getCoinsUseCase.execute(self, args: KotlinUnit()) {
            $0.onSuccess { list in
                self.coins = list as [Coin]
            }
            $0.onError { error in
                print(error)
            }
        }
    }
}
```

## UseCase usage

Head out to [UseCase](UseCase.md) section.

## Example

Check out the usage in the [example](https://github.com/RudolfHladik/Template)
